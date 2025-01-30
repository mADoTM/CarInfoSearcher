package ru.dolzhenkoms.carinfosearcher.service.searcher

import ru.dolzhenkoms.carinfosearcher.model.dto.CarInfoDto
import ru.dolzhenkoms.carinfosearcher.model.jpa.types.SourceType

interface Searcher {

    fun findByVin(vin: String): CarInfoDto?

    fun getType(): SourceType
}