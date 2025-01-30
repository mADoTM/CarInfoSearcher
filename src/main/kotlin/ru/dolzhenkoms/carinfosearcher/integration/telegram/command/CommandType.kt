package ru.dolzhenkoms.carinfosearcher.integration.telegram.command

enum class CommandType(val title: String) {
    HELP("/help"),
    NOW("/now"),
    HISTORY("/history");
}