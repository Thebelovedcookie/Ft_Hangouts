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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import com.example.myapplication.DataBaseHandler.ConversationPreview
import kotlinx.coroutines.delay



@Composable
fun ConversationListScreen(
    context: Context,
    onOpenConversation: (Int, Contact) -> Unit,
    onNewConversation: () -> Unit
) {
    val dbHandler = remember { DataBaseHandler(context) }
    val conversations = remember { mutableStateOf<List<ConversationPreview>>(emptyList()) }

    LaunchedEffect(Unit) {
        while (true) {
            conversations.value = dbHandler.getConversationPreviews()
            delay(2000)
        }
    }

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
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Messages",
                        fontSize = 28.sp,
                        color = Color.White
                    )
                    Button(
                        onClick = { onNewConversation() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF7C7091), // violet/gris comme dans tes autres boutons
                            contentColor = Color.White
                        )
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.add), // icône "nouveau message"
                            contentDescription = "Nouvelle conversation",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
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
                    items(conversations.value) { conv ->
                        ConversationListItem(conv, onOpenConversation)
                        Divider(
                            color = Color.Black.copy(alpha = 0.2f),
                            thickness = 1.dp,
                            modifier = Modifier.padding(start = 60.dp, end = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ConversationListItem(
    conv: ConversationPreview,
    onOpenConversation: (Int, Contact) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onOpenConversation(conv.convId, conv.contact) }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AvatarBox(conv.contact)
        Spacer(modifier = Modifier.width(12.dp))
        ConversationText(conv, Modifier.weight(1f))
        ConversationTimestamp(conv.timestamp)
    }
}

@Composable
fun AvatarBox(contact: Contact) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(getColorForName(contact.name)),
        contentAlignment = Alignment.Center
    ) {
        val displayInitial = if (contact.name.isNotEmpty()) contact.name.first().toString() else "?"
        Text(
            text = displayInitial,
            color = Color.White,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ConversationText(conv: ConversationPreview, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        val displayName = if (conv.contact.name.isNotEmpty()) conv.contact.name else conv.contact.phoneNumber
        Text(
            text = displayName,
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        if (conv.lastMessage.isNotEmpty())
        Text(
            text = conv.lastMessage,
            color = Color.Gray,
            fontSize = 14.sp,
            maxLines = 1
        )
        else
            Text("")
    }
}

@Composable
fun ConversationTimestamp(timestamp: String?) {
    if (timestamp != null) {
        Text(
            text = formatTimestamp(timestamp),
            color = Color.Gray,
            fontSize = 12.sp
        )
    }
}

fun formatTimestamp(timestamp: String): String {
    val millis = timestamp.toLong()
    val messageDate = Date(millis)
    val today = Calendar.getInstance()

    val calMessage = Calendar.getInstance().apply {
        time = messageDate
    }

    val isSameDay = today.get(Calendar.YEAR) == calMessage.get(Calendar.YEAR) &&
            today.get(Calendar.DAY_OF_YEAR) == calMessage.get(Calendar.DAY_OF_YEAR)

    return if (isSameDay) {
        SimpleDateFormat("HH:mm", Locale.getDefault()).format(messageDate)
    } else {
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(messageDate)
    }
}
