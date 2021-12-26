package com.mangpo.bookclub.util

import android.content.Context
import android.content.SharedPreferences

object AccountSharedPreference {

    private const val MY_ACCOUNT: String = "account"

    fun setJWT(context: Context, token: String) {
        val prefs: SharedPreferences =
            context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = prefs.edit()
        editor.putString("TOKEN", token)
        editor.commit()
    }

    fun getJWT(context: Context): String {
        val prefs: SharedPreferences =
            context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        return prefs.getString("TOKEN", "").toString()
    }

    fun clearJWT(context: Context) {
        val prefs: SharedPreferences =
            context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = prefs.edit()
        editor.clear()
        editor.commit()
    }
}