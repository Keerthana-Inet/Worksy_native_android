package com.worksy.hr.art.views.adapter.claimApproval

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.worksy.hr.art.databinding.ClaimApprovalListItemBinding
import com.worksy.hr.art.models.claimApproval.ClaimApprovalProgressResponse
import com.worksy.hr.art.viewmodels.ClaimViewModel

class CompletedClaimApprovalAdapter(
    val context: Context,
    private var progressList: List<ClaimApprovalProgressResponse.ClaimApprovalData.ClaimEmployee>,
    private val viewModel: ClaimViewModel,
    private val navController: NavController,
    private var onViewDetailClick:(position:Int)->Unit
) : RecyclerView.Adapter<CompletedClaimApprovalAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ClaimApprovalListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(progressList: ClaimApprovalProgressResponse.ClaimApprovalData.ClaimEmployee) {
            binding.claimApprovalList = progressList
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ClaimApprovalListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = progressList[position]
        holder.bind(data)
        if(viewModel.isListView.equals(true)){
            holder.binding.approvalListRecyclerview.visibility=View.VISIBLE
        }
        holder.binding.apply {
            header.setOnClickListener {
                onViewDetailClick(position)
            }
            userName.text=progressList[position].name
            rmValue.text=progressList[position].currency+" "+progressList[position].total
            userPosition.text=progressList[position].position
        }
        Log.d("DataArrives", "DataArrives: "+progressList[position])
        var isRecyclerView:Boolean=false
        holder.binding.totalClaim.setOnClickListener {
            if(isRecyclerView.equals(false)){
                holder.binding.approvalListRecyclerview.visibility=View.VISIBLE
                isRecyclerView=true
            }else if(isRecyclerView.equals(true)){
                holder.binding.approvalListRecyclerview.visibility=View.GONE
                isRecyclerView=false
            }
        }
        holder.binding.countTxt.setOnClickListener {
            if(isRecyclerView.equals(false)){
                holder.binding.approvalListRecyclerview.visibility=View.VISIBLE
                isRecyclerView=true
            }else if(isRecyclerView.equals(true)){
                holder.binding.approvalListRecyclerview.visibility=View.GONE
                isRecyclerView=false
            }
        }
        holder.binding.rmValue.setOnClickListener {
            if(isRecyclerView.equals(false)){
                holder.binding.approvalListRecyclerview.visibility=View.VISIBLE
                isRecyclerView=true
            }else if(isRecyclerView.equals(true)){
                holder.binding.approvalListRecyclerview.visibility=View.GONE
                isRecyclerView=false
            }
        }
        holder.binding.userName.setOnClickListener {
            onViewDetailClick.invoke(position)
        }
        holder.binding.userImg.setOnClickListener {
            onViewDetailClick.invoke(position)
        }
        holder.binding.userPosition.setOnClickListener {
            onViewDetailClick.invoke(position)
        }
        var rvAdapter: ApprovalListItemAdapter
        val claimApprovalList = progressList[position].forms
        rvAdapter = ApprovalListItemAdapter(context, claimApprovalList,viewModel,navController,onViewDetailClick = {

        })
        holder.binding.approvalListRecyclerview.layoutManager = LinearLayoutManager(context)
        holder.binding.approvalListRecyclerview.adapter = rvAdapter
    }

    override fun getItemCount(): Int {
        return progressList.size
    }

}
