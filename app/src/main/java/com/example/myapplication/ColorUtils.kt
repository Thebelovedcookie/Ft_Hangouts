package com.example.myapplication

import androidx.compose.ui.graphics.Color
import kotlin.math.absoluteValue

fun getColorForName(name: String): Color {
    val colors = listOf(
        Color(0xFFFFCDD2), // rose pâle
        Color(0xFFBBDEFB), // bleu pâle
        Color(0xFFC8E6C9), // vert pâle
        Color(0xFFFFE0B2), // orange pâle
        Color(0xFFE1BEE7)  // violet pâle
    )
    val index = (name.hashCode().absoluteValue % colors.size)
    return colors[index]
}
