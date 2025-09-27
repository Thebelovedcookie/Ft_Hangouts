package com.example.myapplication

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.telephony.SmsManager
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat

@Composable
fun ChatScreen(convId: Int, contact: Contact, dbHelper: DataBaseHandler, onBack: () -> Unit,
               onEditContact: (Contact?, String) -> Unit)
{
    val messages = remember { mutableStateListOf<Message>() }
    var input by remember { mutableStateOf("") }

    LaunchedEffect(convId) {
        messages.clear()
        messages.addAll(dbHelper.getConversation(convId))
    }

    LaunchedEffect(convId) {
        while (true) {
            messages.clear()
            messages.addAll(dbHelper.getConversation(convId))
            kotlinx.coroutines.delay(2000)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().statusBarsPadding(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = onBack,
                modifier = Modifier.size(40.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.back),
                    contentDescription = "Return",
                    modifier = Modifier.size(32.dp),
                )
            }
            Text(
                text = contact.name.ifEmpty { contact.phoneNumber },
                color = Color.White,
                fontSize = 20.sp,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .clickable {
                        if (contact.id != 0 && dbHelper.contactExists(contact.id)) {
                            onEditContact(contact, contact.phoneNumber)
                        } else {
                            onEditContact(null, contact.phoneNumber)
                        }
                    }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Messages
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(messages) { msg ->
                var showTimestamp by remember { mutableStateOf(false) }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalAlignment = if (msg.isSent) Alignment.End else Alignment.Start
                ) {
                    Row(
                        modifier = Modifier
                            .clickable { showTimestamp = !showTimestamp }
                            .background(
                                if (msg.isSent) Color(0xC81E3A67) else Color.DarkGray,
                                shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
                            )
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = msg.message,
                            color = Color.White,
                            fontSize = 16.sp
                        )
                    }
                    if (showTimestamp) {
                        Text(
                            text = formatTimestamp(msg.timestamp),
                            color = Color.LightGray,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .offset(y = -25.dp)
        ) {
            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                modifier = Modifier.weight(1f),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedTextColor = Color.White
                )
            )
            val context = LocalContext.current
            Button(onClick = {
                if (input.isNotBlank()) {
                    val newMessage = Message(
                        id = 0,
                        convId = convId,
                        message = input,
                        isSent = true,
                        timestamp = System.currentTimeMillis().toString()
                    )

                    messages.add(newMessage)

                    val values = ContentValues().apply {
                        put(COL_CONV_ID, convId)
                        put(COL_MESSAGE, input)
                        put(COL_IS_SENT, true)
                        put(COL_TIMESTAMP, System.currentTimeMillis())
                    }
                    dbHelper.writableDatabase.insert(TABLE_MESSAGE, null, values)

                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS)
                        == PackageManager.PERMISSION_GRANTED) {

                        val smsManager = SmsManager.getDefault()
                        smsManager.sendTextMessage(contact.phoneNumber, null, input, null, null)
                        Toast.makeText(context, "Send", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Permission SEND_SMS missing", Toast.LENGTH_SHORT).show()
                    }

                    input = ""
                }
            }) {
                Text(stringResource(R.string.send_message))
            }
        }
    }
}

