package com.y9vad9.pomodoro.backend.application.routes

import com.y9vad9.pomodoro.backend.application.routes.auth.authRoot
import com.y9vad9.pomodoro.backend.application.routes.timer.timersRoot
import com.y9vad9.pomodoro.backend.codes.integration.SecureCodeProvider
import com.y9vad9.pomodoro.backend.google.auth.GoogleClient
import com.y9vad9.pomodoro.backend.providers.SecureAccessTokenProvider
import com.y9vad9.pomodoro.backend.providers.SecureRefreshTokenProvider
import com.y9vad9.pomodoro.backend.providers.SystemCurrentTimeProvider
import com.y9vad9.pomodoro.backend.repositories.integration.*
import com.y9vad9.pomodoro.backend.repositories.integration.datasource.AuthorizationsDataSource
import com.y9vad9.pomodoro.backend.repositories.integration.datasource.TimerInvitesDataSource
import com.y9vad9.pomodoro.backend.repositories.integration.datasource.TimersDatabaseDataSource
import com.y9vad9.pomodoro.backend.repositories.integration.datasource.UsersDatabaseDataSource
import com.y9vad9.pomodoro.backend.usecases.auth.AuthViaGoogleUseCase
import com.y9vad9.pomodoro.backend.usecases.auth.GetUserIdByAccessTokenUseCase
import com.y9vad9.pomodoro.backend.usecases.auth.RefreshTokenUseCase
import com.y9vad9.pomodoro.backend.usecases.auth.RemoveAccessTokenUseCase
import com.y9vad9.pomodoro.backend.usecases.timers.*
import com.y9vad9.pomodoro.backend.usecases.timers.events.GetLastEventsUseCase
import com.y9vad9.pomodoro.backend.usecases.timers.invites.CreateInviteUseCase
import com.y9vad9.pomodoro.backend.usecases.timers.invites.GetInvitesUseCase
import com.y9vad9.pomodoro.backend.usecases.timers.invites.JoinByInviteUseCase
import com.y9vad9.pomodoro.backend.usecases.timers.invites.RemoveInviteUseCase
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.Database
import java.time.ZoneId
import java.util.*

fun Routing.setupRoutes(database: Database, googleClient: GoogleClient) {
    val authRepository = AuthorizationsRepository(AuthorizationsDataSource(database))
    val linkedSocialsRepository = LinkedSocialsRepository(database)
    val timerInvitesRepository = TimerInvitesRepository(TimerInvitesDataSource(database))
    val timersRepository = TimersRepository(TimersDatabaseDataSource(database))
    val usersRepository = UsersRepository(UsersDatabaseDataSource(database))

    val timeProvider =
        SystemCurrentTimeProvider(TimeZone.getTimeZone(ZoneId.of("Ukraine/Kiev")))
    val accessTokenProvider = SecureAccessTokenProvider
    val refreshTokenProvider = SecureRefreshTokenProvider
    val codesProvider = SecureCodeProvider

    authRoot(
        AuthViaGoogleUseCase(
            linkedSocialsRepository,
            usersRepository,
            accessTokenProvider,
            authRepository,
            timeProvider,
            refreshTokenProvider,
            googleClient
        ),
        RemoveAccessTokenUseCase(authRepository),
        GetUserIdByAccessTokenUseCase(authRepository, timeProvider),
        RefreshTokenUseCase(
            accessTokenProvider, authRepository, timeProvider
        )
    )

    timersRoot(
        CreateTimerUseCase(
            timersRepository,
            timeProvider
        ),
        GetTimersUseCase(timersRepository),
        GetTimerUseCase(timersRepository),
        RemoveTimerUseCase(timersRepository),
        SetTimerSettingsUseCase(timersRepository),
        StartTimerUseCase(timersRepository, timeProvider),
        StopTimerUseCase(timersRepository, timeProvider),
        CreateInviteUseCase(timerInvitesRepository, timersRepository, codesProvider),
        GetInvitesUseCase(timerInvitesRepository, timersRepository),
        JoinByInviteUseCase(timerInvitesRepository, timersRepository),
        RemoveInviteUseCase(timerInvitesRepository, timersRepository),
        GetLastEventsUseCase(timersRepository),
        GetEventUpdatesUseCase(timersRepository)
    )

    ok()
}