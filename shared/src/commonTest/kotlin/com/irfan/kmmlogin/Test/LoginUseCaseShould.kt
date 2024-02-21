package com.irfan.kmmlogin.test

import com.irfan.kmmlogin.UserRepo
import com.irfan.kmmlogin.LoginUseCase
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import io.mockk.verify

import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class LoginUseCaseShould {

    private lateinit var loginUseCase:LoginUseCase
    @MockK
    private lateinit var userRepo: UserRepo
    @BeforeTest
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        loginUseCase = LoginUseCase(userRepo)
    }

    @AfterTest
    fun tearDown(){
        unmockkAll()
    }
    @Test
    fun invokeRepository(){
        loginUseCase()
        verify { userRepo.doLogin() }
    }
}