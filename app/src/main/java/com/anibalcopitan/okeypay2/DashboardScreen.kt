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
import com.google.android.material.textfield.TextInputLayout.LengthCounter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DashboardScreenScreenPreview() {
    OkeyPay2Theme {
        DashboardScreen(0)
    }
}

@Composable
fun DashboardScreen(counter: Int) {
    val context = LocalContext.current
    Column(modifier = Modifier.padding(16.dp)) {
        HeaderText()
        Spacer(modifier = Modifier.height(16.dp))
        inputHeader(counter)
        Spacer(modifier = Modifier.height(16.dp))
        // Save PhoneNumber
        val sharedPreferencesManager = SharedPreferencesManager(context)
        PhoneNumberRegistrationForm(sharedPreferencesManager)

        Spacer(modifier = Modifier.height(16.dp))
        inputSpreadSheet()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun inputHeader(counter: Int) {
    val context = LocalContext.current
    var sharedPreferencesManager = SharedPreferencesManager(context)

    Text(
        text = "Codigo: ${sharedPreferencesManager.getString(SharedPreferencesManager.KEY_ID, "")}",
        fontSize = 16.sp
    )
    Text(
        text = "Correo: ${sharedPreferencesManager.getString(SharedPreferencesManager.KEY_USERNAME, "")}",
        fontSize = 16.sp
    )

    Text(
        text = "Transacciones:" + counter.toString(),
        fontSize = 16.sp
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun inputSpreadSheet() {
    val context = LocalContext.current
    var sharedPreferencesManager = SharedPreferencesManager(context)
    var subscriptionPlan = sharedPreferencesManager.getString(SharedPreferencesManager.KEY_SUBSCRIPTION_PLAN, "")
    val gooleSheetUrl = remember {
        mutableStateOf(TextFieldValue(
            sharedPreferencesManager.getString(SharedPreferencesManager.KEY_GOOGLE_SHEET_URL, "")
        ))
    }

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
            text = "Opcional: contactos a notificar mediante SMS",
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
