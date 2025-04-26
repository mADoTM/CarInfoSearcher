package ru.dolzhenkoms.carinfosearcher.service.searcher

import java.util.UUID
import ru.dolzhenkoms.carinfosearcher.model.dto.CarInfoDto
import ru.dolzhenkoms.carinfosearcher.model.jpa.types.SourceType

interface Searcher {

    fun findByVin(vin: String, userId: UUID): CarInfoDto?

    fun getType(): SourceType
}