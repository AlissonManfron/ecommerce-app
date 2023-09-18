package br.com.amanfron.ecommerce_app.features.createaccount

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import br.com.amanfron.ecommerce_app.R
import br.com.amanfron.ecommerce_app.features.createaccount.CreateAccountViewModel.CreateAccountViewState
import br.com.amanfron.ecommerce_app.ui.customviews.LoadingView
import br.com.amanfron.ecommerce_app.ui.customviews.OutlinedTextError
import br.com.amanfron.ecommerce_app.utils.NavRoutes

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CreateAccountScreen(
    keyboardController: SoftwareKeyboardController?,
    viewModel: CreateAccountViewModel,
    navController: NavHostController
) {
    val context = LocalContext.current
    val state = viewModel.state.value

    CreateAccountScreen(
        state = state,
        onNameChanged = viewModel::setName,
        onEmailChanged = viewModel::setEmail,
        onPasswordChanged = viewModel::setPassword,
        onButtonCreateAccountClick = viewModel::onButtonCreateAccountClick,
        keyboardController = keyboardController
    )

    DisposableEffect(state) {
        when {
            state.isRegisterIsSuccess -> {
                Toast.makeText(
                    context, R.string.create_account_success_message, Toast.LENGTH_SHORT
                ).show()
                navController.navigate(
                    NavRoutes.HOME,
                    navOptions = NavOptions.Builder()
                        .setPopUpTo(NavRoutes.LOGIN, true)
                        .build()
                )
            }

            state.shouldShowDefaultError -> {
                Toast.makeText(
                    context, R.string.try_again_message, Toast.LENGTH_SHORT
                ).show()
            }
        }
        onDispose {
            state.isRegisterIsSuccess = false
            state.shouldShowDefaultError = false
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun CreateAccountScreen(
    state: CreateAccountViewState,
    onNameChanged: (name: String) -> Unit,
    onEmailChanged: (email: String) -> Unit,
    onPasswordChanged: (password: String) -> Unit,
    onButtonCreateAccountClick: () -> Unit,
    keyboardController: SoftwareKeyboardController?
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
            value = state.name,
            onValueChange = { onNameChanged(it) },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            label = { Text(stringResource(id = R.string.name_field_hint)) },
            modifier = Modifier
                .fillMaxWidth()
        )
        OutlinedTextError(state.isNameError)

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = state.email,
            onValueChange = { onEmailChanged(it) },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            label = { Text(stringResource(id = R.string.email_field_hint)) },
            modifier = Modifier
                .fillMaxWidth()
        )
        OutlinedTextError(state.isEmailError)

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = state.password,
            onValueChange = { onPasswordChanged(it) },
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                    onButtonCreateAccountClick()
                }
            ),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            label = { Text(stringResource(id = R.string.password_field_hint)) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
        )
        OutlinedTextError(state.isPasswordError)

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onButtonCreateAccountClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text(text = stringResource(id = R.string.create_account_button_text))
        }
    }

    if (state.shouldShowLoading) {
        LoadingView()
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Preview(showBackground = true)
@Composable
fun PreviewCreateAccountScreen() {
    CreateAccountScreen(
        CreateAccountViewState(),
        onNameChanged = {},
        onEmailChanged = {},
        onPasswordChanged = {},
        onButtonCreateAccountClick = {},
        null
    )
}