package com.example.myapplication

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.myapplication.ui.theme.MyApplicationTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat

class MainActivity : ComponentActivity() {

    private val requestSmsPermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val receiveGranted = permissions[Manifest.permission.RECEIVE_SMS] ?: false
            val sendGranted = permissions[Manifest.permission.SEND_SMS] ?: false

            if (receiveGranted && sendGranted) {
                Toast.makeText(this, "Allowed.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Refused.", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkAndRequestPermissions()

        setContent {
            MyApplicationTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MyApp(context = this@MainActivity, activity = this)
                }
            }
        }
    }

    private fun checkAndRequestPermissions() {
        val neededPermissions = mutableListOf<String>()

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            neededPermissions.add(Manifest.permission.RECEIVE_SMS)
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            neededPermissions.add(Manifest.permission.SEND_SMS)
        }

        if (neededPermissions.isNotEmpty()) {
            requestSmsPermissions.launch(neededPermissions.toTypedArray())
        }
    }


    @Composable
    fun MyApp(context: Context, activity: Activity) {
        var currentScreen by remember { mutableStateOf("conversations") }
        var contactToEdit by remember { mutableStateOf<Contact?>(null) }
        var chatContact by remember { mutableStateOf<Contact?>(null) }
        var currentConvId by remember { mutableStateOf<Int?>(null) }
        var initialPhoneForForm by remember { mutableStateOf("") }

        var headerColor by remember { mutableStateOf(Color.Black) }
        var appLanguage by remember { mutableStateOf("en") }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(headerColor)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Box(modifier = Modifier.weight(1f)) {
                    when (currentScreen) {
                        "keyboard" -> KeyboardScreen(context)
                        "form" -> ContactForm(
                            context,
                            contactToEdit,
                            initialPhone = initialPhoneForForm,
                            onNavigateToList = { currentScreen = "list" })

                        "list" -> ContactListScreen(
                            context,
                            onEditContact = { contact ->
                                contactToEdit = contact
                                currentScreen = "form"
                            })

                        "conversations" -> ConversationListScreen(
                            context,
                            onOpenConversation = { convId, contact ->
                                chatContact = contact
                                currentConvId = convId
                                currentScreen = "chat"
                            },
                            onNewConversation = {
                                currentScreen = "newMessage"
                            }

                        )

                        "chat" -> if (chatContact != null && currentConvId != null) {
                            ChatScreen(
                                convId = currentConvId!!,
                                contact = chatContact!!,
                                dbHelper = DataBaseHandler(context),
                                onBack = { currentScreen = "conversations" },
                                onEditContact = { contact, phone ->
                                    contactToEdit = contact
                                    initialPhoneForForm = phone
                                    currentScreen = "form"
                                }
                            )
                        }

                        "newMessage" -> NewMessageScreen(
                            context,
                            onConversationCreated = { convId, contact ->
                                currentConvId = convId
                                chatContact = contact
                                currentScreen = "chat"
                            }
                        )
                        "settings" -> Settings(
                            currentColor = headerColor,
                            onColorSelected = { selectedColor ->
                                headerColor = selectedColor
                            },
                            currentLanguage = appLanguage,
                            onLanguageSelected = { selectedLang ->
                                appLanguage = selectedLang
                            },
                            activity
                        )
                    }
                }

                // navbar
                BottomBar(currentScreen) { selected ->
                    currentScreen = selected
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

