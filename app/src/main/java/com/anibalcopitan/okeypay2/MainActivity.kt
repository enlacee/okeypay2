package com.anibalcopitan.okeypay2

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OkeyPay2Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }

                // callback code
                val openDialog = remember { mutableStateOf(true) }
                if (openDialog.value) {
                    AlertDialog(
                        onDismissRequest = { openDialog.value = false },
                        title = { Text("Alerta") },
                        text = { Text("¡Esto es una alerta simple!") },
                        confirmButton = {
                            Button(
                                onClick = { openDialog.value = false },
                                content = { Text("Aceptar") }
                            )
                        }
                    )
                }
                //
                // Llamado para solicitar permisos
                requestNotificationPolicyAccess(this@MainActivity)
                //



            }
        }
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
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    OkeyPay2Theme {
        Greeting("Android")
    }
}