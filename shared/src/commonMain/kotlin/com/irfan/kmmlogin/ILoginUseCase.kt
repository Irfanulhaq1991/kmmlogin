package com.irfan.kmmlogin

interface ILoginUseCase {
    suspend operator fun invoke(userName: String, password: String):Result<User>
}