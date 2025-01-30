package ru.dolzhenkoms.carinfosearcher.repository.jpa

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import ru.dolzhenkoms.carinfosearcher.model.jpa.HistoryQueueEntity

interface HistoryQueueEntityRepository : JpaRepository<HistoryQueueEntity, Long> {

    @Query(
        nativeQuery = true,
        value = "SELECT * FROM history_queue_entity LIMIT 1 FOR UPDATE SKIP LOCKED"
    )
    fun select1RandomEntity(): HistoryQueueEntity?
}