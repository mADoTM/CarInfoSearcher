package ru.dolzhenkoms.carinfosearcher.service.prettier

import ru.dolzhenkoms.carinfosearcher.model.jpa.HistoryEntity

interface VehicleHistoryPrettierService {

    fun prettyHistoryToTelegramAnswer(history: List<HistoryEntity>): List<String>
}