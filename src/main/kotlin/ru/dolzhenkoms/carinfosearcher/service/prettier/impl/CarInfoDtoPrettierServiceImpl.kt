package ru.dolzhenkoms.carinfosearcher.service.prettier.impl

import org.springframework.stereotype.Service
import ru.dolzhenkoms.carinfosearcher.model.dto.CarInfoDto
import ru.dolzhenkoms.carinfosearcher.model.jpa.types.SourceType
import ru.dolzhenkoms.carinfosearcher.service.prettier.CarInfoDtoPrettierService

@Service
class CarInfoDtoPrettierServiceImpl : CarInfoDtoPrettierService {
    override fun prettyInfos(infos: Map<SourceType, CarInfoDto?>) = infos
        .filter { it.value != null }
        .map { info ->
            "${info.key.title}:\n${prettyInfo(info.value!!)}"
        }

    private fun prettyInfo(info: CarInfoDto) =
        info.data
            .filter { it.value != null }
            .toList()
            .joinToString(separator = "\n") { "${it.first} - ${it.second!!}" }
}