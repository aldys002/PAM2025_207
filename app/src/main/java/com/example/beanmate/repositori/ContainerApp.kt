package com.example.beanmate2.repositori

import android.app.Application
import com.example.beanmate2.apiservice.ServiceApiBeanMate
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

interface ContainerApp {
    val repositoryBeanMate: RepositoryBeanMate
}

class DefaultContainerApp : ContainerApp {

    private val baseurl = "http://10.0.2.2/beanmate/api/"

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val klien = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(baseurl)
        .addConverterFactory(
            Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                isLenient = true
            }.asConverterFactory("application/json".toMediaType())
        )
        .client(klien)
        .build()

    private val retrofitService: ServiceApiBeanMate by lazy {
        retrofit.create(ServiceApiBeanMate::class.java)
    }

    override val repositoryBeanMate: RepositoryBeanMate by lazy {
        JaringanRepositoryBeanMate(retrofitService)
    }
}

class AplikasiBeanMate : Application() {
    lateinit var container: ContainerApp

    override fun onCreate() {
        super.onCreate()
        container = DefaultContainerApp()
    }
}
