package com.irfan.kmmlogin.test

import com.irfan.kmmlogin.UsrRepo
import com.irfan.kmmlogin.LoginUseCase
import com.irfan.kmmlogin.LoginViewModel
import com.irfan.kmmlogin.LoginViewState
import com.irfan.kmmlogin.UserRmtRspnseDto
import com.irfan.kmmlogin.UsrApi
import com.irfan.kmmlogin.UsrRmtDtaSrc
import com.irfan.kmmlogin.UsrRmtDto
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
    private val usrApiImpl = object :UsrApi{
        override fun authntcat(username: String, password: String): UserRmtRspnseDto {
            return UserRmtRspnseDto(UsrRmtDto("",0),200)
        }
    }

    @Test
    fun doLoginTest() = runTest {
        val usrRmtDtaSrc = UsrRmtDtaSrc(usrApiImpl)
        val usrRepo = UsrRepo(usrRmtDtaSrc)
        val loginUseCase = LoginUseCase(usrRepo)
        val loginViewModel = LoginViewModel(loginUseCase,testScope)
        val loginSpy = LoginViewSpy(loginViewModel, testScope)
        loginSpy.create()
        loginSpy.donLogin("###","###")
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
  fun donLogin(userName:String,password:String) {
        loginViewModel.doLogin(userName,password)
    }
}


