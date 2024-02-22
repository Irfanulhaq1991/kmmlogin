package com.irfan.kmmlogin

data class LoginViewState(
    val isLoading: Boolean = false,
    val isError:Boolean = false,
    val message: String = "",
    val user: User? = null
)
