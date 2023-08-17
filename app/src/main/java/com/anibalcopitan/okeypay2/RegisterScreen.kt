package com.anibalcopitan.okeypay2

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.anibalcopitan.okeypay2.ui.theme.Shapes
import com.anibalcopitan.okeypay2.ui.theme.OkeyPay2Theme


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegisterScreenScreenPreview() {
    OkeyPay2Theme {
        RegisterScreen(LocalContext.current)
    }
}

@Composable
fun RegisterScreen(mContext: Context) {
    Column(modifier = Modifier.padding(16.dp)) {
        HeaderText()
        Spacer(modifier = Modifier.height(18.dp))
        UsernameTextField()
        Spacer(modifier = Modifier.height(4.dp))
        EmailTextField()
        Spacer(modifier = Modifier.height(4.dp))
        PhoneNumberTextField()
        Spacer(modifier = Modifier.height(4.dp))
        PasswordTextField()
        Spacer(modifier = Modifier.height(24.dp))
        ButtonRegister(onClick = {
//            mContext.startActivity(Intent(mContext, RegisterActivity::class.java))
        })
    }
}

@Composable
private fun HeaderText() {
//    Text(text = "Bienvenid@,", fontWeight = FontWeight.Bold, fontSize = 32.sp)
//    Spacer(modifier = Modifier.height(2.dp))
    Text(text = "Regístrese para crear una cuenta", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.LightGray)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UsernameTextField() {
    var username by remember { mutableStateOf("") }

    OutlinedTextField(
        value = username,
        onValueChange = { username = it },
        label = { Text(text = "Nombres") },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PhoneNumberTextField() {
    var phoneNumber by remember { mutableStateOf("") }

    OutlinedTextField(
        value = phoneNumber,
        onValueChange = { phoneNumber = it },
        label = { Text(text = "Celular") },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EmailTextField() {
    var email by remember { mutableStateOf("") }

    OutlinedTextField(
        value = email,
        onValueChange = { email = it },
        label = { Text(text = "Correo") },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PasswordTextField() {
    var password by remember { mutableStateOf("") }

    OutlinedTextField(
        value = password,
        onValueChange = { password = it },
        label = { Text(text = "Crear Contraseña") },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
    )
}

@Composable
private fun ButtonRegister(onClick: () -> Unit) {
    Button(
//        onClick = { /*TODO*/ },
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(vertical = 16.dp, horizontal = 1.dp),
        shape = Shapes.large
    ) {
        Text("REGISTRAR")
    }
}
