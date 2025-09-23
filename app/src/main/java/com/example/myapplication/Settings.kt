package com.example.myapplication

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat

@Composable
fun Settings(
    currentColor: Color,
    onColorSelected: (Color) -> Unit,
    currentLanguage: String,
    onLanguageSelected: (String) -> Unit,
    activity: Activity
) {

    LaunchedEffect(currentColor) {
        val window = activity.window
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars =
            currentColor.luminance() > 0.5f
        window.statusBarColor = currentColor.toArgb()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // --- HEADER ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(230.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.settings_title),
                fontSize = 28.sp,
                color = Color.White
            )
        }

        // --- CONTENT BLOCK ---
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(Color.Black)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.99f)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0xFF121212))
                    .padding(24.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(16.dp))

                    // --- Header Color Selection ---
                    Text(
                        text = stringResource(R.string.choose_header_color),
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        items(
                            listOf(
                                Color.Black, Color.DarkGray, Color.Gray,
                                Color(0xFF6082B6), Color(0xFF7393B3), Color(0xFF8A9A5B),
                                Color(0xFF4B3B4D), Color(0xFF7F5A5A), Color(0xFF6B7B8C),
                                Color(0xFF5C5C5C), Color(0xFF8C7F8C), Color(0xFF9E7D7D),
                                Color(0xFF7D8C8C), Color(0xFFAAAAAA), Color(0xFFB0A3B0),
                                Color(0xFFB77C7C), Color(0xFF808080), Color(0xFF6F6F6F)
                            )
                        ) { color ->
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(color, shape = CircleShape)
                                    .clickable { onColorSelected(color) }
                                    .border(
                                        width = if (color == currentColor) 3.dp else 1.dp,
                                        color = if (color == currentColor) Color.Yellow else Color.White,
                                        shape = CircleShape
                                    )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        items(
                            listOf(
                                Color.Black, Color.DarkGray, Color.Gray,
                                Color(0xFF6082B6), Color(0xFF7393B3), Color(0xFF8A9A5B),
                                Color(0xFF4B3B4D), Color(0xFF7F5A5A), Color(0xFF6B7B8C),
                                Color(0xFF5C5C5C), Color(0xFF8C7F8C), Color(0xFF9E7D7D),
                                Color(0xFF7D8C8C), Color(0xFFAAAAAA), Color(0xFFB0A3B0),
                                Color(0xFFB77C7C), Color(0xFF808080), Color(0xFF6F6F6F)
                            )
                        ) { color ->
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(color, shape = CircleShape)
                                    .clickable { onColorSelected(color) }
                                    .border(
                                        width = if (color == currentColor) 3.dp else 1.dp,
                                        color = if (color == currentColor) Color.Yellow else Color.White,
                                        shape = CircleShape
                                    )
                            )
                        }
                    }


                    Spacer(modifier = Modifier.height(32.dp))

                    // --- Language Selection ---
                    Text(
                        text = stringResource(R.string.choose_language),
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        listOf("fr" to "Français", "en" to "English").forEach { (langCode, label) ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        if (currentLanguage == langCode) Color(0xFF7C7091) else Color.Gray
                                    )
                                    .clickable { onLanguageSelected(langCode) }
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                Text(text = label, color = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }
}
