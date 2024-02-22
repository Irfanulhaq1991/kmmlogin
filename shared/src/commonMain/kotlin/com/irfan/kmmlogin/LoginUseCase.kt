package com.irfan.kmmlogin


class LoginUseCase(private val usrRepo: IUserRepository) : ILoginUseCase {
  override suspend operator fun invoke(userName:String, password:String):Result<User>{
        return usrRepo.authenticate(userName,password)
    }
}
