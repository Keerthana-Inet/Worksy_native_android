package com.worksy.hr.art.views.adapter.claimApproval

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.worksy.hr.art.R
import com.worksy.hr.art.databinding.ClaimApprovalInprogressItemBinding
import com.worksy.hr.art.models.claimApproval.ClaimApprovalProgressResponse
import com.worksy.hr.art.viewmodels.ClaimViewModel
import com.worksy.hr.art.views.adapter.claimRequestListing.TotalClaimAdapter

class ApprovalListItemAdapter(
    val context: Context,
    private var progressList: List<ClaimApprovalProgressResponse.ClaimApprovalData.ClaimEmployee.ClaimForm>,
    private var viewModel: ClaimViewModel,
    private val navController: NavController,
    private var onViewDetailClick:(position:Int)->Unit
) : RecyclerView.Adapter<ApprovalListItemAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ClaimApprovalInprogressItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(progressList: ClaimApprovalProgressResponse.ClaimApprovalData.ClaimEmployee.ClaimForm) {
            binding.claimProgressList = progressList
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ClaimApprovalInprogressItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var isLytRecyclerVisible: Boolean = false
        val data = progressList[position]
        holder.bind(data)
        holder.binding.root.setOnClickListener {
            val bundle= Bundle()
            bundle.putString("Code",progressList[position].code)
            bundle.putString("DetailPageTitle",progressList[position].title)
            bundle.putString("DetailPageDesc",progressList[position].description)
            bundle.putString("formId",progressList[position].id)
            navController.navigate(R.id.navi_claim_approval_detail,bundle)
        }
        holder.binding.apply {
            trId.text=progressList[position].code
            status.text=progressList[position].status
            dateTxt.text=progressList[position].date
            titleTxt.text=progressList[position].title
            subtitleTxt.text=progressList[position].description
            rmValueTxt.text=progressList[position].currency+" "+progressList[position].total
        }
        holder.binding.rmValueTxt.setOnClickListener {
            if (isLytRecyclerVisible.equals(false)) {
                holder.binding.lytRecycler.visibility = View.VISIBLE
                val drawable = ContextCompat.getDrawable(context, R.drawable.up_arrow)
                holder.binding.rmValueTxt.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    null, null, drawable, null
                )
                isLytRecyclerVisible = true
                holder.binding.bottomView.visibility= View.VISIBLE
            }else if(isLytRecyclerVisible.equals(true)){
                holder.binding.lytRecycler.visibility= View.GONE
                val drawable = ContextCompat.getDrawable(context, R.drawable.ic_down_arrow)
                drawable?.setTint(ContextCompat.getColor(context, R.color.text_gray))
                holder.binding.rmValueTxt.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    null, null, drawable, null
                )
                isLytRecyclerVisible=false
                holder.binding.bottomView.visibility= View.GONE
            }
        }
        if(progressList[position].items.isNotEmpty()) {
                var rvAdapter: TotalClaimAdapter
                val totalClaimList = progressList[position].items
                rvAdapter = TotalClaimAdapter(context, totalClaimList, viewModel, onViewDetailClick = {

                })
                holder.binding.totalClaimRecyclerview.layoutManager = LinearLayoutManager(context)
                holder.binding.totalClaimRecyclerview.adapter = rvAdapter
                var totalCount=0
                for(item in progressList[position].items){
                totalCount += item.count.toInt()
                }
                holder.binding.countTxt.text=totalCount.toString()
        }
    }

    override fun getItemCount(): Int {
        return progressList.size
    }

}