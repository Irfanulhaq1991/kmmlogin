package view.registeration

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.irfan.composeexploration.ui.theme.theme3.AppTheme
import kotlinx.coroutines.launch
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import moe.tlaster.precompose.koin.koinViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import view.login.LoginViewModel

@Composable
fun RegisterScene() {


    var userName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    val gender = listOf("Male", "Female")
    val (selectedOption, setSelectedOption) = remember { mutableStateOf(gender[0])}


    Scaffold(snackbarHost = { SnackbarHost(hostState = snackBarHostState) }) { contentPadding ->

        ConstraintLayout(
            modifier = Modifier
                .background(AppTheme.colors.background)
                .scrollable(rememberScrollState(), Orientation.Vertical)
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(15.dp)
        ) {

            val (refLogoIcon,
                refLogoText,
                refName,
                refPassword,
                refConfirmPassword,
                refLoginBtn,
                refRegisterText,
                refRegisterAction,
                refLoader,
                refEmail,
                refGender
                ) = createRefs()
            val topGuideline = createGuidelineFromTop(0.35f)
            Icon(
                Icons.Default.AccountBox,

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
                text = "REGISTER",
                style = AppTheme.typography.label
                    .copy(
                        fontSize = 25.sp,
                        letterSpacing = 5.sp,
                        color = AppTheme.colors.text
                    ),
                modifier = Modifier.constrainAs(refLogoText) {
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                    bottom.linkTo(topGuideline, 80.dp)
                }
            )
            OutlinedTextField(

                modifier = Modifier
                    .testTag("fullName")
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = AppTheme.colors.secondary,
                        shape = RoundedCornerShape(25.dp)
                    )
                    .constrainAs(refName) {
                        top.linkTo(topGuideline)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                maxLines = 1,
                value = userName,
                onValueChange = { userName = it },
                placeholder = {
                    Text(
                        "Enter Full Name",
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
                    .testTag("fullName")
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = AppTheme.colors.secondary,
                        shape = RoundedCornerShape(25.dp)
                    )
                    .constrainAs(refEmail) {
                        top.linkTo(refName.bottom,10.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                maxLines = 1,
                value = userName,
                onValueChange = { userName = it },
                placeholder = {
                    Text(
                        "Enter Email",
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
                    .border(
                        width = 1.dp,
                        color = AppTheme.colors.secondary,
                        shape = RoundedCornerShape(25.dp)
                    )
                    .constrainAs(refPassword) {
                        top.linkTo(refEmail.bottom, 10.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                maxLines = 1,
                value = password,
                onValueChange = { password = it },
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

            OutlinedTextField(
                modifier = Modifier
                    .testTag("confirmPassword")
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = AppTheme.colors.secondary,
                        shape = RoundedCornerShape(25.dp)
                    )
                    .constrainAs(refConfirmPassword) {
                        top.linkTo(refPassword.bottom, 10.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                maxLines = 1,
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                placeholder = {
                    Text(
                        "Confirm Password",
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
            Row( modifier = Modifier
                .testTag("gender")
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = AppTheme.colors.secondary,
                    shape = RoundedCornerShape(25.dp)
                )
                .constrainAs(refGender) {
                    top.linkTo(refConfirmPassword.bottom, 10.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }){
                Text("Gender")
                gender.forEach { option ->
                    RadioButton(
                        selected = option == selectedOption,
                        onClick = { setSelectedOption(option) },
                        colors = RadioButtonDefaults.colors(selectedColor = AppTheme.colors.primary) // Customize the selected color
                    )
                    Text(text = option)
                }
            }

            Button(
                modifier = Modifier
                    .testTag("doRegister")
                    .fillMaxWidth(0.5f)
                    .constrainAs(refLoginBtn) {
                        bottom.linkTo(parent.bottom, 30.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                colors = ButtonDefaults.buttonColors(containerColor = AppTheme.colors.secondary),
                onClick = {
                }

            ) {
                Text(
                    text = "Register",
                    style = AppTheme.typography.label.copy(color = AppTheme.colors.text)
                )
            }
        }
    }

}