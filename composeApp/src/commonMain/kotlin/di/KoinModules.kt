package di

import data.IUserRepository
import data.UserRepository
import data.remote.IUserRemoteDataSource
import data.remote.UserRemoteDataSource
import data.remote.UserRemoteDto
import data.remote.UserRemoteRspnseDto
import data.remote.UsrApi
import moe.tlaster.precompose.viewmodel.ViewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import usecase.ILoginUseCase
import usecase.LoginUseCase
import view.LoginViewModel

val mainModule = module {
    factory<UsrApi> { FakApi() }
    factory<IUserRemoteDataSource> { UserRemoteDataSource(get()) }
    factory<IUserRepository> { UserRepository(get()) }
    factory<ILoginUseCase> { LoginUseCase(get()) }
    factory { LoginViewModel(get()) }
}


class FakApi : UsrApi {
    override suspend fun authntcat(username: String, password: String): UserRemoteRspnseDto {
        return UserRemoteRspnseDto(UserRemoteDto("###", 0), 200)
    }

}