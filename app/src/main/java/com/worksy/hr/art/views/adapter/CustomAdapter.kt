package com.worksy.hr.art.views.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.worksy.hr.art.R

class CustomAdapter(private val context: Context, private val data: List<String>) : BaseAdapter() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var selectedPosition = -1

    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(position: Int): Any {
        return data[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun setSelectedPosition(position: Int) {
        selectedPosition = position
        notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView
        val viewHolder: ViewHolder

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.custom_spinner_dropdown_item, parent, false)

            viewHolder = ViewHolder()
            viewHolder.textView = convertView.findViewById(R.id.textView)

            convertView.tag = viewHolder
        } else {
            viewHolder = convertView.tag as ViewHolder
        }

        viewHolder.textView.visibility= View.VISIBLE
        viewHolder.textView.text = data[position]


        return convertView!!
    }

    private class ViewHolder {
        lateinit var textView: TextView
    }
}
