package com.worksy.hr.art.views.adapter.claimRequestListing

import android.content.Context
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.worksy.hr.art.databinding.CustomFieldItemBinding
import com.worksy.hr.art.models.claimRequestResponse.AddClaimSettingsResponse
import com.worksy.hr.art.views.adapter.CustomAdapter


class CustomFieldAdapter(
    val context: Context,
    private var totalClaimList: AddClaimSettingsResponse.Data.Group,
    private var onViewDetailClick: (position: Int) -> Unit
) : RecyclerView.Adapter<CustomFieldAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: CustomFieldItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(progressList: AddClaimSettingsResponse.Data.Group) {
            binding.totalclaimList = progressList
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            CustomFieldItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = totalClaimList
        holder.bind(data)
        holder.binding.apply {
            val customFields = totalClaimList.items[position].fields.customFields
            if (customFields.isNotEmpty() && position < customFields.size) {
                val currentItem = customFields[position]

                customFields.forEach { customField ->
                    if (customField.title.isNotEmpty()) {
                        relCustomFieldTitle.visibility = View.VISIBLE
                        customTitle.text = customField.title
                        etTitle.hint = customField.key
                        setInputType(etTitle, customField.type)
                    } else {
                        relCustomFieldTitle.visibility = View.GONE
                    }
                    if (customField.options.isNotEmpty()) {
                        relOptions.visibility = View.VISIBLE
                        claimOptionSpinner(holder.binding, customField.options)
                    } else {
                        relOptions.visibility = View.GONE
                    }
                }

            } else {
                relCustomFieldTitle.visibility = View.GONE
                relOptions.visibility = View.GONE
            }
        }
    }

    private fun claimOptionSpinner(
        binding: CustomFieldItemBinding,
        options: List<AddClaimSettingsResponse.Data.Group.Item.Fields.CustomField.Option>
    ) {
        val spinCashAdapter = CustomAdapter(context, options.map { it.key })
        binding.relOptions.visibility = View.VISIBLE

        binding.optionsSpinner.adapter = spinCashAdapter
        binding.optionsSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Nothing to do if nothing selected
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {
                    spinCashAdapter.setSelectedPosition(position)
                    binding.apply {
                        // viewmodel.claimCurrency = options[position].key
                    }
                }
            }
    }
    private fun setInputType(editText: EditText, inputType: String) {
        when (inputType) {
            "number" -> editText.inputType = InputType.TYPE_CLASS_NUMBER
            "text" -> editText.inputType = InputType.TYPE_CLASS_TEXT
            "password" -> editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            "email" -> editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            "phone" -> editText.inputType = InputType.TYPE_CLASS_PHONE
            "multiline" -> editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
            else -> editText.inputType = InputType.TYPE_CLASS_TEXT // Default to text if inputType is unknown
        }
    }
    override fun getItemCount(): Int {
        return totalClaimList.items.size
    }
}
