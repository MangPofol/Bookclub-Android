package com.mangpo.bookclub.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.content.res.Resources
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlin.math.roundToInt

typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

/*네트워크 상태 확인*/
fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val nw = connectivityManager.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false

        return when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            //for other device how are able to connect with Ethernet
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            //for check internet over Bluetooth
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
            else -> false
        }
    } else {
        return connectivityManager.activeNetworkInfo?.isConnected ?: false
    }
}

fun View.fadeIn() {
    alpha = 0f
    visibility = View.VISIBLE

    animate()
        .alpha(1f)
        .setDuration(300)
        .setListener(null)
}

fun View.fadeOut() {
    animate()
        .alpha(0f)
        .setDuration(200)
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                visibility = View.GONE
            }
        })
}

fun getDeviceWidth(): Int {
    // px 반환
    return Resources.getSystem().displayMetrics.widthPixels
}

fun getDeviceHeight(): Int {
    return Resources.getSystem().displayMetrics.heightPixels
}

fun convertDpToPx(context: Context, dp: Int): Int {
    val density = context.resources.displayMetrics.density
    return (dp * density).roundToInt()
}