package com.worksy.hr.art.views.adapter.claimApproval

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.worksy.hr.art.R
import com.worksy.hr.art.databinding.ClaimapprovalPendingItemBinding
import com.worksy.hr.art.models.claimApproval.ClaimApprovalDetailResponse


class ClaimRejectAdapter(
    val context: Context,
    private var progressList: MutableList<ClaimApprovalDetailResponse>,
    private val type:String,
    private var onViewDetailClick: (position: Int) -> Unit,
) : RecyclerView.Adapter<ClaimRejectAdapter.ViewHolder>() {
    var isSelectionMode = false
    inner class ViewHolder(val binding: ClaimapprovalPendingItemBinding) :
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
            } else {0
                ContextCompat.getDrawable(context, R.drawable.ic_down_arrow)?.apply {
                    setTint(ContextCompat.getColor(context, R.color.text_gray))
                }
            }
            binding.rmValueTxt.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, drawable, null)
        }
        fun bind(progressList: ClaimApprovalDetailResponse) {
            binding.claimProgressList = progressList
            binding.lytRecycler.visibility = if (isLytRecyclerVisible) View.VISIBLE else View.GONE
            binding.bottomView.visibility = if (isLytRecyclerVisible) View.VISIBLE else View.GONE
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ClaimapprovalPendingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(progressList[0])
        holder.binding.apply {
            header.setOnClickListener {
                onViewDetailClick(position)
            }
            if (progressList[0].data.form.rejectedItems.isNotEmpty()) {
                var getForm = progressList[0].data.form.rejectedItems[position]
                groupName.text = getForm.groupName
                countTxt.text = getForm.itemCount
                rmValueTxt.text = getForm.currency + " " + getForm.total

                if (progressList[0].data.form.rejectedItems[position].items.isNotEmpty()) {
                    val rvAdapter =
                        ClaimPendingDetailListAdapter(
                            context,
                            progressList,
                            null,
                            type,
                            position,
                            onViewDetailClick
                        )
                    recyclerview.layoutManager = LinearLayoutManager(context)
                    recyclerview.adapter = rvAdapter
                }
            }
        }
    }

    override fun getItemCount(): Int = progressList.size
}
