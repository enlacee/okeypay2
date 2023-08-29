package com.anibalcopitan.okeypay2.util

import okhttp3.*
import java.io.IOException

object HttpUtil {
    private val client: OkHttpClient by lazy {
        OkHttpClient()
    }

    fun sendGetRequest(url: String, onResponse: (String) -> Unit, onFailure: (Exception) -> Unit) {
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onFailure(e)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseData = response.body?.string() ?: ""
                    onResponse(responseData)
                } else {
                    onFailure(IOException("Request failed with code ${response.code}"))
                }
            }
        })
    }

    // Método para enviar solicitudes POST
    fun sendPostRequest(
        url: String,
        headers: Map<String, String>? = null,
        requestBody: RequestBody,
        onResponse: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val requestBuilder = Request.Builder()
            .url(url)
            .post(requestBody) // Utilizamos el cuerpo de la solicitud POST

        // Agregar cabeceras personalizadas si se proporcionan
        headers?.forEach { (key, value) ->
            requestBuilder.addHeader(key, value)
        }

        val request = requestBuilder.build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onFailure(e)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseData = response.body?.string() ?: ""
                    onResponse(responseData)
                } else {
                    onFailure(IOException("Request failed with code ${response.code}"))
                }
            }
        })
    }
}