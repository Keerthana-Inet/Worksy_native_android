package com.worksy.hr.art.views.fragments.splash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.worksy.hr.art.R
import com.worksy.hr.art.databinding.FragmentSplashScreenBinding
import com.worksy.hr.art.repository.data.localdatabse.SharedPrefManager
import com.worksy.hr.art.views.activity.Homeactivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashScreenFragment : Fragment(R.layout.fragment_splash_screen) {

    private var _binding: FragmentSplashScreenBinding?=null
    private val binding get() = _binding!!
    private lateinit var context: Context

    @Inject
    lateinit var appPref:SharedPrefManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSplashScreenBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        // unbind view onDestroyView
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context=this.requireContext()
        Handler(Looper.getMainLooper()).postDelayed({
            when {
                appPref.getToken().toString().isEmpty() -> {
                    findNavController().navigate(R.id.navi_lets_get_started)
                }
                appPref.getCompanyAccountId() == 0 && appPref.getToken().toString().isNotEmpty() -> {
                    findNavController().navigate(R.id.navi_company_selection)
                }
                else -> {
                    Log.d("SplashcreenToken", "PrintToken: ${appPref.getToken()}")
                    val intent = Intent(requireActivity(), Homeactivity::class.java)
                    startActivity(intent)
                }
            }
        }, 3000) // 3000 milliseconds = 3 seconds
        binding.apply {
           /* relGettingReady.setOnClickListener {
                when {
                    appPref.getToken().toString().isEmpty() -> {
                        findNavController().navigate(R.id.navi_splash_get_ready)
                    }
                    appPref.getCompanyAccountId() == 0 && appPref.getToken().toString().isNotEmpty() -> {
                        findNavController().navigate(R.id.navi_company_selection)
                    }
                    else -> {
                        Log.d("SplashcreenToken", "PrintToken: ${appPref.getToken()}")
                        val intent = Intent(requireActivity(), Homeactivity::class.java)
                        startActivity(intent)
                    }
                }
            }*/
        }


    }
}
