package ru.dolzhenkoms.carinfosearcher.integration.telegram.command

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update

interface BotCommand {

    fun execute(update: Update): List<SendMessage>

    fun getCommandName(): String

    fun getCommandDescription(): String
}