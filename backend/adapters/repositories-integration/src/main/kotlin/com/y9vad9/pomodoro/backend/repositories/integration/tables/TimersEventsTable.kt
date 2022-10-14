package com.y9vad9.pomodoro.backend.repositories.integration.tables

import com.y9vad9.pomodoro.backend.repositories.integration.datasource.TimersDatabaseDataSource
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.select

internal object TimersEventsTable : Table() {
    val EVENT_ID = integer("timer_event_id")
        .autoIncrement()

    val TIMER_ID = integer("timer_id")
        .references(TimersTable.TIMER_ID, onDelete = ReferenceOption.CASCADE)

    val EVENT_TYPE = enumeration<TimersDatabaseDataSource.Timer.Event.Type>("event_type")

    val STARTED_AT = long("started_at")
    val FINISHES_AT = long("finishes_at").nullable()


    fun selectLastEvent(timerId: Int) = select { TIMER_ID eq timerId }
}