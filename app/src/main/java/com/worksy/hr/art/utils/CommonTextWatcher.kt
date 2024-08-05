package com.worksy.hr.art.utils

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView

class CommonTextWatcher(
    private val editText: AppCompatEditText,
    private val textView: AppCompatTextView,
    private val type: String,
    var  lengthBefore: Int =0,
    var minLength:Int? =null,
    var maxLength:Int?=null
) : TextWatcher {

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        //Nothing to do
    }



    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        val newText = s.toString()
        when (type) {
            "firstname", "lastname" -> {
                nameTextChange(newText)
            }
            "mobile" -> {
                mobileTextChange(newText,s)
            }

            "mobileemail" -> {
                mobileemailVerify(newText)

            }
            "password" -> {
                passwordTextChange(newText)
            }
            "empty_password" -> {
                emptyPasswordTextChange(newText)
            }
            "mobile_forgot" -> {
                mobileForgotTextChange(newText)

            }
        }



    }

    private fun mobileForgotTextChange(newText: String) {
        textView.visibility= View.GONE
        val zeropatterncheck = Regex("^0[0-9]*$")
        if (newText.matches(zeropatterncheck)) {
            textView.visibility = View.VISIBLE
            textView.text = "Mobile number shouldn't be start with zero"}
    }

    private fun emptyPasswordTextChange(newText: String) {
        if (newText.isEmpty()) {
            textView.visibility = View.VISIBLE
            textView.text = "Invalid password"
        } else {
            textView.visibility = View.GONE
        }

    }

    private fun passwordTextChange(newText: String) {
        if (newText.length !in 8..64 || !Validator.isValidPassword(newText)) {
            textView.visibility = View.VISIBLE
            textView.text = "Invalid password"
        } else {
            textView.visibility = View.GONE
        }

    }

    private fun mobileemailVerify(newText: String) {
        if (newText.isEmpty()) {
            textView.visibility = View.VISIBLE
            textView.text = "Invalid email/mobile number"
        } else {
            textView.visibility = View.GONE
        }
    }

    private fun mobileTextChange(newText: String, s: CharSequence?) {
        if (newText.isNotEmpty()) {
            textView.visibility = View.GONE
            val zeropatterncheck = Regex("^0[0-9]*$")
            if (newText.matches(zeropatterncheck)) {
                textView.visibility = View.VISIBLE
                textView.text = "Mobile number shouldn't be start with zero"
                editText.setText(s?.subSequence(1, s.length))
                editText.setSelection(editText.text!!.length)
            } else {
                val regexPattern = """^[0-9\s]+$"""
                if ((newText.length !in minLength!! + 2..maxLength!! + 3) || (!newText.matches(
                        Regex(regexPattern)
                    ))
                ) {
                    textView.visibility = View.VISIBLE
                    if (minLength != maxLength) {
                        textView.text =
                            "Mobile number should have " + minLength.toString() + " to " + maxLength.toString() + " digits"
                    } else {
                        textView.text =
                            "Mobile number should have " + minLength.toString() + " digits"
                    }
                }
            }
        }

    }

    private fun nameTextChange(newText: String) {
        textView.visibility = View.GONE
        if(newText.length>=50){
            textView.visibility = View.VISIBLE
            textView.text = "Maximum 50 letters"
        }
        if (newText.isNotEmpty() && !newText.matches(Regex("[A-Za-z ]+"))) {
            textView.visibility = View.VISIBLE
            textView.text = "Only alphabets and spaces are allowed"
        }
    }


    override fun afterTextChanged(s: Editable?) {
        when (type) {
            "mobile" -> {
                mobileTextAfterChange(s)
            }
            "email" -> {
                emailTextAfterChange(s)
            }
        }
    }

    private fun emailTextAfterChange(s: Editable?) {
        if (s.toString().isNotEmpty() && !isEmailValid(s.toString())) {
            textView.visibility = View.VISIBLE
            textView.text = "Invalid email address"
        } else {
            textView.visibility = View.GONE
        }

    }

    private fun mobileTextAfterChange(s: Editable?) {
        val lengthBefore = s?.length

        if (lengthBefore != null) {
            val insertionPoints = arrayOf(2, 6, 11)

            for (point in insertionPoints) {
                if (s.length > point && s[point].isDigit()) {
                    s.insert(point, " ")
                }
            }
        }
    }
    private fun isEmailValid(email: String): Boolean {
        val pattern = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
        return email.matches(pattern)
    }
}