package com.example.myapplication

import android.content.Context
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource

@Composable
fun NewMessageScreen(
    context: Context,
    onConversationCreated: (Int, Contact?) -> Unit
) {
    val dbHandler = remember { DataBaseHandler(context) }
    val contacts = remember { mutableStateOf(dbHandler.readData()) }
    var phoneInput by remember { mutableStateOf("") }

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE


    var maxHeaderHeight = 700f
    var minHeaderHeight = 230f
    if (isLandscape) {
        maxHeaderHeight = 250f
        minHeaderHeight = 120f
    }
    var headerHeightPx by remember { mutableStateOf(maxHeaderHeight) }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                val newHeight = (headerHeightPx + delta).coerceIn(minHeaderHeight, maxHeaderHeight)
                val consumed = newHeight - headerHeightPx
                headerHeightPx = newHeight
                return Offset(0f, consumed)
            }
        }
    }

    val animatedHeaderHeight by animateDpAsState(
        targetValue = with(LocalDensity.current) { headerHeightPx.toDp() }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection)
    ) {
        // Header
        if (headerHeightPx > 0f) {
            Box(
                modifier = Modifier
                    .height(animatedHeaderHeight)
                    .fillMaxWidth()
            ) {
                Text(
                    "New Conversations",
                    fontSize = 28.sp,
                    color = Color.White,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(horizontal = 16.dp)
                )
            }
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(Color(0xFF121212))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.99f)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0xFF121212))
                    .padding(16.dp)
            ) {
                Column {
                    OutlinedTextField(
                        value = phoneInput,
                        onValueChange = { input ->
                            if (input.all { it.isDigit() } && input.length <= 10) {
                                phoneInput = input
                            }
                        },
                        label = { Text(stringResource(R.string.phone_label)) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedIndicatorColor = Color(0xFF7C7091),
                            unfocusedIndicatorColor = Color(0xFF7C7091),

                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    LazyColumn(
                        modifier = Modifier.weight(1f)
                    ) {
                        val sortedContacts = contacts.value.sortedBy { it.name.lowercase() }
                        itemsIndexed(sortedContacts) { index, contact ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { phoneInput = contact.phoneNumber }
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(contact.name, color = Color.White)
                                Text(contact.phoneNumber, color = Color.Gray)
                            }

                            if (index < sortedContacts.lastIndex) {
                                Divider(
                                    color = Color.LightGray.copy(alpha = 0.3f),
                                    thickness = 1.dp,
                                    modifier = Modifier.padding(horizontal = 8.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            val phone = phoneInput.takeIf { it.isNotBlank() } ?: return@Button

                            val contactId = dbHandler.getContactIdByPhoneNumber(phone)

                            var contact: Contact? = null
                            if (contactId == null) {
                                contact = Contact(
                                    id = 0,
                                    name = phone,
                                    age = 0,
                                    phoneNumber = phone,
                                    address = "",
                                    mail = ""
                                )
                            } else {
                                contact = dbHandler.getContactById(contactId)
                            }

                            var convId = dbHandler.getIdOfConversation(phone)
                            if (convId == null)
                                convId = dbHandler.insertConversation(phone, contactId)

                            onConversationCreated(convId, contact)
                        },
                        modifier = Modifier.align(Alignment.End),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF7C7091),
                            contentColor = Color.White
                        )
                    ) {
                        Text(stringResource(R.string.new_message))
                    }
                }
            }
        }
    }
}

