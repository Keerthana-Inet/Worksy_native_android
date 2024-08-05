package com.worksy.hr.art.views.adapter.claimRequestListing

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.worksy.hr.art.Constants
import com.worksy.hr.art.databinding.CreateFormDetailclaimItemBinding
import com.worksy.hr.art.models.claimRequestResponse.AddItemRequest
import com.worksy.hr.art.viewmodels.ClaimViewModel

class CreateFormClaimDetailAdapter(
    val context: Context,
    private var progressList: MutableList<AddItemRequest.PendingItem.Item> = mutableListOf(),
    private var viewModel: ClaimViewModel,
    private var onViewDetailClick:(position:Int,progressData:AddItemRequest.PendingItem.Item)->Unit
) : RecyclerView.Adapter<CreateFormClaimDetailAdapter.ViewHolder>() {
    var isSelectionMode = false
    private val selectedItems: MutableSet<Int> = HashSet()
    inner class ViewHolder(val binding: CreateFormDetailclaimItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(progressList: AddItemRequest.PendingItem.Item) {
            binding.claimProgressList = progressList
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            CreateFormDetailclaimItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
    fun getTotalRequestedAmount(): Double {
        var totalAmount = 0.0
        for (item in progressList) {
            totalAmount += item.requestedAmount.toDoubleOrNull() ?: 0.0
        }
        return totalAmount
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = progressList[position]
        if (data != null) {
            holder.bind(data)
        }
        holder.binding.apply {
            secondaryHeaderlyt.setOnClickListener {
                onViewDetailClick(position, progressList[position])
            }
            userName.text = progressList[position].claimItem
            rmValue.text = progressList[position].requestedAmount
            dateTxt.text = progressList[position].transactionData
            tag.text = progressList[position].tagName
            desc.text = progressList[position].reason
            if (progressList[position].fileUrls.isNotEmpty()) {
                viewModel.filePathString=progressList[position].filePath[0].toString()
                viewModel.fileSizeString=progressList[position].fileSize[0].toString()
                try {
                    Glide.with(context).load(progressList[position].fileUrls[0].toString())
                        .into(holder.binding.image1)
                } catch (e: Exception) {
                    Log.d("ExceptionHandling", "ExceptionHandling: " + e.message)
                }
            }else{
                Glide.with(context).load(Constants.IMG_PLACEHOLDER)
                    .into(holder.binding.image1)
            }

        }
    }
    override fun getItemCount(): Int {
        return progressList.size
    }

}