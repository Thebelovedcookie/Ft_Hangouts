package com.example.myapplication

import android.content.Context
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ContactForm(
    context: Context,
    contactToEdit: Contact? = null,
    initialPhone: String = "",
    onNavigateToList: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var mail by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(contactToEdit, initialPhone) {
        name = contactToEdit?.name ?: ""
        age = contactToEdit?.age?.toString() ?: ""
        phone = contactToEdit?.phoneNumber ?: initialPhone
        address = contactToEdit?.address ?: ""
        mail = contactToEdit?.mail ?: ""
    }

    val dbHandler = remember { DataBaseHandler(context) }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(230.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text =
                    if (contactToEdit == null) stringResource(id = R.string.new_contact)
                    else stringResource(id = R.string.edit_contact) ,
                fontSize = 28.sp,
                color = Color.White
            )
        }

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
                    .padding(36.dp)
            ) {
                Column {
                    val textFieldColors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        errorContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color(0xFF7C7091),
                        unfocusedIndicatorColor = Color.Black,
                        disabledIndicatorColor = Color.Black,
                        errorIndicatorColor = Color.Red,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        disabledTextColor = Color.Gray,
                        errorTextColor = Color.Red,
                    )

                    OutlinedTextField(value = name, onValueChange = { name = it.replaceFirstChar { it.uppercase() } }, label = { Text(stringResource(id = R.string.name_label)) }, modifier = Modifier.fillMaxWidth(), colors = textFieldColors)
                    OutlinedTextField(value = age, onValueChange = { if (it.all { ch -> ch.isDigit() }) age = it }, label = { Text("Age") }, modifier = Modifier.fillMaxWidth(), colors = textFieldColors)
                    OutlinedTextField(value = phone, onValueChange = { if (it.all { ch -> ch.isDigit() } && it.length <= 10) phone = it }, label = { Text(stringResource(id = R.string.phone_label)) }, modifier = Modifier.fillMaxWidth(), colors = textFieldColors)
                    OutlinedTextField(value = address, onValueChange = { address = it }, label = { stringResource(id = R.string.address_label) }, modifier = Modifier.fillMaxWidth(), colors = textFieldColors)
                    OutlinedTextField(value = mail, onValueChange = { mail = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth(), colors = textFieldColors)

                    errorMessage?.let {
                        Text(it, color = Color.Red, modifier = Modifier.padding(vertical = 8.dp))
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Boutons
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        val errorNameText = stringResource(R.string.error_name)
                        Button(
                            onClick = {
                                if (name.isBlank()) {
                                    errorMessage = errorNameText
                                    return@Button
                                }
                                val ageInt = age.toIntOrNull() ?: 0
                                val contact = Contact(contactToEdit?.id ?: 0, name, ageInt, phone, address, mail)
                                val savedContactId = if (contactToEdit == null) dbHandler.insertData(contact) else { dbHandler.updateData(contact); contact.id }
                                val convId = dbHandler.getIdOfConversation(phone)
                                if (convId != null) dbHandler.updateConversationContactId(convId, savedContactId)

                                name = ""; age = ""; phone = ""; address = ""; mail = ""
                                onNavigateToList()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7C7091), contentColor = Color.White),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(stringResource(id = R.string.save_button))
                        }

                        Spacer(modifier = Modifier.size(16.dp))

                        Button(
                            onClick = {
                                onNavigateToList()
                                name = ""; age = ""; phone = ""; address = ""; mail = ""
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7C7091), contentColor = Color.White)
                        ) {
                            Text(stringResource(id = R.string.cancel_button))
                        }
                    }
                }
            }
        }
    }
}

