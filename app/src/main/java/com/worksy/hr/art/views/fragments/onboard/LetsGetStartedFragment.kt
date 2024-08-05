package com.worksy.hr.art.views.fragments.onboard

import ExitConfirmationDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.worksy.hr.art.R
import com.worksy.hr.art.databinding.FragmentLetsGetStartedBinding
import com.worksy.hr.art.repository.data.localdatabse.SharedPrefManager
import com.worksy.hr.art.utils.NetworkUtils.accountTypeFace
import com.worksy.hr.art.utils.NetworkUtils.loginTypeFace
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LetsGetStartedFragment : Fragment(R.layout.fragment_lets_get_started) {

    private var _binding: FragmentLetsGetStartedBinding?=null
    private val binding get() = _binding!!
    private val RC_SIGN_IN = 1
    private var mGoogleSignInClient: GoogleSignInClient? = null
    @Inject
    lateinit var appPref:SharedPrefManager
    var pref:String=""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLetsGetStartedBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        // unbind view onDestroyView
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(arguments!=null){
            pref=requireArguments().getString("clear_preferences").toString()
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            showExitConfirmationDialog()
        }
        accountTypeFace(requireActivity(),binding.dontHaveAccountBtn)
        loginTypeFace(requireActivity(),binding.loginCommpanyid,"Log in with","Company ID",requireActivity().resources.getColor(R.color.gray),requireActivity().resources.getColor(R.color.black))
        loginTypeFace(
            requireActivity(),
            binding.loginMobileNumber,
            "Log in with",
            "Mobile Number",
            requireActivity().resources.getColor(R.color.gray),
            requireActivity().resources.getColor(R.color.black)
        )
        loginTypeFace(
            requireActivity(),
            binding.loginWithEmail,
            "Log in with",
            "Email",
            requireActivity().resources.getColor(R.color.gray),
            requireActivity().resources.getColor(R.color.black)
        )
        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()

        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        binding.apply {
            backArrow.setOnClickListener {
                Log.d("CheckPreference", "CheckPreference: "+pref)
                if(pref.equals("clear_pref")){
                    showExitConfirmationDialog()
                }else {
                    findNavController().popBackStack()
                }
            }
            loginCommpanyid.setOnClickListener {
                findNavController().navigate(R.id.navi_loginwith_companyId)
            }
            loginMobileNumber.setOnClickListener {
                findNavController().navigate(R.id.navi_loginwith_mobile_number)
            }
            loginWithEmail.setOnClickListener {
                findNavController().navigate(R.id.navi_loginwith_email)
            }
            dontHaveAccountBtn.setOnClickListener {
                findNavController().navigate(R.id.navi_register)
            }
            qrCodeLogin.setOnClickListener {
                findNavController().navigate(R.id.navi_qr_code_login)
            }
        }
    }
    private fun showExitConfirmationDialog() {
        val dialog = ExitConfirmationDialog()
        dialog.onExitConfirmed = { requireActivity().finishAffinity() }
        dialog.show(parentFragmentManager, "ExitConfirmationDialog")
    }
    private fun signIn() {
        val intent = mGoogleSignInClient!!.signInIntent
        startActivityForResult(intent, RC_SIGN_IN)
    }
    override fun onStart() {
        super.onStart()
        val account = GoogleSignIn.getLastSignedInAccount(requireActivity())
        if(account!=null) {
            Toast.makeText(requireActivity(),"Your account "+account!!.email+" is logged",Toast.LENGTH_SHORT).show()
          // findNavController().navigate(R.id.navi_loginwith_email)
        }
    }
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task =
                GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                val account : GoogleSignInAccount? = task.getResult(ApiException::class.java)
                Toast.makeText(requireActivity(),account!!.email+"  logged successfully",Toast.LENGTH_SHORT).show()
              //  findNavController().navigate(R.id.navi_loginwith_email)

            } catch (e: ApiException) {
                // The ApiException status code indicates the detailed failure reason.
                // Please refer to the GoogleSignInStatusCodes class reference for more information.
                Log.e("TAG","signInResult:failed code=" + e.statusCode)
            }
        }
    }
}