package com.worksy.hr.art.views.adapter.company

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.worksy.hr.art.R
import com.worksy.hr.art.databinding.CompanyListItemBinding
import com.worksy.hr.art.models.companies.ListCompaniesResponse

class CompanyListAdapter(
    private val context: Context,
    private var companyList: List<ListCompaniesResponse.CompanyData>,
    private val onViewDetailClick: (position: Int,accountId:Int) -> Unit
) : RecyclerView.Adapter<CompanyListAdapter.ViewHolder>() {

    var selectedPosition = -1

    inner class ViewHolder(val binding: CompanyListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(companyData: ListCompaniesResponse.CompanyData, isSelected: Boolean) {

            if (isSelected) {
                binding.relCompanyA.background = ContextCompat.getDrawable(context, R.drawable.selected_bg_filter)
                binding.companyTxt.setTextColor(ContextCompat.getColor(context, R.color.button_txt))
                binding.cmpnyCheckbox.visibility= View.VISIBLE
            } else {
                binding.relCompanyA.background = ContextCompat.getDrawable(context, R.drawable.bg_filter)
                binding.companyTxt.setTextColor(ContextCompat.getColor(context, R.color.light_black_txt))
                binding.cmpnyCheckbox.visibility= View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CompanyListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = companyList[position]
        holder.bind(data, position == selectedPosition)
        holder.binding.companyTxt.text = companyList[position].name
        var accountId=companyList[position].account_id
        holder.binding.relCompanyA.setOnClickListener {
            val previousSelectedPosition = selectedPosition
            selectedPosition = position // Use 'position' directly here
            notifyItemChanged(previousSelectedPosition)
            notifyItemChanged(selectedPosition)
            onViewDetailClick(position, companyList[position].account_id)
        }
    }

    override fun getItemCount(): Int {
        return companyList.size
    }

    fun updateCompanyList(newList: List<ListCompaniesResponse.CompanyData>) {
        companyList = newList
        notifyDataSetChanged()
    }
}
