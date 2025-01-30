package ru.dolzhenkoms.carinfosearcher.service.jpa.impl

import org.springframework.stereotype.Service
import ru.dolzhenkoms.carinfosearcher.model.jpa.HistoryEntity
import ru.dolzhenkoms.carinfosearcher.model.jpa.types.SourceType
import ru.dolzhenkoms.carinfosearcher.repository.jpa.HistoryEntityRepository
import ru.dolzhenkoms.carinfosearcher.service.jpa.HistoryEntityService
import java.time.Instant

@Service
class HistoryEntityServiceImpl(
    private val repository: HistoryEntityRepository
) : HistoryEntityService {

    override fun tryToFillHistoryByNewValues(vin: String, values: Map<String, String?>, sourceType: SourceType) {
        val actualValues = repository.findNewestByVinAndSourceType(vin, sourceType.name)
            .associateBy { entity -> entity.fieldName }

        values.filterValues { it != null }.map { pair ->
            val isHistoricValueActual = actualValues[pair.key]?.createdTs?.isBefore(
                Instant.now().minusMillis(
                    THIRTY_DAYS_IN_MILLISECONDS
                )
            ) == false

            if (pair.value != null && !isHistoricValueActual)
                pair.key to pair.value
            else
                null
        }.filterNotNull().forEach {
            repository.save(
                HistoryEntity(
                    vin = vin,
                    sourceType = sourceType,
                    value = it.second,
                    fieldName = it.first
                )
            )
        }
    }

    override fun findHistoryByVin(vin: String) = repository.findAllByVin(vin)

    private companion object {
        private const val THIRTY_DAYS_IN_MILLISECONDS = 30L * 24 * 60 * 60 * 1000
    }

}