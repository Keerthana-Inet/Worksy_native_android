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

class ClaimApprovalAdapter(
    val context: Context,
    private var progressListData: MutableList<ClaimApprovalDetailResponse>,
    private val type:String,
    private var onViewDetailClick: (position: Int) -> Unit,
) : RecyclerView.Adapter<ClaimApprovalAdapter.ViewHolder>() {

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
            } else {
                ContextCompat.getDrawable(context, R.drawable.ic_down_arrow)?.apply {
                    setTint(ContextCompat.getColor(context, R.color.text_gray))
                }
            }
            binding.rmValueTxt.setCompoundDrawablesRelativeWithIntrinsicBounds(
                null,
                null,
                drawable,
                null
            )
        }

        fun bind(progressList: ClaimApprovalDetailResponse) {
            binding.claimProgressList = progressList
          /*  val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val pendingItem = progressList.data.form.approvedItems[position]
                // Set up child adapter
                val rvAdapter = ClaimPendingDetailListAdapter(
                    context,
                    progressListData,
                    null,
                    "Approve",
                    position,
                    onViewDetailClick,
                    requestStoragePermission
                )

                binding.recyclerview.layoutManager = LinearLayoutManager(context)
                binding.recyclerview.adapter = rvAdapter

                binding.groupName.text = pendingItem.groupName
                binding.countTxt.text = pendingItem.itemCount
                binding.rmValueTxt.text = "${pendingItem.currency} ${pendingItem.total}"
                binding.lytRecycler.visibility =
                    if (isLytRecyclerVisible) View.VISIBLE else View.GONE
                binding.bottomView.visibility =
                    if (isLytRecyclerVisible) View.VISIBLE else View.GONE
            }*/
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ClaimapprovalPendingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(progressListData[0])
        holder.binding.apply {
            if (progressListData[0].data.form.approvedItems.isNotEmpty()) {
                val getForm = progressListData[0].data.form.approvedItems[position]
                groupName.text = getForm.groupName
                countTxt.text = getForm.itemCount
                rmValueTxt.text = getForm.currency + " " + getForm.total

                if(type.equals("Approve")) {
                    if (progressListData[0].data.form.approvedItems[position].items.isNotEmpty()) {
                        detailAdapter(position,recyclerview,"Approve")
                    }
                }
            }
        }
    }

    private fun detailAdapter(position: Int, recyclerview: RecyclerView,type:String) {
        val rvAdapter = ClaimPendingDetailListAdapter(
            context,
            progressListData,
            null,
            type,
            position,
            onViewDetailClick
        )
        recyclerview.layoutManager = LinearLayoutManager(context)
        recyclerview.adapter = rvAdapter
    }

    override fun getItemCount(): Int = progressListData[0].data.form.approvedItems.size
}
