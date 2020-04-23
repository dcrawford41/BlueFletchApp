package com.example.bluefletchchat.api

import com.example.bluefletchassignment.AdapterConstants
import com.example.bluefletchassignment.ViewType


    class Create(
        val username: String,
        val password: String,
        val firstname: String,
        val lastname: String
    )

    class Login(
        val username: String,
        val password: String
    )

    class Post(
        val text: String
    )

    data class LoginResponse(
        val token: String
    )

    data class CreateResponse(
        val token: String
    )

    class LogoutResponse

    data class UserResponse(
        val firstName: String,
        val lastName: String,
        val profilePic: String,
        val username: String
    )

    data class UserNameResponse(
        val firstName: String,
        val lastName: String,
        val profilePic: String,
        val username: String
    )


    class Comments : ArrayList<Comment>()

    class UserPictureResponse

    class ErrorResponse(
        val code: String,
        val message: String
    )

    class PostMessage(
        val createdAt: String,
        val updatedAt: String,
        val id: String,
        val text: String,
        val user: User,
        val comments: Comment
    ) : ViewType {
        override fun getViewType() = AdapterConstants.POSTS
        }


    class PostUpdate(
        val text: String
    )


    class CommentUpdate(
        val text: String
    )






class FeedResponse : ArrayList<FeedItem>()

data class FeedItem(
    val comments: List<Comment>?,
    val createdAt: String,
    val id: String,
    val text: String,
    val updatedAt: String,
    val user: User,
    val username: String
) : ViewType {
    override fun getViewType() = AdapterConstants.POSTS
}

data class Comment(
    val createdAt: String,
    val id: Int,
    val text: String,
    val timestamp: String,
    val updatedAt: String,
    val username: String
)

data class User(
    val firstName: String,
    val lastName: String,
    val profilePic: String,
    val username: String
)





