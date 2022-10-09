package com.y9vad9.pomodoro.backend.repositories.integration.tables

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.select

object TimerParticipantsTable : Table() {
    val TIMER_ID = integer("timer_id")
        .references(TimersTable.TIMER_ID, onDelete = ReferenceOption.CASCADE)
    val PARTICIPANT_ID = integer("")
        .references(UsersTable.USER_ID, onDelete = ReferenceOption.CASCADE)

    private val uniqueKey = uniqueIndex(TIMER_ID, PARTICIPANT_ID)

    fun selectParticipantsOf(timerId: Int) = TimerParticipantsTable
        .select { TIMER_ID eq timerId }
}