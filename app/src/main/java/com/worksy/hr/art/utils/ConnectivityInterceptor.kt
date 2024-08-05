package com.worksy.hr.art.utils

import android.util.Log
import androidx.annotation.NonNull
import com.worksy.hr.art.repository.data.localdatabse.SharedPrefManager
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * Created by PKB on 07-07-2023.
 * PKB@gmail.com
 */
class ConnectivityInterceptor(private val appPreferences: SharedPrefManager) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(@NonNull chain: Interceptor.Chain): Response {
            var request = chain.request()
            if (appPreferences.getToken()?.isNotEmpty()!!) {
                Log.e("BarrerToken", appPreferences.getToken()!!)
                request = request
                    .newBuilder()
                    .addHeader("Authorization", "Bearer ${appPreferences.getToken()}")
                    .addHeader("Content-Type", "application/json")
                    .build()
            }


        return chain.proceed(request)

    }

}