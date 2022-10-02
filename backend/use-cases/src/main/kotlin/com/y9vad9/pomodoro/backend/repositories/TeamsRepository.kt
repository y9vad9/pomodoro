package com.y9vad9.pomodoro.backend.repositories

import com.y9vad9.pomodoro.backend.domain.entity.UserId

interface TeamsRepository {
    suspend fun create(name: String, ownerId: Int)
    suspend fun getMembers(teamId: TeamId): List<UserId>
    suspend fun getOwners(teamId: TeamId): List<UserId>
    suspend fun getGroups(userId: UserId): List<TeamId>
    suspend fun getGroupInfo(teamId: TeamId): GroupInfo

    @JvmInline
    value class TeamId(val int: Int)

    class GroupInfo(
        val groupId: Int,
        val membersCount: Int
    )
}