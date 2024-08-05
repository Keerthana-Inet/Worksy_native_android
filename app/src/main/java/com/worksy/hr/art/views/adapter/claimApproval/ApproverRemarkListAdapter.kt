package com.worksy.hr.art.views.adapter.claimApproval

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.worksy.hr.art.databinding.ApproverRemarkItemBinding
import com.worksy.hr.art.models.claimApproval.ApproverRemarkListResponse

class ApproverRemarkListAdapter(
    val context: Context,
    private val approverFlowItems: List<ApproverRemarkListResponse>
) : RecyclerView.Adapter<ApproverRemarkListAdapter.ViewHolder>() {
    var isSelectionMode = false
    inner class ViewHolder(val binding: ApproverRemarkItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ApproverRemarkListResponse) {
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ApproverRemarkItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(approverFlowItems[position])
        holder.binding.apply {
            Glide.with(context)
                .load(approverFlowItems[position].imageUrl)
                .into(approverImg)
            entitlementTxt.text = approverFlowItems[position].entitlement

            val shapeAppearanceModel =  approverImg.shapeAppearanceModel.toBuilder()
                .setAllCornerSizes(40f) // Ensure fully circular shape
                .build()
            approverImg.shapeAppearanceModel = shapeAppearanceModel
            approverImg.strokeColor = ContextCompat.getColorStateList(context, approverFlowItems[position].colorResId)
            approverImg.strokeWidth = 2F // Set stroke width in pixel
            approverImg.setPadding(2, 2, 2, 2)
        }
    }

    override fun getItemCount(): Int = approverFlowItems.size
}
