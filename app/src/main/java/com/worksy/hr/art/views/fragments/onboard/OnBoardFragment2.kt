package com.worksy.hr.art.views.fragments.onboard

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.worksy.hr.art.R
import com.worksy.hr.art.databinding.FragmentOnBoard2Binding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnBoardFragment2 : Fragment(R.layout.fragment_on_board2) {

    private var _binding: FragmentOnBoard2Binding?=null
    private val binding get() = _binding!!
    private lateinit var context: Context

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentOnBoard2Binding.inflate(inflater, container, false)
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
    }
}