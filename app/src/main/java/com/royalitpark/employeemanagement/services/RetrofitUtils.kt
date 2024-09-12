package com.bookiron.itpark.services

import com.bookiron.itpark.services.RetrofitClient.API_KEY
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    val API_KEY="E28DA37796A786FAB9DC6427B1D19"
    private const val BASE_URL = "https://crm.rajasreetownship.in/api/"
    val CATEGORY_IMAGE_PATH="https://ritps.com/bookiron/backend/files/service_categories/"
    val PRODUCT_IMAGE_PATH="https://ritps.com/bookiron/backend/files/products"
    val PROFILE_IMAGE_PATH="https://crm.rajasreetownship.in/file_uploads/employee/"
    val CMS_IMAGE_PATH="https://crm.rajasreetownship.in/file_uploads/employee/documents/"
    val PRIVACY_URL="https://rrtutors.com"
    val TERMS_CONDITION="https://rrtutors.com"
    var intercepter= HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

    val okHttpClient = OkHttpClient()
        .newBuilder()
        .addInterceptor(intercepter)
        .build()
    fun getClient(): Retrofit =
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    val apiInterface = getClient(). create(ApiInterface::class.java)
}

object RequestInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestbuilder= request .newBuilder()

        requestbuilder.header("Content-Type","application/json");
        //requestbuilder.header("api_key",API_KEY);


        return chain.proceed(request)
    }
}