package com.irfan.kmmlogin

class LoginUseCase(private val usrRepo: UsrRepo) {
     operator fun invoke(){
        usrRepo.authntict()
    }
}
