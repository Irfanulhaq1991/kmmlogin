package data

import usecase.User

interface IUserRepository {
    suspend fun authenticate(userName: String, password: String): Result<User>
}