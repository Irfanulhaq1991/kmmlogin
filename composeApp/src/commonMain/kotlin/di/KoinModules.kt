package di

import data.IUserRepository
import data.UserRepository
import data.remote.IUserRemoteDataSource
import data.remote.UserRemoteDataSource
import data.remote.UserRemoteDto
import data.remote.UserRemoteRspnseDto
import data.remote.UsrApi
import kotlinx.coroutines.delay
import org.koin.dsl.module
import domain.usecase.ILoginUseCase
import domain.usecase.LoginUseCase
import org.koin.core.context.startKoin
import view.login.LoginViewModel



val shareMainModule = module {
    factory<UsrApi> { FakApi() }
    factory<IUserRemoteDataSource> { UserRemoteDataSource(get()) }
    factory<IUserRepository> { UserRepository(get()) }
    factory<ILoginUseCase> { LoginUseCase(get()) }
    factory {
        LoginViewModel(get())
    }

}




class FakApi : UsrApi {
    override suspend fun authntcat(username: String, password: String): UserRemoteRspnseDto {
        if (username.isEmpty() || password.isEmpty()) throw IllegalArgumentException("username and password are required fields")
        delay(2000)
        return UserRemoteRspnseDto(UserRemoteDto("###", 0), 200)
    }

}