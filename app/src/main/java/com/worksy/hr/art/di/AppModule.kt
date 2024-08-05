package com.worksy.hr.art.di

import android.content.Context
import com.worksy.hr.art.Constants.BASE_URL
import com.worksy.hr.art.Constants.MY_PREF
import com.worksy.hr.art.repository.data.localdatabse.SharedPrefManager
import com.worksy.hr.art.repository.data.networkapi.MyWebApi
import com.worksy.hr.art.repository.data.networkapi.UrlInterceptor
import com.worksy.hr.art.utils.ConnectivityInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideCacheDirectory(@ApplicationContext context: Context): File {
        return File(context.cacheDir, "http-cache")
    }

    @Provides
    @Singleton
    fun provideCacheSize(): Long {
        return 80 * 1024 * 1024 // 40 MB
    }
  /*  @Singleton
    @Provides
    fun provideMyDao(@ApplicationContext app: Context): MyDao =
        Room.databaseBuilder(app, MyDatabase::class.java, MY_DATABASE_NAME)
            .addMigrations(MIGRATION_1_2)
            .build()
            .getDao()*/

    @Singleton
    @Provides
    fun provideRetrofitClient(okHttpClient: OkHttpClient): MyWebApi {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val httpClient = OkHttpClient().newBuilder()
        httpClient.addInterceptor(logging)

        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .build()
            .create(
                MyWebApi::class.java
            )
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        interceptor: Interceptor,
        appPreferences: SharedPrefManager,
        cacheDirectory: File, // Inject the cache directory
        cacheSize: Long // Inject the cache size
    ): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .connectTimeout(6, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .addInterceptor(logging)
//            .addInterceptor(interceptor)
            .addInterceptor(ConnectivityInterceptor(appPreferences))
            .cache(Cache(cacheDirectory, cacheSize)) // Set the cache for OkHttpClient

            .build()
    }

    @Singleton
    @Provides
    fun provideUrlInterceptor(sharedPrefManager: SharedPrefManager): Interceptor =
        UrlInterceptor(sharedPrefManager)

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext app: Context): SharedPrefManager {
        val sharePref = app.getSharedPreferences(MY_PREF, Context.MODE_PRIVATE)
        return SharedPrefManager(sharePref)
    }
}

