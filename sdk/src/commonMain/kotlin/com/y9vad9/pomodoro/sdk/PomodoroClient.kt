package com.y9vad9.pomodoro.sdk

import com.y9vad9.pomodoro.sdk.exceptions.AuthorizationException
import com.y9vad9.pomodoro.sdk.exceptions.BadRequestException
import com.y9vad9.pomodoro.sdk.exceptions.ConnectionException
import com.y9vad9.pomodoro.sdk.results.*
import com.y9vad9.pomodoro.sdk.results.serializer.ResultsSerializersModule
import com.y9vad9.pomodoro.sdk.types.TimerSettings
import com.y9vad9.pomodoro.sdk.types.serializer.TypesSerializersModule
import com.y9vad9.pomodoro.sdk.types.value.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.plus

/**
 * Pomodoro API Client.
 * @param client custom client.
 */
public class PomodoroClient(
    private val client: HttpClient
) {
    public constructor(url: String) : this(HttpClient {
        defaultRequest {
            url(url)
            contentType(ContentType.parse("application/json"))
        }

        install(HttpTimeout) {
            connectTimeoutMillis = 15000L
            requestTimeoutMillis = 15000L
            socketTimeoutMillis = 15000L
        }

        install(ContentNegotiation) {
            json(Json {
                explicitNulls = false
                serializersModule = ResultsSerializersModule + TypesSerializersModule
            })
        }
    })

    public constructor() : this("https://pomodoro.y9vad9.com")

    public suspend fun authViaGoogle(code: Code): SignWithGoogleResult {
        return client.post("auth/google") {
            parameter("code", code)
        }.toResult()
    }

    /**
     * Checks whether host is available or not.
     */
    public suspend fun ok(): Boolean = client.get("ok").status.isSuccess()

    /**
     * Gets user id by [accessToken].
     */
    public suspend fun getUserId(accessToken: AccessToken): GetUserIdResult {
        return client.post("auth/user-id") {
            header(HttpHeaders.Authorization, accessToken)
        }.toResult()
    }

    /**
     * Removes current authorization by [accessToken].
     */
    public suspend fun removeToken(accessToken: AccessToken): RemoveTokenResult {
        return client.delete("auth") {
            header(HttpHeaders.Authorization, accessToken)
        }.toResult()
    }

    /**
     * Renews access token by [refreshToken].
     * Old token will no longer usable.
     */
    public suspend fun renewToken(refreshToken: RefreshToken): RenewTokenResult {
        return client.post("auth/renew") {
            parameter("refresh_token", refreshToken)
        }.toResult()
    }

    /**
     * Creates timer by [accessToken].
     * @param name name of a timer (max 50 symbols)
     * @param settings timer settings
     */
    public suspend fun createTimer(
        accessToken: AccessToken,
        name: Name,
        settings: TimerSettings
    ): CreateTimerResult {
        return client.post("timers") {
            header(HttpHeaders.Authorization, accessToken)
            setBody(CreateTimerRequest(name, settings))
        }.toResult()
    }

    /**
     * Gets timer by [timerId].
     * @param timerId unique identifier of a timer.
     */
    public suspend fun getTimer(
        accessToken: AccessToken,
        timerId: TimerId
    ): GetTimerResult {
        return client.get("timers") {
            header(HttpHeaders.Authorization, accessToken)
            parameter("timer_id", timerId)
        }.toResult()
    }

    /**
     * Gets user's timers by [accessToken].
     */
    public suspend fun getTimers(
        accessToken: AccessToken
    ): GetTimersResult {
        return client.get("timers/all") {
            header(HttpHeaders.Authorization, accessToken)
        }.toResult()
    }

    /**
     * Removes timer by [timerId] with [accessToken].
     * @param timerId unique identifier of a timer.
     */
    public suspend fun removeTimer(
        accessToken: AccessToken,
        timerId: TimerId
    ): RemoveTimerResult {
        return client.delete("timers") {
            header(HttpHeaders.Authorization, accessToken)
            parameter("timer_id", timerId.int)
        }.toResult()
    }

    /**
     * Sets new timer settings.
     * @param timerId unique identifier of a timer.
     */
    public suspend fun setTimerSettings(
        accessToken: AccessToken,
        timerId: TimerId,
        patch: TimerSettings.Patch
    ): SetTimerSettingsResult {
        return client.patch("timers") {
            header(HttpHeaders.Authorization, accessToken)
            parameter("timer_id", timerId)
            parameter("settings", patch)
        }.toResult()
    }

    /**
     * Starts timer with [timerId].
     * @param timerId unique identifier of a timer.
     */
    public suspend fun startTimer(
        accessToken: AccessToken,
        timerId: TimerId
    ): StartTimerResult {
        return client.post("timers/start") {
            header(HttpHeaders.Authorization, accessToken)
            parameter("timer_id", timerId)
        }.toResult()
    }

    /**
     * Stops timer with [timerId].
     * @param timerId unique identifier of a timer.
     */
    public suspend fun stopTimer(
        accessToken: AccessToken,
        timerId: TimerId
    ): StopTimerResult {
        return client.post("timers/stop") {
            header(HttpHeaders.Authorization, accessToken)
            parameter("timer_id", timerId)
        }.toResult()
    }

    /**
     * Creates invite for [timerId].
     * @param timerId unique identifier of a timer.
     * @param max max count of joiners.
     */
    public suspend fun createInvite(
        accessToken: AccessToken,
        timerId: TimerId,
        max: Count
    ): CreateInviteResult {
        return client.post("timers/invites") {
            header(HttpHeaders.Authorization, accessToken)
            parameter("timer_id", timerId)
            parameter("max_joiners", max)
        }.toResult()
    }

    /**
     * Gets all invites for [timerId].
     * @param timerId unique identifier of a timer.
     */
    public suspend fun getInvites(
        accessToken: AccessToken,
        timerId: TimerId
    ): StopTimerResult {
        return client.get("timers/invites/all") {
            header(HttpHeaders.Authorization, accessToken)
            parameter("timer_id", timerId)
        }.toResult()
    }

    /**
     * Joins by [code] to timer.
     * @param code invite code
     */
    public suspend fun joinTimerByInviteCode(
        accessToken: AccessToken,
        code: Code
    ): StopTimerResult {
        return client.post("timers/invites/join") {
            header(HttpHeaders.Authorization, accessToken)
            parameter("code", code)
        }.toResult()
    }

    /**
     * Joins by [code] to timer.
     * @param code invite code
     */
    public suspend fun removeInvite(
        accessToken: AccessToken,
        code: Code
    ): RemoveInviteResult {
        return client.delete("timers/invites") {
            header(HttpHeaders.Authorization, accessToken)
            parameter("code", code)
        }.toResult()
    }

    /**
     * Gets last events of [timerId].
     */
    public suspend fun getLastEvents(
        accessToken: AccessToken,
        timerId: TimerId,
        boundaries: IntRange,
        lastKnownId: TimerEventId? = null
    ): GetLastEventsResult {
        return client.get("timers/events/last") {
            header(HttpHeaders.Authorization, accessToken)
            parameter("start", boundaries.first)
            parameter("end", boundaries.last)
            parameter("last_known_id", lastKnownId)
            parameter("timer_id", timerId)
        }.toResult()
    }


    /**
     * Checks whether response is success or not.
     * @throws AuthorizationException if access token is invalid.
     * @throws ConnectionException if there is connection timeout.
     * @throws BadRequestException if some data is invalid (usually should not throw).
     * @return [T] if everything is okay.
     */
    private suspend inline fun <reified T> HttpResponse.toResult(): T {
        return when {
            status.isSuccess() -> body()
            status == HttpStatusCode.Unauthorized -> throw AuthorizationException()
            status == HttpStatusCode.BadRequest -> throw BadRequestException(bodyAsText())
            else -> throw ConnectionException(status.description)
        }
    }

    @Serializable
    private class CreateTimerRequest(
        val name: Name,
        val settings: TimerSettings,
    )
}