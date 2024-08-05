package com.worksy.hr.art.views.adapter.claimRequestListing

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.worksy.hr.art.R
import com.worksy.hr.art.databinding.ClaimRequestPendingItemBinding
import com.worksy.hr.art.models.claimRequestResponse.GetClaimFormResponse
import com.worksy.hr.art.viewmodels.ClaimViewModel

class ClaimRequestApproveAdapter(
    val context: Context,
    private val type:String,
    private val viewmodel:ClaimViewModel,
    private var progressList: MutableList<GetClaimFormResponse>,
    private var onViewDetailClick: (position: Int) -> Unit
) : RecyclerView.Adapter<ClaimRequestApproveAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ClaimRequestPendingItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        var isLytRecyclerVisible: Boolean = false

        init {
            binding.rmValueTxt.setOnClickListener {
                toggleRecyclerVisibility()
            }
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

        fun bind(progressList: GetClaimFormResponse) {
            binding.claimProgressList = progressList
            binding.lytRecycler.visibility = if (isLytRecyclerVisible) View.VISIBLE else View.GONE
            binding.bottomView.visibility = if (isLytRecyclerVisible) View.VISIBLE else View.GONE


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ClaimRequestPendingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(progressList[0])
        holder.binding.apply {
            header.setOnClickListener {
                onViewDetailClick(position)
            }
            if (progressList[position].data.form.approvedItems.isNotEmpty()) {
                var getForm = progressList[0].data.form.approvedItems[position]
                totalTxt.text = getForm.groupName
                countTxt.text = getForm.itemCount
                rmValueTxt.text = getForm.currency + " " + getForm.total

                val rvAdapter = ClaimRequestDetailAdapter(context, progressList,type,position,null,viewmodel)
                recyclerview.layoutManager = LinearLayoutManager(context)
                recyclerview.adapter = rvAdapter
            }
        }
    }

    override fun getItemCount(): Int = progressList.size
}
