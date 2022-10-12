package com.y9vad9.pomodoro.backend.repositories

interface TimerInvitesRepository {
    suspend fun getInvites(timerId: TimersRepository.TimerId): List<Invite>
    suspend fun removeInvite(code: Code)
    suspend fun getInvite(code: Code): Invite?
    suspend fun setInviteLimit(code: Code, limit: Limit)

    suspend fun createInvite(
        timerId: TimersRepository.TimerId,
        code: Code,
        limit: Limit
    )

    class Invite(
        val timerId: TimersRepository.TimerId,
        val code: Code,
        val limit: Limit
    )

    /**
     * Invite code.
     */
    @JvmInline
    value class Code(val string: String)

    /**
     * Specifies max count of users that can be joined by specific [Code].
     */
    @JvmInline
    value class Limit(val int: Int)
}