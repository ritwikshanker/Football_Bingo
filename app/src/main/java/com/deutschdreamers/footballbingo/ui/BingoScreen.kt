package com.deutschdreamers.footballbingo.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.deutschdreamers.footballbingo.GameState
import com.deutschdreamers.footballbingo.Language

@Composable
fun BingoScreen(
    gameState: GameState,
    onBack: () -> Unit,
    onToggleSquare: (Int) -> Unit,
    onNewCard: (Language, Int) -> Unit
) {
    var showWinDialog by remember { mutableStateOf(false) }

    LaunchedEffect(gameState.winningLines.size) {
        if (gameState.winningLines.isNotEmpty()) showWinDialog = true
    }

    val winSquares = remember(gameState.winningLines) {
        gameState.winningLines.flatten().toSet()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        BingoTopBar(
            language = gameState.language,
            cardNumber = gameState.cardNumber,
            onBack = onBack,
            onRefresh = { onNewCard(gameState.language, (gameState.cardNumber % 20) + 1) }
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(6.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            BingoGrid(
                items = gameState.items,
                markedSquares = gameState.markedSquares,
                winSquares = winSquares,
                onSquareTapped = onToggleSquare
            )
        }
    }

    if (showWinDialog) {
        WinDialog(
            language = gameState.language,
            onDismiss = { showWinDialog = false },
            onNewCard = {
                showWinDialog = false
                onNewCard(gameState.language, (gameState.cardNumber % 20) + 1)
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BingoTopBar(
    language: Language,
    cardNumber: Int,
    onBack: () -> Unit,
    onRefresh: () -> Unit
) {
    TopAppBar(
        title = {
            Column {
                Text(
                    text = when (language) {
                        Language.ENGLISH -> "Football Bingo"
                        Language.GERMAN -> "Fußball Bingo"
                        Language.HINDI -> "फुटबॉल बिंगो"
                    },
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp,
                    color = Color.White
                )
                Text(
                    text = when (language) {
                        Language.ENGLISH -> "Card #$cardNumber"
                        Language.GERMAN -> "Karte #$cardNumber"
                        Language.HINDI -> "कार्ड #$cardNumber"
                    },
                    fontSize = 12.sp,
                    color = Amber
                )
            }
        },
        navigationIcon = {
            Box(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White.copy(alpha = 0.15f))
                    .clickable(onClick = onBack),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "←", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
        },
        actions = {
            Box(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White.copy(alpha = 0.15f))
                    .clickable(onClick = onRefresh),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "↻", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = GreenDark
        ),
        modifier = Modifier.statusBarsPadding()
    )
}

@Composable
private fun BingoGrid(
    items: List<String>,
    markedSquares: Set<Int>,
    winSquares: Set<Int>,
    onSquareTapped: (Int) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        for (row in 0..4) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                for (col in 0..4) {
                    val index = row * 5 + col
                    BingoCell(
                        text = items[index],
                        isMarked = index in markedSquares,
                        isWinning = index in winSquares,
                        onClick = { onSquareTapped(index) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun BingoCell(
    text: String,
    isMarked: Boolean,
    isWinning: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bgColor by animateColorAsState(
        when {
            isWinning -> WinGold
            isMarked -> GreenMid
            else -> Color.White
        },
        label = "cell_bg"
    )
    val borderColor by animateColorAsState(
        when {
            isWinning -> AmberDark
            isMarked -> GreenDark
            else -> Color(0xFFCCCCCC)
        },
        label = "cell_border"
    )
    val textColor = when {
        isWinning -> Color(0xFF1A1A1A)
        isMarked -> Color.White
        else -> Color(0xFF1A1A1A)
    }
    val scale by animateFloatAsState(
        targetValue = if (isMarked || isWinning) 0.97f else 1f,
        animationSpec = spring(dampingRatio = 0.6f),
        label = "cell_scale"
    )

    Box(
        modifier = modifier
            .aspectRatio(0.78f)
            .scale(scale)
            .clip(RoundedCornerShape(6.dp))
            .background(bgColor)
            .border(1.5.dp, borderColor, RoundedCornerShape(6.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 8.5.sp,
            lineHeight = 11.sp,
            textAlign = TextAlign.Center,
            color = textColor,
            fontWeight = if (isMarked || isWinning) FontWeight.SemiBold else FontWeight.Normal,
            modifier = Modifier.padding(horizontal = 3.dp, vertical = 4.dp),
            overflow = TextOverflow.Clip,
            maxLines = 6
        )
    }
}

@Composable
private fun WinDialog(
    language: Language,
    onDismiss: () -> Unit,
    onNewCard: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        title = {
            Text(
                text = "🎉  BINGO!  🎉",
                fontSize = 30.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Text(
                text = when (language) {
                    Language.ENGLISH -> "You completed a line! 🎊"
                    Language.GERMAN -> "Du hast eine Reihe! 🎊"
                    Language.HINDI -> "आपने बिंगो किया! 🎊"
                },
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            Button(
                onClick = onNewCard,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Amber,
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = when (language) {
                        Language.ENGLISH -> "New Card"
                        Language.GERMAN -> "Neue Karte"
                        Language.HINDI -> "नया कार्ड"
                    },
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = when (language) {
                        Language.ENGLISH -> "Keep Playing"
                        Language.GERMAN -> "Weiterspielen"
                        Language.HINDI -> "खेलते रहें"
                    },
                    color = GreenDark
                )
            }
        }
    )
}
