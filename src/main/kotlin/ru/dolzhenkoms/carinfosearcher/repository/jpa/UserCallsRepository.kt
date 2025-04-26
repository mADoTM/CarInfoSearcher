package ru.dolzhenkoms.carinfosearcher.repository.jpa

import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository
import ru.dolzhenkoms.carinfosearcher.model.jpa.UserCallsEntity

interface UserCallsRepository : JpaRepository<UserCallsEntity, UUID>