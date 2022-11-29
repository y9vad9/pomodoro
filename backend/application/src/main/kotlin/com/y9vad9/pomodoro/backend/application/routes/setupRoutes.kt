package com.y9vad9.pomodoro.backend.application.routes

import com.y9vad9.pomodoro.backend.application.plugins.AuthorizationPlugin
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
import com.y9vad9.pomodoro.backend.usecases.timers.invites.CreateInviteUseCase
import com.y9vad9.pomodoro.backend.usecases.timers.invites.GetInvitesUseCase
import com.y9vad9.pomodoro.backend.usecases.timers.invites.JoinByInviteUseCase
import com.y9vad9.pomodoro.backend.usecases.timers.invites.RemoveInviteUseCase
import io.ktor.server.application.*
import io.ktor.server.routing.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.jetbrains.exposed.sql.Database
import java.time.ZoneId
import java.util.*

fun Routing.setupRoutes(
    authRepository: AuthorizationsRepository,
    linkedSocialsRepository: LinkedSocialsRepository,
    timerInvitesRepository: TimerInvitesRepository,
    timersRepository: TimersRepository,
    usersRepository: UsersRepository,
    sessionsRepository: SessionsRepository,
    schedulesRepository: SchedulesRepository,
    googleClient: GoogleClient
) {
    val timeProvider =
        SystemCurrentTimeProvider(TimeZone.getTimeZone(ZoneId.of("Europe/Kiev")))
    val accessTokenProvider = SecureAccessTokenProvider
    val refreshTokenProvider = SecureRefreshTokenProvider
    val codesProvider = SecureCodeProvider

    application.install(AuthorizationPlugin) {
        authorize = GetUserIdByAccessTokenUseCase(authRepository, timeProvider)
    }

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
        SetTimerSettingsUseCase(timersRepository, sessionsRepository),
        StartTimerUseCase(timersRepository, timeProvider, sessionsRepository),
        StopTimerUseCase(timersRepository, sessionsRepository),
        CreateInviteUseCase(timerInvitesRepository, timersRepository, codesProvider),
        GetInvitesUseCase(timerInvitesRepository, timersRepository),
        JoinByInviteUseCase(timerInvitesRepository, timersRepository),
        RemoveInviteUseCase(timerInvitesRepository, timersRepository),
        JoinSessionUseCase(timersRepository, sessionsRepository, schedulesRepository, timeProvider),
        LeaveSessionUseCase(sessionsRepository, schedulesRepository),
        ConfirmStartUseCase(timersRepository, sessionsRepository, timeProvider)
    )
    ok()
}

fun Routing.setupRoutesWithDatabase(
    database: Database,
    googleClient: GoogleClient
) {
    val authRepository = AuthorizationsRepository(AuthorizationsDataSource(database))
    val linkedSocialsRepository = LinkedSocialsRepository(database)
    val timerInvitesRepository = TimerInvitesRepository(TimerInvitesDataSource(database))
    val timersRepository = TimersRepository(TimersDatabaseDataSource(database))
    val usersRepository = UsersRepository(UsersDatabaseDataSource(database))
    val schedulesRepository = SchedulesRepository(
        CoroutineScope(Dispatchers.Default + SupervisorJob())
    )
    val sessionsRepository =
        SessionsRepository(
            timersRepository,
            CoroutineScope(Dispatchers.Default + SupervisorJob())
        )

    setupRoutes(
        authRepository,
        linkedSocialsRepository,
        timerInvitesRepository,
        timersRepository,
        usersRepository,
        sessionsRepository,
        schedulesRepository,
        googleClient
    )
}