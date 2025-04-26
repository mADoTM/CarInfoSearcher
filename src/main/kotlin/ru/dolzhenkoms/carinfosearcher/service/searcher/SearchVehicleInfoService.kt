package ru.dolzhenkoms.carinfosearcher.service.searcher

import java.util.UUID
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

    fun searchInfo(vin: String, userId: UUID): Map<SourceType, CarInfoDto?> {
        return runBlocking {
            searchers.map { searcher ->
                async {
                    searcher.getType() to searcher.findByVin(vin, userId)
                }
            }.awaitAll()
        }.toMap()
    }
}