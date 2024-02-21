package com.irfan.kmmlogin.test


import com.irfan.kmmlogin.LoginUseCase
import com.irfan.kmmlogin.LoginViewModel
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import io.mockk.verify
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test


class LoginViewModelShould {

    @MockK
    private lateinit var loginUseCase: LoginUseCase
    private lateinit var loginViewModel: LoginViewModel

    @BeforeTest
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        loginViewModel = LoginViewModel(loginUseCase)
    }

    @AfterTest
    fun tearDown(){
        unmockkAll()
    }
    @Test
    fun invokeLoginUseCase() {
        loginViewModel.doLogin()
        verify { loginUseCase.invoke() }
    }
}