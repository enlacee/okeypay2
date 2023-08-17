package com.anibalcopitan.okeypay2

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.anibalcopitan.okeypay2.ui.theme.OkeyPay2Theme

import android.content.ComponentName
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import java.net.URLEncoder

class MainActivity : ComponentActivity() {
    companion object {
        const val API_BASE_URL: String = "https://script.google.com/macros/s/AKfycby8v71PHzEXLlIQnG36JSW_FQgVjGo_tM3GYN6cUx_V9KsnXz1DtT2vB3kOtot4nwA-/exec"
        const val MY_CHANNEL_ID: String = "myChannel"
        const val MY_NOTIFICATION_ID: Int = 1
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OkeyPay2Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomeView("Android")
                }

                // Llamado para solicitar permisos
                requestNotificationPolicyAccess(this@MainActivity)
                //

                // LISTENER NOTIFICATION
                if (!isNotificationListenerEnabled(this@MainActivity)) {
                    requestNotificationAccess()
                } else {
                    startNotificationListenerService()
                }
            }
        }
    }
    
    private fun requestNotificationAccess() {
        val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
        startActivity(intent)
    }
    private fun isNotificationListenerEnabled(context: Context): Boolean {
        val componentName = ComponentName(context, TheNotificationListener::class.java)
        val flat = Settings.Secure.getString(contentResolver, "enabled_notification_listeners")
        return flat != null && flat.contains(componentName.flattenToString())
    }
    private fun startNotificationListenerService() {
        this.registerPushListener()
    }
    private fun registerPushListener() {
        val intentFilter = IntentFilter()
        intentFilter.addAction("com.anibalcopitan.okeypay2")
        registerReceiver(broadcastReceiver, intentFilter)
    }
    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == "com.anibalcopitan.okeypay2") {
                val message = intent.getStringExtra("message")
                if (
                    !message.isNullOrEmpty()
                ) {
                    var URLAPI = MainActivity.API_BASE_URL
                    var mensajeEncoded = URLEncoder.encode(message, "UTF-8")
                    var url = URLAPI + "?op=listenersms&mensaje=" + mensajeEncoded

                    val getResquest = StringRequest(
                        Request.Method.GET,
                        url,
                        { response ->
                            createSimpleNotification("[insert][ok] " + message.toString())
                            // saveErrorToServer("[insert][ok] " + message.toString())
                        },
                        { error ->
                            createSimpleNotification("[insert][error] " + error.toString())
                            // saveErrorToServer(error.toString()) // debuging
                        }
                    )
                    Volley.newRequestQueue(this@MainActivity).add(getResquest)
                }
            }
        }
    }

    fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                MainActivity.MY_CHANNEL_ID,
                "MySuperChannel",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "SUSCRIBETE"
            }

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel(channel)
        }
    }

    fun createSimpleNotification(message: String="") {
        this.createChannel()

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val flag = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, flag)

        var builder = NotificationCompat.Builder(this, MainActivity.MY_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_menu_add)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(message)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        // 01 Uso normal notificacion
        val notificationManager = NotificationManagerCompat.from(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        notificationManager.notify(MainActivity.MY_NOTIFICATION_ID, builder.build())
    }
}


// Llamado desde tu Activity o Fragment para solicitar permisos
fun requestNotificationPolicyAccess(context: Context) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Comprobar si ya se tienen los permisos para cambiar la configuración del modo "No molestar"
        if (!notificationManager.isNotificationPolicyAccessGranted) {
            // Abrir la pantalla de ajustes para permitir el acceso a la configuración del modo "No molestar"
            val intent = Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
            context.startActivity(intent)
        }
}

@Composable
fun HomeView(name: String, modifier: Modifier = Modifier) {
//    Text(
//        text = "Hello $name!",
//        modifier = modifier
//    )
    LoginScreen(LocalContext.current)
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeViewPreview() {
    OkeyPay2Theme {
        LoginScreen(LocalContext.current)
    }
}