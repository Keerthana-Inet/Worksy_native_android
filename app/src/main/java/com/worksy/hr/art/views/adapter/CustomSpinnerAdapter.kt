package com.worksy.hr.art.views.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.worksy.hr.art.R

data class SpinnerItem(val text: String)
class CustomSpinnerAdapter(
    context: Context,
    private val resource: Int,
    private val items: List<SpinnerItem>
) : ArrayAdapter<SpinnerItem>(context, resource, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createItemView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createItemView(position, convertView, parent)
    }

    private fun createItemView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(resource, parent, false)
        val item = items[position]

        val text = view.findViewById<TextView>(R.id.spinner_txt_item)

        text.text = item.text

        return view
    }
}