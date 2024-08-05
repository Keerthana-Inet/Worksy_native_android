package com.worksy.hr.art.views.fragments.resetpassword


import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.worksy.hr.art.R
import com.worksy.hr.art.databinding.FragmentOtpVerifyBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class OtpVerifyFragment : Fragment(R.layout.fragment_otp_verify) {

    private var _binding: FragmentOtpVerifyBinding?=null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentOtpVerifyBinding.inflate(inflater, container, false)
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
                findNavController().popBackStack()
            }
            pinview.requestFocus()
            pinview.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    charSequence: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    // Not used in this case
                }

                override fun onTextChanged(
                    charSequence: CharSequence,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                    // Not used in this case
                }

                override fun afterTextChanged(editable: Editable) {
                    val pinText = editable.toString()
                    if(pinText.length.equals(6)){
                        findNavController().navigate(R.id.navi_create_new_password)
                    }
                    if (!pinText.isEmpty()) {
                        // Change the line color to your desired highlight color
                        pinview.setLineColor(
                            ContextCompat.getColor(
                                requireActivity(),
                                R.color.onboard_color
                            )
                        )

                    } else {
                        // Change the line color back to the default color
                        pinview.setLineColor(
                            ContextCompat.getColor(
                                requireActivity(),
                                R.color.thin_gray
                            )
                        )
                    }
                }
            })
        }
    }

}