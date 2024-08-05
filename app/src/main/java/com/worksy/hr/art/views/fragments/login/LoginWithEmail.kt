package com.worksy.hr.art.views.fragments.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.worksy.hr.art.R
import com.worksy.hr.art.databinding.FragmentLoginByMobileNumberBinding
import com.worksy.hr.art.databinding.FragmentLoginWithEmailBinding
import com.worksy.hr.art.repository.data.localdatabse.SharedPrefManager
import com.worksy.hr.art.utils.CommonTextWatcher
import com.worksy.hr.art.utils.NetworkUtils
import com.worksy.hr.art.utils.UIResult
import com.worksy.hr.art.viewmodels.LoginViewModel
import com.worksy.hr.art.views.activity.Homeactivity
import com.worksy.hr.art.views.activity.SplashHomeActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginWithEmail : Fragment(R.layout.fragment_login_with_email) {

    private var _binding: FragmentLoginWithEmailBinding?=null
    var token:String=""
    @Inject
    lateinit var appPrefManager: SharedPrefManager
    private val binding get() = _binding!!
    private lateinit var viewModel: LoginViewModel
    val loginViewModel: LoginViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginWithEmailBinding.inflate(inflater, container, false)
        viewModel = loginViewModel
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
        setUpFragment()
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
            loginBtn.setOnClickListener {
                login()
            }
            emailAddress.setOnFocusChangeListener() { view, hasFocus ->
                if (!hasFocus) {
                    hideKeyboard(view)
                }
            }
            loginPassword.setOnFocusChangeListener() { view, hasFocus ->
                if (!hasFocus) {
                    hideKeyboard(view)
                }
            }
           // loginPassword.transformationMethod = AsteriskTransformationMethod()
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
        }
    }

    private fun login() {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

        val email = binding.emailAddress.text.toString().replace(Regex("\\s+"), "")
        val password = binding.loginPassword.text.toString().replace(Regex("\\s+"), "")
        if (email.isEmpty()) {
            binding.emailAddress.error = "Enter Valid Email Address"
            return
        }
        if(!email.matches(Regex(emailPattern))){
            binding.emailAddress.error = "Enter Valid Email Address"
            return
        }
        if(password.isEmpty()){
            binding.loginPassword.error="Enter Valid Password"
        }
        else {
          //  viewModel.userEmailLogin(email, password)
            viewModel.userEmailLogin(email,password)
        }
    }


    private fun setUpFragment() {
        viewModel.userEmailLogin.observe(viewLifecycleOwner) {
            if (it != null) {
                when (it) {
                    is UIResult.Success -> {
                        SplashHomeActivity.hideLoading()
                        if(it.data?.status.equals("success")) {
                            token = it.data?.data?.accessToken.toString()
                            appPrefManager.setToken(token ?: "")
                            appPrefManager.setName(it.data?.data?.user?.name.toString())
                            appPrefManager.setEmail(it.data?.data?.user?.email.toString())
                            appPrefManager.setPhoneNumber(it.data?.data?.user?.phoneNumber.toString())
                            appPrefManager.setCompanyCode(it.data?.data?.user?.companyCode.toString())
                            appPrefManager.setUserName(it.data?.data?.user?.username.toString())
                            Log.d("LoginResponse", "LoginResponse: " + it.data.toString())
                            Toast.makeText(requireActivity(), it.data?.message, Toast.LENGTH_SHORT)
                                .show()
                            findNavController().navigate(R.id.navi_company_selection)
                          //  findNavController().navigate(R.id.navi_home_permission_setup)
                        }else if(it.data?.status.equals("error")){
                            Toast.makeText(requireActivity(), it.data?.message, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }

                    is UIResult.Error -> {
                        Toast.makeText(requireActivity(),it.message, Toast.LENGTH_SHORT).show()
                        SplashHomeActivity.hideLoading()
                    }
                    is UIResult.Loading -> {
                        SplashHomeActivity.showLoading()
                    }
                }
            }
        }

    }


    fun hideKeyboard(view: View) {
        val inputMethodManager =
            view.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?
        inputMethodManager!!.hideSoftInputFromWindow(view.windowToken, 0)
    }
}