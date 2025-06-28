package br.com.amanfron.ecommerce_app.features.login

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.amanfron.ecommerce_app.features.login.LoginViewModel.LoginViewState
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    val state = mutableStateOf(LoginViewState())
    var loginClicked = false
    var createAccountClicked = false
    var navigateToHome = false

    @Before
    fun setUp() {
        composeTestRule.setContent {
            LoginScreen(
                uiState = state.value,
                onEmailChanged = { state.value = state.value.copy(email = it) },
                onPasswordChanged = { state.value = state.value.copy(password = it) },
                navigateToCreateAccount = { createAccountClicked = true },
                navigateToHome = { navigateToHome = true },
                onLoginButtonClick = { loginClicked = true })
        }
    }

    @Test
    fun loginScreen_displaysInitialState_andCallAllCallbacks() {
        composeTestRule.onNodeWithTag("Email").assertIsDisplayed()
        composeTestRule.onNodeWithTag("Senha").assertIsDisplayed()
        composeTestRule.onNodeWithText("Login").assertIsDisplayed()
        composeTestRule.onNodeWithText("Crie sua conta aqui").assertIsDisplayed()

        composeTestRule.onNodeWithTag("Email").performTextInput("a@a.com")
        composeTestRule.onNodeWithTag("Senha").performTextInput("novaSenha123")

        assert(state.value.email == "a@a.com")
        assert(state.value.password == "novaSenha123")

        composeTestRule.onNodeWithText("Crie sua conta aqui").performClick()
        assert(createAccountClicked)

        composeTestRule.onNodeWithText("Login").performClick()
        assert(loginClicked)

        state.value = state.value.copy(isSuccessLogin = true)
        assert(state.value.isSuccessLogin)
        composeTestRule.waitForIdle()
        assert(navigateToHome)
    }
}

