package   com.worksy.hr.art.repository.data.networkapi

import com.worksy.hr.art.repository.data.localdatabse.SharedPrefManager
import okhttp3.Interceptor
import okhttp3.Response


class UrlInterceptor(private val preferences: SharedPrefManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
        return chain.proceed(request.build())
    }


}