package usecase

interface ILoginUseCase {
    suspend operator fun invoke(userName: String, password: String):Result<User>
}