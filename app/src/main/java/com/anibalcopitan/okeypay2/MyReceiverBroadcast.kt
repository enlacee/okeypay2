package com.anibalcopitan.okeypay2

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsManager
import android.util.Log
import com.anibalcopitan.okeypay2.data.phonenumberregistration.SharedPreferencesManager
import com.anibalcopitan.okeypay2.util.HttpUtil
import java.net.URLEncoder
import kotlin.properties.Delegates

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
    private lateinit var subscriptionPlan: String
    private lateinit var sharedPreferencesManager: SharedPreferencesManager
    private lateinit var urlGoogleSheet: String
    private lateinit var counter: String
    private var counterAsInt by Delegates.notNull<Int>()
    override fun onReceive(context: Context, intent: Intent) {
        notificationUtil = NotificationUtil(context)
        sharedPreferencesManager = SharedPreferencesManager(context)
        subscriptionPlan = sharedPreferencesManager.getString(SharedPreferencesManager.KEY_SUBSCRIPTION_PLAN, "")
        urlGoogleSheet = sharedPreferencesManager.getString(SharedPreferencesManager.KEY_GOOGLE_SHEET_URL, "")
        counter = sharedPreferencesManager.getString(SharedPreferencesManager.KEY_COUNTER, "")
        counterAsInt = if (!counter.isNullOrBlank()) counter.toInt() else 0

        if (intent.action == MyReceiverBroadcast.ID_ACTION) {
            val message = intent.getStringExtra("message")
            if ( !message.isNullOrEmpty() ) {
                // save counter
                counterAsInt += 1
                sharedPreferencesManager.saveString(SharedPreferencesManager.KEY_COUNTER, counterAsInt.toString())

                // task 01: send sms
                sendMessageToAllNumbersAdded(context, message.toString())

                // si no tienes subscription 1 no envia REQUEST
                if (subscriptionPlan == "1" && urlGoogleSheet.isNotEmpty()) {
                    // task 02: create notification
                    var URLAPI = MainActivity.API_OKEYPAY
                    var mensajeEncoded = URLEncoder.encode(message, "UTF-8")
                    var urlGoogleSheet = URLEncoder.encode(urlGoogleSheet, "UTF-8")
                    var url =
                        "$URLAPI?op=listenersms&mensaje=$mensajeEncoded&urlgooglesheet=$urlGoogleSheet"

                    Log.i("debug", "username= " + sharedPreferencesManager.getString(SharedPreferencesManager.KEY_USERNAME, ""))
                    Log.i("debug", "urlGoogleSheet= $urlGoogleSheet")
                    Log.i("debug", "message= $message")

                    //v3
                    /*
                    val client = OkHttpClient()
                    val request = Requestokhttp3.Builder()
                        .url(url) // URL de la API que deseas consultar
                        .build()
                    client.newCall(request).enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            Log.i("debug", "e=" + e.toString())
                            // Manejar el error en caso de fallo de la solicitud
                        }

                        override fun onResponse(call: Call, response: Response) {
                            if (response.isSuccessful) {
                                val responseData = response.body?.string()
                                Log.i("debug", "okok=" + responseData)
//                                runOnUiThread {
//                                    // Actualizar la interfaz de usuario con los datos recibidos
//                                    // responseData contiene la respuesta del servidor en formato String
//                                }
                            }
                        }
                    })
                    */
                    //v4
                    HttpUtil.sendGetRequest(url,
                        onResponse = { responseData ->
                            Log.i("debug", "okok=$responseData")
                            notificationUtil.createSimpleNotification("[insert][ok] " + message.toString())
                        },
                        onFailure = { e ->
                            Log.i("debug", "error=${e.toString()}")
                            notificationUtil.createSimpleNotification("[insert][error] " + e.toString())
                        }
                    )

                }
            }
        }
    }

    /*
    * SEND MESSAGE TO ALL CONTACTS
    * */
    private fun sendMessageToAllNumbersAdded(context: Context, message: String) {

        val sharedPreferencesManager = SharedPreferencesManager(context)
        var phonesNumbers: Array<String> = sharedPreferencesManager.getPhoneNumbers();

        /*
        * Validation if data number is correct
        * */
        for (originalPhoneNumber in phonesNumbers) {
            val phoneNumber = originalPhoneNumber.trim().replace(" ", "")
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