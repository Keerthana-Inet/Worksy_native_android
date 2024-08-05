package com.worksy.hr.art.views.fragments.claimRequest

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.worksy.hr.art.R
import com.worksy.hr.art.databinding.FragmentClaimRequestPendingBinding
import com.worksy.hr.art.models.claimRequestResponse.GetClaimFormResponse
import com.worksy.hr.art.viewmodels.ClaimViewModel
import com.worksy.hr.art.views.adapter.claimRequestListing.ClaimRequestPendingAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClaimRequestPending() : Fragment(R.layout.fragment_claim_request_pending), ClaimRequestPendingAdapter.SelectionModeListener{
    private lateinit var rvAdapter: ClaimRequestPendingAdapter
    private var _binding: FragmentClaimRequestPendingBinding?=null
    private val binding get() = _binding!!
    var checkoxSelection:Boolean=false
    private lateinit var context: Context
    private val viewmodel: ClaimViewModel by activityViewModels()
    private  var getClaimFormResponse: GetClaimFormResponse?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentClaimRequestPendingBinding.inflate(inflater, container, false)
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
        var jsonStringData=viewmodel.jsonString
        Log.d("jsonStringData", "jsonStringData: "+jsonStringData)
        val gson = Gson()
        if(viewmodel.jsonString!=null) {
             getClaimFormResponse =
                gson.fromJson(viewmodel.jsonString.toString(), GetClaimFormResponse::class.java)
        }
        // Extract the form list from the response
       claimRequestPendingData(getClaimFormResponse)
    }

    private fun claimRequestPendingData(getClaimFormResponse: GetClaimFormResponse?) {
        if(getClaimFormResponse!=null && getClaimFormResponse!!.data!=null) {
            if (getClaimFormResponse!!.data.form.pendingItems.isNotEmpty()) {
                val formList: MutableList<GetClaimFormResponse> =
                    mutableListOf(getClaimFormResponse!!)

                if(!formList.isNullOrEmpty()) {
                    Log.d("FormList", formList.toString())
                    rvAdapter = ClaimRequestPendingAdapter(requireContext(), formList, "Pending",findNavController(),viewmodel,viewLifecycleOwner,requireActivity()) {
                        //  findNavController().navigate(R.id.navigation_approval)
                    }.apply {
                        selectionModeListener = this@ClaimRequestPending
                    }
                    binding.pendingRecyclerview.visibility = View.VISIBLE
                   // binding.handTxt.visibility=View.VISIBLE
                    binding.pendingRecyclerview.layoutManager =
                        LinearLayoutManager(requireContext())
                    binding.pendingRecyclerview.adapter = rvAdapter
                }else{
                    binding.handTxt.visibility=View.GONE
                }
            } else {
                binding.handTxt.visibility=View.GONE
                binding.pendingRecyclerview.visibility = View.GONE
            }
        }else {
            binding.handTxt.visibility=View.GONE
            binding.pendingRecyclerview.visibility = View.GONE
        }
    }

    override fun onSelectionCount(selectedCount: Int, selectedID: String) {
        binding.selectedCount.text=selectedCount.toString()+" Selected"
    }

    override fun onResume() {
        super.onResume()
        claimRequestPendingData(getClaimFormResponse)
    }

}


