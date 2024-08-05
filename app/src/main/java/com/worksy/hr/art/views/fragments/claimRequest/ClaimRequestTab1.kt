package com.worksy.hr.art.views.fragments.claimRequest

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.worksy.hr.art.R
import com.worksy.hr.art.databinding.FragmentClaimRequestTab1Binding
import com.worksy.hr.art.repository.data.localdatabse.SharedPrefManager
import com.worksy.hr.art.viewmodels.LoginViewModel
import com.worksy.hr.art.views.activity.Homeactivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ClaimRequestTab1 : Fragment(R.layout.fragment_claim_request_tab1) {

    private var _binding: FragmentClaimRequestTab1Binding?=null
    private val binding get() = _binding!!
    private lateinit var context: Context
    private val viewmodel : LoginViewModel by activityViewModels()
    @Inject
    lateinit var appPref:SharedPrefManager
    private var titleTextChanged = false
    private var descTextChanged = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentClaimRequestTab1Binding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        (activity as? Homeactivity)?.setBottomNavigationVisibility(View.GONE)
        // unbind view onDestroyView
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context = this.requireContext()
        (activity as? Homeactivity)?.setBottomNavigationVisibility(View.GONE)

        appPref.setFormTitle("")
        appPref.setFormDesc("")

        val etTitle = binding.etTitle
        val etDesc = binding.etDesc

        etTitle.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus && titleTextChanged) {
                appPref.setFormTitle(etTitle.text.toString())
                titleTextChanged = false
            }
        }

        etDesc.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus && descTextChanged) {
                appPref.setFormDesc(etDesc.text.toString())
                descTextChanged = false
            }
        }

        etTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                titleTextChanged = true
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        etDesc.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                descTextChanged = true
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }
}
