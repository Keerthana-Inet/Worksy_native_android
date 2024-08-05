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
import com.worksy.hr.art.databinding.FragmentClaimRejectBinding
import com.worksy.hr.art.models.claimApproval.ClaimApprovalDetailResponse
import com.worksy.hr.art.models.claimApproval.ClaimPendingResponse
import com.worksy.hr.art.viewmodels.ClaimViewModel
import com.worksy.hr.art.views.adapter.claimApproval.ClaimRejectAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClaimReject : Fragment(R.layout.fragment_claim_reject){
    private lateinit var rvAdapter: ClaimRejectAdapter
    private var progressList: MutableList<ClaimPendingResponse> = mutableListOf()
    private var _binding: FragmentClaimRejectBinding?=null
    private val binding get() = _binding!!
    var checkoxSelection:Boolean=false
    private lateinit var context: Context
    private val viewModel : ClaimViewModel by activityViewModels()
    private  var getClaimFormResponse: ClaimApprovalDetailResponse?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentClaimRejectBinding.inflate(inflater, container, false)
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
       /* // Notify parent fragment
        binding.root.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                binding.root.viewTreeObserver.removeOnGlobalLayoutListener(this)
                (parentFragment as? ClaimApprovalDetailFragment)?.adjustViewPagerHeight(2)
            }
        })*/
        val gson = Gson()
        if(viewModel.ApprovalJsonString!=null){
            getClaimFormResponse = gson.fromJson(
                viewModel.ApprovalJsonString!!.toString(),
                ClaimApprovalDetailResponse::class.java
            )}
        // Extract the form list from the response
       claimRejectData(getClaimFormResponse)
    }

    private fun claimRejectData(getClaimFormResponse: ClaimApprovalDetailResponse?) {
        if (getClaimFormResponse!=null && getClaimFormResponse!!.data != null) {
            if (getClaimFormResponse!!.data.form.rejectedItems.isNotEmpty()) {
                val formList: MutableList<ClaimApprovalDetailResponse> =
                    mutableListOf(getClaimFormResponse!!)
                binding.pendingRecyclerview.layoutManager = LinearLayoutManager(requireContext())
                rvAdapter = ClaimRejectAdapter(requireContext(), formList, "Reject",onViewDetailClick = {

                }).apply {
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
        claimRejectData(getClaimFormResponse)
    }
}

