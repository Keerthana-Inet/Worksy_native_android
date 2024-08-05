package com.worksy.hr.art.utils

import android.text.Editable
import android.text.InputFilter
import android.text.Spanned
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import java.util.regex.Pattern

class NumberTextWatcherWithoutMinimum(private val editText: EditText, private val maxDigitsBeforeDecimal: Int, private val maxDigitsAfterDecimal: Int,private val tvError:TextView) : TextWatcher, InputFilter {

    private val decimalPattern = "^(\\d{0,$maxDigitsBeforeDecimal})(\\.\\d{0,$maxDigitsAfterDecimal})?\$"
    private val pattern = Pattern.compile(decimalPattern)

    override fun filter(
        source: CharSequence?,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        val builder = StringBuilder(dest)
        builder.replace(dstart, dend, source?.subSequence(start, end).toString())

        val matcher = pattern.matcher(builder.toString().replace("[^\\d.]".toRegex(), ""))

        if (!matcher.matches()) {
            tvError.visible()
            return ""
        }else{
            tvError.gone()
        }
        return null
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        //Nothing to do
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        //Nothing to do
    }

    override fun afterTextChanged(s: Editable?) {
        editText.removeTextChangedListener(this)

        val originalText = s.toString().replace(",", "")

        val formattedText = formatNumberWithCommas(originalText)

        // Apply the formatted text to the EditText
        editText.setText(formattedText)

        // Set the cursor position to the end of the text
        editText.setSelection(formattedText.length)

        // Reapply the TextWatcher
        editText.addTextChangedListener(this)


    }


    private fun formatNumberWithCommas(number: String): String {
        if (number.isEmpty() || number == ".") {
            return number
        }

        val decimalIndex = number.indexOf('.')
        val wholePart = if (decimalIndex != -1) number.substring(0, decimalIndex) else number
        val decimalPart = if (decimalIndex != -1) number.substring(decimalIndex) else ""

        val formattedBuilder = StringBuilder()
        val length = wholePart.length
        for (i in 0 until length) {
            formattedBuilder.append(wholePart[i])
            if ((length - i - 1) % 3 == 0 && i != length - 1) {
                formattedBuilder.append(",")
            }
        }

        return formattedBuilder.append(decimalPart).toString()
    }


}