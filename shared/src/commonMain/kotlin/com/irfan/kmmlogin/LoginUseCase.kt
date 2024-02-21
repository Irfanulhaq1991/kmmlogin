package com.irfan.kmmlogin

class LoginUseCase(private val userRepo: UserRepo) {
     operator fun invoke(){
        userRepo.doLogin()
    }
}
