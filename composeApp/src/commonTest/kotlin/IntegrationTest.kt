import com.varabyte.truthish.assertThat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class IntegrationTest {

    private val testScope = TestScope(UnconfinedTestDispatcher());
    private val successUserApi = object :UsrApi{
        override suspend fun authntcat(username: String, password: String): UserRemoteRspnseDto {
            return UserRemoteRspnseDto(UserRemoteDto("###",0),200)
        }
    }

    private val errorUserApi = object :UsrApi{
        override suspend fun authntcat(username: String, password: String): UserRemoteRspnseDto {
            return throw Exception("No Network")
        }
    }

    @Test
    fun successLoginTest() = runTest {
        val userRemoteDataSource = UserRemoteDataSource(successUserApi)
        val usrRepo = UserRepository(userRemoteDataSource)
        val loginUseCase = LoginUseCase(usrRepo)
        val loginViewModel = LoginViewModel(loginUseCase,testScope)
        val loginSpy = LoginViewSpy(3, loginViewModel, testScope)
        loginSpy.create()
        loginSpy.donLogin("###","###")
        val loginViewStates = loginSpy.loginViewStates;
        assertThat(loginViewStates.size).isEqualTo(3)
        assertThat(loginViewStates[0].isLoading).isEqualTo(false)
        assertThat(loginViewStates[0].user).isNull()
        assertThat(loginViewStates[1].isLoading).isTrue()
        assertThat(loginViewStates[2].isLoading).isFalse()
        assertThat(loginViewStates[2].user).isNotNull()
    }
    @Test
    fun errorLoginTest() = runTest {
        val userRemoteDataSource = UserRemoteDataSource(errorUserApi)
        val usrRepo = UserRepository(userRemoteDataSource)
        val loginUseCase = LoginUseCase(usrRepo)
        val loginViewModel = LoginViewModel(loginUseCase,testScope)
        val loginSpy = LoginViewSpy(3, loginViewModel, testScope)
        loginSpy.create()
        loginSpy.donLogin("###","###")
        val loginViewStates = loginSpy.loginViewStates;
        assertThat(loginViewStates.size).isEqualTo(3)
        assertThat(loginViewStates[0].isLoading).isEqualTo(false)
        assertThat(loginViewStates[0].user).isNull()
        assertThat(loginViewStates[1].isLoading).isTrue()
        assertThat(loginViewStates[2].isLoading).isFalse()
        assertThat(loginViewStates[2].user).isNull()
        assertThat(loginViewStates[2].isError).isTrue()
    }

    @Test
    fun loginOldStateRetainTest() = runTest {
        val userRemoteDataSource = UserRemoteDataSource(successUserApi)
        val usrRepo = UserRepository(userRemoteDataSource)
        val loginUseCase = LoginUseCase(usrRepo)
        val loginViewModel = LoginViewModel(loginUseCase,testScope)
        val loginSpy = LoginViewSpy(3, loginViewModel, testScope)
        loginSpy.create()
        loginSpy.donLogin("###","###")
        val loginViewStates = loginSpy.loginViewStates

        assertThat(loginViewStates.size).isEqualTo(3)
        assertThat(loginViewStates[0].isLoading).isEqualTo(false)
        assertThat(loginViewStates[0].user).isNull()
        assertThat(loginViewStates[1].isLoading).isTrue()
        assertThat(loginViewStates[2].isLoading).isFalse()
        assertThat(loginViewStates[2].user).isNotNull()

        loginSpy.loginViewStates.clear()
        loginSpy.setCount(2)
        loginSpy.donLogin("###","###")
        val loginViewStates2 = loginSpy.loginViewStates;

        assertThat(loginViewStates2.size).isEqualTo(2)
        assertThat(loginViewStates2[0].isLoading).isTrue()
        assertThat(loginViewStates2[0].user).isNotNull()
        assertThat(loginViewStates2[1].isLoading).isFalse()


    }
}

class LoginViewSpy(
    private var statCount:Int = 0,
    private val loginViewModel: LoginViewModel,
    private val scope: CoroutineScope
) {
    val loginViewStates = mutableListOf<LoginViewState>()
   private var countDownLatch = CountDownLatch(statCount);
   fun setCount(count:Int){
       countDownLatch = CountDownLatch(count);

   }
    fun create() {
        scope.launch {
            loginViewModel.loginStateFlow.collect {
                loginViewStates.add(it)
                countDownLatch.countDown()
            }
        }
    }
  suspend fun donLogin(userName:String, password:String) {
        loginViewModel.doLogin(userName,password)
      countDownLatch.await()
    }
}


