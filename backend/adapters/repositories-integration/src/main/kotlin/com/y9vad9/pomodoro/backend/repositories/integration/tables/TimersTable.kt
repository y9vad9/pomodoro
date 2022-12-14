package com.y9vad9.pomodoro.backend.repositories.integration.tables

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object TimersTable : Table() {

    val TIMER_ID = integer("timer_id")
        .autoIncrement()

    val TIMER_NAME = varchar("timer_name", 50)

    val OWNER_ID = integer("owner_id")
        .references(UsersTable.USER_ID, onDelete = ReferenceOption.CASCADE)

    val CREATION_TIME = long("creation_time")

    val WORK_TIME = long("work_time")
    val REST_TIME = long("rest_time")
    val BIG_REST_TIME = long("big_rest_time")
    val BIG_REST_TIME_ENABLED = bool("big_rest_time_enabled")
    val BIG_REST_PER = integer("big_rest_per")
    val IS_EVERYONE_CAN_PAUSE = bool("is_everyone_can_pause")
    val IS_CONFIRMATION_REQUIRED = bool("is_confirmation_required")

    override val primaryKey: PrimaryKey = PrimaryKey(TIMER_ID)
}