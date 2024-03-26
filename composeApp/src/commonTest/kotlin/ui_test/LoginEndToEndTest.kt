package ui_test

import App
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.click
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.isNotDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performGesture
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.runComposeUiTest
import di.apiModule
import di.shareMainModule
import initKoin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.koin.compose.getKoin
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.koinApplication
import org.koin.test.KoinTest
import org.koin.test.mock.declare
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test


// does works on iOS
class LoginEndToEndTest:KoinTest {

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun test_Loader_Displayed_On_Done_Taped() = runComposeUiTest {
        setContent {
            App()
        }

        onNodeWithTag("userName").performTextInput("Hello")
        onNodeWithTag("password").performTextInput("Hello")
        onNodeWithTag("password").performImeAction()
        onNodeWithTag("loader").assertExists()
        runOnIdle {
            runBlocking { delay(2000) }
        }
        onNodeWithTag("loader").assertDoesNotExist()
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun test_Loader_Displayed_On_Login_Button_Clicked() = runComposeUiTest {
        setContent {
            App()
        }
        onNodeWithTag("userName").performTextInput("Hello")
        onNodeWithTag("password").performTextInput("Hello")
        onNodeWithTag("doLogin").performClick()
        onNodeWithTag("loader").assertExists()
        runOnIdle {
            runBlocking { delay(2000) }
        }
        onNodeWithTag("loader").assertDoesNotExist()
    }
    @OptIn(ExperimentalTestApi::class)
    @Test
    fun test_Error_Displayed_On_No_Inputs_And_Button_Clicked() = runComposeUiTest {
        setContent {
            App()
        }
        onNodeWithTag("doLogin").performClick()
        onNodeWithText("Unknown Error").assertExists()
    }
}