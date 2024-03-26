package view.login

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.irfan.composeexploration.ui.theme.theme3.AppTheme
import kotlinx.coroutines.launch
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import moe.tlaster.precompose.koin.koinViewModel

@Composable
fun LoginScene(
    viewModel: LoginViewModel = koinViewModel(LoginViewModel::class),
    onRegisterClicked: () -> Unit
) {

    var userName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }

    val state by viewModel.loginStateFlow.collectAsStateWithLifecycle(scope.coroutineContext)

    LaunchedEffect(state) {
        if (state.isError && state.message.isNotEmpty()) {
            scope.launch {
                snackBarHostState.showSnackbar(state.message)
            }
            viewModel.errorMessageDisplayed()
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(hostState = snackBarHostState) }) { _ ->

        ConstraintLayout(
            modifier = Modifier
                .background(AppTheme.colors.background)
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .testTag("root")
                .padding(15.dp)
                .pointerInput(Unit) {
                    detectTapGestures {
                        keyboardController?.hide()
                    }
                }
        ) {

            val (refLogoIcon, refLogoText, refUsername, refPassword, refSwitch, refSwitchText, refLoginBtn, refRegisterText, refRegisterAction, refLoader) = createRefs()
            val topGuideline = createGuidelineFromTop(0.45f)
            Icon(
                Icons.Default.Lock,

                contentDescription = "...",
                tint = AppTheme.colors.secondary,
                modifier = Modifier
                    .size(100.dp)
                    .constrainAs(refLogoIcon) {
                        bottom.linkTo(refLogoText.top, 10.dp)
                        end.linkTo(refLogoText.end)
                        start.linkTo(refLogoText.start)

                    }
            )
            Text(
                text = "LOGIN",
                style = AppTheme.typography.label
                    .copy(
                        fontSize = 25.sp,
                        letterSpacing = 5.sp,
                        color = AppTheme.colors.text
                    ),
                modifier = Modifier
                    .testTag("login")
                    .constrainAs(refLogoText) {
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                        bottom.linkTo(topGuideline, 80.dp)
                    }
            )
            OutlinedTextField(

                modifier = Modifier
                    .testTag("userName")
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
                    .border(
                        width = 1.dp,
                        color = AppTheme.colors.secondary,
                        shape = RoundedCornerShape(25.dp)
                    )
                    .constrainAs(refUsername) {
                        top.linkTo(topGuideline)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                maxLines = 1,
                value = userName,
                onValueChange = { userName = it },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                placeholder = {
                    Text(
                        "Enter User Name",
                        style = AppTheme.typography.body.copy(color = AppTheme.colors.text)
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedLabelColor = AppTheme.colors.secondary,
                    cursorColor = AppTheme.colors.secondary
                ),
                textStyle = AppTheme.typography.body.copy(color = AppTheme.colors.text)
            )

            OutlinedTextField(
                modifier = Modifier
                    .testTag("password")
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
                    .border(
                        width = 1.dp,
                        color = AppTheme.colors.secondary,
                        shape = RoundedCornerShape(25.dp)
                    )
                    .constrainAs(refPassword) {
                        top.linkTo(refUsername.bottom, 10.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                maxLines = 1,
                value = password,
                onValueChange = { password = it },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    keyboardController?.hide()
                    viewModel.doLogin(userName, password)
                }),
                placeholder = {
                    Text(
                        "Enter Password",
                        style = AppTheme.typography.body.copy(color = AppTheme.colors.text)
                    )

                },
                visualTransformation = PasswordVisualTransformation(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedLabelColor = AppTheme.colors.secondary,
                    cursorColor = AppTheme.colors.secondary
                ),
                textStyle = AppTheme.typography.body.copy(color = AppTheme.colors.text)

            )


            Switch(
                checked = rememberMe,
                modifier = Modifier
                    .padding(start = 2.dp, top = 5.dp)
                    .constrainAs(refSwitch) {
                        top.linkTo(refPassword.bottom, 10.dp)
                        start.linkTo(refPassword.start, 10.dp)
                    },
                onCheckedChange = { rememberMe = it },
                colors = SwitchDefaults.colors(
                    uncheckedTrackColor = Color.Transparent,
                    uncheckedThumbColor = AppTheme.colors.primary,
                    uncheckedBorderColor = AppTheme.colors.secondary,
                    checkedThumbColor = AppTheme.colors.secondary,
                    checkedTrackColor = AppTheme.colors.primary,
                    checkedBorderColor = AppTheme.colors.secondary
                )
            )
            Text(
                text = "Remember me",
                style = AppTheme.typography.label.copy(AppTheme.colors.text),
                modifier = Modifier
                    .padding(start = 15.dp, end = 5.dp)
                    .constrainAs(refSwitchText) {
                        start.linkTo(refSwitch.end)
                        top.linkTo(refSwitch.top)
                        bottom.linkTo(refSwitch.bottom)
                    }
            )

            Button(
                modifier = Modifier
                    .testTag("doLogin")
                    .fillMaxWidth(0.5f)
                    .constrainAs(refLoginBtn) {
                        top.linkTo(refSwitch.bottom, 30.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                colors = ButtonDefaults.buttonColors(containerColor = AppTheme.colors.secondary),
                onClick = {
                    keyboardController?.hide()
                    viewModel.doLogin(userName, password)
                }

            ) {
                Text(
                    text = "Login",
                    style = AppTheme.typography.label.copy(color = AppTheme.colors.text)
                )
            }
            if (state.isLoading)
                CircularProgressIndicator(
                    modifier = Modifier
                        .testTag("loader")
                        .width(25.dp)
                        .constrainAs(refLoader) {
                            top.linkTo(refLoginBtn.bottom, 10.dp)
                            start.linkTo(refLoginBtn.start)
                            end.linkTo(refLoginBtn.end)
                        },
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    strokeWidth = 2.dp
                )

            Text("Click here to register",
                style = AppTheme.typography.body.copy(color = AppTheme.colors.text),
                modifier = Modifier
                    .testTag("register")
                    .constrainAs(refRegisterText) {
                        top.linkTo(refLoginBtn.bottom, 50.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .clickable {
                        scope.launch {
                            onRegisterClicked()
                        }

                    })
        }
    }

}
