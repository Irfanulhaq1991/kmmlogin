package com.irfan.kmmlogin.Test


import com.irfan.kmmlogin.LoginUseCase
import com.irfan.kmmlogin.LoginViewModel
import com.irfan.kmmlogin.utilities.TestUtility
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test


class LoginViewModelShould {

    @MockK
    private lateinit var loginUseCase: LoginUseCase
    private lateinit var loginViewModel: LoginViewModel

    @BeforeTest
    fun setup(){
        TestUtility.setup(this)
        loginViewModel = LoginViewModel(loginUseCase)
    }
    @AfterTest
    fun tearDown(){
        TestUtility.tearDown()
    }
    @Test
    fun invokeLoginUseCase() {
        loginViewModel.doLogin()
        verify { loginUseCase.invoke() }
    }
}