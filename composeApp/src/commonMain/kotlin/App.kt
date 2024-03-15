import androidx.compose.runtime.*
import com.irfan.composeexploration.ui.theme.theme3.AppTheme
import di.mainModule
import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.rememberNavigator
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.core.context.startKoin

import view.login.LoginScene
import view.registeration.RegisterScene

@OptIn(ExperimentalResourceApi::class)
@Composable
@Preview
fun App() {
    startKoin {
        modules(mainModule)
    }

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
                    RegisterScene()
                }
            }
        }
    }
}