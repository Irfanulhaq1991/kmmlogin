

class UserRemoteDataSource(private val api: UsrApi) : IUserRemoteDataSource {
  override suspend fun authenticate(username:String, password:String): Result<UserRemoteDto> {
        try {
            val response = api.authntcat(username,password)
            if (response.statusCode == 200) {
                return Result.success(response.userRemoteDto!!)
            }else{
                return Result.failure(Throwable("Not valid user"))
            }
        } catch (e: Exception) {
            if (e.message == "Network Error")
                return Result.failure(Throwable("Network Error"))
            else {
                return Result.failure(Throwable("Unknown Error"))
            }
        }


    }

}
