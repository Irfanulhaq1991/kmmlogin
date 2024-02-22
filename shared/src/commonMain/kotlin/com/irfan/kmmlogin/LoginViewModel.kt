package com.irfan.kmmlogin

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default + Job())
) {


    //    private val customCoroutineScope = CoroutineScope(Job()) //
    private val _loginStateFlow = MutableStateFlow(LoginViewState())
    val loginStateFlow: StateFlow<LoginViewState> = _loginStateFlow

    fun doLogin(userName:String,password:String) {
        _loginStateFlow.update { oldState ->
            oldState.copy(isLoading = true)
        }.run {
           loginUseCase.invoke(userName,password)
            .fold({
                _loginStateFlow.update { oldState->
                    oldState.copy(isLoading = false,isError = true, user = it)
                }

            },{
                _loginStateFlow.update {oldState ->
                    oldState.copy(isLoading = false,isError = true, message = it.message?:"")
                }
            })
        }

    }
}
