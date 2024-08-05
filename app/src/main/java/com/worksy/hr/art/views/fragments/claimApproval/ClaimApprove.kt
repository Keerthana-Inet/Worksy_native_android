package com.worksy.hr.art.views.fragments.claimApproval

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.worksy.hr.art.R
import com.worksy.hr.art.databinding.FragmentClaimApproveBinding
import com.worksy.hr.art.models.claimApproval.ClaimApprovalDetailResponse
import com.worksy.hr.art.models.claimApproval.ClaimPendingResponse
import com.worksy.hr.art.viewmodels.ClaimViewModel
import com.worksy.hr.art.views.adapter.claimApproval.ClaimApprovalAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClaimApprove : Fragment(R.layout.fragment_claim_approve){
    private lateinit var rvAdapter: ClaimApprovalAdapter
    private var progressList: MutableList<ClaimPendingResponse> = mutableListOf()
    private var _binding: FragmentClaimApproveBinding?=null
    private val binding get() = _binding!!
    private lateinit var context: Context
    private  var getClaimFormResponse: ClaimApprovalDetailResponse?=null
    private val viewModel : ClaimViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentClaimApproveBinding.inflate(inflater, container, false)
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
        val gson = Gson()
        if(viewModel.ApprovalJsonString!=null){
         getClaimFormResponse = gson.fromJson(
            viewModel.ApprovalJsonString!!.toString(),
            ClaimApprovalDetailResponse::class.java
        )}
        // Extract the form list from the response
        claimApproveData(getClaimFormResponse)
    }

    private fun claimApproveData(getClaimFormResponse: ClaimApprovalDetailResponse?) {
        if (getClaimFormResponse!=null && getClaimFormResponse!!.data != null) {
            if (getClaimFormResponse!!.data.form.approvedItems.isNotEmpty()) {
                val formList: MutableList<ClaimApprovalDetailResponse> =
                    mutableListOf(getClaimFormResponse!!)
                binding.pendingRecyclerview.layoutManager = LinearLayoutManager(requireActivity())
                rvAdapter = ClaimApprovalAdapter(requireActivity(), formList, "Approve",onViewDetailClick = {
                }).apply {

                }
                binding.pendingRecyclerview.adapter = rvAdapter
                binding.pendingRecyclerview.visibility = View.VISIBLE
                rvAdapter.notifyDataSetChanged()
            } else {
                binding.pendingRecyclerview.visibility = View.GONE
            }
        }else {
            binding.pendingRecyclerview.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        claimApproveData(getClaimFormResponse)
    }
}

