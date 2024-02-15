package com.irfan.kmmlogin

class LoginUseCase(private val loginRepository: LoginRepository) {
     operator fun invoke(){
        loginRepository.doLogin()
    }
}
