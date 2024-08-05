package com.worksy.hr.art.views.adapter.claimRequestListing

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.worksy.hr.art.databinding.TotalClaimListItemBinding
import com.worksy.hr.art.models.claimApproval.ClaimApprovalProgressResponse
import com.worksy.hr.art.viewmodels.ClaimViewModel

class TotalClaimAdapter(
    val context: Context,
    private var totalClaimList: List<ClaimApprovalProgressResponse.ClaimApprovalData.ClaimEmployee.ClaimForm.ClaimItem>,
    private var viewModel:ClaimViewModel,
    private var onViewDetailClick:(position:Int)->Unit
) : RecyclerView.Adapter<TotalClaimAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: TotalClaimListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(progressList: ClaimApprovalProgressResponse.ClaimApprovalData.ClaimEmployee.ClaimForm.ClaimItem) {
            binding.totalclaimList = progressList
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            TotalClaimListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = totalClaimList[position]
        holder.bind(data)
        holder.binding.apply {
            totalItemCount.text=totalClaimList[position].count
            totalClaimItemTxt.text=totalClaimList[position].name
            rmValueTotalclaim.text=totalClaimList[position].currency+" "+totalClaimList[position].subtotal
        }
    }

    override fun getItemCount(): Int {
        return totalClaimList.size
    }

}