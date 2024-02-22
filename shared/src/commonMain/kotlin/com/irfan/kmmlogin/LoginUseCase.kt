package com.irfan.kmmlogin

class LoginUseCase(private val usrRepo: UsrRepo) {
     suspend operator fun invoke(userName:String, password:String):Result<User>{
        return usrRepo.authntict(userName,password)
    }
}
