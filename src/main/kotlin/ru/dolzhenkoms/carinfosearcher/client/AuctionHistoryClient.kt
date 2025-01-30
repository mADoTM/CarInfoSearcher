package ru.dolzhenkoms.carinfosearcher.client

import org.jsoup.Connection
import org.jsoup.Jsoup
import org.springframework.stereotype.Component
import ru.dolzhenkoms.carinfosearcher.configuration.properties.AuctionHistoryRestProperties
import ru.dolzhenkoms.carinfosearcher.configuration.properties.HttpProperties

@Component
class AuctionHistoryClient(
    private val auctionHistoryRestProperties: AuctionHistoryRestProperties,
    private val httpProperties: HttpProperties,
) {

    fun execute(query: String): Connection.Response {
        val fullSearchUrl = "${auctionHistoryRestProperties.url}$query"

        return Jsoup.connect(fullSearchUrl)
            .method(Connection.Method.GET)
            .timeout(auctionHistoryRestProperties.timeout)
            .userAgent(httpProperties.userAgent)
            .execute()
    }
}