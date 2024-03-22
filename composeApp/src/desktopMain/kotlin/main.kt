import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
fun main() = application {
    initKoin()
    Window(onCloseRequest = ::exitApplication, title = "kmm_login") {
        App(Platform.NON_PHONE)
    }
}