package com.anibalcopitan.okeypay2

import android.content.IntentFilter
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.anibalcopitan.okeypay2.data.phonenumberregistration.SharedPreferencesManager
import com.anibalcopitan.okeypay2.ui.theme.OkeyPay2Theme

class DashboardActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val n = 0 //sharedPreferencesManager.getCounter()
        setContent {
            OkeyPay2Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting(n)
                }
            }
        }
    }
}

@Composable
fun Greeting(counter: Int) {
    DashboardScreen(counter)
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    OkeyPay2Theme {
        Greeting(0)
    }
}