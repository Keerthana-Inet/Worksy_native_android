package com.worksy.hr.art.utils

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.ForegroundColorSpan
import android.text.style.TypefaceSpan
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.FragmentActivity
import com.worksy.hr.art.R
import java.net.Inet4Address
import java.net.NetworkInterface
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object NetworkUtils {
    fun getIpAddress(): String {
        val networkInterfaces = NetworkInterface.getNetworkInterfaces()

        while (networkInterfaces.hasMoreElements()) {
            val networkInterface = networkInterfaces.nextElement()
            val inetAddresses = networkInterface.inetAddresses

            while (inetAddresses.hasMoreElements()) {
                val inetAddress = inetAddresses.nextElement()

                if (inetAddress is Inet4Address && !inetAddress.isLoopbackAddress) {
                    return inetAddress.hostAddress
                }
            }
        }
        return "Unknown IP Address"
    }
    fun getCurrentTimestampInSeconds(): Long {
        return System.currentTimeMillis() / 1000
    }

    fun formatTimestamp(): String {
        val timestamp = getCurrentTimestampInSeconds()
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp * 1000
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }
   /* fun customTypeface(
        requireActivity: FragmentActivity,
        binding: TextView,
        inputString: String,
        font1Style: Int,
        font2Style: Int,
        firstColorText: Int,
        secondTextColor: Int
    ) {
        var text=inputString
        val spannableString= SpannableString(text)

        val font1 = ResourcesCompat.getFont(requireActivity,font1Style)
        val customTypefaceSpan1 = CustomTypefaceSpan(font1!!)
        spannableString.setSpan(customTypefaceSpan1,0,21, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)

        val textFirst= ForegroundColorSpan(requireActivity.resources.getColor(firstColorText))
        spannableString.setSpan(textFirst,0,21, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)

        // Set "Register" in font2
        val font2 = ResourcesCompat.getFont(requireActivity,font2Style)
        val customTypefaceSpan2 = CustomTypefaceSpan(font2!!)
        spannableString.setSpan(customTypefaceSpan2, 22, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        val secondText= ForegroundColorSpan(requireActivity.resources.getColor(secondTextColor))
        spannableString.setSpan(secondText,22,text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.text=spannableString
    }*/
   fun accountTypeFace(
       requireActivity: FragmentActivity,
       binding: TextView
   ) {
       var text="Don't have an account? Register"
       val spannableString= SpannableString(text)

       val font1 = ResourcesCompat.getFont(requireActivity, R.font.inter_regular)
       val customTypefaceSpan1 = CustomTypefaceSpan(font1!!)
       spannableString.setSpan(customTypefaceSpan1,0,21, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)

       val textFirst= ForegroundColorSpan(Color.GRAY)
       spannableString.setSpan(textFirst,0,21, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)

       // Set "Register" in font2
       val font2 = ResourcesCompat.getFont(requireActivity, R.font.inter_semibold)
       val customTypefaceSpan2 = CustomTypefaceSpan(font2!!)
       spannableString.setSpan(customTypefaceSpan2, 22, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

       val secondText= ForegroundColorSpan(requireActivity.resources.getColor(R.color.button_txt))
       spannableString.setSpan(secondText,22,text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
       binding.text=spannableString
   }
    fun loginTypeFace(
        requireActivity: FragmentActivity,
        binding: TextView,
        inputTextStr: String,
        lastStr: String,
        color: Int,
        color1: Int
    ) {
        val spannableString = SpannableString("$inputTextStr $lastStr")

        val font1 = ResourcesCompat.getFont(requireActivity, R.font.inter_regular)
        val font2 = ResourcesCompat.getFont(requireActivity, R.font.inter_semibold)

        val customTypefaceSpan1 = CustomTypefaceSpan(font1!!)
        spannableString.setSpan(customTypefaceSpan1, 0, inputTextStr.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        val textFirst = ForegroundColorSpan(color)
        spannableString.setSpan(textFirst, 0, inputTextStr.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        val customTypefaceSpan2 = CustomTypefaceSpan(font2!!)
        spannableString.setSpan(customTypefaceSpan2, inputTextStr.length + 1, spannableString.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        val secondText = ForegroundColorSpan((color1))
        spannableString.setSpan(secondText, inputTextStr.length + 1, spannableString.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        binding.text = spannableString
    }

}
class CustomTypefaceSpan(private val typeface: Typeface) : TypefaceSpan("") {
    override fun updateDrawState(ds: TextPaint) {
        applyCustomTypeFace(ds, typeface)
    }

    override fun updateMeasureState(paint: TextPaint) {
        applyCustomTypeFace(paint, typeface)
    }

    private fun applyCustomTypeFace(paint: Paint, tf: Typeface) {
        val oldStyle: Int
        val old = paint.typeface
        oldStyle = old?.style ?: 0
        val fake = oldStyle and tf.style.inv()
        if (fake and Typeface.BOLD != 0) {
            paint.isFakeBoldText = true
        }
        if (fake and Typeface.ITALIC != 0) {
            paint.textSkewX = -0.25f
        }
        paint.typeface = tf
    }
}
