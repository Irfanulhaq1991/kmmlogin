package view.registeration
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import domain.usecase.ILoginUseCase

class RegisterViewModel(
    private val loginUseCase: ILoginUseCase,
):ViewModel() {
    private var  job: Job = Job()
    private val _loginStateFlow = MutableStateFlow(RegisterViewState())
    val loginStateFlow: StateFlow<RegisterViewState> = _loginStateFlow

    fun doLogin(userName:String,password:String) {
        job.cancel()
        _loginStateFlow.update { it.copy(isLoading = true) }
        job =  viewModelScope.launch {
                   loginUseCase.invoke(userName,password)
               .fold(
                   {_loginStateFlow.update { state-> state.copy(isLoading = false, user = it) } },
                   {_loginStateFlow.update {stat -> stat.copy(isLoading = false,isError = true, message = it.message?:"") } }
               )
       }
    }

    fun errorMessageDisplayed(){
        _loginStateFlow.update { it.copy(isLoading = false, message = "", isError = false) }
    }
}
