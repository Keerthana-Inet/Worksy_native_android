package com.worksy.hr.art.views.fragments.login

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import com.worksy.hr.art.R
import com.worksy.hr.art.databinding.FragmentLoginQRBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginQRFragment : Fragment(R.layout.fragment_login_q_r) {
    companion object {
        private const val PERMISSION_REQUEST_CAMERA = 1
    }
    private var _binding: FragmentLoginQRBinding?=null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginQRBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        // unbind view onDestroyView
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkCameraPermission()
    }
    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestCameraPermission()
        } else {
            initQRCodeScanner()
        }
    }
    private fun requestCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                arrayOf(Manifest.permission.CAMERA),
                PERMISSION_REQUEST_CAMERA
            )
        }
    }
    private fun initQRCodeScanner() {
        IntentIntegrator.forSupportFragment(this)
            .setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
            .setPrompt("Scan a QR code")
            .setBeepEnabled(true)
            .setOrientationLocked(true)
            .setBarcodeImageEnabled(true)
            .initiateScan()
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CAMERA) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initQRCodeScanner()
            } else {
                Toast.makeText(requireContext(), "Camera permission is required", Toast.LENGTH_LONG).show()
                activity?.finish()
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val result: IntentResult? = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        result?.let {
            if (it.contents.isNullOrEmpty()) {
                Toast.makeText(context, "Scanned result is empty", Toast.LENGTH_SHORT).show()
            } else {
                val scannedData = it.contents // This is the scanned data (token ID)
                Toast.makeText(context, "Scanned data: $scannedData", Toast.LENGTH_SHORT).show()
                processScannedToken(scannedData)
                findNavController().navigate(R.id.navi_home_permission_setup)
            }
        }
    }

    private fun processScannedToken(scannedData: String?) {
        Toast.makeText(context, "Scanned token: $scannedData", Toast.LENGTH_SHORT).show()
    }

}