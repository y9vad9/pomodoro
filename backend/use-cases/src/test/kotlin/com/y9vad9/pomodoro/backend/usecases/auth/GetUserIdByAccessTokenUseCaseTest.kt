package com.y9vad9.pomodoro.backend.usecases.auth

import com.y9vad9.pomodoro.backend.providers.MockedCurrentTimeProvider
import com.y9vad9.pomodoro.backend.repositories.AuthorizationsRepository
import com.y9vad9.pomodoro.backend.repositories.MockedAuthorizationsRepository
import com.y9vad9.pomodoro.backend.repositories.UsersRepository
import kotlinx.coroutines.runBlocking
import java.time.Duration
import kotlin.test.BeforeTest
import kotlin.test.Test

class GetUserIdByAccessTokenUseCaseTest {
   private val repository = MockedAuthorizationsRepository()
   private val useCase = GetUserIdByAccessTokenUseCase(repository, MockedCurrentTimeProvider)

   @BeforeTest
   fun before() {
      runBlocking {
          repository.create(
              UsersRepository.UserId(0),
              AuthorizationsRepository.AccessToken("..."),
              AuthorizationsRepository.RefreshToken(".."),
              MockedCurrentTimeProvider.provide() + Duration.ofDays(7).toMillis()
          )
          repository.create(
              UsersRepository.UserId(1),
              AuthorizationsRepository.AccessToken("...."),
              AuthorizationsRepository.RefreshToken("....."),
              MockedCurrentTimeProvider.provide() + Duration.ofDays(7).toMillis()
          )
      }
   }

    @Test
    fun testSuccess(): Unit = runBlocking {
        val result = useCase(AuthorizationsRepository.AccessToken("..."))
        assert(result is GetUserIdByAccessTokenUseCase.Result.Success)
    }

    @Test
    fun testFailure() = runBlocking {
        val result = useCase(AuthorizationsRepository.AccessToken(".."))
        assert(result is GetUserIdByAccessTokenUseCase.Result.NotFound)
    }
}