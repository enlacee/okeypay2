package com.anibalcopitan.okeypay2

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsManager
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.anibalcopitan.okeypay2.data.phonenumberregistration.SharedPreferencesManager
import java.net.URLEncoder

class MyReceiverBroadcast : BroadcastReceiver() {

    /*
    * Ref: https://developer.android.com/guide/components/broadcasts#kotlin
    * */
    companion object {
//        const val ID_ACTION: String = "com.anibalcopitan.okeypay2.broadcast.SEND_DATA_NOTIFICATION"
//        const val ID_ACTION_CLEAR_DATA: String = "com.anibalcopitan.okeypay2.broadcast.CLEAR_DATA"
        const val ID_ACTION: String = "com.anibalcopitan.okeypay2.broadcast.SEND_DATA_NOTIFICATION"
        const val KEY_NAME_MESSAGE: String = "message"
    }

    private lateinit var notificationUtil: NotificationUtil

    override fun onReceive(context: Context, intent: Intent) {
        notificationUtil = NotificationUtil(context)

        if (intent.action == MyReceiverBroadcast.ID_ACTION) {
            val message = intent.getStringExtra("message")
            if ( !message.isNullOrEmpty() ) {
                // task 01: send sms
                sendMessageToAllNumbersAdded(context, message.toString())

                // task 02: create notification
                var URLAPI = MainActivity.API_BASE_URL
                var mensajeEncoded = URLEncoder.encode(message, "UTF-8")
                var url = URLAPI + "?op=listenersms&mensaje=" + mensajeEncoded

                val getResquest = StringRequest(
                    Request.Method.GET,
                    url,
                    { response ->
                        notificationUtil.createSimpleNotification("[insert][ok] " + message.toString())
                        // createSimpleNotification("[insert][ok] " + message.toString())
                        // saveErrorToServer("[insert][ok] " + message.toString())
                    },
                    { error ->
                        notificationUtil.createSimpleNotification("[insert][error] " + error.toString())
                        // saveErrorToServer(error.toString()) // debuging
                    }
                )
                Volley.newRequestQueue(context).add(getResquest)
            }
        }
    }

    /*
    * SEND MESSAGE TO ALL CONTACTS
    * */
    private fun sendMessageToAllNumbersAdded(context: Context, message: String) {

        val sharedPreferencesManager = SharedPreferencesManager(context)
        val phonesNumbers: Array<String> = sharedPreferencesManager.getPhoneNumbers();

        /*
        * Validation if data number is correct
        * */
        for (phoneNumber in phonesNumbers) {
            if (phoneNumber.length >= 9 && phoneNumber.matches(Regex("""\+?\d{9,}"""))) {
                sendMessage(phoneNumber, message)
            } else {
                Log.e("error.phone.number","Invalid phone number: $phoneNumber")
            }
        }
    }

    /*
    *
    * Simple function to send message with `android.permission.SEND_SMS` GRATTED
    *
    * */
    private fun sendMessage(phoneNumber: String, message: String) {
        try {
            // on below line initializing sms manager.
            val smsManager: SmsManager = SmsManager.getDefault()
            // on below line sending sms
            smsManager.sendTextMessage(phoneNumber, null, message, null, null)
        } catch (e: Exception) {
            Log.e("error.catch", e.message.toString())
        }

    }

}