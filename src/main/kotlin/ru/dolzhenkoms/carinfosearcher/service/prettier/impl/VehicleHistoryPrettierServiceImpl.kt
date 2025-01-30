package ru.dolzhenkoms.carinfosearcher.service.prettier.impl

import org.springframework.stereotype.Service
import ru.dolzhenkoms.carinfosearcher.model.jpa.HistoryEntity
import ru.dolzhenkoms.carinfosearcher.service.prettier.VehicleHistoryPrettierService
import ru.dolzhenkoms.carinfosearcher.utils.toLocalDate

@Service
class VehicleHistoryPrettierServiceImpl : VehicleHistoryPrettierService {

    override fun prettyHistoryToTelegramAnswer(history: List<HistoryEntity>): List<String> {
        val historyBySource = history.groupBy { it.sourceType }

        val mapBySourceAndFieldNames = historyBySource.map { listOfSource ->
            listOfSource.key to listOfSource.value.sortedBy { it.createdTs }.groupBy { it.fieldName }
        }.toMap()

        return mapBySourceAndFieldNames.map { sourceWithFields ->
            "${sourceWithFields.key.title}\n${prettyListOfFieldsHistory(sourceWithFields.value)}"
        }
    }

    private fun prettyListOfFieldsHistory(fieldsHistory: Map<String, List<HistoryEntity>>): String {
        return fieldsHistory.map { prettyListOfOneFieldHistory(it.key, it.value) }.joinToString(separator = "\n") { it }
    }

    private fun prettyListOfOneFieldHistory(fieldName: String, history: List<HistoryEntity>): String {
        return "$fieldName:\n" +
                history.joinToString(separator = "\n") { "${it.createdTs?.toLocalDate() ?: ""} - ${it.value} " }
    }
}