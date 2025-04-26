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
import java.time.Instant
import java.util.UUID
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import ru.dolzhenkoms.carinfosearcher.model.jpa.types.SourceType

@Entity
@Table(name = "history_entity")
@EntityListeners(AuditingEntityListener::class)
class HistoryEntity (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "vin", nullable = false, updatable = false)
    val vin: String,

    @Column(name = "value", nullable = true, updatable = false)
    val value: String?,

    @Column(name = "field_name", nullable = false, updatable = false)
    val fieldName: String,

    @Enumerated(EnumType.STRING)
    val sourceType: SourceType,

    @Column(name = "user_call_id")
    val userCallId: UUID?,

    @CreatedDate
    @Column(name = "created_ts", nullable = false, updatable = false)
    var createdTs: Instant? = null,

)