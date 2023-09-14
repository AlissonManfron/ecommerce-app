package br.com.amanfron.ecommerce_app.features.login

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import br.com.amanfron.ecommerce_app.R
import br.com.amanfron.ecommerce_app.features.login.LoginViewModel.LoginViewState
import br.com.amanfron.ecommerce_app.ui.customviews.OutlinedTextError
import br.com.amanfron.ecommerce_app.ui.theme.EcommerceappTheme
import br.com.amanfron.ecommerce_app.utils.NavRoutes

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginScreen(
    keyboardController: SoftwareKeyboardController?,
    navController: NavController,
    viewModel: LoginViewModel
) {
    val context = LocalContext.current
    val state = viewModel.state.value

    LoginScreen(
        state,
        keyboardController,
        onEmailChanged = viewModel::setEmail,
        onPasswordChanged = viewModel::setPassword,
        onButtonLoginClick = viewModel::onButtonLoginClick,
        onButtonCreateAccountClick = { navController.navigate(NavRoutes.CREATE_ACCOUNT) }
    )

    DisposableEffect(state) {
        when {
            state.isSuccessLogin -> {
                Toast.makeText(context, "Logado com sucesso!", Toast.LENGTH_SHORT).show()
                navController.navigate(NavRoutes.HOME)
            }

            state.shouldShowDefaultError -> {
                Toast.makeText(
                    context,
                    "Ops, ocorreu um erro, tente novamente!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        onDispose {
            state.isSuccessLogin = false
            state.shouldShowDefaultError = false
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun LoginScreen(
    state: LoginViewState,
    keyboardController: SoftwareKeyboardController?,
    onEmailChanged: (email: String) -> Unit,
    onPasswordChanged: (password: String) -> Unit,
    onButtonLoginClick: () -> Unit,
    onButtonCreateAccountClick: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = null,
            modifier = Modifier.size(120.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = state.email,
            onValueChange = { onEmailChanged(it) },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            placeholder = { Text(text = "Email") }
        )
        OutlinedTextError(state.isEmailError)

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = state.password,
            onValueChange = { onPasswordChanged(it) },
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                    onButtonLoginClick()
                }
            ),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            placeholder = { Text(text = "Password") }
        )
        OutlinedTextError(state.isPasswordError)

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onButtonLoginClick() },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text(text = "Login")
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = { onButtonCreateAccountClick() }
        ) {
            Text(text = "Crie sua conta aqui")
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    EcommerceappTheme {
        LoginScreen(
            LoginViewState(),
            null,
            onEmailChanged = {},
            onPasswordChanged = {},
            onButtonLoginClick = {},
            onButtonCreateAccountClick = {}
        )
    }
}