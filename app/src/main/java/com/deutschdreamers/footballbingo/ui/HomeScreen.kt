package com.deutschdreamers.footballbingo.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.deutschdreamers.footballbingo.Language

val GreenDark = Color(0xFF1B5E20)
val GreenMid = Color(0xFF2E7D32)
val Amber = Color(0xFFFFC107)

@Composable
fun HomeScreen(onStartGame: (Language, Int) -> Unit, onAbout: () -> Unit) {
    var selectedLanguage by remember { mutableStateOf(Language.ENGLISH) }
    var cardNumber by remember { mutableStateOf(1) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(GreenDark, GreenMid)))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            // Top row: about/info button pinned to the right
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = onAbout) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "About",
                        tint = Color.White
                    )
                }
            }

            // Scrollable centred content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 28.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

            Text(text = "⚽", fontSize = 80.sp)
            Spacer(Modifier.height(12.dp))

            Text(
                text = when (selectedLanguage) {
                    Language.ENGLISH -> "Football Bingo"
                    Language.GERMAN -> "Fußball Bingo"
                    Language.HINDI -> "फुटबॉल बिंगो"
                },
                fontSize = 34.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            Text(
                text = when (selectedLanguage) {
                    Language.ENGLISH -> "Party Game Edition"
                    Language.GERMAN -> "Party-Spiel Edition"
                    Language.HINDI -> "पार्टी गेम संस्करण"
                },
                fontSize = 14.sp,
                color = Amber,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(48.dp))

            SectionLabel(
                text = when (selectedLanguage) {
                    Language.ENGLISH -> "LANGUAGE"
                    Language.GERMAN -> "SPRACHE"
                    Language.HINDI -> "भाषा"
                }
            )
            Spacer(Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Language.entries.forEach { lang ->
                    LanguageButton(
                        language = lang,
                        isSelected = lang == selectedLanguage,
                        onClick = { selectedLanguage = lang },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(Modifier.height(40.dp))

            SectionLabel(
                text = when (selectedLanguage) {
                    Language.ENGLISH -> "CARD NUMBER"
                    Language.GERMAN -> "KARTENNUMMER"
                    Language.HINDI -> "कार्ड नंबर"
                }
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = when (selectedLanguage) {
                    Language.ENGLISH -> "Each number gives everyone a unique card!"
                    Language.GERMAN -> "Jede Nummer ergibt eine einzigartige Karte!"
                    Language.HINDI -> "हर नंबर का अलग कार्ड होगा!"
                },
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.5f),
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(16.dp))

            CardNumberPicker(
                value = cardNumber,
                onDecrement = { if (cardNumber > 1) cardNumber-- },
                onIncrement = { if (cardNumber < 20) cardNumber++ }
            )

            Spacer(Modifier.height(52.dp))

            Button(
                onClick = { onStartGame(selectedLanguage, cardNumber) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Amber,
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(30.dp),
                elevation = ButtonDefaults.buttonElevation(8.dp)
            ) {
                Text(
                    text = when (selectedLanguage) {
                        Language.ENGLISH -> "🎉  Start Game!"
                        Language.GERMAN -> "🎉  Spiel starten!"
                        Language.HINDI -> "🎉  खेल शुरू करें!"
                    },
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(48.dp))
            } // inner content Column
        } // outer system-bar Column
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White.copy(alpha = 0.55f),
        letterSpacing = 2.sp
    )
}

@Composable
private fun LanguageButton(
    language: Language,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bgColor by animateColorAsState(
        if (isSelected) Amber else Color.White.copy(alpha = 0.12f),
        label = "lang_bg"
    )
    val textColor by animateColorAsState(
        if (isSelected) Color.Black else Color.White,
        label = "lang_text"
    )

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(bgColor)
            .clickable(onClick = onClick)
            .padding(vertical = 14.dp, horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = language.flag, fontSize = 26.sp)
            Spacer(Modifier.height(6.dp))
            Text(
                text = language.displayName,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = textColor,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun CardNumberPicker(
    value: Int,
    onDecrement: () -> Unit,
    onIncrement: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(28.dp)
    ) {
        StepButton(label = "−", onClick = onDecrement, enabled = value > 1)

        Text(
            text = value.toString().padStart(2, '0'),
            color = Color.White,
            fontSize = 52.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.widthIn(min = 88.dp),
            textAlign = TextAlign.Center
        )

        StepButton(label = "+", onClick = onIncrement, enabled = value < 20)
    }
}

@Composable
private fun StepButton(label: String, onClick: () -> Unit, enabled: Boolean) {
    val alpha = if (enabled) 1f else 0.3f
    Box(
        modifier = Modifier
            .size(56.dp)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.15f * alpha))
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = Color.White.copy(alpha = alpha),
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 30.sp
        )
    }
}
