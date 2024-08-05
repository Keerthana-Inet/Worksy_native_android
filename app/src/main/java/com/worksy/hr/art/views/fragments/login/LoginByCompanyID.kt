package com.worksy.hr.art.views.fragments.login


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
import com.worksy.hr.art.R
import com.worksy.hr.art.databinding.FragmentLoginByCompanyIDBinding
import com.worksy.hr.art.utils.NetworkUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginByCompanyID : Fragment(R.layout.fragment_login_by_company_i_d) {

    private var _binding: FragmentLoginByCompanyIDBinding?=null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginByCompanyIDBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        // unbind view onDestroyView
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        NetworkUtils.accountTypeFace(requireActivity(), binding.dontHaveAccountBtn)
        binding.apply {
            backArrow.setOnClickListener {
                findNavController().popBackStack()
            }
            loginPassword.setOnFocusChangeListener { v, hasFocus ->
                if (!hasFocus) {
                    hideKeyboard(v)
                }
            }
            dontHaveAccountBtn.setOnClickListener {
                findNavController().navigate(R.id.navi_register)
            }
            ivPasswd.setOnClickListener {
                // Toggle password visibility
                if (loginPassword.transformationMethod == PasswordTransformationMethod.getInstance()) {
                    // Password is hidden, show it
                    loginPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                    ivPasswd.setImageResource(R.drawable.ic_eyeopen)
                } else {
                    // Password is visible, hide it
                    loginPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                    ivPasswd.setImageResource(R.drawable.ic_eye_closed)
                }
                // Move cursor to the end of the text
                loginPassword.text?.let { it1 -> loginPassword.setSelection(it1.length) }
            }
            forgotPasswdBtn.setOnClickListener {
                findNavController().navigate(R.id.navi_resetpassword)
            }
            loginBtn.setOnClickListener {
                findNavController().navigate(R.id.navi_home_permission_setup)
            }
        }
    }
    fun hideKeyboard(view: View) {
        val inputMethodManager =
            view.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?
        inputMethodManager!!.hideSoftInputFromWindow(view.windowToken, 0)
    }
}