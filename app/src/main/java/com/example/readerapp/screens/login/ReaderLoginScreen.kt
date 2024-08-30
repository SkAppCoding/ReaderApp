package com.example.readerapp.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.readerapp.R
import com.example.readerapp.components.EmailInput
import com.example.readerapp.components.PasswordInput
import com.example.readerapp.components.ReaderLogo
import com.example.readerapp.components.TopSpacer
import com.example.readerapp.navigation.ReaderScreens

@Composable
fun LoginScreen(navController: NavHostController,
                viewModel: LoginScreenViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {

    val showLoginForm = rememberSaveable { mutableStateOf(true) }

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            TopSpacer()
            ReaderLogo(color = MaterialTheme.colorScheme.onBackground)
            Spacer(modifier = Modifier.height(15.dp))

            //show Login or Register Form
            if (showLoginForm.value)
                UserForm(loading = false, isCreateAccount = false) { email, password ->
                    viewModel.signInUserWithEmailAndPassword(email, password){
                        navController.navigate(ReaderScreens.ReaderHomeScreen.name)
                    }
                }
            else {
                UserForm(loading = false, isCreateAccount = true) { email, password ->
                    viewModel.createUserWithEmailAndPassword(email,password){
                        navController.navigate(ReaderScreens.ReaderHomeScreen.name)
                    }
                }
            }


            Row(
                modifier = Modifier.padding(15.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val text = if (showLoginForm.value) stringResource(id = R.string.SignUp) else stringResource(R.string.Login)
                val preText = if (showLoginForm.value) stringResource(id = R.string.NewUser) else stringResource(R.string.AlreadyUser)
                Text(text = preText)
                Text(text = text, modifier = Modifier
                    .padding(start = 5.dp)
                    .clickable {
                        showLoginForm.value = !showLoginForm.value
                    },
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Bold
                )
            }

        }
    }
}

@Preview
@Composable
fun UserForm(
    loading: Boolean = false,
    isCreateAccount: Boolean = false,
    onDone: (String, String) -> Unit = { email, pwd -> }
) {
    val email = rememberSaveable { mutableStateOf("") }
    val password = rememberSaveable { mutableStateOf("") }
    val passwordVisibility = rememberSaveable { mutableStateOf(false) }
    val passwordFocusRequest = FocusRequester.Default
    val keyboardController = LocalSoftwareKeyboardController.current
    val valid = remember(email.value, password.value) {
        email.value.trim().isNotEmpty() && password.value.isNotEmpty()
    }

    val modifier = Modifier
        .background(MaterialTheme.colorScheme.background)
        .verticalScroll(rememberScrollState())

    Column(modifier.padding(start = 30.dp, end = 30.dp), horizontalAlignment = Alignment.CenterHorizontally) {

        if (isCreateAccount)
            Text(
                text = stringResource(id = R.string.CreateAccountHelp),
                modifier = Modifier.padding(4.dp)
            )
        else
            Text(text = "")

        EmailInput(emailState = email, enabled = !loading, onAction = KeyboardActions {
            passwordFocusRequest.requestFocus()
        })

        PasswordInput(
            modifier = Modifier.focusRequester(passwordFocusRequest),
            passwordState = password,
            labelId = stringResource(id = R.string.Password),
            enabled = !loading,
            passwordVisibility = passwordVisibility,
            onAction = KeyboardActions {
                if (!valid) return@KeyboardActions
                onDone(email.value.trim(), password.value.trim())
            })

        SubmitButton(
            textId = if (isCreateAccount) stringResource(id = R.string.CreateAccount)
            else stringResource(id = R.string.Login),
            loading = loading,
            validInputs = valid
        ) {
            onDone(email.value.trim(), password.value.trim())
            keyboardController?.hide()
        }

    }
}

@Composable
fun SubmitButton(
    textId: String,
    loading: Boolean,
    validInputs: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(3.dp)
            .fillMaxWidth(),
        enabled = !loading && validInputs,
        shape = CircleShape
    ) {

        if (loading) CircularProgressIndicator(modifier = Modifier.size(25.dp))
        else Text(text = textId, modifier = Modifier.padding(5.dp))

    }
}


