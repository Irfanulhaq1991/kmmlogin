package com.irfan.kmmlogin

interface IUserRepository {
    suspend fun authenticate(userName: String, password: String): Result<User>
}