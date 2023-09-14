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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import br.com.amanfron.ecommerce_app.R
import br.com.amanfron.ecommerce_app.features.createaccount.CreateAccountViewModel.CreateAccountViewState
import br.com.amanfron.ecommerce_app.ui.customviews.LoadingView
import br.com.amanfron.ecommerce_app.ui.customviews.OutlinedTextError

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
                Toast.makeText(context, "Registrado com sucesso!", Toast.LENGTH_SHORT).show()
                navController.popBackStack()
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
            label = { Text("Nome") },
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
            label = { Text("Email") },
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
            label = { Text("Senha") },
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
            Text(text = "Criar Conta")
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