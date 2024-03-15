package view.login

import domain.model.User

data class LoginViewState(
    val isLoading: Boolean = false,
    val isError:Boolean = false,
    val message: String = "",
    val user: User? = null
)
