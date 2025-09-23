package com.example.myapplication

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun KeyboardScreen(context: Context) {
    val scrollState = rememberScrollState()
    var input by remember { mutableStateOf("") }
    var showForm by remember { mutableStateOf(false) }
    var phoneToPrefill by remember { mutableStateOf("") }

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
                    .height(230.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Clavier",
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
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {

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
                                    onClick = { input = "" },
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

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp)
                        ) {
                            Row {
                                NumberButton(1) { if (input.length < 10) input += it.toString() }
                                NumberButton(2) { if (input.length < 10) input += it.toString() }
                                NumberButton(3) { if (input.length < 10) input += it.toString() }
                            }
                            Row {
                                NumberButton(4) { if (input.length < 10) input += it.toString() }
                                NumberButton(5) { if (input.length < 10) input += it.toString() }
                                NumberButton(6) { if (input.length < 10) input += it.toString() }
                            }
                            Row {
                                NumberButton(7) { if (input.length < 10) input += it.toString() }
                                NumberButton(8) { if (input.length < 10) input += it.toString() }
                                NumberButton(9) { if (input.length < 10) input += it.toString() }
                            }
                            Row {
                                NumberButton(0) { if (input.length < 10) input += it.toString() }
                            }
                        }

                        Button(
                            onClick = {
                                phoneToPrefill = input
                                showForm = true
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF7C7091),
                                contentColor = Color.White
                            ),
                            modifier = Modifier
                                .align(Alignment.End)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.add),
                                contentDescription = "Add Contact",
                                modifier = Modifier.size(32.dp),
                            )
                        }
                    }
                }
            }
        }
    }
}



@Composable
fun NumberButton(number: Int, onClick: (digit: Char) -> Unit) {
    OutlinedButton(
        onClick = {
            onClick(number.digitToChar())
        },
        modifier = Modifier
            .width(120.dp)
            .padding(15.dp),
        border = BorderStroke(0.dp, Color.Black)
    ) {
        Text(
            text = number.toString(),
            fontSize = 30.sp,
            color = Color.White
        )
    }
}
