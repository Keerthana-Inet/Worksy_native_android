package com.worksy.hr.art.views.fragments.home

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.worksy.hr.art.R
import com.worksy.hr.art.databinding.FragmentCompletedBinding
import com.worksy.hr.art.models.travelRequest.InProgressResponse
import com.worksy.hr.art.views.adapter.travelRequest.CompletedAdapter
import com.worksy.hr.art.views.adapter.travelRequest.InProgressAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CompletedFragment : Fragment(R.layout.fragment_completed) {
    private lateinit var rvAdapter: CompletedAdapter
    private var progressList: MutableList<InProgressResponse.Data> = mutableListOf()
    private var _binding: FragmentCompletedBinding?=null
    private val binding get() = _binding!!
    private lateinit var context: Context

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCompletedBinding.inflate(inflater, container, false)
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
        progressList.clear()
        rvAdapter = CompletedAdapter(requireContext(), progressList, onViewDetailClick = {
            findNavController().navigate(R.id.navi_travel_request_detailpage)
        })
        binding.completedRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        binding.completedRecyclerview.adapter = rvAdapter

        // Update data and notify adapter
        val newData = ProgressData()
        progressList.addAll(newData)
        rvAdapter.notifyDataSetChanged()
    }
}
private fun ProgressData(): List<InProgressResponse.Data> {
    val data = mutableListOf<InProgressResponse.Data>()
    data.add(
        InProgressResponse.Data(
            "TR-107",
            "International",
            "Singapore HR Tech Conference & Expo 24",
            "Business trip to SG to attend Singapore HR Technology Conference & Expo 24",
            "International",
            "Singapore",
            "12 - 14 Jun, 2024",
            "RM 5,000.00",
            "Approved","Stage 1: Travel Requisition Form"
        )
    )
    data.add(
        InProgressResponse.Data(
            "TR-108",
            "Domestic",
            "Singapore HR Tech Conference & Expo 24",
            "Business trip to SG to attend Singapore HR Technology Conference & Expo 24",
            "International",
            "Singapore",
            "12 - 14 Jun, 2024",
            "RM 5,000.00",
            "Rejected","Stage 1: Travel Requisition Form"
        )
    )
    data.add(
        InProgressResponse.Data(
            "TR-109",
            "International",
            "Singapore HR Tech Conference & Expo 24",
            "Business trip to SG to attend Singapore HR Technology Conference & Expo 24",
            "International",
            "Singapore",
            "12 - 14 Jun, 2024",
            "RM 5,000.00",
            "Approved","Stage 1: Travel Requisition Form"
        )
    )
    data.add(
        InProgressResponse.Data(
            "TR-110",
            "Domestic",
            "Singapore HR Tech Conference & Expo 24",
            "Business trip to SG to attend Singapore HR Technology Conference & Expo 24",
            "International",
            "Singapore",
            "12 - 14 Jun, 2024",
            "RM 5,000.00",
            "Rejected","Stage 1: Travel Requisition Form"
        )
    )
    // Add more data as needed
    return data
}
