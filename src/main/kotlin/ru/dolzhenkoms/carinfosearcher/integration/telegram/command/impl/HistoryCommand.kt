package ru.dolzhenkoms.carinfosearcher.integration.telegram.command.impl

import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import ru.dolzhenkoms.carinfosearcher.integration.telegram.command.BotCommand
import ru.dolzhenkoms.carinfosearcher.integration.telegram.command.CommandType
import ru.dolzhenkoms.carinfosearcher.service.UserCallsService
import ru.dolzhenkoms.carinfosearcher.service.jpa.HistoryEntityService
import ru.dolzhenkoms.carinfosearcher.service.prettier.VehicleHistoryPrettierService

@Component
class HistoryCommand(
    private val historyService: HistoryEntityService,
    private val prettierService: VehicleHistoryPrettierService,
    private val userCallsService: UserCallsService,
) : BotCommand {
    override fun execute(update: Update): List<SendMessage> {
        userCallsService.saveUserCall(
            update.message?.from?.userName ?: "[bad username]",
            CommandType.HISTORY
        )

        val chatId = update.message.chatId?.toString().toString()

        val textRawsFromUpdate = update.message.text?.split(" ")

        val vin = textRawsFromUpdate?.get(1) ?: throw IllegalArgumentException("Некорректно передан VIN")

        return prettierService.prettyHistoryToTelegramAnswer(historyService.findHistoryByVin(vin))
            .map { prettyHistory ->
                SendMessage().apply {
                    this.chatId = chatId
                    this.text = prettyHistory
                }
            }
            .ifEmpty {
                buildNotFoundMessage(chatId)
            }
    }

    override fun getCommandName() = CommandType.HISTORY.title

    override fun getCommandDescription() =
        """
        Поиск истории авто по VIN из открытых источников, лежащих в БД
        
        Например: /history 5XXGT4L36LG451441
        """.trimIndent()

    private fun buildNotFoundMessage(chatId: String) = listOf(
        SendMessage().apply {
            this.chatId = chatId
            this.text = "По вашему запросу не была найдена история"
        }
    )
}