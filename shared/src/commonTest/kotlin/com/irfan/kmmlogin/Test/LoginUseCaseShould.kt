package com.irfan.kmmlogin.test

import com.irfan.kmmlogin.UsrRepo
import com.irfan.kmmlogin.LoginUseCase
import com.irfan.kmmlogin.User
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import io.mockk.verify

import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class LoginUseCaseShould {

    private lateinit var loginUseCase:LoginUseCase
    @MockK
    private lateinit var usrRepo: UsrRepo
    @BeforeTest
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        loginUseCase = LoginUseCase(usrRepo)
    }

    @AfterTest
    fun tearDown(){
        unmockkAll()
    }
    @Test
    fun invokeRepository(){
        every { usrRepo.authntict(any(),any()) } returns Result.success(User(1,"###"))
        loginUseCase("###","###")
        verify { usrRepo.authntict(any(),any()) }
    }
}