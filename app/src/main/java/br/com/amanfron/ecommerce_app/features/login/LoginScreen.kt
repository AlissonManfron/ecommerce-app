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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import br.com.amanfron.ecommerce_app.R
import br.com.amanfron.ecommerce_app.features.login.LoginViewModel.LoginViewState
import br.com.amanfron.ecommerce_app.ui.customviews.LoadingView
import br.com.amanfron.ecommerce_app.ui.customviews.OutlinedTextError
import br.com.amanfron.ecommerce_app.ui.theme.EcommerceAppTheme
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

    LaunchedEffect(state) {
        when {
            state.isSuccessLogin -> {
                state.isSuccessLogin = false
                Toast.makeText(context, R.string.login_success_message, Toast.LENGTH_SHORT).show()
                navController.navigate(
                    NavRoutes.HOME,
                    navOptions = NavOptions.Builder()
                        .setPopUpTo(NavRoutes.LOGIN, true)
                        .build()
                )
            }

            state.shouldShowDefaultError -> {
                state.shouldShowDefaultError = false
                Toast.makeText(context, R.string.try_again_message, Toast.LENGTH_SHORT).show()
            }
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
            placeholder = {
                Text(text = stringResource(id = R.string.email_field_hint))
            }
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
            placeholder = { Text(text = stringResource(id = R.string.password_field_hint)) }
        )
        OutlinedTextError(state.isPasswordError)

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onButtonLoginClick() },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text(text = stringResource(id = R.string.login_login_button_text))
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = { onButtonCreateAccountClick() }
        ) {
            Text(text = stringResource(id = R.string.login_create_account_button_text))
        }
    }

    if (state.shouldShowLoading) {
        LoadingView()
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() = EcommerceAppTheme {
    LoginScreen(
        LoginViewState(),
        null,
        onEmailChanged = {},
        onPasswordChanged = {},
        onButtonLoginClick = {},
        onButtonCreateAccountClick = {}
    )
}