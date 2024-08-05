package com.worksy.hr.art.views.fragments.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.worksy.hr.art.R
import com.worksy.hr.art.databinding.FragmentInProgressBinding
import com.worksy.hr.art.models.travelRequest.InProgressResponse
import com.worksy.hr.art.views.adapter.travelRequest.InProgressAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InProgressFragment : Fragment(R.layout.fragment_in_progress) {

    private lateinit var rvAdapter: InProgressAdapter
    private val progressList = mutableListOf<InProgressResponse.Data>()
    private var _binding: FragmentInProgressBinding? = null
    private val binding get() = _binding!!
    private lateinit var context: Context

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        _binding = FragmentInProgressBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // unbind view onDestroyView
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context = this.requireContext()
        progressList.clear()
        progressList.add(
            InProgressResponse.Data(
                "TR-107",
                "International",
                "Singapore HR Tech Conference & Expo 24",
                "Business trip to SG to attend Singapore HR Technology Conference & Expo 24",
                "International",
                "Singapore",
                "12 - 14 Jun, 2024",
                "RM 5,000.00",
                "Created", "Stage 1: Travel Requisition Form"
            )
        )
        progressList.add(
            InProgressResponse.Data(
                "TR-108",
                "Domestic",
                "Singapore HR Tech Conference & Expo 24",
                "Business trip to SG to attend Singapore HR Technology Conference & Expo 24",
                "International",
                "Singapore",
                "12 - 14 Jun, 2024",
                "RM 5,000.00",
                "Pending", "Stage 1: Travel Requisition Form"
            )
        )
        progressList.add(
            InProgressResponse.Data(
                "TR-109",
                "International",
                "Singapore HR Tech Conference & Expo 24",
                "Business trip to SG to attend Singapore HR Technology Conference & Expo 24",
                "International",
                "Singapore",
                "12 - 14 Jun, 2024",
                "RM 5,000.00",
                "Created", "Stage 1: Travel Requisition Form"
            )
        )
        progressList.add(
            InProgressResponse.Data(
                "TR-110",
                "Domestic",
                "Singapore HR Tech Conference & Expo 24",
                "Business trip to SG to attend Singapore HR Technology Conference & Expo 24",
                "International",
                "Singapore",
                "12 - 14 Jun, 2024",
                "RM 5,000.00",
                "Pending", "Stage 1: Travel Requisition Form"
            )
        )
        rvAdapter = InProgressAdapter(requireContext(), progressList, onViewDetailClick = {
            findNavController().navigate(R.id.navi_travel_request_detailpage)
        })
        binding.inProgressRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.inProgressRecyclerView.adapter = rvAdapter
    }
}