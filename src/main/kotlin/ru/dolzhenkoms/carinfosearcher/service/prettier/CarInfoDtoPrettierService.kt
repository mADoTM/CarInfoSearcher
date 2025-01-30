package ru.dolzhenkoms.carinfosearcher.service.prettier

import ru.dolzhenkoms.carinfosearcher.model.dto.CarInfoDto
import ru.dolzhenkoms.carinfosearcher.model.jpa.types.SourceType

interface CarInfoDtoPrettierService {

    fun prettyInfos(infos: Map<SourceType, CarInfoDto?>): List<String>
}