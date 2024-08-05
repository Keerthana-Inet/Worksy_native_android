package com.worksy.hr.art.views.fragments.permissionsetup

import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.worksy.hr.art.R
import com.worksy.hr.art.databinding.BottomsheetHelpDialogBinding
import com.worksy.hr.art.databinding.FragmentClockDeviceSetupBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClockDeviceSetupFragment : Fragment(R.layout.fragment_clock_device_setup) {

    private var _binding: FragmentClockDeviceSetupBinding?=null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentClockDeviceSetupBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        // unbind view onDestroyView
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val model = Build.MODEL
        val manufacturer = Build.MANUFACTURER
        binding.biometricTitle.text=manufacturer
        binding.biometricDesc.text=model
        binding.helpIcon.setOnClickListener {
            showBottomSheetDialog()
        }
    }

    private fun showBottomSheetDialog() {
        var bottomSheetDialog: BottomSheetDialog? = null
        var bottomSheet:View?=null
        bottomSheetDialog = BottomSheetDialog(requireActivity(), R.style.BottomSheetDialog)
        val binding: BottomsheetHelpDialogBinding = BottomsheetHelpDialogBinding.inflate(layoutInflater)
        bottomSheetDialog!!.setContentView(binding.root)
        binding.apply {
            backTxt.setOnClickListener {
                bottomSheetDialog.dismiss()
            }
            closeIcon.setOnClickListener {
                bottomSheetDialog.dismiss()
            }
        }
        bottomSheetDialog!!.setOnShowListener { _ ->
            // Adjust the BottomSheet behavior to a custom peek height
            bottomSheet = bottomSheetDialog?.findViewById<View>(
                com.google.android.material.R.id.design_bottom_sheet,
            )
            if (bottomSheet != null) {
                val behavior = BottomSheetBehavior.from(bottomSheet!!)

                // Calculate the peek height as 80% of the screen height
                val screenHeight = Resources.getSystem().displayMetrics.heightPixels
                val customPeekHeight = (screenHeight * 0.95).toInt()

                behavior.peekHeight = customPeekHeight
                behavior.state = BottomSheetBehavior.STATE_COLLAPSED // You can use STATE_EXPANDED if you want it fully open by default
            } else {
                // Handle the case where the bottomSheet is null (optional)
            }
        }
        bottomSheetDialog!!.show()
    }
}