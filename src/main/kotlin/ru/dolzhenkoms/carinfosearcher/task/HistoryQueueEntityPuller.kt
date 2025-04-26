package ru.dolzhenkoms.carinfosearcher.task

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.transaction.Transactional
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import ru.dolzhenkoms.carinfosearcher.repository.jpa.HistoryQueueEntityRepository
import ru.dolzhenkoms.carinfosearcher.service.jpa.HistoryEntityService


@Component
class HistoryQueueEntityPuller(
    private val queueRepository: HistoryQueueEntityRepository,
    private val historyService: HistoryEntityService,
    private val objectMapper: ObjectMapper,
) {

    @Scheduled(cron = "\${queue-puller.cron}")
    @Transactional
    fun fillHistoryByNewValues() {
        val newValues = queueRepository.select1RandomEntity() ?: return

        val typeRef = object : TypeReference<HashMap<String, String?>>() {}
        val historyValues = runCatching { objectMapper.readValue(newValues.data, typeRef) }.getOrNull() ?: return

        historyService.tryToFillHistoryByNewValues(
            newValues.vin,
            historyValues,
            newValues.sourceType,
            newValues.userCallId!!,
        )

        queueRepository.delete(newValues)
    }
}