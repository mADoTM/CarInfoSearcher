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
import ru.dolzhenkoms.carinfosearcher.integration.telegram.command.CommandType

@Entity
@Table(name = "user_calls")
@EntityListeners(AuditingEntityListener::class)
class UserCallsEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: UUID? = null,

    @Column(name = "username", nullable = false, updatable = false)
    val username: String,

    @Column(name = "command_name", nullable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    val command: CommandType,

    @CreatedDate
    @Column(name = "created_ts", nullable = false, updatable = false)
    var created: Instant? = null

)