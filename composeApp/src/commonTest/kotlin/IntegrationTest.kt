import com.varabyte.truthish.assertThat
import data.remote.UserRemoteDataSource
import data.remote.UserRemoteDto
import data.remote.UserRemoteRspnseDto
import data.UserRepository
import data.remote.UsrApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import domain.usecase.LoginUseCase
import view.login.LoginViewModel
import view.login.LoginViewState
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class IntegrationTest {
    private val successUserApi = FakeSuccessApi()
    private val errorUserApi = FakeErrorApi()

    private var userRemoteDataSource = UserRemoteDataSource(successUserApi)
    private var usrRepo = UserRepository(userRemoteDataSource)
    private var loginUseCase = LoginUseCase(usrRepo)
    private var loginViewModel = LoginViewModel(loginUseCase)

    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
    private var testScope = TestScope(testDispatcher);
    private var loginSpy = LoginViewSpy(3, loginViewModel, testScope)



    @BeforeTest
    fun setup() {
        loginSpy.create()
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tear() {
        Dispatchers.resetMain()
        loginSpy.destroy()
    }


    @Test
    fun successLoginTest() = runTest {
        loginSpy.donLogin("###", "###")
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
        userRemoteDataSource = UserRemoteDataSource(errorUserApi)
         usrRepo = UserRepository(userRemoteDataSource)
         loginUseCase = LoginUseCase(usrRepo)
         loginViewModel = LoginViewModel(loginUseCase)
        loginSpy = LoginViewSpy(3, loginViewModel, testScope)
        loginSpy.create()
        loginSpy.donLogin("###", "###")
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
        loginSpy.donLogin("###", "###")
        val loginViewStates = loginSpy.loginViewStates

        assertThat(loginViewStates.size).isEqualTo(3)
        assertThat(loginViewStates[0].isLoading).isEqualTo(false)
        assertThat(loginViewStates[0].user).isNull()
        assertThat(loginViewStates[1].isLoading).isTrue()
        assertThat(loginViewStates[2].isLoading).isFalse()
        assertThat(loginViewStates[2].user).isNotNull()

        loginSpy.loginViewStates.clear()
        loginSpy.setCount(2)
        loginSpy.donLogin("###", "###")
        val loginViewStates2 = loginSpy.loginViewStates;

        assertThat(loginViewStates2.size).isEqualTo(2)
        assertThat(loginViewStates2[0].isLoading).isTrue()
        assertThat(loginViewStates2[0].user).isNotNull()
        assertThat(loginViewStates2[1].isLoading).isFalse()


    }
}

// Test Doubles
class LoginViewSpy(
    private var statCount: Int = 0,
    private val loginViewModel: LoginViewModel,
    private val scope: CoroutineScope
) {
    val loginViewStates = mutableListOf<LoginViewState>()
    private var countDownLatch = CountDownLatch(statCount);
    fun setCount(count: Int) {
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
   fun destroy(){
       loginViewStates.clear()
   }

    suspend fun donLogin(userName: String, password: String) {
        loginViewModel.doLogin(userName, password)
        countDownLatch.await()
    }
}



class FakeSuccessApi : UsrApi {
    override suspend fun authntcat(username: String, password: String): UserRemoteRspnseDto {
        return UserRemoteRspnseDto(UserRemoteDto("###", 0), 200)
    }
}

class FakeErrorApi:  UsrApi {
    override suspend fun authntcat(username: String, password: String): UserRemoteRspnseDto {
        return throw Exception("No Network")
    }
}

