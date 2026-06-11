package com.deutschdreamers.footballbingo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
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

@Composable
fun FootballBingoApp() {
    var gameState by remember { mutableStateOf<GameState?>(null) }

    if (gameState == null) {
        HomeScreen(
            onStartGame = { language, cardNumber ->
                gameState = GameState.create(language, cardNumber)
            }
        )
    } else {
        BingoScreen(
            gameState = gameState!!,
            onBack = { gameState = null },
            onToggleSquare = { index -> gameState = gameState?.toggleSquare(index) },
            onNewCard = { language, cardNumber ->
                gameState = GameState.create(language, cardNumber)
            }
        )
    }
}
