package com.lalosapps.firebaseauth.ui.auth

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.google.firebase.auth.FirebaseUser
import com.lalosapps.firebaseauth.R
import com.lalosapps.firebaseauth.data.Resource
import com.lalosapps.firebaseauth.navigation.ROUTE_HOME
import com.lalosapps.firebaseauth.navigation.ROUTE_SIGNUP
import com.lalosapps.firebaseauth.ui.theme.AppTheme
import com.lalosapps.firebaseauth.ui.theme.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    loginFlow: Resource<FirebaseUser>?,
    onLoginClick: (String, String) -> Unit,
    onNavigate: (String) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {

        val (refHeader, refEmail, refPassword, refButtonLogin, refTextSignup, refLoader) = createRefs()
        val spacing = MaterialTheme.spacing

        Box(
            modifier = Modifier
                .constrainAs(refHeader) {
                    top.linkTo(parent.top, spacing.extraLarge)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                }
                .wrapContentSize()
        ) {
            AuthHeader()
        }


        TextField(
            value = email,
            onValueChange = {
                email = it
            },
            label = {
                Text(text = stringResource(id = R.string.email))
            },
            modifier = Modifier.constrainAs(refEmail) {
                top.linkTo(refHeader.bottom, spacing.extraLarge)
                start.linkTo(parent.start, spacing.large)
                end.linkTo(parent.end, spacing.large)
                width = Dimension.fillToConstraints
            },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                autoCorrect = false,
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
        )

        TextField(
            value = password,
            onValueChange = {
                password = it
            },
            label = {
                Text(text = stringResource(id = R.string.password))
            },
            modifier = Modifier.constrainAs(refPassword) {
                top.linkTo(refEmail.bottom, spacing.medium)
                start.linkTo(parent.start, spacing.large)
                end.linkTo(parent.end, spacing.large)
                width = Dimension.fillToConstraints
            },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                autoCorrect = false,
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            )
        )

        Button(
            onClick = { onLoginClick(email, password) },
            modifier = Modifier.constrainAs(refButtonLogin) {
                top.linkTo(refPassword.bottom, spacing.large)
                start.linkTo(parent.start, spacing.extraLarge)
                end.linkTo(parent.end, spacing.extraLarge)
                width = Dimension.fillToConstraints
            }
        ) {
            Text(
                text = stringResource(id = R.string.login),
                style = MaterialTheme.typography.titleMedium
            )
        }

        Text(
            modifier = Modifier
                .constrainAs(refTextSignup) {
                    top.linkTo(refButtonLogin.bottom, spacing.medium)
                    start.linkTo(parent.start, spacing.extraLarge)
                    end.linkTo(parent.end, spacing.extraLarge)
                }
                .clickable {
                    onNavigate(ROUTE_SIGNUP)
                },
            text = stringResource(id = R.string.dont_have_account),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface
        )

        loginFlow?.let {
            when (it) {
                Resource.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.constrainAs(refLoader) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    })
                }
                is Resource.Success -> {
                    LaunchedEffect(key1 = Unit) {
                        onNavigate(ROUTE_HOME)
                    }
                }
                is Resource.Failure -> {
                    val context = LocalContext.current
                    LaunchedEffect(key1 = it) {
                        Toast.makeText(context, it.exception.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
fun LoginScreenPreviewLight() {
    AppTheme {
        LoginScreen(null, { _, _ -> }, {})
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun LoginScreenPreviewDark() {
    AppTheme {
        LoginScreen(null, { _, _ -> }, {})
    }
}
