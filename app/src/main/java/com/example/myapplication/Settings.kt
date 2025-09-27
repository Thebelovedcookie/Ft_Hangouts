package com.example.myapplication

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource

@Composable
fun Settings(
    currentColor: Color,
    onColorSelected: (Color) -> Unit,
) {
    val colorList = listOf(
        Color.Black, Color.DarkGray, Color.Gray,
        Color(0xFF6082B6), Color(0xFF7393B3), Color(0xFF8A9A5B),
        Color(0xFF4B3B4D), Color(0xFF7F5A5A), Color(0xFF6B7B8C),
        Color(0xFF5C5C5C), Color(0xFF8C7F8C), Color(0xFF9E7D7D),
        Color(0xFF7D8C8C), Color(0xFFAAAAAA), Color(0xFFB0A3B0),
        Color(0xFFB77C7C), Color(0xFF808080), Color(0xFF6F6F6F)
    )

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE


    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // --- HEADER ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(if (isLandscape) 50.dp else 230.dp),
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
            if (isLandscape) {
                Landscape2(colorList, currentColor, onColorSelected)
            } else {
                Portrait2(colorList, currentColor, onColorSelected)
            }

        }
    }
}

@Composable
fun Landscape2(colorList: List<Color>, currentColor: Color, onColorSelected: (Color) -> Unit) {
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
            Spacer(modifier = Modifier.height(8.dp))

            // --- Header background color ---
            Text(
                text = stringResource(R.string.choose_header_color),
                color = Color.White
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(colorList) { color ->
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
        }
    }
}

@Composable
fun Portrait2( colorList: List<Color>, currentColor: Color, onColorSelected: (Color) -> Unit) {
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

            // --- Header background color ---
            Text(
                text = stringResource(R.string.choose_header_color),
                color = Color.White
            )

            Spacer(modifier = Modifier.height(24.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                items(colorList) { color ->
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
        }
    }
}