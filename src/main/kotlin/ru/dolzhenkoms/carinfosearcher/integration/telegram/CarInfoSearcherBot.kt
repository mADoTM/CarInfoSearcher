package ru.dolzhenkoms.carinfosearcher.integration.telegram

import org.springframework.stereotype.Component
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import ru.dolzhenkoms.carinfosearcher.configuration.properties.TelegramBotProperties
import ru.dolzhenkoms.carinfosearcher.integration.telegram.command.CommandFactory
import ru.dolzhenkoms.carinfosearcher.integration.telegram.command.CommandType

@Component
class CarInfoSearcherBot(
    private val telegramBotProperties: TelegramBotProperties,
    private val commandFactory: CommandFactory,
) : TelegramLongPollingBot(telegramBotProperties.token) {

    override fun getBotUsername(): String {
        return telegramBotProperties.botName
    }

    override fun onUpdateReceived(update: Update?) {
        if (update == null || update.hasCallbackQuery() || !update.hasMessage()) return

        handleMessage(update)
    }

    private fun handleMessage(update: Update) {
        val messageText = update.message.text

        val commandText = messageText.split(" ")[0]

        try {
            val command = commandFactory.getCommandByString(commandText)

            command.execute(update).forEach { message ->
                executeMessage(message)
            }

        } catch (e: Exception) {
            val errorMessage = e.message ?: "Что-то пошло не так во время выполнения команды"
            executeErrorMessage(errorMessage, update.message.chatId)
            executeCommandsList(update)
        }
    }

    private fun executeCommandsList(update: Update) {
        val commandsList = commandFactory.getCommandByString(CommandType.HELP.title)
        commandsList.execute(update).forEach { message ->
            executeMessage(message)
        }
    }

    private fun executeErrorMessage(message: String, chatId: Long) {
        executeMessage(SendMessage().apply {
            this.text = message
            this.chatId = chatId.toString()
        })
    }

    private fun executeMessage(message: SendMessage?) {
        execute(message)
    }
}