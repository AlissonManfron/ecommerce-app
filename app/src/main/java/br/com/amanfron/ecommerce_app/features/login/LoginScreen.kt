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
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.amanfron.ecommerce_app.R
import br.com.amanfron.ecommerce_app.features.login.LoginViewModel.LoginEffect.NavigateToHome
import br.com.amanfron.ecommerce_app.features.login.LoginViewModel.LoginEffect.ShowErrorToast
import br.com.amanfron.ecommerce_app.features.login.LoginViewModel.LoginIntent
import br.com.amanfron.ecommerce_app.features.login.LoginViewModel.LoginViewState
import br.com.amanfron.ecommerce_app.ui.customviews.LoadingView
import br.com.amanfron.ecommerce_app.ui.customviews.OutlinedTextError
import br.com.amanfron.ecommerce_app.ui.theme.EcommerceAppTheme
import br.com.amanfron.ecommerce_app.ui.utils.ObserveAsEvents

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    navigateToCreateAccount: () -> Unit,
    navigateToHome: () -> Unit,
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    viewModel.effect.ObserveAsEvents { effect ->
        when (effect) {
            is NavigateToHome -> navigateToHome()
            is ShowErrorToast -> {
                Toast.makeText(context, R.string.try_again_message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    LoginScreen(
        uiState = uiState,
        keyboardController = keyboardController,
        onEmailChanged = { viewModel.onIntent(LoginIntent.SetEmail(it)) },
        onPasswordChanged = { viewModel.onIntent(LoginIntent.SetPassword(it)) },
        onLoginButtonClick = { viewModel.onIntent(LoginIntent.OnLoginClick) },
        onCreateAccountButtonClick = navigateToCreateAccount
    )
}

@Composable
private fun LoginScreen(
    uiState: LoginViewState,
    keyboardController: SoftwareKeyboardController?,
    onEmailChanged: (email: String) -> Unit,
    onPasswordChanged: (password: String) -> Unit,
    onLoginButtonClick: () -> Unit,
    onCreateAccountButtonClick: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_splash_logo),
            contentDescription = null,
            modifier = Modifier.size(160.dp)
        )

        Spacer(modifier = Modifier.height(40.dp))

        TextField(
            value = uiState.email,
            onValueChange = { onEmailChanged(it) },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .testTag("Email"),
            placeholder = {
                Text(text = stringResource(id = R.string.email_field_hint))
            }
        )
        OutlinedTextError(uiState.isEmailError)

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = uiState.password,
            onValueChange = { onPasswordChanged(it) },
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                    onLoginButtonClick()
                }
            ),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .testTag("Senha"),
            placeholder = { Text(text = stringResource(id = R.string.password_field_hint)) }
        )
        OutlinedTextError(uiState.isPasswordError)

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onLoginButtonClick() },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text(text = stringResource(id = R.string.login_login_button_text))
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = { onCreateAccountButtonClick() }
        ) {
            Text(text = stringResource(id = R.string.login_create_account_button_text))
        }
    }

    if (uiState.shouldShowLoading) {
        LoadingView()
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() = EcommerceAppTheme {
    LoginScreen(
        LoginViewState(),
        keyboardController = null,
        onEmailChanged = {},
        onPasswordChanged = {},
        onLoginButtonClick = {},
        onCreateAccountButtonClick = {}
    )
}
