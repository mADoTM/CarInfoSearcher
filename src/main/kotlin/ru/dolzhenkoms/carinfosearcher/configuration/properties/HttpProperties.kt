package ru.dolzhenkoms.carinfosearcher.configuration.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("http")
class HttpProperties {
    lateinit var userAgent: String
}