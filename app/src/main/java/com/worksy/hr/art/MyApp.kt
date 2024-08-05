package com.worksy.hr.art
import android.app.Application
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.widget.Toast
import com.worksy.hr.art.utils.NetworkReceiver
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class MyApp : Application(), NetworkReceiver.NetworkChangeListener {

    private var networkBroadcastReceiver: NetworkReceiver? = null

    override fun onCreate() {
        super.onCreate()
        networkBroadcastReceiver = NetworkReceiver(this)
        registerNetworkBroadcastReceiver()
    }

    override fun onTerminate() {
        super.onTerminate()
        unregisterNetworkBroadcastReceiver()
    }

    private fun registerNetworkBroadcastReceiver() {
        val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(networkBroadcastReceiver, intentFilter)
    }

    private fun unregisterNetworkBroadcastReceiver() {
        unregisterReceiver(networkBroadcastReceiver)
    }

    override fun onNetworkAvailable() {
    }

    override fun onNetworkUnavailable() {
        showToast("Turn your internet connection")
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}