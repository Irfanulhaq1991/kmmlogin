package ui_test

import App
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.isNotDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.runComposeUiTest
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.koin.test.KoinTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test


// does works on iOS
class LoginEndToEndTest:KoinTest {

    @BeforeTest
    fun setUp() {
    }

    @AfterTest
    fun tearDown() {
    }



    @OptIn(ExperimentalTestApi::class)
    @Test
    fun `Loader is displayed when keyboard done taped`() = runComposeUiTest {
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
    fun `Loader is displayed is when login button clicked`() = runComposeUiTest {
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
    fun `Error is displayed when username and password is empty and login button is clicked`() = runComposeUiTest {
        setContent {
            App()
        }
        onNodeWithTag("doLogin").performClick()
        onNodeWithText("Unknown Error").assertExists()
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun `Register screen displayed when register label is clicked`() = runComposeUiTest {
        setContent {
            App()
        }
        onNodeWithTag("register").performClick()
        onNodeWithTag("photo").isDisplayed()
        onNodeWithTag("doCancel").performClick()
        onNodeWithText("Confirm").isDisplayed()
        onNodeWithText("Confirm").performClick()
        onNodeWithTag("doLogin").isDisplayed()
    }
}