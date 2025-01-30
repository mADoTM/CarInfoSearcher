package ru.dolzhenkoms.carinfosearcher.configuration.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("telegram")
class TelegramBotProperties {
    lateinit var token: String

    lateinit var botName: String
}