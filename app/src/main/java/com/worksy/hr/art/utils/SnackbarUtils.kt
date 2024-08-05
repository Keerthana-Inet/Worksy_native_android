package com.worksy.hr.art.utils
import android.content.Intent
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.BaseTransientBottomBar.ANIMATION_MODE_FADE
import com.google.android.material.snackbar.Snackbar
import com.worksy.hr.art.R
import com.worksy.hr.art.views.activity.MainActivity

object SnackbarUtils {
    fun showLongSnackbar(view: View, message: String) {
        val parentView = view.rootView.findViewById<View>(android.R.id.content) ?: view
        val snackbar = Snackbar.make(parentView ?: view, message, Snackbar.LENGTH_LONG)
        val snackbarView = snackbar.view
        snackbar.setAnimationMode(ANIMATION_MODE_FADE)
        snackbarView.setBackgroundColor(ContextCompat.getColor(view.context, R.color.yellow))
        val params = snackbar.view.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.BOTTOM
        snackbar.view.layoutParams = params
        snackbar.show()
    }
}
