package com.smaart.idrico.model

import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import com.google.gson.internal.LinkedTreeMap
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.Header
class Api {
    public suspend fun login(email:String?, pass:String?): Response<LoginData> {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()

        val apiService: LoginInterface = retrofit.create(LoginInterface::class.java)
        val response = apiService.getResponse(email, pass)
        return response
    }
    public suspend fun actions(token:String?):LinkedTreeMap<String,Any>?{
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.HEADERS
            })
            .build()
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()

        val apiService: ActionsInterface = retrofit.create(ActionsInterface::class.java)
        val response = apiService.getResponse(token)
        return response.body()?.get("actions")
    }
}
interface LoginInterface {
    @FormUrlEncoded
    @POST("login")
    suspend fun getResponse(
        @Field("u") email: String?,
        @Field("p") pass: String?
    ): Response<LoginData>
}
data class LoginData(
    @SerializedName("Token") val token: String
)
interface ActionsInterface {
    @GET("waterPermissions")
    suspend fun getResponse(@Header("Token") token: String?): Response<LinkedTreeMap<String,LinkedTreeMap<String,Any>>>
}