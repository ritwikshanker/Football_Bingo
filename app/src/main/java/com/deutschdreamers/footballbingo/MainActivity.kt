package com.deutschdreamers.footballbingo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import com.deutschdreamers.footballbingo.ui.AboutScreen
import com.deutschdreamers.footballbingo.ui.BingoScreen
import com.deutschdreamers.footballbingo.ui.HomeScreen
import com.deutschdreamers.footballbingo.ui.theme.FootballBingoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FootballBingoTheme {
                FootballBingoApp()
            }
        }
    }
}

sealed class Screen {
    object Home : Screen()
    object About : Screen()
    object Game : Screen()
}

@Composable
fun FootballBingoApp() {
    var screen by remember { mutableStateOf<Screen>(Screen.Home) }
    var gameState by remember { mutableStateOf<GameState?>(null) }

    when (screen) {
        is Screen.Home -> HomeScreen(
            onStartGame = { language, cardNumber ->
                gameState = GameState.create(language, cardNumber)
                screen = Screen.Game
            },
            onAbout = { screen = Screen.About }
        )
        is Screen.About -> AboutScreen(
            onBack = { screen = Screen.Home }
        )
        is Screen.Game -> BingoScreen(
            gameState = gameState!!,
            onBack = { screen = Screen.Home },
            onToggleSquare = { index -> gameState = gameState?.toggleSquare(index) },
            onNewCard = { language, cardNumber ->
                gameState = GameState.create(language, cardNumber)
            }
        )
    }
}
