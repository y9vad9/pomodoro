package com.y9vad9.pomodoro.backend.usecases.auth

import com.y9vad9.pomodoro.backend.domain.DateTime
import com.y9vad9.pomodoro.backend.repositories.AuthorizationsRepository
import com.y9vad9.pomodoro.backend.repositories.MockedAuthorizationsRepository
import com.y9vad9.pomodoro.backend.repositories.UsersRepository
import kotlinx.coroutines.runBlocking
import kotlin.test.BeforeTest
import kotlin.test.Test

class GetUserIdByAccessTokenUseCaseTest {
   private val repository = MockedAuthorizationsRepository()
   private val useCase = GetUserIdByAccessTokenUseCase(repository)

   @BeforeTest
   fun before() {
      runBlocking {
         repository.create(UsersRepository.UserId(0), AuthorizationsRepository.AccessToken("..."), DateTime(-1))
         repository.create(UsersRepository.UserId(1), AuthorizationsRepository.AccessToken("...."), DateTime(-1))
      }
   }

   @Test
   fun testSuccess() = runBlocking {
      val result = useCase(AuthorizationsRepository.AccessToken("..."))
      assert(result is GetUserIdByAccessTokenUseCase.Result.Success)
   }

   @Test
   fun testFailure() = runBlocking {
      val result = useCase(AuthorizationsRepository.AccessToken(".."))
      assert(result is GetUserIdByAccessTokenUseCase.Result.NotFound)
   }
}