package com.worksy.hr.art.views.fragments.permissionsetup

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.worksy.hr.art.R
import com.worksy.hr.art.databinding.CustomBiometricDialogBinding
import com.worksy.hr.art.databinding.FragmentPermissionSetUpBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.Executor
import java.util.concurrent.Executors
@AndroidEntryPoint
class PermissionSetUpFragment : Fragment(R.layout.fragment_permission_set_up) {

    private var _binding: FragmentPermissionSetUpBinding? = null
    private val binding get() = _binding!!
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var executor: Executor
    var selectedTabPosition = 0
    private companion object {
        private const val PERMISSION_REQUEST_CODE = 100
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPermissionSetUpBinding.inflate(inflater, container, false)
        return binding.root
    }


    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {

            biometricSwitchBtn.setOnCheckedChangeListener { buttonView, isChecked ->
                if(isChecked) {
                    checkAndRequestPermissions()
                   // updateButtonColor(binding,true)
                }else{

                }
            }
            cameraSwitchBtn.setOnCheckedChangeListener { buttonView, isChecked ->
                if(isChecked){
                    checkAndRequestPermissions()

                }else{

                }
            }
            notificationSwitchBtn.setOnCheckedChangeListener { buttonView, isChecked ->
                if(isChecked){
                    notificationDialog()

                }else{

                }
            }
        }
    }

  /*  private fun updateButtonColor(binding: FragmentPermissionSetUpBinding, buttonColor: Boolean) {
        if(binding.cameraSwitchBtn.isChecked && binding.biometricSwitchBtn.isChecked && binding.notificationSwitchBtn.isChecked){
            binding.continueBtn.setBackgroundDrawable(resources.getDrawable(R.drawable.button_background_round_corner))
            binding.continueBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            binding.continueBtn.isClickable = true
        }else if(buttonColor.equals(false)){
            binding.continueBtn.setBackgroundDrawable(resources.getDrawable(R.drawable.not_selected_background_color))
            binding.continueBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_gray))
            binding.continueBtn.isClickable = false
        }
    }*/

    private fun notificationDialog() {
        val dialogBinding = CustomBiometricDialogBinding.inflate(layoutInflater)

        val dialog = AlertDialog.Builder(requireActivity())
            .setView(dialogBinding.root)
            .create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogBinding.apply {
            faceIdTitle.text = "“Worksy” Would Like to Send You Notifications"
            faceIdSubtitle.text = "Notifications may include alerts, sounds and icon badges. These can be configured in settings."

            allowBtn.setOnClickListener {
                dialog.dismiss()
            }

            dontallowBtn.setOnClickListener {
                dialog.dismiss()

            }
        }

        dialog.show()
    }

    private fun checkAndRequestPermissions() {
        val cameraPermission = Manifest.permission.CAMERA
        val biometricPermission = Manifest.permission.USE_BIOMETRIC

        val cameraPermissionGranted =
            ContextCompat.checkSelfPermission(requireContext(), cameraPermission) == PackageManager.PERMISSION_GRANTED
        val biometricPermissionGranted =
            ContextCompat.checkSelfPermission(requireContext(), biometricPermission) == PackageManager.PERMISSION_GRANTED

        if (!cameraPermissionGranted || !biometricPermissionGranted) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(cameraPermission, biometricPermission),
                PERMISSION_REQUEST_CODE
            )
        } else {
            // Both permissions are already granted, proceed with biometric authentication setup
            binding.cameraSwitchBtn.isChecked = true  // Set the toggle to be checked
            authenticateWithBiometrics()
            binding.biometricSwitchBtn.isChecked = true
        }
    }



    private fun checkPermissionsAndSetSwitches() {
        val cameraPermissionGranted =
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        val biometricPermissionGranted =
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.USE_BIOMETRIC) == PackageManager.PERMISSION_GRANTED

        // Set the switches based on permission status
        binding.cameraSwitchBtn.isChecked = cameraPermissionGranted
        binding.biometricSwitchBtn.isChecked = biometricPermissionGranted
        binding.notificationSwitchBtn.isChecked=true

        // If both permissions are granted, proceed with biometric authentication setup
        if (cameraPermissionGranted && biometricPermissionGranted) {
            authenticateWithBiometrics()
        }
    }

    private fun authenticateWithBiometrics() {
        val biometricManager = BiometricManager.from(requireActivity())
        val canAuthenticate = biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)

        when (canAuthenticate) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                // Proceed with biometric authentication
                executor = Executors.newSingleThreadExecutor()
                val dialogBinding = CustomBiometricDialogBinding.inflate(layoutInflater)

                val dialog = AlertDialog.Builder(requireActivity())
                    .setView(dialogBinding.root)
                    .create()
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialogBinding.apply {
                    faceIdTitle.text = "Authenticate with Face ID"
                    faceIdSubtitle.text = "Place your face in front of the sensor"

                    allowBtn.setOnClickListener {
                        dialog.dismiss()
                        biometricPrompt = BiometricPrompt(requireActivity(), executor, callback)
                        val promptInfo = BiometricPrompt.PromptInfo.Builder()
                            .setTitle("Authenticate with Face ID")
                            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
                            .setNegativeButtonText("Cancel")
                            .setConfirmationRequired(true)
                            .build()
                        biometricPrompt.authenticate(promptInfo)
                    }

                    dontallowBtn.setOnClickListener {
                        dialog.dismiss()
                    }
                }

                dialog.show()
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                showToast("Biometric authentication not available on this device")
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                showToast("Biometric hardware is currently unavailable")
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                // Handle the case where no biometric credentials are enrolled
                showToast("No biometric or device credentials are enrolled. Please enroll a biometric or device credential.")
            }
            BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED -> {
                showToast("Biometric security update required")
            }
            BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED -> {
                showToast("Biometric authentication is not supported")
            }
            else -> {
                showToast("Unknown biometric error")
            }
        }
    }

    private val callback = object : BiometricPrompt.AuthenticationCallback() {
        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
            super.onAuthenticationSucceeded(result)
            showToast("Biometric authentication succeeded")
        }

        override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
            super.onAuthenticationError(errorCode, errString)
            showToast("Biometric authentication error: $errString")
        }

        override fun onAuthenticationFailed() {
            super.onAuthenticationFailed()
            showToast("Biometric authentication failed")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                // All permissions granted, proceed with biometric authentication setup
                authenticateWithBiometrics()
                binding.cameraSwitchBtn.isChecked=true
                binding.biometricSwitchBtn.isChecked=true
            } else {
                // Handle permission denial
                Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showToast(message: String) {
        activity?.runOnUiThread {
            Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
        }
    }
}
