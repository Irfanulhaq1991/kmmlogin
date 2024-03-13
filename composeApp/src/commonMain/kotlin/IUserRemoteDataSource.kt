

interface IUserRemoteDataSource {
    suspend fun authenticate(username: String, password: String): Result<UserRemoteDto>
}