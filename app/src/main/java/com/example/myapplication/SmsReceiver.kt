package com.example.myapplication

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.telephony.SmsMessage
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class SmsReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            val bundle = intent.extras
            val pdus = bundle?.get("pdus") as? Array<*>

            if (pdus != null && context != null) {
                val dbHandler = DataBaseHandler(context)

                for (pdu in pdus) {
                    val sms = SmsMessage.createFromPdu(pdu as ByteArray)
                    var sender = sms.originatingAddress ?: continue
                    val message = sms.messageBody

                    if (sender.startsWith("+33")) {
                        sender = sender.replaceFirst("+33", "0")
                    }

                    var contactId = dbHandler.getContactIdByPhoneNumber(sender)
                    if (contactId == null) {
                        contactId = dbHandler.insertContact(sender) //create a contact with number as name
                    }
                    var convId = dbHandler.getIdOfConversation(sender)
                    if (convId == null) {
                        //creer une conv
                        convId = dbHandler.insertConversation(sender, contactId)
                    }

                    // Insert le message dans la base message
                    dbHandler.insertMessage(convId, message, false, System.currentTimeMillis())

                    val localIntent = Intent("com.apayen.NEW_SMS")
                    LocalBroadcastManager.getInstance(context).sendBroadcast(localIntent)
                }
            }
        } else {
            Log.w("SmsReceiver", "Intent ignoré : ${intent?.action}")
        }
    }
}
