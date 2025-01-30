package ru.dolzhenkoms.carinfosearcher.utils

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

fun Instant?.toLocalDate() =
    runCatching { LocalDate.ofInstant(this, ZoneId.of(MOSCOW_TIMEZONE)) }.getOrNull()

const val MOSCOW_TIMEZONE = "Europe/Moscow"