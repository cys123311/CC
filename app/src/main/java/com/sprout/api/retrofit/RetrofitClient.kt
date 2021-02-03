package com.sprout.api.retrofit

import android.provider.SyncStateContract
import com.sprout.App
import com.sprout.api.ApiService
import com.sprout.api.URLConstant
import com.sprout.api.interceptor.LoggingInterceptor
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.sprout.utils.MyMmkv
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitClient {

    companion object {
        fun getInstance() =
            SingletonHolder.INSTANCE

        private lateinit var retrofit: Retrofit
        private lateinit var interceptor: Interceptor
    }

    private object SingletonHolder {
        val INSTANCE = RetrofitClient()
    }

    private var cookieJar: PersistentCookieJar = PersistentCookieJar(
        SetCookieCache(),
        SharedPrefsCookiePersistor(App.instance)
    )

    init {
        retrofit = Retrofit.Builder()
            .client(getOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(URLConstant.BASE_URL)
            .build()

        interceptor = Interceptor { chain ->
            val request = chain.request()
                .newBuilder()
                .addHeader("charset", "UTF-8")
                .addHeader("sprout-token", MyMmkv.getString(URLConstant.token))
                .build()

            chain.proceed(request)
        }
    }

    private fun getOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .cookieJar(cookieJar)
            .addInterceptor(LoggingInterceptor())
//            .addInterceptor(interceptor) //添加自定义拦截器
            .sslSocketFactory(SSLContextSecurity.createIgnoreVerifySSL("TLS"))
            .build()
    }


    fun create(): ApiService = retrofit.create(
        ApiService::class.java
    )


}