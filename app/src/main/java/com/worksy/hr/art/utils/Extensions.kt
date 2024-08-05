package com.worksy.hr.art.utils

import android.content.Context
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.worksy.hr.art.R

/**
 * To set Textview text color.
 * @param id A color value from color.xml.
 * @param context-
 */

fun TextView.setTextColor(context: Context, @ColorRes id: Int) {
    setTextColor(ContextCompat.getColor(context, id))
}

/**
 * To set Textview background color
 * @param id A drawable file id  from drawable.
 * @param context-
 */
fun TextView.setBackground(context: Context, @DrawableRes id: Int) {
    this.background = AppCompatResources.getDrawable(context, id)
}

fun View.gone() = run { this.visibility = View.GONE }
fun View.invisible() = run { this.visibility = View.INVISIBLE }
fun View.visible() = run { this.visibility = View.VISIBLE }



fun View.showTooltipMessage(context: Context, message: String,uppercase:String, lowercase:String,number:String,symbol:String) {
    val typeface = ResourcesCompat.getFont(context, R.font.inter_black)
    println(typeface)
    val originalString = message
    val boldWords = listOf(uppercase, lowercase,number,symbol)
    val spannableStringBuilder = SpannableStringBuilder(originalString)

    for (word in boldWords) {
        val startIndex = originalString.indexOf(word)
        val endIndex = startIndex + word.length

        if (startIndex != -1) {
            val boldStyle = StyleSpan(Typeface.BOLD)
            spannableStringBuilder.setSpan(boldStyle, startIndex, endIndex, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        }
    }
}
