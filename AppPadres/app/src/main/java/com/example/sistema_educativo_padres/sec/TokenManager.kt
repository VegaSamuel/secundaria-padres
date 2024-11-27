package com.example.sistema_educativo_padres.sec

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

object TokenManager {
    private val PREF_NAME = "AppPreferences"
    private val TOKEN_KEY = "authToken"

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun guardarToken(context: Context, token: String) {
        val prefs = getPreferences(context)
        prefs.edit().putString(TOKEN_KEY, token).apply()
        Log.d("Token Guardado", obtenerToken(context).toString())
    }

    fun obtenerToken(context: Context): String? {
        val prefs = getPreferences(context)
        val token = prefs.getString(TOKEN_KEY, null)
        Log.d("TOKEN_MANAGER", "Token obtenido: $token")

        return token
    }

    fun borrarToken(context: Context) {
        val prefs = getPreferences(context)
        prefs.edit().remove(TOKEN_KEY).apply()
    }
}