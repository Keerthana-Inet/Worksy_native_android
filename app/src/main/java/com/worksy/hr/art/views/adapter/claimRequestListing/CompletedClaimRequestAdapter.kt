package com.worksy.hr.art.views.adapter.claimRequestListing

import android.content.Context
import android.content.res.ColorStateList
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.worksy.hr.art.R
import com.worksy.hr.art.databinding.ClaimRequestInprogressItemBinding
import com.worksy.hr.art.models.claimRequestResponse.ClaimRequestListResponse

class CompletedClaimRequestAdapter(
    val context: Context,
    private var progressList: List<ClaimRequestListResponse.ClaimRequestData.ClaimForm>,
    private var onViewDetailClick:(position:Int,code:String,title:String,desc:String?,formId:String)->Unit
) : RecyclerView.Adapter<CompletedClaimRequestAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ClaimRequestInprogressItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(progressList: ClaimRequestListResponse.ClaimRequestData.ClaimForm) {
            binding.claimProgressList = progressList
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ClaimRequestInprogressItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var isLytRecyclerVisible:Boolean=false
        val data = progressList[position]
        holder.bind(data)
        var claimREquestCode=data.code
        var codeTitle=data.title
        var codeDesc=data.description
        var formId=data.id
        holder.binding.header.setOnClickListener {
            onViewDetailClick.invoke(position,claimREquestCode,codeTitle,codeDesc,formId)
        }
        Log.d("Dataaaa", "Dataaaa: "+data)
        holder.binding.status.text=data.status
        holder.binding.countTxt.text=data.total
        holder.binding.trId.text=data.code
        holder.binding.dateTxt.text=data.date
        holder.binding.titleTxt.text=data.title
        holder.binding.subtitleTxt.text=data.description

        if(data.status.equals("Approved")){
            holder.binding.status.background = ContextCompat.getDrawable(context, R.drawable.blue_background_border)
            holder.binding.status.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.background_green))
            val drawable = ContextCompat.getDrawable(context, R.drawable.getting_ready_icon)
            drawable?.setTint(ContextCompat.getColor(context, R.color.icon_green)) // Change tint color here
            holder.binding.status.setCompoundDrawablesRelativeWithIntrinsicBounds(
                drawable, null, null, null
            )
            holder.binding.status.setTextColor(ContextCompat.getColor(context, R.color.text_dark_green))
        }else if(data.status.equals("Rejected")){
            holder.binding.status.background = ContextCompat.getDrawable(context, R.drawable.blue_background_border)
            holder.binding.status.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.background_red))
            val drawable = ContextCompat.getDrawable(context, R.drawable.getting_ready_icon)
            drawable?.setTint(ContextCompat.getColor(context, R.color.icon_red)) // Change tint color here
            holder.binding.status.setCompoundDrawablesRelativeWithIntrinsicBounds(
                drawable, null, null, null
            )
            holder.binding.status.setTextColor(ContextCompat.getColor(context, R.color.text_dark_red))
        }
        holder.binding.totalLyt.setOnClickListener {
            if (isLytRecyclerVisible.equals(false)) {
                holder.binding.lytRecycler.visibility = View.VISIBLE
                val drawable = ContextCompat.getDrawable(context, R.drawable.up_arrow)
                holder.binding.rmValueTxt.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    null, null, drawable, null
                )
                holder.binding.bottomView.visibility=View.VISIBLE
                isLytRecyclerVisible = true
            }else if(isLytRecyclerVisible.equals(true)){
                holder.binding.lytRecycler.visibility=View.GONE
                val drawable = ContextCompat.getDrawable(context, R.drawable.ic_down_arrow)
                drawable?.setTint(ContextCompat.getColor(context, R.color.text_gray))
                holder.binding.rmValueTxt.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    null, null, drawable, null
                )
                isLytRecyclerVisible=false
                holder.binding.bottomView.visibility=View.GONE
            }
        }

        var rvAdapter: TotalClaimRequestAdapter


        rvAdapter = TotalClaimRequestAdapter(context, progressList[position].items) {

        }
        holder.binding.totalClaimRecyclerview.layoutManager = LinearLayoutManager(context)
        holder.binding.totalClaimRecyclerview.adapter = rvAdapter
    }

    override fun getItemCount(): Int {
        return progressList.size
    }

}