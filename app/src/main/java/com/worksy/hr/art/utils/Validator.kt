package com.worksy.hr.art.utils

import android.text.TextUtils
import android.util.Patterns
import java.util.regex.Matcher
import java.util.regex.Pattern


class Validator {

    companion object {
        fun isValidPassword(password: String?): Boolean {
            val pattern: Pattern
            val matcher: Matcher
            val passwordPattern = "^(?=.*[A-Za-z])(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@~#\\$%^&*()_+\\-=\\{\\}\\[\\]|\\\\:;\"'<>,.?/~])[A-Za-z\\d!@~#\\$%^&*()_+\\-=\\{\\}\\[\\]|\\\\:;\"'<>,.?/~]{8,}$"
            pattern = Pattern.compile(passwordPattern)
            matcher = pattern.matcher(password)
            return matcher.matches()
        }



        fun isValidEmail(target: CharSequence?): Boolean {
            return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
        }
    }

}
