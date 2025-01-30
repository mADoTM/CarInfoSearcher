package ru.dolzhenkoms.carinfosearcher.client

import org.jsoup.Connection
import org.jsoup.Jsoup
import org.springframework.stereotype.Component
import ru.dolzhenkoms.carinfosearcher.configuration.properties.BdiHistoryRestProperties
import ru.dolzhenkoms.carinfosearcher.configuration.properties.HttpProperties

@Component
class BdiHistoryClient(
    private val bdiHistoryRestProperties: BdiHistoryRestProperties,
    private val httpProperties: HttpProperties,
) {

    fun execute(query: String): Connection.Response {
        val fullSearchUrl = "${bdiHistoryRestProperties.url}$query"

        return Jsoup.connect(fullSearchUrl)
            .method(Connection.Method.GET)
            .timeout(bdiHistoryRestProperties.timeout)
            .userAgent(httpProperties.userAgent)
            .execute()
    }
}

