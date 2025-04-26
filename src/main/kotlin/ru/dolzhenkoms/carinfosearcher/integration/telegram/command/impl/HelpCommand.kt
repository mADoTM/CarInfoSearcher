package ru.dolzhenkoms.carinfosearcher.integration.telegram.command.impl

import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import ru.dolzhenkoms.carinfosearcher.integration.telegram.command.BotCommand
import ru.dolzhenkoms.carinfosearcher.integration.telegram.command.CommandType
import ru.dolzhenkoms.carinfosearcher.service.UserCallsService

@Component
class HelpCommand(
    private var commandsList: List<BotCommand>,
    private val userCallsService: UserCallsService,
) : BotCommand {

    @PostConstruct
    fun addMyself() {
        commandsList += this
    }

    override fun execute(update: Update): List<SendMessage> = listOf(SendMessage().apply {
        chatId = update.message.chatId?.toString().toString()

        text = commandsList.joinToString(separator = "\n~~~~~~~~~\n") { command ->
            "${command.getCommandName()} - ${command.getCommandDescription()}\n"
        }
    }).also {
        userCallsService.saveUserCall(update.message?.from?.userName ?: "[bad username]", CommandType.HELP)
    }

    override fun getCommandName() = CommandType.HELP.title

    override fun getCommandDescription() =
        "Справка по командам"
}