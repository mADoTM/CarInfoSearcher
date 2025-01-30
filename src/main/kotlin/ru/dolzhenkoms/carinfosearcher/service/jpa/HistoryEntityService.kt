package ru.dolzhenkoms.carinfosearcher.service.jpa

import ru.dolzhenkoms.carinfosearcher.model.jpa.HistoryEntity
import ru.dolzhenkoms.carinfosearcher.model.jpa.types.SourceType

interface HistoryEntityService {

    fun tryToFillHistoryByNewValues(vin: String, values: Map<String, String?>, sourceType: SourceType)

    fun findHistoryByVin(vin: String): List<HistoryEntity>
}
