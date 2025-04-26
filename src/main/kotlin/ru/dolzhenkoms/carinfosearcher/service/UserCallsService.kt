package ru.dolzhenkoms.carinfosearcher.service

import java.util.UUID
import org.springframework.stereotype.Service
import ru.dolzhenkoms.carinfosearcher.integration.telegram.command.CommandType
import ru.dolzhenkoms.carinfosearcher.model.jpa.UserCallsEntity
import ru.dolzhenkoms.carinfosearcher.repository.jpa.UserCallsRepository

@Service
class UserCallsService(
    private val repository: UserCallsRepository
) {

    fun saveUserCall(username: String, command: CommandType): UUID =
        repository.save(
            UserCallsEntity(
                username = username,
                command = command
            )
        ).id ?: throw RuntimeException("Something went wrong in saving username")

}