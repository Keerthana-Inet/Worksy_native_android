package com.worksy.hr.art.views.adapter.travelRequest

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.worksy.hr.art.databinding.TravelRequestUserAdapterBinding
import com.worksy.hr.art.models.travelRequest.TravelRequestTab3Response

class TravelRequestTab3Adapter(
    val context: Context,
    private var travelList: MutableList<TravelRequestTab3Response.RequestData>,
    private var onViewDetailClick:(position:Int)->Unit
) : RecyclerView.Adapter<TravelRequestTab3Adapter.ViewHolder>() {

    inner class ViewHolder(val binding: TravelRequestUserAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(travelList: TravelRequestTab3Response.RequestData) {
            binding.travelList = travelList
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            TravelRequestUserAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = travelList[position]
        holder.bind(data)
        try {
            Glide.with(context).load(travelList[position].image).into(holder.binding.userProfile)
        }catch (e:Exception){
            Log.d("ExceptionHandling", "ExceptionHandling: "+e.message.toString())
        }
        holder.binding.minus.setOnClickListener {
            onViewDetailClick.invoke(position)
            deleteItem(position)
        }
        if(travelList.size == position+1){
            holder.binding.divider.visibility= View.GONE
        }
    }

    private fun deleteItem(position: Int) {
        travelList.removeAt(position)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return travelList.size
    }

}
