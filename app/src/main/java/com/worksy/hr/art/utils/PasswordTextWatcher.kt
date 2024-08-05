package com.worksy.hr.art.utils

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView

class PasswordTextWatcher(
    private val editText: AppCompatEditText,
    private val textView: AppCompatTextView,
    private val type: String,
    private val passwd: AppCompatEditText,
    private val confirmpasswd: AppCompatEditText,
    private val textviewerror: AppCompatTextView
) : TextWatcher {

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        //Nothing to do
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        val newText = s.toString()
        if (type == "password") {
            println("confirmpasswd"+confirmpasswd.text.toString())
            textView.visibility = View.GONE
            textviewerror.visibility = View.GONE
            if (confirmpasswd.text.toString().isNotEmpty() && confirmpasswd.text.toString() != newText) {
                textviewerror.visibility = View.VISIBLE
                textviewerror.text = "Password mismatch"
            }
            if (newText.length !in 8..64 || !Validator.isValidPassword(newText)) {
                textView.visibility = View.VISIBLE
                textView.text = "Invalid password"
            }/* else {
                textView.visibility = View.GONE
                textviewerror.visibility = View.GONE
            }*/
        } else if (type == "confirmpassword") {
            if (passwd.text.toString().isNotEmpty() && passwd.text.toString() != newText) {
                textView.visibility = View.VISIBLE
                textView.text = "Password mismatch"
            } else if (newText.length !in 8..64 || !Validator.isValidPassword(newText)) {
                textView.visibility = View.VISIBLE
                textView.text = "Invalid password"
            } else {
                textView.visibility = View.GONE
            }
        }
    }

    override fun afterTextChanged(s: Editable?) {
        //Nothing to do
    }

}