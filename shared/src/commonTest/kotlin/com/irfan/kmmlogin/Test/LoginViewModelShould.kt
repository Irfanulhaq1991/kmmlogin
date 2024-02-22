package com.irfan.kmmlogin.test


import com.irfan.kmmlogin.ILoginUseCase
import com.irfan.kmmlogin.LoginUseCase
import com.irfan.kmmlogin.LoginViewModel
import com.irfan.kmmlogin.User
import io.mockative.Mock
import io.mockative.any
import io.mockative.classOf
import io.mockative.coEvery
import io.mockative.mock
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test


class LoginViewModelShould {

    @Mock
    private val loginUseCase = mock(classOf<ILoginUseCase>())
    private lateinit var loginViewModel: LoginViewModel

    @BeforeTest
    fun setUp() {
        loginViewModel = LoginViewModel(loginUseCase)
    }

    @Test
    fun invokeLoginUseCase()= runTest {
        coEvery { loginUseCase.invoke(any(),any()) }.returns(Result.success(User(1,"###")))
        loginViewModel.doLogin("###","###")
        coEvery { loginUseCase.invoke(any(),any()) }
    }
}