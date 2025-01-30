package ru.dolzhenkoms.carinfosearcher.model.jpa

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import ru.dolzhenkoms.carinfosearcher.model.jpa.types.SourceType

@Entity
@Table(name = "history_queue_entity")
@EntityListeners(AuditingEntityListener::class)
class HistoryQueueEntity (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "vin", nullable = false, updatable = false)
    val vin: String,

    @Column(name = "data", nullable = true, updatable = false)
    val data: String?,

    @Enumerated(EnumType.STRING)
    val sourceType: SourceType,
)