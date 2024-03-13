import com.varabyte.truthish.assertThat
import data.remote.IUserRemoteDataSource
import data.remote.UserRemoteDto
import data.UserRepository
import io.mockative.Mock
import io.mockative.any
import io.mockative.classOf
import io.mockative.coEvery
import io.mockative.mock
import kotlinx.coroutines.test.runTest
import usecase.User
import kotlin.test.BeforeTest

import kotlin.test.Test

class LoginRepositoryShould{


    @Mock
    private val userRemoteDataSource = mock(classOf<IUserRemoteDataSource>())
    private lateinit var usrRepo: UserRepository

    @BeforeTest
    fun setup(){
        usrRepo = UserRepository(userRemoteDataSource)
    }

    @Test
    fun callRemoteDataSource()= runTest{
        coEvery{ userRemoteDataSource.authenticate(any(),any()) }.returns( Result.success(
            UserRemoteDto("",200)
        ))
        usrRepo.authenticate("###","###")
        coEvery { userRemoteDataSource.authenticate(any(),any())}
    }
    @Test
    fun returnDomainModel() = runTest{
       val userRemoteDto =  UserRemoteDto("",200)
        coEvery { userRemoteDataSource.authenticate(any(),any()) }.returns(Result.success(userRemoteDto))
        val result = usrRepo.authenticate("##","###").getOrThrow()
        assertThat(result).isInstanceOf(User::class)
    }

    @Test
    fun returnError() = runTest(){
        coEvery { userRemoteDataSource.authenticate(any(),any()) }.returns(Result.failure<UserRemoteDto>(Throwable("Error")))
        val result = usrRepo.authenticate("##","###")
        assertThat(result.isFailure).isTrue()
        result.onFailure {
            assertThat(it.message).isEqualTo("Error")
       }
     }
}