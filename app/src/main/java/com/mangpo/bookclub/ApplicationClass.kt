package com.mangpo.bookclub

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.google.gson.GsonBuilder
import com.mangpo.bookclub.config.AccessTokenInterceptor
import com.mangpo.bookclub.database.OurpageDatabase
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

class ApplicationClass : Application() {
    companion object {
        const val TAG: String = "OUR-PAGE"
        const val BASE_URL = BuildConfig.BASE_URL
        const val KAKAO_BASE_URL = BuildConfig.KAKAO_BOOK_BASE_URL

        lateinit var encryptedPrefs: SharedPreferences
        lateinit var prefs: SharedPreferences
        lateinit var baseAPI: Retrofit
        lateinit var kakaoAPI: Retrofit
        lateinit var database: OurpageDatabase
    }

    override fun onCreate() {
        super.onCreate()

        val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
        val mainKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)

        prefs = applicationContext.getSharedPreferences(TAG, Context.MODE_PRIVATE)

        encryptedPrefs = EncryptedSharedPreferences.create(
            TAG,
            mainKeyAlias,
            this.applicationContext,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        database = OurpageDatabase.getDatabase(this)

        val okHttpClient = OkHttpClient.Builder().readTimeout(30000, TimeUnit.MILLISECONDS)
            .connectTimeout(30000, TimeUnit.MILLISECONDS).addInterceptor(AccessTokenInterceptor())
            .build()
        val gson = GsonBuilder().setLenient().create()
        baseAPI = Retrofit.Builder().baseUrl(BASE_URL).client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson)).build()
        kakaoAPI = Retrofit.Builder()
            .baseUrl(KAKAO_BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}