package ru.dolzhenkoms.carinfosearcher.service.searcher

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service
import ru.dolzhenkoms.carinfosearcher.model.dto.CarInfoDto
import ru.dolzhenkoms.carinfosearcher.model.jpa.types.SourceType

@Service
class SearchVehicleInfoService(
    private val searchers: List<Searcher>
) {

    fun searchInfo(vin: String): Map<SourceType, CarInfoDto?> {
        return runBlocking {
            searchers.map { searcher ->
                async {
                    searcher.getType() to searcher.findByVin(vin)
                }
            }.awaitAll()
        }.toMap()
    }
}