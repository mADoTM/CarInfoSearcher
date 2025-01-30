package ru.dolzhenkoms.carinfosearcher.configuration.properties

import org.springframework.boot.context.properties.ConfigurationProperties

sealed class RestProperties {
    open lateinit var url: String

    open var timeout: Int = 0
}

@ConfigurationProperties("rest.bdi-history")
class BdiHistoryRestProperties : RestProperties()

@ConfigurationProperties("rest.auction-history")
class AuctionHistoryRestProperties : RestProperties()