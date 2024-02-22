package com.irfan.kmmlogin

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

class LoginViewModel(
    private val loginUseCase: ILoginUseCase,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default + Job())
) {
    private var  job: Job = Job()
    private val _loginStateFlow = MutableStateFlow(LoginViewState())
    val loginStateFlow: StateFlow<LoginViewState> = _loginStateFlow

    fun doLogin(userName:String,password:String) {
        job.cancel()
        _loginStateFlow.update { it.copy(isLoading = true) }
        job =  scope.launch {
                   loginUseCase.invoke(userName,password)
               .fold(
                   {_loginStateFlow.update { state-> state.copy(isLoading = false,isError = false, user = it) } },
                   {_loginStateFlow.update {stat -> stat.copy(isLoading = false,isError = true, message = it.message?:"") }
                   })
       }
    }
}
