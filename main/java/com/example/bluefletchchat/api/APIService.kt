package com.example.bluefletchassignment


import com.example.bluefletchchat.api.*
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface APIService {
    //account api requests
    @POST("/account/create")
    fun accountCreate(@Body body: Create):Call<CreateResponse>
    @POST("/account/login")
    fun accountLogin(@Body body: Login):Call<LoginResponse>
    @PUT("/account/logout")
    fun accountLogout(@Header("authorization") header: String):Call<LogoutResponse>
    //user api requests
    @GET("/user/")
    fun userInfo(@Header("authorization")header: String):Call<UserResponse>
    @POST("/user/picture")
    fun userPicture(@Header("authorization")header: String,
                    @Part("profileImage")image: RequestBody):Call<UserPictureResponse>
    @GET("/user/{username}")
    fun userName(@Header("authorization")header: String,
                 @Path("username")path: String):Call<UserNameResponse>
    //feed api requests
    @GET("/feed/")
    fun feedInfo(@Header("authorization")header: String,
                 @Query("start")start: String?,
                 @Query("limit")limit: String):Call<ResponseBody>
    @POST("/feed/post/")
    fun feedPost(@Header("authorization")header: String,
                 @Body body: Post):Call<ResponseBody>
    @PUT("/feed/post/")
    fun feedPostUpdate(@Header("authorization")header: String,
                       @Body body: Post):Call<ResponseBody>
    @POST("/feed/:postId/comment")
    fun feedComment(@Header("authorization")header: String,
                    @Path("postId")path: String,
                    @Body body: Post):Call<ResponseBody>
    @PUT("/feed/:postId/comment")
    fun feedCommentUpdate(@Header("authorization")header: String,
                          @Path("postId")path: String,
                          @Body body: Post):Call<ResponseBody>
}