package view.registeration

import domain.model.User

data class RegisterViewState(
    val isLoading: Boolean = false,
    val isError:Boolean = false,
    val message: String = "",
    val user: User? = null
)
