package com.worksy.hr.art.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo


class NetworkReceiver(private val listener: NetworkChangeListener) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo

        if (networkInfo != null && networkInfo.isConnected) {
            // Network is connected
            listener.onNetworkAvailable()
        } else {
            // Network is disconnected
            listener.onNetworkUnavailable()
        }
    }

    interface NetworkChangeListener {
        fun onNetworkAvailable()
        fun onNetworkUnavailable()
    }
}