
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class UserRepository(private val userRemoteDataSource: IUserRemoteDataSource) : IUserRepository {
   override suspend fun authenticate(userName:String, password:String):Result<User> = withContext(Dispatchers.IO){
        userRemoteDataSource.authenticate(userName,password)
            .fold({
                Result.success(it.toUser())
            },{
                Result.failure(it)
            })
    }
    
}
