package com.worksy.hr.art.views.adapter.claimRequestListing

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.worksy.hr.art.R
import com.worksy.hr.art.databinding.CreateformClaimItemBinding
import com.worksy.hr.art.models.claimRequestResponse.AddItemRequest
import com.worksy.hr.art.viewmodels.ClaimViewModel

class CreateFormClaimAdapter(
    val context: Context,
    private var progressList: MutableList<AddItemRequest.PendingItem> = mutableListOf(),
    private val viewmodel: ClaimViewModel,
    private var onViewDetailClick:(position:Int,progressData:AddItemRequest.PendingItem,itemData:AddItemRequest.PendingItem.Item)->Unit
) : RecyclerView.Adapter<CreateFormClaimAdapter.ViewHolder>() {
    var isLytRecyclerVisible: Boolean = false
    var isSelectionMode = false
    private val selectedItems: MutableSet<Int> = HashSet()
    var itemTotalAmount:Double? =0.0
    interface SelectionModeListener {
        fun onSelectionModeChanged(isSelectionMode: Boolean)
        fun onSelectionCount(selectedCount: Int, selectedID:String)
    }
    inner class ViewHolder(val binding: CreateformClaimItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.rmValueTxt.setOnClickListener {
                toggleRecyclerVisibility()
            }
        }
        fun bind(progressListData: AddItemRequest.PendingItem) {
            binding.claimProgressList = progressListData
            binding.lytRecycler.visibility = if (isLytRecyclerVisible) View.VISIBLE else View.GONE
            binding.bottomView.visibility = if (isLytRecyclerVisible) View.VISIBLE else View.GONE

            // Set up child adapter
            val rvAdapter = CreateFormClaimDetailAdapter(context,progressListData.items,viewmodel,onViewDetailClick={
                    position,data->
            })
            binding.recyclerview.layoutManager = LinearLayoutManager(context)
            binding.recyclerview.adapter = rvAdapter

            // Calculate and update the total requested amount in login Viewmodel
            val totalAmount = rvAdapter.getTotalRequestedAmount()
            viewmodel.updateItemTotalAmount(totalAmount)
            itemTotalAmount=totalAmount

            // item count get from CreateFormClaimDetailAdapter
            val itemCount = rvAdapter.getItemCount()
            binding.countTxt.text = itemCount.toString()
        }
        private fun toggleRecyclerVisibility() {
            isLytRecyclerVisible = !isLytRecyclerVisible
            binding.lytRecycler.visibility = if (isLytRecyclerVisible) View.VISIBLE else View.GONE
            binding.bottomView.visibility = if (isLytRecyclerVisible) View.VISIBLE else View.GONE

            val drawable = if (isLytRecyclerVisible) {
                ContextCompat.getDrawable(context, R.drawable.up_arrow)
            } else {
                ContextCompat.getDrawable(context, R.drawable.ic_down_arrow)?.apply {
                    setTint(ContextCompat.getColor(context, R.color.text_gray))
                }
            }
            binding.rmValueTxt.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, drawable, null)
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            CreateformClaimItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var isLytRecyclerVisible: Boolean = false
        val data = progressList[position]
        holder.bind(data)

        holder.binding.apply {
            totalTxt.text = data.groupName

            header.setOnClickListener {
                onViewDetailClick(position,progressList[position],progressList[position].items[0])
            }
            rmValueTxt.text ="MYR "+itemTotalAmount.toString()
        }
        holder.binding.rmValueTxt.setOnClickListener {
            if (!isLytRecyclerVisible) {
                holder.binding.lytRecycler.visibility = View.VISIBLE
                val drawable = ContextCompat.getDrawable(context, R.drawable.up_arrow)
                holder.binding.rmValueTxt.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    null, null, drawable, null
                )
                isLytRecyclerVisible = true
                holder.binding.bottomView.visibility = View.VISIBLE
            } else {
                holder.binding.lytRecycler.visibility = View.GONE
                val drawable = ContextCompat.getDrawable(context, R.drawable.ic_down_arrow)
                drawable?.setTint(ContextCompat.getColor(context, R.color.text_gray))
                holder.binding.rmValueTxt.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    null, null, drawable, null
                )
                isLytRecyclerVisible = false
                holder.binding.bottomView.visibility = View.GONE
            }
        }

        var rvAdapter: CreateFormClaimDetailAdapter

        rvAdapter = CreateFormClaimDetailAdapter(context,data.items,viewmodel,onViewDetailClick={
                position,data->
            onViewDetailClick(position,progressList[0],data)
        })
        holder.binding.recyclerview.layoutManager = LinearLayoutManager(context)
        holder.binding.recyclerview.adapter = rvAdapter

        //this to calculate the item count in full list
        var totalItemCount=0
        var totalReqAmount=0.0
        for (i in 0 until itemCount){
            val adapter = CreateFormClaimDetailAdapter(context,progressList[i].items,viewmodel,onViewDetailClick={
                position,data->
            })
            totalItemCount+=adapter.getItemCount()
            totalReqAmount+=adapter.getTotalRequestedAmount()
        }
        viewmodel.updateItemTotalCount(totalItemCount)
        viewmodel.updateItemTotalAmount(totalReqAmount)
    }

    override fun getItemCount(): Int {
        return progressList.size
    }
}