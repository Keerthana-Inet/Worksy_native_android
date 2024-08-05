package com.worksy.hr.art.utils

import android.R
import android.content.Context
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner

class CustomSpinner(private val context: Context) {

     fun setupSpinner(spinner: Spinner, items: List<String>, selectedItem: String?) {
        val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        selectedItem?.let {
            val position = items.indexOf(it)
            if (position >= 0) spinner.setSelection(position)
        }
    }
}