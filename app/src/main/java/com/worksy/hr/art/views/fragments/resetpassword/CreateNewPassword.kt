package com.worksy.hr.art.views.fragments.resetpassword

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.worksy.hr.art.R
import com.worksy.hr.art.databinding.FragmentCreateNewPasswordBinding
import com.worksy.hr.art.databinding.ResetpasswdDialogBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateNewPassword : Fragment(R.layout.fragment_create_new_password) {

    private var _binding: FragmentCreateNewPasswordBinding?=null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCreateNewPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        // unbind view onDestroyView
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            backArrow.setOnClickListener {
                findNavController().navigate(R.id.navi_otpverify)
            }
            confirmBtn.setOnClickListener {
                resetPasswdSuccessDialog()
            }
            etNewPasswd.setOnFocusChangeListener { v, hasFocus ->
                if (!hasFocus) {
                    hideKeyboard(v)
                }
            }
            etConfirmPasswd.setOnFocusChangeListener { v, hasFocus ->
                if (!hasFocus) {
                    hideKeyboard(v)
                }
            }
            ivEyepasswd.setOnClickListener {
                // Toggle password visibility
                if (etNewPasswd.transformationMethod == PasswordTransformationMethod.getInstance()) {
                    // Password is hidden, show it
                    etNewPasswd.transformationMethod = HideReturnsTransformationMethod.getInstance()
                    ivEyepasswd.setImageResource(R.drawable.ic_eyeopen)
                } else {
                    // Password is visible, hide it
                    etNewPasswd.transformationMethod = PasswordTransformationMethod.getInstance()
                    ivEyepasswd.setImageResource(R.drawable.ic_eye_closed)
                }
                // Move cursor to the end of the text
                etNewPasswd.text?.let { it1 -> etNewPasswd.setSelection(it1.length) }
            }
            ivConfeyepasswd.setOnClickListener {
                // Toggle password visibility
                if (etConfirmPasswd.transformationMethod == PasswordTransformationMethod.getInstance()) {
                    // Password is hidden, show it
                    etConfirmPasswd.transformationMethod = HideReturnsTransformationMethod.getInstance()
                    ivConfeyepasswd.setImageResource(R.drawable.ic_eyeopen)
                } else {
                    // Password is visible, hide it
                    etConfirmPasswd.transformationMethod = PasswordTransformationMethod.getInstance()
                    ivConfeyepasswd.setImageResource(R.drawable.ic_eye_closed)
                }
                // Move cursor to the end of the text
                etConfirmPasswd.text?.let { it1 -> etConfirmPasswd.setSelection(it1.length) }
            }
        }

    }
    fun hideKeyboard(view: View) {
        val inputMethodManager =
            view.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?
        inputMethodManager!!.hideSoftInputFromWindow(view.windowToken, 0)
    }
    private fun resetPasswdSuccessDialog() {
        val bottomSheetDialog = BottomSheetDialog(requireActivity(), R.style.BottomSheetDialog)
        val binding: ResetpasswdDialogBinding = ResetpasswdDialogBinding.inflate(layoutInflater)

        bottomSheetDialog.setContentView(binding.root)
        bottomSheetDialog.show()
    }
}