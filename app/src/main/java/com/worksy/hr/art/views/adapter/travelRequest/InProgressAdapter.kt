package com.worksy.hr.art.views.adapter.travelRequest

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.worksy.hr.art.R
import com.worksy.hr.art.databinding.LayoutInprogressAdapterBinding
import com.worksy.hr.art.models.travelRequest.InProgressResponse


class InProgressAdapter(
    val context: Context,
    private var progressList: List<InProgressResponse.Data>,
    private var onViewDetailClick:(position:Int)->Unit
) : RecyclerView.Adapter<InProgressAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: LayoutInprogressAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(progressList: InProgressResponse.Data) {
            binding.progressList = progressList
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            LayoutInprogressAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = progressList[position]
        holder.bind(data)
        holder.binding.header.setOnClickListener {
            onViewDetailClick.invoke(position)
        }
        holder.binding.status.text=data.status
                if(data.status.equals("Created")){
                    holder.binding.status.background = ContextCompat.getDrawable(context, R.drawable.blue_background_border)
                    holder.binding.status.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.thin_gray))
                    val drawable = ContextCompat.getDrawable(context, R.drawable.getting_ready_icon)
                    drawable?.setTint(ContextCompat.getColor(context, R.color.text_gray)) // Change tint color here
                    holder.binding.status.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        drawable, null, null, null
                    )
                    holder.binding.status.setTextColor(ContextCompat.getColor(context,R.color.light_black_txt))
                }else if(data.status.equals("Pending")){
                    holder.binding.status.background = ContextCompat.getDrawable(context, R.drawable.blue_background_border)
                    holder.binding.status.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.thin_blue))
                    val drawable = ContextCompat.getDrawable(context, R.drawable.getting_ready_icon)
                    drawable?.setTint(ContextCompat.getColor(context, R.color.button_txt)) // Change tint color here
                    holder.binding.status.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        drawable, null, null, null
                    )
                    holder.binding.status.setTextColor(ContextCompat.getColor(context,R.color.button_txt))
                }
    }

    override fun getItemCount(): Int {
        return progressList.size
    }

}