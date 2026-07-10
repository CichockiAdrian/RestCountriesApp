package com.example.restcountriesapp.feature.auth

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract

@Composable
fun AuthRoute(
    modifier: Modifier = Modifier
) {
    val signInLauncher = rememberLauncherForActivityResult(
        FirebaseAuthUIActivityResultContract(),
    ) { result ->
        // Result is handled by FirebaseAuth.AuthStateListener in Repository/ViewModel
    }

    AuthScreen(
        onSignInClick = {
            val signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(
                    listOf(
                        AuthUI.IdpConfig.GoogleBuilder().build()
                    )
                )
                .build()
            signInLauncher.launch(signInIntent)
        },
        modifier = modifier
    )
}
