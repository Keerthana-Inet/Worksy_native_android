package com.worksy.hr.art.views.fragments.travelRequest
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.worksy.hr.art.R
import com.worksy.hr.art.databinding.FragmentTravelRequestDetailPageBinding
import com.worksy.hr.art.models.travelRequest.ClaimItemResponse
import com.worksy.hr.art.views.activity.Homeactivity
import com.worksy.hr.art.views.adapter.CustomSpinnerAdapter
import com.worksy.hr.art.views.adapter.SpinnerItem
import com.worksy.hr.art.views.adapter.travelRequest.TravelRequestDetaiItemAdapter
import dagger.hilt.android.AndroidEntryPoint
@AndroidEntryPoint
class TravelRequestDetailPage : Fragment(R.layout.fragment_travel_request_detail_page) {
    private var rvAdapter: TravelRequestDetaiItemAdapter?=null
    private var _binding: FragmentTravelRequestDetailPageBinding?=null
    private val binding get() = _binding!!
    private lateinit var context: Context
    private val travelReqItem = mutableListOf<ClaimItemResponse>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTravelRequestDetailPageBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        (activity as? Homeactivity)?.setBottomNavigationVisibility(View.VISIBLE)
        // unbind view onDestroyView
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context=this.requireContext()
        (activity as? Homeactivity)?.setBottomNavigationVisibility(View.GONE)
        binding.apply {
            val items = listOf(SpinnerItem("Travel Requisition Form"))
            val adapter = CustomSpinnerAdapter(requireActivity(), R.layout.custom_spinner_item, items)
            spinner.adapter = adapter
            spinner.adapter = adapter
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selectedItem = items[position]
                    // Exclude hint text from processing
                    if (position != 0) {
                        Toast.makeText(
                            requireActivity(),
                            "Selected Item: $selectedItem",
                            Toast.LENGTH_SHORT
                        ).show()
                        // Add your processing logic here for selected items
                    }
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Handle case where nothing is selected (optional)
                }
            }
            travelReqItem.add(
                ClaimItemResponse("Accommodation","RM 1,500.00","Custom Field","Peak Season")
            )
            travelReqItem.add(
                ClaimItemResponse("Flight","RM 1,500.00","Custom Field","Peak Season")
            )
            rvAdapter = TravelRequestDetaiItemAdapter(requireContext(), travelReqItem, onViewDetailClick = {

            })
            travelReqRecyclerview.layoutManager = LinearLayoutManager(requireContext())
            travelReqRecyclerview.adapter = rvAdapter
        }
    }
}
