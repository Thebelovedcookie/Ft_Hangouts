package com.example.myapplication

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun KeyboardScreen(context: Context) {
    val scrollState = rememberScrollState()
    var input by remember { mutableStateOf("") }
    var showForm by remember { mutableStateOf(false) }
    var phoneToPrefill by remember { mutableStateOf("") }

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    if (showForm) {
        ContactForm(
            context,
            initialPhone = phoneToPrefill,
            onNavigateToList = {
                showForm = false
                input = ""
            }
        )
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // --- HEADER ---
            Box(
                modifier = Modifier
                    .height(if (isLandscape) 50.dp else 230.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.keyboard),
                    fontSize = 28.sp,
                    color = Color.White
                )
            }

            // --- CONTENT ---
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(Color.Black),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.99f)
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color(0xFF121212))
                        .padding(if (isLandscape) 10.dp else 16.dp)
                ) {
                    if (isLandscape) {
                        Landscape(
                            input = input,
                            onInputChange = { input = it },
                            onAddContact = {
                                phoneToPrefill = input
                                showForm = true
                            },
                            scrollState = scrollState
                        )
                    } else {
                        Portrait(
                            input = input,
                            onInputChange = { input = it },
                            onAddContact = {
                                phoneToPrefill = input
                                showForm = true
                            },
                            scrollState = scrollState
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Portrait(
    input: String,
    onInputChange: (String) -> Unit,
    onAddContact: () -> Unit,
    scrollState: ScrollState
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // --- Zone d'affichage du numéro ---
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(
                text = input,
                fontSize = 38.sp,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.Center)
                    .verticalScroll(scrollState),
                textAlign = TextAlign.Center
            )

            if (input.isNotEmpty()) {
                IconButton(
                    onClick = { onInputChange("") },
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Effacer",
                        modifier = Modifier.size(48.dp),
                        tint = Color.White
                    )
                }
            }
        }

        // --- Boutons numériques ---
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Row {
                NumberButton(false,1) { if (input.length < 10) onInputChange(input + it.toString()) }
                NumberButton(false,2) { if (input.length < 10) onInputChange(input + it.toString()) }
                NumberButton(false,3) { if (input.length < 10) onInputChange(input + it.toString()) }
            }
            Row {
                NumberButton(false,4) { if (input.length < 10) onInputChange(input + it.toString()) }
                NumberButton(false,5) { if (input.length < 10) onInputChange(input + it.toString()) }
                NumberButton(false,6) { if (input.length < 10) onInputChange(input + it.toString()) }
            }
            Row {
                NumberButton(false,7) { if (input.length < 10) onInputChange(input + it.toString()) }
                NumberButton(false,8) { if (input.length < 10) onInputChange(input + it.toString()) }
                NumberButton(false,9) { if (input.length < 10) onInputChange(input + it.toString()) }
            }
            Row {
                NumberButton(false,0) { if (input.length < 10) onInputChange(input + it.toString()) }
            }
        }

        // --- Bouton ajouter contact ---
        Button(
            onClick = onAddContact,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF7C7091),
                contentColor = Color.White
            ),
            modifier = Modifier.align(Alignment.End)
        ) {
            Image(
                painter = painterResource(id = R.drawable.add),
                contentDescription = "Add Contact",
                modifier = Modifier.size(32.dp),
            )
        }
    }
}


@Composable
fun Landscape(
    input: String,
    onInputChange: (String) -> Unit,
    onAddContact: () -> Unit,
    scrollState: ScrollState
) {
    // --- LANDSCAPE: Row layout ---
    Row(
        modifier = Modifier.fillMaxSize(),
//        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Partie affichage input
        Box(
            modifier = Modifier
                .weight(0.5f)
                .padding(8.dp)
        ) {
            Button(
            onClick = onAddContact,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF7C7091),
                contentColor = Color.White
            )
        ) {
            Image(
                painter = painterResource(id = R.drawable.add),
                contentDescription = "Add Contact",
                modifier = Modifier.size(32.dp),
            )
        }
            Text(
                text = input,
                fontSize = 38.sp,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.Center)
                    .verticalScroll(scrollState),
                textAlign = TextAlign.Center
            )

            if (input.isNotEmpty()) {
                IconButton(
                    onClick = { onInputChange("") },
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Effacer",
                        modifier = Modifier.size(48.dp),
                        tint = Color.White
                    )
                }
            }
        }

        // Partie boutons numériques
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .weight(0.5f)
                .fillMaxHeight()
//                .padding(8.dp)
        ) {
            Spacer(modifier = Modifier.height(1.dp))
            Row {
                NumberButton(true,1) { if (input.length < 10) onInputChange(input + it) }
                NumberButton(true,2) { if (input.length < 10) onInputChange(input + it) }
                NumberButton(true,3) { if (input.length < 10) onInputChange(input + it) }
            }
            Row {
                NumberButton(true,4) { if (input.length < 10) onInputChange(input + it) }
                NumberButton(true,5) { if (input.length < 10) onInputChange(input + it) }
                NumberButton(true,6) { if (input.length < 10) onInputChange(input + it) }
            }
            Row {
                NumberButton(true,7) { if (input.length < 10) onInputChange(input + it) }
                NumberButton(true,8) { if (input.length < 10) onInputChange(input + it) }
                NumberButton(true,9) { if (input.length < 10) onInputChange(input + it) }
            }
            Row {
                NumberButton(true,0) { if (input.length < 10) onInputChange(input + it) }
            }
        }
    }
}


@Composable
fun NumberButton(isLandscape: Boolean, number: Int, onClick: (digit: Char) -> Unit ) {
    OutlinedButton(
        onClick = {
            onClick(number.digitToChar())
        },
        modifier = Modifier
            .width(if (isLandscape) 80.dp else 120.dp)
            .padding(if (isLandscape) 7.dp else 15.dp),
        border = BorderStroke(1.dp, Color(0xFF7C7091))
    ) {
        Text(
            text = number.toString(),
            fontSize = if (isLandscape) 15.sp else 30.sp,
            color = Color.White
        )
    }
}
