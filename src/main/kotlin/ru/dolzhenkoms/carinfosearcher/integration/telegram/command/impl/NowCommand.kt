package ru.dolzhenkoms.carinfosearcher.integration.telegram.command.impl

import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import ru.dolzhenkoms.carinfosearcher.integration.telegram.command.BotCommand
import ru.dolzhenkoms.carinfosearcher.integration.telegram.command.CommandType
import ru.dolzhenkoms.carinfosearcher.service.prettier.CarInfoDtoPrettierService
import ru.dolzhenkoms.carinfosearcher.service.searcher.SearchVehicleInfoService

@Component
class NowCommand(
    private val searchInfoService: SearchVehicleInfoService,
    private val prettier: CarInfoDtoPrettierService,
) : BotCommand {
    override fun execute(update: Update): List<SendMessage> {
        val chatId = update.message.chatId?.toString().toString()

        val textRawsFromUpdate = update.message.text?.split(" ")

        val vin = textRawsFromUpdate?.get(1) ?: throw IllegalArgumentException("Некорректно передан VIN")

        val foundedInfos = searchInfoService.searchInfo(vin)

        if (foundedInfos.isEmpty()) return buildNotFoundMessage(chatId)

        val prettyInfos = prettier.prettyInfos(foundedInfos)

        return prettyInfos.map { text ->
            SendMessage().apply {
                this.chatId = chatId
                this.text = text
            }
        }
    }

    override fun getCommandName() = CommandType.NOW.title

    override fun getCommandDescription() =
        """Возвращает данные об авто из открытых источников.
    
Пример: /now 5XXGT4L36LG451441
    
Примечание: запрос к источникам происходит только раз в день, если ранее был получен успешный ответ🕒
В ином случае возвращается закэшированный ответ
    """.trimMargin()

    private fun buildNotFoundMessage(chatId: String) = listOf(
        SendMessage().apply {
            this.chatId = chatId
            this.text = "По вашему запросу не ответил ни один из источников"
        }
    )
}