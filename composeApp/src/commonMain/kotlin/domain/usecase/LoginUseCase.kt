package domain.usecase

import data.IUserRepository
import domain.model.User

class LoginUseCase(private val usrRepo: IUserRepository) : ILoginUseCase {
  override suspend operator fun invoke(userName:String, password:String):Result<User>{
        return usrRepo.authenticate(userName,password)
    }
}
