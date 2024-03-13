
import com.varabyte.truthish.assertThat
import data.remote.UserRemoteDataSource
import data.remote.UserRemoteDto
import data.remote.UserRemoteRspnseDto
import data.remote.UsrApi

import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class UsrRmtDtaSrcShould{

    @Test
    fun returnSuccess() = runTest{
        val user = UserRemoteDto("Jams",101)
        val api = getUserApiWith(user,200)
        val result: UserRemoteDto = UserRemoteDataSource(api).authenticate("##","##").getOrThrow()
        assertThat(result).isEqualTo(user)
    }
    @Test
    fun returnError() = runTest{
        val api = getUserApiWith(null,400)
        UserRemoteDataSource(api).authenticate("##","###").onFailure {
            assertThat(it.message == "Not valid user").isTrue()
        }
    }

    @Test
    fun returnNetworkError()= runTest{
        val api = getApiWithException()
        UserRemoteDataSource(api).authenticate("###","###").onFailure {
            assertThat(it.message == "Network Error").isTrue()
        }
    }



    private fun getApiWithException(): UsrApi {
            return object : UsrApi {
                override suspend fun authntcat(username: String, password: String): UserRemoteRspnseDto {
                    return throw Exception("Network Error") // App own exception classes may be created
                }
            }
    }


    private fun getUserApiWith(userRemoteDto: UserRemoteDto?, statusCode:Int): UsrApi {
        return object : UsrApi {
            override suspend fun authntcat(username: String, password: String): UserRemoteRspnseDto {
                return UserRemoteRspnseDto(userRemoteDto,statusCode)
            }

        }
    }

}