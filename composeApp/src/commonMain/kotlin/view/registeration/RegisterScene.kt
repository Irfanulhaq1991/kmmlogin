package view.registeration

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.irfan.composeexploration.ui.theme.theme3.AppTheme
import dev.icerock.moko.media.compose.BindMediaPickerEffect
import dev.icerock.moko.media.compose.rememberMediaPickerControllerFactory
import dev.icerock.moko.media.compose.toImageBitmap
import dev.icerock.moko.media.picker.MediaSource
import dev.icerock.moko.permissions.DeniedAlwaysException
import dev.icerock.moko.permissions.DeniedException
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.PermissionsControllerFactory
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import kotlinx.coroutines.launch

@Composable
fun RegisterScene(onCancel: () -> Unit) {


    var userName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    val premiumNonPremiumOptions = listOf("Premium", "Non Premium")
    val (selectedOption, setSelectedOption) = remember { mutableStateOf(premiumNonPremiumOptions[0]) }

    val genderOptions = listOf("Select Gender", "Male", "Female", "Prefer Not To Reveal")
    var selectedItem by remember { mutableStateOf(genderOptions[0]) }
    var expanded by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    val factory: PermissionsControllerFactory = rememberPermissionsControllerFactory()
    val controller: PermissionsController =
        remember(factory) { factory.createPermissionsController() }
    val mediaFactory = rememberMediaPickerControllerFactory()
    val mediaPicker = remember(mediaFactory) { mediaFactory.createMediaPickerController() }
    var profileImage by remember { mutableStateOf<ImageBitmap?>(null) }
    BindEffect(controller)
    BindMediaPickerEffect(mediaPicker)


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
                refPremiumNonPremiumOption,
                refGenderOptions
            ) = createRefs()
            val topGuideline = createGuidelineFromTop(0.30f)

            if (profileImage != null)
                Image(
                    bitmap = profileImage!!,
                    contentDescription = "...",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(120.dp)
                        .constrainAs(refLogoIcon) {
                            bottom.linkTo(refLogoText.top, 10.dp)
                            end.linkTo(refLogoText.end)
                            start.linkTo(refLogoText.start)
                        }
                        .clip(RoundedCornerShape(percent = 10))
                        .clickable {
                            scope.launch {
                                try {
                                    controller.providePermission(Permission.GALLERY)
                                    // Permission has been granted successfully.
                                    val result = mediaPicker.pickImage(MediaSource.GALLERY)
                                    profileImage = result.toImageBitmap()
                                } catch (deniedAlways: DeniedAlwaysException) {
                                    // Permission is always denied.
                                    controller.openAppSettings()
                                    snackBarHostState.showSnackbar(deniedAlways.message.toString())
                                } catch (denied: DeniedException) {
                                    // Permission was denied.
                                    snackBarHostState.showSnackbar(denied.message.toString())
                                }
                            }
                        }
                )
            else
                Icon(
                    Icons.Default.AccountBox,
                    contentDescription = "...",
                    tint = AppTheme.colors.secondary,
                    modifier = Modifier
                        .size(120.dp)
                        .constrainAs(refLogoIcon) {
                            bottom.linkTo(refLogoText.top, 10.dp)
                            end.linkTo(refLogoText.end)
                            start.linkTo(refLogoText.start)
                        }
                        .clip(RoundedCornerShape(percent = 10))
                        .clickable {
                            scope.launch {
                                try {
                                    controller.providePermission(Permission.GALLERY)
                                    // Permission has been granted successfully.
                                    val result = mediaPicker.pickImage(MediaSource.GALLERY)
                                    profileImage = result.toImageBitmap()
                                } catch (deniedAlways: DeniedAlwaysException) {
                                    // Permission is always denied.
                                    controller.openAppSettings()
                                    snackBarHostState.showSnackbar(deniedAlways.message.toString())
                                } catch (denied: DeniedException) {
                                    // Permission was denied.
                                    snackBarHostState.showSnackbar(denied.message.toString())
                                }
                            }
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
                    bottom.linkTo(topGuideline, 60.dp)
                }
            )


            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .testTag("premiumNonPremiumOption")
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = AppTheme.colors.secondary,
                        shape = RoundedCornerShape(25.dp)
                    ).padding(start = 5.dp)
                    .constrainAs(refPremiumNonPremiumOption) {
                        top.linkTo(topGuideline)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }) {
                premiumNonPremiumOptions.forEach { option ->
                    RadioButton(
                        selected = option == selectedOption,
                        onClick = { setSelectedOption(option) },
                        colors = RadioButtonDefaults.colors(selectedColor = AppTheme.colors.primary) // Customize the selected color
                    )
                    Text(
                        text = option,
                        style = AppTheme.typography.body.copy(color = AppTheme.colors.text)
                    )
                }
            }

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
                        top.linkTo(refPremiumNonPremiumOption.bottom, 10.dp)
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
                        top.linkTo(refName.bottom, 10.dp)
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


            Box(
                modifier = Modifier
                    .testTag("genderContainer")
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = AppTheme.colors.secondary,
                        shape = RoundedCornerShape(25.dp)
                    )
                    .constrainAs(refGenderOptions) {
                        top.linkTo(refEmail.bottom, 10.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            ) {
                Column {
                    OutlinedTextField(
                        value = selectedItem,
                        onValueChange = { },
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = { Icon(Icons.Outlined.ArrowDropDown, null) },
                        readOnly = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            focusedLabelColor = AppTheme.colors.secondary,
                            cursorColor = AppTheme.colors.secondary,

                            ),
                        textStyle = AppTheme.typography.body.copy(
                            color =
                            AppTheme.colors.text
                        )

                    )

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier
                            .fillMaxWidth()

                    ) {
                        genderOptions.forEach { item ->
                            DropdownMenuItem(
                                onClick = {
                                    selectedItem = item
                                    expanded = false
                                }
                            ) {
                                Text(
                                    text = item,
                                    style = AppTheme.typography.body.copy(color = AppTheme.colors.text)

                                )
                            }
                        }
                    }
                }

                Spacer(
                    modifier = Modifier
                        .matchParentSize()
                        .background(Color.Transparent)
                        .padding(10.dp)
                        .clickable(
                            onClick = { expanded = !expanded }
                        )
                )

            }


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
                        top.linkTo(refGenderOptions.bottom, 10.dp)
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

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .testTag("doRegister")
                    .fillMaxWidth()
                    .constrainAs(refRegisterAction) {
                        top.linkTo(refConfirmPassword.bottom, 30.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .padding(start = 12.dp, end = 12.dp)
            ) {
                Button(
                    modifier = Modifier
                        .padding(5.dp)
                        .weight(1f)
                        .testTag("doRegister"),
                    colors = ButtonDefaults.buttonColors(containerColor = AppTheme.colors.secondary),
                    onClick = {
                    }

                ) {
                    Text(
                        text = "Register",
                        style = AppTheme.typography.label.copy(color = AppTheme.colors.text)
                    )
                }

                Button(
                    modifier = Modifier
                        .padding(5.dp)
                        .weight(1f)
                        .testTag("doCancel"),
                    colors = ButtonDefaults.buttonColors(containerColor = AppTheme.colors.secondary),
                    onClick = {
                        showDialog = true
                    }

                ) {
                    Text(
                        text = "Cancel",
                        style = AppTheme.typography.label.copy(color = AppTheme.colors.text)
                    )
                }
            }

            if (showDialog)
                AlertDialog(
                    text = {
                        Text(
                            "Are You Sure To Cancel?",
                            style = AppTheme.typography.label.copy(color = AppTheme.colors.text)
                        )
                    },
                    onDismissRequest = { showDialog = false },
                    confirmButton = {
                        Button(
                            modifier = Modifier
                                .padding(5.dp)
                                .testTag("doCancel"),
                            colors = ButtonDefaults.buttonColors(containerColor = AppTheme.colors.secondary),

                            onClick = {
                                showDialog = false
                                onCancel()
                            }
                        ) {
                            Text(
                                "Confirm",
                                style = AppTheme.typography.body.copy(color = AppTheme.colors.text)
                            )
                        }
                    },
                    dismissButton = {
                        Button(
                            modifier = Modifier
                                .padding(5.dp)
                                .testTag("doCancel"),
                            colors = ButtonDefaults.buttonColors(containerColor = AppTheme.colors.secondary),
                            onClick = {
                                showDialog = false
                            }
                        ) {
                            Text(
                                "Dismiss",
                                style = AppTheme.typography.body.copy(color = AppTheme.colors.text)
                            )
                        }
                    },
                )
        }
    }

}