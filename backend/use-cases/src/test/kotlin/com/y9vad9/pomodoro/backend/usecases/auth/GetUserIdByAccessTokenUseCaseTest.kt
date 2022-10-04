package com.y9vad9.pomodoro.backend.usecases.auth

import com.y9vad9.pomodoro.backend.domain.DateTime
import com.y9vad9.pomodoro.backend.domain.entity.AccessToken
import com.y9vad9.pomodoro.backend.domain.entity.UserId
import com.y9vad9.pomodoro.backend.repositories.MockedAuthorizationsRepository
import kotlinx.coroutines.runBlocking
import kotlin.test.BeforeTest
import kotlin.test.Test

class GetUserIdByAccessTokenUseCaseTest {
   private val repository = MockedAuthorizationsRepository()
   private val useCase = GetUserIdByAccessTokenUseCase(repository)

   @BeforeTest
   fun before() {
      runBlocking {
         repository.create(UserId(0), AccessToken("..."), DateTime(-1))
         repository.create(UserId(1), AccessToken("...."), DateTime(-1))
      }
   }

   @Test
   fun testSuccess() = runBlocking {
      val result = useCase(AccessToken("..."))
      assert(result is GetUserIdByAccessTokenUseCase.Result.Success)
   }

   @Test
   fun testFailure() = runBlocking {
      val result = useCase(AccessToken(".."))
      assert(result is GetUserIdByAccessTokenUseCase.Result.NotFound)
   }
}