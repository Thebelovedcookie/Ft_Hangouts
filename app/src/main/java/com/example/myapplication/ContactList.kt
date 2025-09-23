package com.example.myapplication

import android.content.Context
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ContactListScreen(context: Context, onEditContact: (Contact) -> Unit) {
    val dbHandler = remember { DataBaseHandler(context) }
    val contacts = remember { mutableStateOf(dbHandler.readData()) }

    val lazyListState = rememberLazyListState()

    val maxHeaderHeight = 700f
    val minHeaderHeight = 230f
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
                    .fillMaxWidth(),
//                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Contacts",
                    fontSize = 28.sp,
                    color = Color.White
                )
            }
        }
        // List scrollable
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
                LazyColumn(state = lazyListState) {
                    items(contacts.value.sortedBy { it.name.lowercase() }) { contact ->
                        ListContact(
                            contact = contact,
                            dbHandler = dbHandler,
                            onEditContact = { selectedContact ->
                                onEditContact(selectedContact)
                            },
                            onContactsChanged = {
                                contacts.value = dbHandler.readData()
                            }
                        )


                        Divider(
                            color = Color.Black.copy(alpha = 0.3f), //test
                            thickness = 1.dp,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ListContact(
    contact: Contact,
    dbHandler: DataBaseHandler,
    onEditContact: (Contact) -> Unit,
    onContactsChanged: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ContactAvatar(contact.name, modifier = Modifier.padding(end = 8.dp))

        Text(
            "${contact.name} ",
            modifier = Modifier
                .weight(1f)
                .clickable { onEditContact(contact) },
            color = Color.White,
            fontSize = 20.sp
        )

        Button(
            onClick = {
                dbHandler.deleteContact(contact.id)
                onContactsChanged()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF7C7091),
                contentColor = Color.White,
                disabledContainerColor = Color.Gray,
                disabledContentColor = Color.LightGray
            )
        ) {
            Image(
                painter = painterResource(id = R.drawable.delete),
                contentDescription = "Delete",
                modifier = Modifier.size(20.dp),
            )
        }
    }
}

@Composable
fun ContactAvatar(name: String, modifier: Modifier = Modifier) {
    val bgColor = getColorForName(name)
    val initial = name.firstOrNull()?.uppercase() ?: "?"

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(bgColor)
    ){
        Text(
            text = initial,
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}





























