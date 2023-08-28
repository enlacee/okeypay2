package com.anibalcopitan.okeypay2

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.opengl.Visibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.anibalcopitan.okeypay2.data.phonenumberregistration.PhoneNumberRegistrationForm
import com.anibalcopitan.okeypay2.data.phonenumberregistration.SharedPreferencesManager
import com.anibalcopitan.okeypay2.ui.theme.OkeyPay2Theme
import com.anibalcopitan.okeypay2.ui.theme.Shapes
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DashboardScreenScreenPreview() {
    OkeyPay2Theme {
        DashboardScreen(LocalContext.current)
    }
}

@Composable
fun DashboardScreen(context: Context) {
    Column(modifier = Modifier.padding(16.dp)) {
        HeaderText()
        Spacer(modifier = Modifier.height(18.dp))
//        SubscriptionInfoRow()
        Spacer(modifier = Modifier.height(8.dp))
        UrlInputRow()
        Spacer(modifier = Modifier.height(18.dp))

        // Save PhoneNumber
        val sharedPreferencesManager = SharedPreferencesManager(context)
        PhoneNumberRegistrationForm(sharedPreferencesManager)
    }
}

@Composable
fun SubscriptionInfoRow() {
    val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val startDate = dateFormatter.parse("2023-08-15")?.time ?: System.currentTimeMillis()
    val endDate = dateFormatter.parse("2023-08-25")?.time ?: startDate + (10 * 24 * 60 * 60 * 1000) // Fecha de fin: 10 días después del inicio
    var daysRemaining by remember { mutableIntStateOf(3) }

    // Calculate days remaining based on current date and end date
    LaunchedEffect(endDate) {
        val currentTime = System.currentTimeMillis()
        val days = ((endDate - currentTime) / (24 * 60 * 60 * 1000)).toInt()
        daysRemaining = if (days >= 0) days else 0
    }

    Column {
        Row {
            Text(
                text = "Días restantes de suscripción: ",
                fontSize = 18.sp,

            )
            Text(
                text = "$daysRemaining días",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }

        Text(
            text = "Fecha de inicio: ${SimpleDateFormat("dd/MM/yyyy").format(Date(startDate))}",
            fontSize = 16.sp
        )
        Text(
            text = "Fecha de fin: ${SimpleDateFormat("dd/MM/yyyy").format(Date(endDate))}",
            fontSize = 16.sp
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UrlInputRow() {
    val context = LocalContext.current
    var sharedPreferencesManager = SharedPreferencesManager(context)
    var subscriptionPlan = sharedPreferencesManager.getString(SharedPreferencesManager.KEY_SUBSCRIPTION_PLAN, "")
    val gooleSheetUrl = remember {
        mutableStateOf(TextFieldValue(
            sharedPreferencesManager.getString(SharedPreferencesManager.KEY_GOOGLE_SHEET_URL, "")
        ))
    }
//    val url = remember {
//        mutableStateOf(TextFieldValue("https://www.ejemplo.com"))
//    }

    Text(
        text = "Codigo: ${sharedPreferencesManager.getString(SharedPreferencesManager.KEY_ID, "")}",
        fontSize = 16.sp
    )
    Text(
        text = "Correo: ${sharedPreferencesManager.getString(SharedPreferencesManager.KEY_USERNAME, "")}",
        fontSize = 16.sp
    )
    Spacer(modifier = Modifier.height(6.dp))
    // on below line adding a spacer.
    Spacer(modifier = Modifier.height(2.dp))

    // on below line adding a button to open URL
    if (
        subscriptionPlan == "1" &&
        gooleSheetUrl.toString().isNotEmpty()
    ) {
        Button(
            onClick = {
                val urlIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(gooleSheetUrl.value.text)
                )
                context.startActivity(urlIntent)
            },
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 16.dp, horizontal = 1.dp),
            shape = Shapes.large,
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary)
        ) {
            Text(
                text = "Abrir Hoja de Calculo",
                fontSize = 15.sp
            )
        }
    }

}

// on below line we are creating
// contact picker function.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactNumbersRow() {
    var contactNumber1 by remember { mutableStateOf("") }
    var contactNumber2 by remember { mutableStateOf("") }

    fun isValidNumber(number: String): Boolean {
        return number.length > 9 && number.length <=12
    }

    Column {
        Text(
            text = "Contactos a notificar por SMS:",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        OutlinedTextField(
            value = contactNumber1,
            onValueChange = { contactNumber1 = it },
            label = { Text("Número 1") },
//            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            isError = !isValidNumber(contactNumber1),
            modifier = Modifier.fillMaxWidth(),
        )

        OutlinedTextField(
            value = contactNumber2,
            onValueChange = { contactNumber2 = it },
            label = { Text("Número 2") },
//            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            isError = !isValidNumber(contactNumber2),
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { /* Lógica para realizar la prueba de pago */ },
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 16.dp, horizontal = 1.dp),
            shape = Shapes.large
        ) {
            Text(text = "GUARDAR")
        }
    }
}

@Composable
private fun HeaderText() {
    Text(text = stringResource(id = R.string.app_name), fontWeight = FontWeight.Bold, fontSize = 32.sp)
    Spacer(modifier = Modifier.height(2.dp))
}
