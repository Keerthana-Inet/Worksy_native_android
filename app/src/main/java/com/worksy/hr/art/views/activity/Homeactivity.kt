package com.worksy.hr.art.views.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.worksy.hr.art.R
import com.worksy.hr.art.databinding.ActivityHomeBinding
import com.worksy.hr.art.repository.data.localdatabse.SharedPrefManager
import com.worksy.hr.art.utils.gone
import com.worksy.hr.art.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class Homeactivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    var badgeDrawable: BadgeDrawable? = null
    private lateinit var navController: NavController
    private val CREDENTIAL_PICKER_REQUEST = 1
    private val SMS_CONSENT_REQUEST = 2

    @Inject
    lateinit var appPref: SharedPrefManager

    companion object {
        const val REQUEST_CODE = 100
        private var isLoading = false
        lateinit var mContext: Homeactivity

        fun showLoading() {
            if (!isLoading) {
                mContext.binding.progressCircular.visible()
                isLoading = true
            }
        }

        fun hideLoading() {
            if (isLoading) {
                mContext.binding.progressCircular.gone()
                isLoading = false
            }
        }

        fun back() {
            mContext.finish()
        }

        fun showLongSnackbar(view: View, message: String) {
            val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
            val snackbarView = snackbar.view
            snackbar.animationMode = BaseTransientBottomBar.ANIMATION_MODE_FADE
            snackbarView.setBackgroundColor(ContextCompat.getColor(view.context, R.color.dark_blue))
            snackbar.animationMode = BaseTransientBottomBar.ANIMATION_MODE_SLIDE
            snackbar.show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        binding = ActivityHomeBinding.inflate(layoutInflater)
        mContext = this
        setContentView(binding.root)
        setUpView()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //createNotificationChannel()
        }
    }
    fun setBottomNavigationVisibility(visibility: Int) {
        findViewById<BottomNavigationView>(R.id.nav_view)?.visibility = visibility
    }
    private fun setUpView() {
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navController = findNavController(R.id.nav_host_fragment_activity_home)
        navView.setupWithNavController(navController)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                findNavController(R.id.nav_host_fragment_activity_home).popBackStack()
            } else if (resultCode == RESULT_CANCELED) {
                // Write your code if there's no result
            }
        }
    }
}