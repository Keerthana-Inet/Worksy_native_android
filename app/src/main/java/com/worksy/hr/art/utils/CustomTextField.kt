package com.worksy.hr.art.utils

import android.content.Context
import android.widget.EditText

class CustomTextField(
    private val context: Context
) {
    fun setupTextField(
        textField: EditText,
    ) {
        textField.apply {}
    }
    fun getText(textField: EditText): String {
        return textField.text.toString()
    }
}