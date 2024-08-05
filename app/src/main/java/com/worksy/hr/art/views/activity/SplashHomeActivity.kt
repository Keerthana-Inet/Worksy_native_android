package com.worksy.hr.art.views.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.worksy.hr.art.R
import com.worksy.hr.art.databinding.ActivitySplashHomeBinding
import com.worksy.hr.art.utils.gone
import com.worksy.hr.art.utils.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashHomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashHomeBinding
    companion object{
        private var isLoading = false
        private lateinit var mContext: SplashHomeActivity
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
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashHomeBinding.inflate(layoutInflater)
        mContext = this
        setContentView(binding.root)
        if(intent.getBooleanExtra("splashGetReady",false)) {
            val navController = supportFragmentManager.findFragmentById(R.id.navHostSplashFragment)
                ?.findNavController()
            navController?.navigate(R.id.splashFragment)
        }
    }
}