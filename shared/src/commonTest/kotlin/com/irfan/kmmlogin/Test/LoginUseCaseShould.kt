package com.irfan.kmmlogin.Test

import com.irfan.kmmlogin.LoginRepository
import com.irfan.kmmlogin.LoginUseCase
import com.irfan.kmmlogin.utilities.TestUtility
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class LoginUseCaseShould {

    private lateinit var loginUseCase:LoginUseCase
    @MockK
    private lateinit var loginRepository: LoginRepository
    @BeforeTest
    fun setUp() {
        TestUtility.setup(this)
        loginUseCase = LoginUseCase(loginRepository)
    }

    @AfterTest
    fun tearDown() {
        TestUtility.tearDown()
    }
    @Test
    fun invokeRepository(){
        loginUseCase()
        verify { loginRepository.doLogin() }
    }
}