package com.worksy.hr.art.views.fragments.travelRequest

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.worksy.hr.art.R
import com.worksy.hr.art.databinding.FragmentTravelRequestTab4Binding
import com.worksy.hr.art.databinding.LayoutAddClaimBinding
import com.worksy.hr.art.models.travelRequest.ClaimItemResponse
import com.worksy.hr.art.views.adapter.travelRequest.ClaimItemAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TravelRequestTab4 : Fragment(R.layout.fragment_travel_request_tab4) {

    private var rvAdapter: ClaimItemAdapter?=null
    private var _binding: FragmentTravelRequestTab4Binding?=null
    private val binding get() = _binding!!
    private lateinit var context: Context
    private val claimItem = mutableListOf<ClaimItemResponse>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTravelRequestTab4Binding.inflate(inflater, container, false)
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
        claimItem.clear()
        binding.apply {
            val items = listOf("Flight")
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
            budgetItemLyt.setOnClickListener {
                showBottomSheetDialog(binding)
            }
            claimItem.add(
                ClaimItemResponse("Accommodation","RM 1,500.00","Custom Field","Peak Season")
            )
            claimItem.add(
                ClaimItemResponse("Flight","RM 1,500.00","Custom Field","Peak Season")
            )
            rvAdapter = ClaimItemAdapter(requireContext(), claimItem, onViewDetailClick = {

            })
            claimRecyclerview.layoutManager = LinearLayoutManager(requireContext())
            claimRecyclerview.adapter = rvAdapter
        }
    }
    private fun showBottomSheetDialog(travelRequest: FragmentTravelRequestTab4Binding) {
        var bottomSheetDialog: BottomSheetDialog? = null
        var bottomSheet:View?=null
        bottomSheetDialog = BottomSheetDialog(requireActivity(), R.style.BottomSheetDialog)
        val binding: LayoutAddClaimBinding = LayoutAddClaimBinding.inflate(layoutInflater)
        bottomSheetDialog!!.setContentView(binding.root)
        binding.apply {
            backImg.setOnClickListener {
                bottomSheetDialog.dismiss()
            }
            cancelBtn.setOnClickListener {
                bottomSheetDialog.dismiss()
            }
            addBtn.setOnClickListener {
                bottomSheetDialog.dismiss()
                travelRequest.relSpinner.visibility=View.GONE
                travelRequest.claimRecyclerview.visibility=View.VISIBLE
            }
            val items = listOf("Accommodation")
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
        }
        bottomSheetDialog!!.setOnShowListener { _ ->
            // Adjust the BottomSheet behavior to a custom peek height
            bottomSheet = bottomSheetDialog?.findViewById<View>(
                com.google.android.material.R.id.design_bottom_sheet,
            )
            if (bottomSheet != null) {
                val behavior = BottomSheetBehavior.from(bottomSheet!!)

                // Calculate the peek height as 80% of the screen height
                val screenHeight = Resources.getSystem().displayMetrics.heightPixels
                val customPeekHeight = (screenHeight * 0.99).toInt()

                behavior.peekHeight = customPeekHeight
                behavior.state = BottomSheetBehavior.STATE_COLLAPSED
                Log.d("BottomSheet", "Calculated Peek Height: $customPeekHeight")
                Log.d("BottomSheet", "Actual Height: ${bottomSheet?.height}")
            // You can use STATE_EXPANDED if you want it fully open by default
            } else {
                // Handle the case where the bottomSheet is null (optional)
            }
        }
        bottomSheetDialog!!.show()
    }
}