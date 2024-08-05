package com.worksy.hr.art.views.fragments.register

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.navigation.fragment.findNavController
import com.worksy.hr.art.R
import com.worksy.hr.art.databinding.FragmentRegisterBinding
import com.worksy.hr.art.utils.NetworkUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment(R.layout.fragment_register) {

    private var _binding: FragmentRegisterBinding?=null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        // unbind view onDestroyView
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        NetworkUtils.loginTypeFace(
            requireActivity(),
            binding.termsConditionBtn,
             "I agree to Worksy's",
            "License Agreement,Terms, & Privacy Policy",
            requireActivity().resources.getColor(R.color.black),
            requireActivity().resources.getColor(R.color.button_txt)
        )
        binding.apply {
            backArrow.setOnClickListener {
                findNavController().popBackStack()
            }
            registerBtn.setOnClickListener {

            }
            loginPassword.setOnFocusChangeListener { v, hasFocus ->
                if (!hasFocus) {
                    hideKeyboard(v)
                }
            }
            ivEyepasswd.setOnClickListener {
                // Toggle password visibility
                if (loginPassword.transformationMethod == PasswordTransformationMethod.getInstance()) {
                    // Password is hidden, show it
                    loginPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                    ivEyepasswd.setImageResource(R.drawable.ic_eyeopen)
                } else {
                    // Password is visible, hide it
                    loginPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                    ivEyepasswd.setImageResource(R.drawable.ic_eye_closed)
                }
                // Move cursor to the end of the text
                loginPassword.text?.let { it1 -> loginPassword.setSelection(it1.length) }
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
}