package dev.ml.lansonesscanapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import dev.ml.lansonesscanapp.ui.screens.MainScreen
import dev.ml.lansonesscanapp.ui.screens.SplashScreen
import dev.ml.lansonesscanapp.ui.theme.LansonesScanAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LansonesScanAppTheme {
                var showSplash by remember { mutableStateOf(true) }
                
                if (showSplash) {
                    SplashScreen(onSplashFinished = { showSplash = false })
                } else {
                    MainScreen()
                }
            }
        }
    }
}