package ru.dolzhenkoms.carinfosearcher.service.jpa

import java.util.UUID
import ru.dolzhenkoms.carinfosearcher.model.jpa.HistoryEntity
import ru.dolzhenkoms.carinfosearcher.model.jpa.types.SourceType

interface HistoryEntityService {

    fun tryToFillHistoryByNewValues(vin: String, values: Map<String, String?>, sourceType: SourceType, userId: UUID)

    fun findHistoryByVin(vin: String): List<HistoryEntity>
}
