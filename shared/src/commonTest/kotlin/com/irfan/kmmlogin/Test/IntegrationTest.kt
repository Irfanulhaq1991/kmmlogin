package com.irfan.kmmlogin.Test

import com.irfan.kmmlogin.LoginRepository
import com.irfan.kmmlogin.LoginUseCase
import com.irfan.kmmlogin.LoginViewModel
import com.irfan.kmmlogin.LoginViewState
import com.varabyte.truthish.assertThat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class IntegrationTest {

    private val testScope = TestScope(UnconfinedTestDispatcher());
    @Test
    fun doLoginTest() = runTest {
        val loginRepository = LoginRepository()
        val loginUseCase = LoginUseCase(loginRepository)
        val loginViewModel = LoginViewModel(loginUseCase,testScope)
        val loginSpy = LoginViewSpy(loginViewModel, testScope)
        loginSpy.create()
        loginSpy.donLogin()
        val loginViewStates = loginSpy.loginViewStates;
        assertThat(loginViewStates.size).isEqualTo(2)
        assertThat(loginViewStates[1].isLoading).isEqualTo(true)
    }
}

class LoginViewSpy(
    private val loginViewModel: LoginViewModel,
    private val scope: CoroutineScope
) {
    val loginViewStates = mutableListOf<LoginViewState>()

    fun create() {
        scope.launch {
            loginViewModel.loginStateFlow.collect {
                loginViewStates.add(it)
            }
        }
    }
  fun donLogin() {
        loginViewModel.doLogin()
    }
}

