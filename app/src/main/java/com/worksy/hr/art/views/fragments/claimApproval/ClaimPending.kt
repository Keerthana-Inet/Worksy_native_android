package com.worksy.hr.art.views.fragments.claimApproval

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.worksy.hr.art.R
import com.worksy.hr.art.databinding.FragmentClaimPendingBinding
import com.worksy.hr.art.models.claimApproval.ClaimApprovalDetailResponse
import com.worksy.hr.art.models.claimApproval.ClaimPendingResponse
import com.worksy.hr.art.repository.data.localdatabse.SharedPrefManager
import com.worksy.hr.art.viewmodels.ClaimViewModel
import com.worksy.hr.art.views.adapter.claimApproval.ClaimPendingAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ClaimPending : Fragment(R.layout.fragment_claim_pending), ClaimPendingAdapter.SelectionModeListener{
    private lateinit var rvAdapter: ClaimPendingAdapter
    private var progressList: MutableList<ClaimPendingResponse> = mutableListOf()
    private var _binding: FragmentClaimPendingBinding?=null
    private val binding get() = _binding!!
    var checkoxSelection:Boolean=false
    private lateinit var context: Context
    private val viewModel : ClaimViewModel by activityViewModels()
    private  var claimApprovalResponse: ClaimApprovalDetailResponse?=null
    var totalAmount=""
    var id=""
    companion object {
        private const val REQUEST_CODE_STORAGE_PERMISSION = 100
    }

    @Inject
    lateinit var appPref:SharedPrefManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentClaimPendingBinding.inflate(inflater, container, false)
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
        val gson = Gson()
        if(viewModel.ApprovalJsonString!=null) {
             claimApprovalResponse = gson.fromJson(
                viewModel.ApprovalJsonString.toString(),
                ClaimApprovalDetailResponse::class.java
            )
        }
        // Extract the form list from the response
        claimPendingData(claimApprovalResponse)
        binding.headerLyt.setOnClickListener {
            if(checkoxSelection.equals(false)) {
                rvAdapter.selectAll()
                binding.checkbox.isChecked = true
                checkoxSelection=true
            }else{
                rvAdapter.deselectAll()
                binding.checkbox.isChecked = false
                checkoxSelection=false
            }
        }
        binding.deSelect.setOnClickListener {
            rvAdapter.deselectAll()
            binding.checkbox.isChecked = false
            checkoxSelection=false
            viewModel.clearSelections()
        }
    }

    private fun claimPendingData(claimApprovalResponse: ClaimApprovalDetailResponse?) {
        if(claimApprovalResponse !=null && claimApprovalResponse!!.data!=null) {
            if (this.claimApprovalResponse!!.data.form.pendingItems.isNotEmpty()) {
                val formList: MutableList<ClaimApprovalDetailResponse> =
                    mutableListOf(this.claimApprovalResponse!!)
                if(!formList.isNullOrEmpty()) {
                    binding.handTxt.visibility=View.VISIBLE
                    binding.pendingRecyclerview.layoutManager =
                        LinearLayoutManager(requireContext())
                    rvAdapter = ClaimPendingAdapter(
                        requireContext(),
                        appPref,
                        formList,
                        viewModel,
                        "Pending",
                        onViewDetailClick = {
                        }) {
                        requestStoragePermission()
                    }.apply {
                        selectionModeListener = this@ClaimPending
                    }
                    binding.pendingRecyclerview.adapter = rvAdapter
                    binding.pendingRecyclerview.visibility = View.VISIBLE
                }else{
                    binding.handTxt.visibility=View.GONE
                }
            } else {
                binding.pendingRecyclerview.visibility = View.GONE
                binding.handTxt.visibility=View.GONE
            }
        }
        else {
            binding.pendingRecyclerview.visibility = View.GONE
            binding.handTxt.visibility=View.GONE
        }
    }
    fun requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CODE_STORAGE_PERMISSION)
        } else {
            // Permission is already granted, proceed with the action
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_STORAGE_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with the action
            } else {
                // Permission denied, show a message to the user
            }
        }
    }
    override fun onSelectionModeChanged(isSelectionMode: Boolean) {
        binding.headerLyt.visibility = if (isSelectionMode) View.VISIBLE else View.GONE
        binding.selectedCount.visibility = if (isSelectionMode) View.VISIBLE else View.GONE
        binding.deSelect.visibility = if (isSelectionMode) View.VISIBLE else View.GONE
    }

    override fun onSelectionCount(selectedCount: Int, selectedID: String) {
        binding.selectedCount.text=selectedCount.toString()+" Selected"
    }
    override fun onResume() {
        super.onResume()
       claimPendingData(claimApprovalResponse)
    }
}


