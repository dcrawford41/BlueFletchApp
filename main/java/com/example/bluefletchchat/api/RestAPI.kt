package com.example.bluefletchassignment

import com.example.bluefletchchat.api.*
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class RestAPI() {

    private val apiService: APIService
    var okHttpClient = OkHttpClient.Builder()
        .connectTimeout(1, TimeUnit.MINUTES)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .build()

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://us-central1-bluefletch-learning-assignment.cloudfunctions.net/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(APIService::class.java)
    }

    fun postCreate(body: Create): Call<CreateResponse> {
        return apiService.accountCreate(body)
    }

    fun postLogin(body: Login): Call<LoginResponse> {
        return apiService.accountLogin(body)
    }

    fun putLogout(header: String): Call<LogoutResponse> {
        return apiService.accountLogout(header)
    }

    fun getUserInfo(header: String): Call<UserResponse> {
        return apiService.userInfo(header)
    }

    fun postPicture(header: String, image: RequestBody): Call<UserPictureResponse> {
        return apiService.userPicture(header, image)
    }

    fun getUserNameInfo(header: String, username: String): Call<UserNameResponse> {
        return apiService.userName(header, username)
    }

    fun getFeedInfo(header: String, start: String?, limit: String): Call<ResponseBody> {
        return apiService.feedInfo(header, start, limit)
    }

    fun postFeed(header: String, body: Post): Call<ResponseBody> {
        return apiService.feedPost(header, body)
    }

    fun updatePost(header: String, body: Post):Call<ResponseBody> {
        return apiService.feedPostUpdate(header, body)
    }

    fun postComment(header: String, postId: String, body: Post):Call<ResponseBody> {
        return apiService.feedComment(header, postId, body)
    }

    fun updateComment(header: String, postId: String, body: Post):Call<ResponseBody> {
        return apiService.feedCommentUpdate(header, postId, body)
    }

}