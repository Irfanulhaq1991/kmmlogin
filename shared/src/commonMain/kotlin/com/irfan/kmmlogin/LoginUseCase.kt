package com.irfan.kmmlogin

class LoginUseCase(private val usrRepo: UsrRepo) {
     operator fun invoke(userName:String,password:String){
        usrRepo.authntict(userName,password)
    }
}
