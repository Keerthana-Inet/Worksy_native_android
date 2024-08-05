package com.worksy.hr.art.views.adapter.travelRequest

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.worksy.hr.art.databinding.AdapterClaimAddItemBinding
import com.worksy.hr.art.models.travelRequest.ClaimItemResponse


class ClaimItemAdapter(
    val context: Context,
    private var claimItem: MutableList<ClaimItemResponse>,
    private var onViewDetailClick:(position:Int)->Unit
) : RecyclerView.Adapter<ClaimItemAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: AdapterClaimAddItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(claimItem: ClaimItemResponse) {
            binding.claimItem = claimItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            AdapterClaimAddItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = claimItem[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int {
        return claimItem.size
    }

}