package com.example.halfsubmission.data.remote.retrofit

import com.example.halfsubmission.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiConfig {
    companion object {
        fun getApiService(): ApiService {
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
            }

            val client = OkHttpClient.Builder().apply {
                addInterceptor(loggingInterceptor)
                connectTimeout(30, TimeUnit.SECONDS)
                readTimeout(30, TimeUnit.SECONDS)
                writeTimeout(30, TimeUnit.SECONDS)
            }.build()

            return Retrofit.Builder().apply {
                baseUrl(BuildConfig.BASE_URL)
                addConverterFactory(GsonConverterFactory.create())
                client(client)
            }.build().create(ApiService::class.java)
        }
    }
}