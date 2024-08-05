package com.worksy.hr.art.views.adapter.claimRequestListing

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.worksy.hr.art.databinding.MultipleImageItemBinding
import com.worksy.hr.art.models.claimRequestResponse.GetClaimFormResponse

class ClaimImageAdapter(
    val context: Context,
    private var progressList: MutableList<GetClaimFormResponse>
) : RecyclerView.Adapter<ClaimImageAdapter.ViewHolder>() {
    var isSelectionMode = false
    inner class ViewHolder(val binding: MultipleImageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(progressList: GetClaimFormResponse) {
            binding.claimProgressList = progressList
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MultipleImageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(progressList[position])
    }

    override fun getItemCount(): Int = progressList.size
}
