package ru.dolzhenkoms.carinfosearcher.service.searcher.bdi.impl

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import ru.dolzhenkoms.carinfosearcher.client.BdiHistoryClient
import ru.dolzhenkoms.carinfosearcher.configuration.cache.BDI_HISTORY
import ru.dolzhenkoms.carinfosearcher.configuration.consts.BODY_TYPE
import ru.dolzhenkoms.carinfosearcher.configuration.consts.DESCRIPTION
import ru.dolzhenkoms.carinfosearcher.configuration.consts.IMAGE_URL
import ru.dolzhenkoms.carinfosearcher.configuration.consts.MANUFACTURER_COUNTRY
import ru.dolzhenkoms.carinfosearcher.configuration.consts.ORIGINAL_SOURCE_URL
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
    override fun findByVin(vin: String): CarInfoDto? {
        val searchHttpResponse = runCatching { bdiHistoryClient.execute(query = vin) }
            .getOrNull()

        val body = searchHttpResponse?.parse()
        val imageUrl = body?.getElementsByClass("owl-carousel owl-moneymaker2")
            ?.first()
            ?.childNode(2)
            ?.childNode(0)
            ?.toString()
            ?.split("\\\"")
            ?.get(3)
        val carMainInfo = body?.getElementById("product")

        val description = carMainInfo?.childNode(5)?.childNode(0).toString()
        val bodyType = carMainInfo?.childNode(13)?.childNode(0).toString()
        val manufacturerCountry = carMainInfo?.childNode(15)?.childNode(0).toString()

        val data = mapOf(
            DESCRIPTION to description,
            BODY_TYPE to bodyType,
            MANUFACTURER_COUNTRY to manufacturerCountry,
            IMAGE_URL to imageUrl,
            ORIGINAL_SOURCE_URL to searchHttpResponse?.url().toString(),
        )

        return carMainInfo?.let {
            CarInfoDto(
                data
            )
        }?.also {
            val dataAsJson = objectMapper.writeValueAsString(data)

            repository.save(HistoryQueueEntity(
                vin = vin,
                data = dataAsJson,
                sourceType = SourceType.BDI_HISTORY
            ))
        }
    }

    override fun getType() = SourceType.BDI_HISTORY
}