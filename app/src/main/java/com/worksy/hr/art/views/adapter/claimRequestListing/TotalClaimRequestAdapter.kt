package com.worksy.hr.art.views.adapter.claimRequestListing

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.worksy.hr.art.databinding.TotalClaimRequestItemBinding
import com.worksy.hr.art.models.claimRequestResponse.ClaimRequestListResponse

class TotalClaimRequestAdapter(
    val context: Context,
    private var totalClaimList: List<ClaimRequestListResponse.ClaimRequestData.ClaimForm.ClaimItem>,
    private var onViewDetailClick: (position: Int) -> Unit
) : RecyclerView.Adapter<TotalClaimRequestAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: TotalClaimRequestItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(progressList: ClaimRequestListResponse.ClaimRequestData.ClaimForm.ClaimItem) {
            binding.totalclaimList = progressList
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            TotalClaimRequestItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = totalClaimList[position]
        holder.bind(data)
        holder.binding.totalItemCount.text=data.count
        holder.binding.totalClaimItemTxt.text=data.name
        holder.binding.rmValueTotalclaim.text=data.currency+" "+data.subtotal
    }

    override fun getItemCount(): Int {
        return totalClaimList.size
    }

}