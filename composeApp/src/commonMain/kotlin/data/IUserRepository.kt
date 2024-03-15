package data

import domain.model.User

interface IUserRepository {
    suspend fun authenticate(userName: String, password: String): Result<User>
}