package ru.dolzhenkoms.carinfosearcher.configuration.telegram

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession
import ru.dolzhenkoms.carinfosearcher.integration.telegram.CarInfoSearcherBot

@Configuration
class TelegramBotConfiguration {

    @Bean
    fun telegramBotsApi(bot: CarInfoSearcherBot) =
        TelegramBotsApi(DefaultBotSession::class.java).apply {
            registerBot(bot)
        }
}