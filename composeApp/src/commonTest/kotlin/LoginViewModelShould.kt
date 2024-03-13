
import io.mockative.Mock
import io.mockative.any
import io.mockative.classOf
import io.mockative.coEvery
import io.mockative.mock
import kotlinx.coroutines.test.runTest
import usecase.ILoginUseCase
import usecase.User
import view.LoginViewModel
import kotlin.test.BeforeTest
import kotlin.test.Test


class LoginViewModelShould {

    @Mock
    private val loginUseCase = mock(classOf<ILoginUseCase>())
    private lateinit var loginViewModel: LoginViewModel

    @BeforeTest
    fun setUp() {
        loginViewModel = LoginViewModel(loginUseCase)
    }

    @Test
    fun invokeLoginUseCase()= runTest {
        coEvery { loginUseCase.invoke(any(),any()) }.returns(Result.success(User(1,"###")))
        loginViewModel.doLogin("###","###")
        coEvery { loginUseCase.invoke(any(),any()) }
    }
}