package com.irfan.kmmlogin

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
        val loginUseCase = LoginUseCase()
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


