package ru.dolzhenkoms.carinfosearcher.service.searcher.bdi.impl

import com.fasterxml.jackson.databind.ObjectMapper
import java.util.UUID
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import ru.dolzhenkoms.carinfosearcher.client.BdiHistoryClient
import ru.dolzhenkoms.carinfosearcher.configuration.cache.BDI_HISTORY
import ru.dolzhenkoms.carinfosearcher.configuration.consts.DESCRIPTION
import ru.dolzhenkoms.carinfosearcher.configuration.consts.ENGINE_VOLUME
import ru.dolzhenkoms.carinfosearcher.configuration.consts.IMAGE_URL
import ru.dolzhenkoms.carinfosearcher.configuration.consts.ORIGINAL_SOURCE_URL
import ru.dolzhenkoms.carinfosearcher.configuration.consts.REPAIR_COST
import ru.dolzhenkoms.carinfosearcher.model.dto.CarInfoDto
import ru.dolzhenkoms.carinfosearcher.model.jpa.HistoryQueueEntity
import ru.dolzhenkoms.carinfosearcher.model.jpa.types.SourceType
import ru.dolzhenkoms.carinfosearcher.repository.jpa.HistoryQueueEntityRepository
import ru.dolzhenkoms.carinfosearcher.service.searcher.bdi.BdiHistorySearcher

@Service
class BdiHistorySearcherImpl(
    private val bdiHistoryClient: BdiHistoryClient,
    private val repository: HistoryQueueEntityRepository,
    private val objectMapper: ObjectMapper
) : BdiHistorySearcher {

    @Cacheable(BDI_HISTORY, key = "#vin")
    override fun findByVin(vin: String, userId: UUID): CarInfoDto? {
        val searchHttpResponse = runCatching { bdiHistoryClient.execute(query = vin) }
            .getOrNull()

        val body = searchHttpResponse?.parse()
        val imageUrl = body?.getElementById("listing_images_slider")?.childNode(1)?.attr("data-thumb")
        val carMainInfo = body?.getElementsByClass("table-responsive")?.get(0)?.childNode(7)?.childNode(3)

        val description = carMainInfo?.childNode(3)?.childNode(3)?.childNode(1)?.childNode(0)
            ?.toString() + " " + carMainInfo?.childNode(5)?.childNode(3)?.childNode(1)?.childNode(0)?.toString()

        val engine = carMainInfo?.childNode(7)?.childNode(3)?.childNode(0).toString()
        val repairCost = carMainInfo?.childNode(27)?.childNode(3)?.childNode(0).toString()

        val data = mapOf(
            DESCRIPTION to description,
            IMAGE_URL to imageUrl,
            ORIGINAL_SOURCE_URL to searchHttpResponse?.url().toString(),
            ENGINE_VOLUME to engine,
            REPAIR_COST to repairCost
        )

        return carMainInfo?.let {
            CarInfoDto(
                data
            )
        }?.also {
            val dataAsJson = objectMapper.writeValueAsString(data)

            repository.save(
                HistoryQueueEntity(
                    vin = vin,
                    data = dataAsJson,
                    sourceType = SourceType.BDI_HISTORY,
                    userCallId = userId,
                )
            )
        }
    }

    override fun getType() = SourceType.BDI_HISTORY
}