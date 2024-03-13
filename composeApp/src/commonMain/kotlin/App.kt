import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import com.irfan.composeexploration.ui.theme.theme3.AppTheme
import di.mainModule
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.core.context.startKoin

import view.LoginScreen

@OptIn(ExperimentalResourceApi::class)
@Composable
@Preview
fun App() {
    AppTheme {
        LoginScreen()
    }
}