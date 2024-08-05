package com.worksy.hr.art.views.fragments.login

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.worksy.hr.art.R
import com.worksy.hr.art.databinding.FragmentLoginByEmailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginByEmail : Fragment(R.layout.fragment_login_by_email) {

    private var _binding: FragmentLoginByEmailBinding?=null
    private val binding get() = _binding!!
    private var mGoogleSignInClient : GoogleSignInClient? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginByEmailBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        // unbind view onDestroyView
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        val acct = GoogleSignIn.getLastSignedInAccount(requireActivity())
        Log.d("GoogleAccount", "GoogleAccount: "+acct)
        binding.cancelBtn.setOnClickListener {
            signOut()
        }
    }

    private fun signOut() {
        mGoogleSignInClient?.signOut()?.addOnCompleteListener(requireActivity()
        ) { Toast.makeText(requireActivity(), "Signed Out", Toast.LENGTH_LONG).show() }
        findNavController().navigate(R.id.navi_lets_get_started)
    }
}