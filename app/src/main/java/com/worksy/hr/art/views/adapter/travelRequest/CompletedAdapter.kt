package com.worksy.hr.art.views.adapter.travelRequest

import android.content.Context
import android.content.res.ColorStateList
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.worksy.hr.art.R
import com.worksy.hr.art.databinding.LayoutCompletedAdapterBinding
import com.worksy.hr.art.models.travelRequest.InProgressResponse


class CompletedAdapter(
    val context: Context,
    private var progressList: MutableList<InProgressResponse.Data>,
    private var onViewDetailClick:(position:Int)->Unit,
) : RecyclerView.Adapter<CompletedAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: LayoutCompletedAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(progressList: InProgressResponse.Data) {
            binding.completedList = progressList
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            LayoutCompletedAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = progressList[position]
        holder.bind(data)
        holder.binding.userdetailLyt.setOnClickListener {
            onViewDetailClick.invoke(position)
        }
        Log.d("Dataaaa", "Dataaaa: "+data)
        // Here you can append your data
        holder.binding.trId.text = data.trID
        holder.binding.trType.text = data.trType
        holder.binding.titleTxt.text = data.titletxt
        holder.binding.subtitleTxt.text = data.subTitle
        holder.binding.type.text = data.type
        holder.binding.destination.text = data.destination
        holder.binding.date.text = data.date
        holder.binding.budget.text = data.budget
        holder.binding.status.text = data.status

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
    }

    override fun getItemCount(): Int {
        return progressList.size
    }

}