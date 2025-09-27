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
                    val sender = sms.originatingAddress ?: continue
                    val message = sms.messageBody

                    val contactId = dbHandler.getContactIdByPhoneNumber(sender)

                    var convId = dbHandler.getIdOfConversation(sender)
                    if (convId == null) {
                        convId = dbHandler.insertConversation(sender, contactId)
                    }

                    // Insert le message dans la base
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
