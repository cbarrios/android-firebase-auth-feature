package com.lalosapps.firebaseauth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.lalosapps.firebaseauth.navigation.AppNavHost
import com.lalosapps.firebaseauth.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                AppNavHost()
            }
        }
    }
}