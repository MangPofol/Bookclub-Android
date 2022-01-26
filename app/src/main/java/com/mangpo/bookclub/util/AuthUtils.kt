package com.mangpo.bookclub.util

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

object AuthUtils {

    private const val OURPAGE: String = "ourpage"
    private val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
    private val mainKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)

    fun setJWT(context: Context, token: String) {   //JWT 저장
        val prefs: SharedPreferences = EncryptedSharedPreferences.create(
            OURPAGE,
            mainKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        with(prefs.edit()) {
            putString("TOKEN", token)
            apply()
        }
    }

    fun setEmail(context: Context, email: String) {   //email 저장
        val prefs: SharedPreferences = EncryptedSharedPreferences.create(
            OURPAGE,
            mainKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        with(prefs.edit()) {
            putString("email", email)
            apply()
        }
    }

    fun setPassword(context: Context, password: String) {   //password 저장
        val prefs: SharedPreferences = EncryptedSharedPreferences.create(
            OURPAGE,
            mainKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        with(prefs.edit()) {
            putString("password", password)
            apply()
        }
    }

    fun getJWT(context: Context): String {  //JWT 가져오기
        val prefs: SharedPreferences = EncryptedSharedPreferences.create(
            OURPAGE,
            mainKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        return prefs.getString("TOKEN", "").toString()
    }

    fun getEmail(context: Context): String {  //email 가져오기
        val prefs: SharedPreferences = EncryptedSharedPreferences.create(
            OURPAGE,
            mainKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        return prefs.getString("email", "").toString()
    }

    fun getPassword(context: Context): String {  //password 가져오기
        val prefs: SharedPreferences = EncryptedSharedPreferences.create(
            OURPAGE,
            mainKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        return prefs.getString("password", "").toString()
    }

    fun clear(context: Context) {    //JWT, ID, password 초기화
        val prefs: SharedPreferences = EncryptedSharedPreferences.create(
            OURPAGE,
            mainKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        with(prefs.edit()) {
            clear()
            apply()
        }
    }
}