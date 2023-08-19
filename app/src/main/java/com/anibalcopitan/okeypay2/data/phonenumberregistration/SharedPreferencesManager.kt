package com.anibalcopitan.okeypay2.data.phonenumberregistration

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesManager(context: Context) {

    companion object {
        private const val KEY_MY_PREFS = "MyPrefs-SharedPreferencesManager-PhoneNumber"
        private const val KEY_PHONE_NUMBER_1 = "phoneNumber1"
        private const val KEY_PHONE_NUMBER_2 = "phoneNumber2"
        private const val KEY_FLAG = "flag"
    }
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(KEY_MY_PREFS, Context.MODE_PRIVATE)

    fun saveFormData(phoneNumber1: String, phoneNumber2: String) {
        val editor = sharedPreferences.edit()
        editor.putString(KEY_PHONE_NUMBER_1, phoneNumber1)
        editor.putString(KEY_PHONE_NUMBER_2, phoneNumber2)

        editor.apply()
    }

    fun getFormData(): FormModel {
        val phoneNumber1 = sharedPreferences.getString(KEY_PHONE_NUMBER_1, "") ?: ""
        val phoneNumber2 = sharedPreferences.getString(KEY_PHONE_NUMBER_2, "") ?: ""
        return FormModel(phoneNumber1, phoneNumber2)
    }

    fun getPhoneNumbers(): Array<String> {
        val phoneNumber1 = sharedPreferences.getString(KEY_PHONE_NUMBER_1, "") ?: ""
        val phoneNumber2 = sharedPreferences.getString(KEY_PHONE_NUMBER_2, "") ?: ""

        // Crear y retornar un array con los números de teléfono obtenidos
        return arrayOf(phoneNumber1, phoneNumber2)
    }

    /**
     * save data flag
     */
    fun saveFormDataFlag(flag:Boolean){
        val editor = sharedPreferences.edit()
        editor.putBoolean(KEY_FLAG, flag)

        editor.apply()
    }
    fun getFormDataFlag(): FormModelFlag {
        val flag = sharedPreferences.getBoolean(KEY_FLAG, true) ?: true
        return FormModelFlag(flag)
    }

    data class FormModel(val phoneNumber1: String, val phoneNumber2: String)
    data class FormModelFlag(val flag: Boolean)
}


