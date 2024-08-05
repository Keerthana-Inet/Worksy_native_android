package com.worksy.hr.art.views.fragments.onboard

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.worksy.hr.art.R
import com.worksy.hr.art.databinding.FragmentGetStartedBinding
import com.worksy.hr.art.utils.NetworkUtils.accountTypeFace
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GetStartedFragment : Fragment(R.layout.fragment_get_started) {

    private var _binding: FragmentGetStartedBinding?=null
    private val binding get() = _binding!!
    private lateinit var context: Context

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentGetStartedBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        // unbind view onDestroyView
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context=this.requireContext()
        accountTypeFace(requireActivity(),binding.dontHaveAccountBtn)
        binding.btnLetsGetStarted.setOnClickListener {
            findNavController().navigate(R.id.navi_lets_get_started)
        }
        binding.dontHaveAccountBtn.setOnClickListener {
            findNavController().navigate(R.id.navi_register)
        }
    }
}
