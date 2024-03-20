import androidx.compose.runtime.Composable
import com.irfan.composeexploration.ui.theme.theme3.AppTheme
import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.rememberNavigator
import org.jetbrains.compose.ui.tooling.preview.Preview
import view.login.LoginScene
import view.registeration.RegisterScene

@Composable
@Preview
fun App() {

    PreComposeApp {
        val navigator = rememberNavigator()
        AppTheme {
            NavHost(
                navigator = navigator,
                initialRoute = "/login",
            ){
                scene("/login") {
                    LoginScene(onRegisterClicked = {
                        navigator.navigate("/register")
                    })
                }
                scene("/register") {
                    RegisterScene{ navigator.goBack() }
                }
            }
        }
    }
}
