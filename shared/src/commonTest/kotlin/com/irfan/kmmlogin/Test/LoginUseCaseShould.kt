package com.irfan.kmmlogin.test

import com.irfan.kmmlogin.IUserRepository
import com.irfan.kmmlogin.LoginUseCase
import com.irfan.kmmlogin.User
import io.mockative.Mock
import io.mockative.any
import io.mockative.classOf
import io.mockative.coEvery
import io.mockative.coVerify
import io.mockative.mock
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class LoginUseCaseShould {

    private lateinit var loginUseCase:LoginUseCase
    @Mock
    private val usrRepo =  mock(classOf<IUserRepository>())
    @BeforeTest
    fun setUp() {
        loginUseCase = LoginUseCase(usrRepo)
    }

    @Test
    fun invokeRepository()= runTest{
        coEvery { usrRepo.authenticate(any(),any()) }.returns(Result.success(User(1,"###")))
        loginUseCase("###","###")
        coVerify { usrRepo.authenticate(any(),any()) }
    }
}