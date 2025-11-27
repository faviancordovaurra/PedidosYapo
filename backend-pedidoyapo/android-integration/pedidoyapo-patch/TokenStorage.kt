package com.pedidosyapo.network

import android.content.Context
import android.content.SharedPreferences

object TokenStorage {
    private const val PREFS_NAME = "com.pedidosyapo.prefs"
    private const val KEY_TOKEN = "key_token"

    private fun prefs(context: Context): SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveToken(context: Context, token: String) {
        prefs(context).edit().putString(KEY_TOKEN, token).apply()
    }

    fun getToken(context: Context): String? {
        return prefs(context).getString(KEY_TOKEN, null)
    }

    fun clearToken(context: Context) {
        prefs(context).edit().remove(KEY_TOKEN).apply()
    }
}
