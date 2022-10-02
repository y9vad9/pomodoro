package com.y9vad9.pomodoro.backend.domain

import com.y9vad9.pomodoro.backend.domain.entity.Identifier

class Group(
    val owners: Set<Identifier>
)