package com.anibalcopitan.okeypay2

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

import com.anibalcopitan.okeypay2.ui.theme.Shapes
import com.anibalcopitan.okeypay2.ui.theme.OkeyPay2Theme


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenScreenPreview() {
    OkeyPay2Theme {
        LoginScreen(LocalContext.current)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(mContext: Context) {
    val openDialog = remember { mutableStateOf(false) }
    var credentials by remember { mutableStateOf(Credentials()) }
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    Column(modifier = Modifier.padding(16.dp)) {
        HeaderText()
        Spacer(modifier = Modifier.height(28.dp))
        //PhoneNumberTextField()
        OutlinedTextField(
            value = credentials.login,
            onValueChange = { data -> credentials = credentials.copy(login = data) },
            label = { Text(text = "Correo") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
        )
        //
        Spacer(modifier = Modifier.height(4.dp))
        //PasswordTextField()
        OutlinedTextField(
            value = credentials.pwd,
            onValueChange = { data -> credentials = credentials.copy(pwd = data) },
            label = { Text(text = "Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
        )
        //
        Spacer(modifier = Modifier.height(24.dp))
        ButtonLogin(mContext) {
            if (!checkCredentials(credentials, context)) {
                credentials = Credentials()
            }
        }
        Spacer(modifier = Modifier.height(44.dp))
        ButtonToRegister { openDialog.value = true }
    }

    // Open Modal
    if (openDialog.value) {
        Dialog(onDismissRequest = { openDialog.value = false }) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                color = MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(size = 0.dp)
            ){
//                RegisterScreen(mContext)
                RegisterScreen(mContext, object : DialogCallback {
                    override fun closeDialog() {
                        openDialog.value = false
                    }
                })
            }
        }
    }
}


fun checkCredentials(creds: Credentials, context: Context): Boolean {
    if (creds.isNotEmpty() && creds.login == "pprios@pprios.com" && creds.pwd == "clave123") {
        context.startActivity(Intent(context, DashboardActivity::class.java))
        (context as Activity).finish()
        return true
    } else {
        Toast.makeText(context, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
        return false
    }
}

data class Credentials(
    var login: String = "pprios@pprios.com",
    var pwd: String = "clave123",
) {
    fun isNotEmpty(): Boolean {
        return login.isNotEmpty() && pwd.isNotEmpty()
    }
}

@Composable
private fun HeaderText() {
    Text(text = stringResource(id = R.string.app_name), fontWeight = FontWeight.Bold, fontSize = 32.sp)
    Spacer(modifier = Modifier.height(2.dp))
    Text(text = "Inicia sesión para continuar", fontWeight = FontWeight.Bold, fontSize = 26.sp, color = Color.LightGray)
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
private fun PasswordTextField() {
    var password by remember { mutableStateOf("") }

    OutlinedTextField(
        value = password,
        onValueChange = { password = it },
        label = { Text(text = "Contraseña") },
        modifier = Modifier.fillMaxWidth(),
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
    )
}

@Composable
private fun ButtonLogin(mContext : Context, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(vertical = 16.dp, horizontal = 1.dp),
        shape = Shapes.large
    ) {
        Text("INICIAR SESIÓN")
    }
}

@Composable
private fun ButtonToRegister(onClick: () -> Unit) {
    Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
        Text("¿No tienes una cuenta? ")
        Text("Regístrate ",
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.clickable(onClick = onClick)
        )
    }
}