package ru.dolzhenkoms.carinfosearcher.integration.telegram.command

import org.springframework.stereotype.Component

@Component
class CommandFactory(
    botCommands: List<BotCommand>,
) {

    private val commands = botCommands.associateBy { command -> command.getCommandName() }

    fun getCommandByString(command: String): BotCommand =
        commands[command] ?: throw NoSuchElementException("Not found command $command")
}