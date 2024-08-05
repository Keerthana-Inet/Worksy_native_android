package com.worksy.hr.art.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.worksy.hr.art.R
import java.lang.ref.WeakReference

class NetworkChangeReceiver(context: Context) : BroadcastReceiver() {

    private val contextReference = WeakReference(context)
    private var bottomSheetDialog: BottomSheetDialog? = null

    init {
        val strongContext = contextReference.get()
        if (strongContext != null) {
            bottomSheetDialog = BottomSheetDialog(strongContext)
            // Set up your bottom sheet content here
            bottomSheetDialog?.setContentView(R.layout.layout_no_connectivity)
            bottomSheetDialog?.setCancelable(false)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        val isConnected = isConnected(context)

        if (isConnected) {
            // Internet connection is back
            bottomSheetDialog?.dismiss()
        } else {
            // No internet connection, show dialog if not showing
            bottomSheetDialog?.let {
                if (!it.isShowing) {
                    it.show()
                }
            }
        }
    }

    private fun isConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }
}