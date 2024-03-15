package domain.usecase

import domain.model.User

interface ILoginUseCase {
    suspend operator fun invoke(userName: String, password: String):Result<User>
}