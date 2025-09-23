package com.example.myapplication

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun BottomBar(
    currentScreen: String,
    onScreenSelected: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black)
            .padding(vertical = 8.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { onScreenSelected("keyboard") },
            modifier = Modifier.size(48.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.dialpad),
                contentDescription = "Keyboard",
                modifier = Modifier.size(28.dp),
                colorFilter = ColorFilter.tint(
                    if (currentScreen == "keyboard") Color(0xFFB0BEC5)
                    else Color.LightGray
                )
            )
        }

        IconButton(
            onClick = { onScreenSelected("list") },
            modifier = Modifier.size(48.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.contact2),
                contentDescription = "Contact",
                modifier = Modifier.size(28.dp),
                colorFilter = ColorFilter.tint(
                    if (currentScreen == "list") Color(0xFFB0BEC5)
                    else Color.LightGray
                )
            )
        }

        IconButton(
            onClick = { onScreenSelected("conversations") },
            modifier = Modifier.size(48.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.chat),
                contentDescription = "Messages",
                modifier = Modifier.size(28.dp),
                colorFilter = ColorFilter.tint(
                    if (currentScreen == "conversations") Color(0xFFB0BEC5)
                    else Color.LightGray
                )
            )
        }

        IconButton(
            onClick = { onScreenSelected("settings") },
            modifier = Modifier.size(48.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.settings),
                contentDescription = "Messages",
                modifier = Modifier.size(28.dp),
                colorFilter = ColorFilter.tint(
                    if (currentScreen == "settings") Color(0xFFB0BEC5)
                    else Color.LightGray
                )
            )
        }
    }
}
