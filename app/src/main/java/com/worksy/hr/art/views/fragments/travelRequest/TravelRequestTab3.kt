package com.worksy.hr.art.views.fragments.travelRequest

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.worksy.hr.art.R
import com.worksy.hr.art.databinding.FragmentTravelRequestTab3Binding
import com.worksy.hr.art.models.travelRequest.TravelRequestTab3Response
import com.worksy.hr.art.views.adapter.travelRequest.TravelRequestTab3Adapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TravelRequestTab3 : Fragment(R.layout.fragment_travel_request_tab3) {

    private var _binding: FragmentTravelRequestTab3Binding?=null
    private val binding get() = _binding!!
    private lateinit var context: Context
    private lateinit var rvAdapter: TravelRequestTab3Adapter
    private val travelList = mutableListOf<TravelRequestTab3Response.RequestData>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTravelRequestTab3Binding.inflate(inflater, container, false)
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
        travelList.clear()
        binding.apply {
            val items = listOf("Select User")

            val adapter = ArrayAdapter(requireActivity(), R.layout.simple_dropdown, items)
            adapter.setDropDownViewResource(R.layout.simple_dropdown)
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val selectedItem = items[position]

                    // Exclude hint text from processing
                    if (position != 0) {
                        Toast.makeText(requireActivity(), "Selected Item: $selectedItem", Toast.LENGTH_SHORT).show()
                        // Add your processing logic here for selected items
                    }
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Handle case where nothing is selected (optional)
                }
            }
            travelList.add(
              TravelRequestTab3Response.RequestData(R.drawable.userprofile,"Olivia Rhye","Senior Sales Executive")
            )
            travelList.add(
                TravelRequestTab3Response.RequestData(R.drawable.userprofile,"Anita Cruz","Sales Manager")
            )
            rvAdapter = TravelRequestTab3Adapter(requireContext(), travelList, onViewDetailClick = {

            })
            userRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            userRecyclerView.adapter = rvAdapter
        }
    }
}
