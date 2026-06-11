package com.deutschdreamers.footballbingo.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.Hyphens
import androidx.compose.ui.text.style.LineBreak
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
            .background(MaterialTheme.colorScheme.background)
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
                .verticalScroll(rememberScrollState())
                .padding(6.dp)
                .navigationBarsPadding(),
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
                    fontSize = 18.sp
                )
                Text(
                    text = when (language) {
                        Language.ENGLISH -> "Card #$cardNumber"
                        Language.GERMAN -> "Karte #$cardNumber"
                        Language.HINDI -> "कार्ड #$cardNumber"
                    },
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
        actions = {
            IconButton(onClick = onRefresh) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "New card"
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
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
    val scheme = MaterialTheme.colorScheme
    val bgColor by animateColorAsState(
        when {
            isWinning -> scheme.secondary
            isMarked  -> scheme.primary
            else      -> scheme.surface
        },
        label = "cell_bg"
    )
    val borderColor by animateColorAsState(
        when {
            isWinning -> scheme.secondary
            isMarked  -> scheme.primaryContainer
            else      -> scheme.outlineVariant
        },
        label = "cell_border"
    )
    val textColor = when {
        isWinning -> scheme.onSecondary
        isMarked  -> scheme.onPrimary
        else      -> scheme.onSurface
    }
    val scale by animateFloatAsState(
        targetValue = if (isMarked || isWinning) 0.97f else 1f,
        animationSpec = spring(dampingRatio = 0.6f),
        label = "cell_scale"
    )

    Box(
        modifier = modifier
            .aspectRatio(0.7f)
            .scale(scale)
            .clip(RoundedCornerShape(6.dp))
            .background(bgColor)
            .border(1.5.dp, borderColor, RoundedCornerShape(6.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 11.sp,
            lineHeight = 14.sp,
            textAlign = TextAlign.Center,
            color = textColor,
            fontWeight = if (isMarked || isWinning) FontWeight.SemiBold else FontWeight.Normal,
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp),
            overflow = TextOverflow.Clip,
            maxLines = 6,
            style = TextStyle(
                hyphens = Hyphens.Auto,
                lineBreak = LineBreak.Paragraph
            )
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
                onClick = onDismiss,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = when (language) {
                        Language.ENGLISH -> "Keep Playing"
                        Language.GERMAN -> "Weiterspielen"
                        Language.HINDI -> "खेलते रहें"
                    },
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onNewCard,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = when (language) {
                        Language.ENGLISH -> "New Card"
                        Language.GERMAN -> "Neue Karte"
                        Language.HINDI -> "नया कार्ड"
                    }
                )
            }
        }
    )
}
