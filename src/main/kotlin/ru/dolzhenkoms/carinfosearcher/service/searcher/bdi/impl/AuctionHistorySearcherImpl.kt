package ru.dolzhenkoms.carinfosearcher.service.searcher.bdi.impl

import com.fasterxml.jackson.databind.ObjectMapper
import java.util.UUID
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import ru.dolzhenkoms.carinfosearcher.client.AuctionHistoryClient
import ru.dolzhenkoms.carinfosearcher.configuration.cache.AUCTION_HISTORY
import ru.dolzhenkoms.carinfosearcher.configuration.consts.BODY_TYPE
import ru.dolzhenkoms.carinfosearcher.configuration.consts.DESCRIPTION
import ru.dolzhenkoms.carinfosearcher.configuration.consts.IMAGE_URL
import ru.dolzhenkoms.carinfosearcher.configuration.consts.KNOW_DAMAGES
import ru.dolzhenkoms.carinfosearcher.configuration.consts.MILEAGE
import ru.dolzhenkoms.carinfosearcher.configuration.consts.ORIGINAL_SOURCE_URL
import ru.dolzhenkoms.carinfosearcher.configuration.consts.TRANSMISSION
import ru.dolzhenkoms.carinfosearcher.model.dto.CarInfoDto
import ru.dolzhenkoms.carinfosearcher.model.jpa.HistoryQueueEntity
import ru.dolzhenkoms.carinfosearcher.model.jpa.types.SourceType
import ru.dolzhenkoms.carinfosearcher.repository.jpa.HistoryQueueEntityRepository
import ru.dolzhenkoms.carinfosearcher.service.searcher.bdi.AuctionHistorySearcher

@Service
class AuctionHistorySearcherImpl(
    private val client: AuctionHistoryClient,
    private val objectMapper: ObjectMapper,
    private val queueEntityRepository: HistoryQueueEntityRepository,
) : AuctionHistorySearcher {

    @Cacheable(AUCTION_HISTORY, key = "#vin")
    override fun findByVin(vin: String, userId: UUID): CarInfoDto? {
        val searchHttpResponse = runCatching { client.execute(query = vin) }.getOrNull()

        val body = searchHttpResponse?.parse()

        val carProductInfo =
            body?.getElementById("maincontent")?.getElementsByAttributeValue("itemtype", "http://schema.org/Product")

        val description =
            carProductInfo?.get(0)?.getElementsByAttributeValue("itemprop", "description")?.attr("content")
        val imageUrl = carProductInfo?.get(0)?.getElementsByAttributeValue("itemprop", "image")?.get(0)?.attr("href")

        val carInfo =
            body?.getElementById("maincontent")?.getElementsByAttributeValue("itemtype", "http://schema.org/Car")

        val bodyType = carInfo?.get(0)?.getElementsByAttributeValue("itemprop", "bodyType")?.attr("content")
        val mileage =
            carInfo?.get(0)?.getElementsByAttributeValue("itemprop", "mileageFromOdometer")?.attr("content")
        val knownVehicleDamages =
            carInfo?.get(0)?.getElementsByAttributeValue("itemprop", "knownVehicleDamages")?.attr("content")
        val transmission =
            carInfo?.get(0)?.getElementsByAttributeValue("itemprop", "vehicleTransmission")?.attr("content")

        val data = mapOf(
            DESCRIPTION to description,
            BODY_TYPE to bodyType,
            IMAGE_URL to imageUrl,
            MILEAGE to mileage,
            KNOW_DAMAGES to knownVehicleDamages,
            TRANSMISSION to transmission,
            ORIGINAL_SOURCE_URL to searchHttpResponse?.url()?.toString()
        )


        return body?.let {
            CarInfoDto(data)
        }?.also {
            val dataAsJson = objectMapper.writeValueAsString(data)

            queueEntityRepository.save(
                HistoryQueueEntity(
                    vin = vin,
                    data = dataAsJson,
                    sourceType = SourceType.AUCTION_HISTORY,
                    userCallId = userId,
                )
            )
        }
    }

    override fun getType() = SourceType.AUCTION_HISTORY
}