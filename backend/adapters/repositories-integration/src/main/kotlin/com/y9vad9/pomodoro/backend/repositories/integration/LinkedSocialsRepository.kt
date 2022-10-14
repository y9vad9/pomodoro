package com.y9vad9.pomodoro.backend.repositories.integration

import com.y9vad9.pomodoro.backend.repositories.LinkedSocialsRepository
import com.y9vad9.pomodoro.backend.repositories.UsersRepository
import com.y9vad9.pomodoro.backend.repositories.integration.tables.LinkedGoogleAccountsTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import com.y9vad9.pomodoro.backend.repositories.LinkedSocialsRepository as Contract

class LinkedSocialsRepository(
    private val database: Database
) : Contract {
    override suspend fun getSocials(userId: UsersRepository.UserId): List<LinkedSocialsRepository.Social> {
        return newSuspendedTransaction(db = database) {
            val output = mutableListOf<LinkedSocialsRepository.Social>()
            LinkedGoogleAccountsTable.fromUser(userId)?.toExternal()
                ?.let { output.add(it) }

            return@newSuspendedTransaction output
        }
    }

    override suspend fun linkSocial(
        userId: UsersRepository.UserId,
        social: LinkedSocialsRepository.Social
    ): Unit = newSuspendedTransaction(db = database) {
        if (social is LinkedSocialsRepository.Social.Google)
            LinkedGoogleAccountsTable.insert(
                LinkedGoogleAccountsTable.LinkedGoogleAccount(
                    userId, social.accountId, social.email
                )
            )
        else error("Not supported")
    }

    override suspend fun getBySocial(
        social: LinkedSocialsRepository.Social
    ): UsersRepository.UserId? = newSuspendedTransaction(db = database) {
        return@newSuspendedTransaction when (social) {
            is LinkedSocialsRepository.Social.Google ->
                LinkedGoogleAccountsTable.getByAccountEmail(social.email)?.userId

            else -> null
        }
    }

    private fun LinkedGoogleAccountsTable.LinkedGoogleAccount.toExternal(): LinkedSocialsRepository.Social.Google {
        return LinkedSocialsRepository.Social.Google(accountId, email)
    }
}