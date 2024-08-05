package com.worksy.hr.art.views.fragments.claimRequest

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.worksy.hr.art.R
import com.worksy.hr.art.databinding.FragmentClaimRequestRejectTabBinding
import com.worksy.hr.art.models.claimApproval.ClaimPendingResponse
import com.worksy.hr.art.models.claimRequestResponse.GetClaimFormResponse
import com.worksy.hr.art.viewmodels.ClaimViewModel
import com.worksy.hr.art.views.adapter.claimRequestListing.ClaimRequestRejectAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClaimRequestRejectTab() : Fragment(R.layout.fragment_claim_request_reject_tab){
    private lateinit var rvAdapter: ClaimRequestRejectAdapter
    private var progressList: MutableList<ClaimPendingResponse> = mutableListOf()
    private var _binding: FragmentClaimRequestRejectTabBinding?=null
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
        _binding = FragmentClaimRequestRejectTabBinding.inflate(inflater, container, false)
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
        var jsonStringData=viewmodel.jsonString
        Log.d("jsonStringData", "jsonStringData: " + jsonStringData)
        val gson = Gson()
        if (viewmodel.jsonString != null) {
            getClaimFormResponse =
                gson.fromJson(viewmodel.jsonString.toString(), GetClaimFormResponse::class.java)
        }
        claimRequestRejectData(getClaimFormResponse)
    }

    private fun claimRequestRejectData(getClaimFormResponse: GetClaimFormResponse?) {
        if (getClaimFormResponse != null && getClaimFormResponse!!.data != null) {
            if (getClaimFormResponse!!.data.form.rejectedItems.isNotEmpty()) {
                val formList: MutableList<GetClaimFormResponse> =
                    mutableListOf(getClaimFormResponse!!)
                binding.pendingRecyclerview.layoutManager = LinearLayoutManager(requireContext())
                rvAdapter = ClaimRequestRejectAdapter(requireContext(), "Reject",viewmodel,formList) {

                }
                binding.pendingRecyclerview.adapter = rvAdapter
                binding.pendingRecyclerview.visibility = View.VISIBLE
            } else {
                binding.pendingRecyclerview.visibility = View.GONE
            }
        }else {
            binding.pendingRecyclerview.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        claimRequestRejectData(getClaimFormResponse)
    }
}

