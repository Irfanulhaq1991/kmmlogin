
import io.mockative.Mock
import io.mockative.any
import io.mockative.classOf
import io.mockative.coEvery
import io.mockative.mock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import domain.usecase.ILoginUseCase
import domain.model.User
import view.login.LoginViewModel
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test


class LoginViewModelShould {

    @Mock
    private val loginUseCase = mock(classOf<ILoginUseCase>())
    private lateinit var loginViewModel: LoginViewModel

    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        loginViewModel = LoginViewModel(loginUseCase)
    }
    @AfterTest
    fun tear() {
        Dispatchers.resetMain()
    }
    @Test
    fun invokeLoginUseCase()= runTest {
        coEvery { loginUseCase.invoke(any(),any()) }.returns(Result.success(User(1,"###")))
        loginViewModel.doLogin("###","###")
        coEvery { loginUseCase.invoke(any(),any()) }
    }
}