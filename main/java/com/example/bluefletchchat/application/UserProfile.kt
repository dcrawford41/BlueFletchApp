package com.example.bluefletchchat.application

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.example.bluefletchassignment.MainView
import com.example.bluefletchassignment.RestAPI

import com.example.bluefletchchat.R
import com.example.bluefletchchat.api.UserResponse
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_user_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserProfile : Fragment() {

    val restAPI = RestAPI()
    val args: UserProfileArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getUserData()
    }

    private fun getUserData() {
        try {
            restAPI.getUserInfo(args.token).enqueue(object : Callback<UserResponse> {
                override fun onResponse(
                    call: Call<UserResponse>?,
                    response: Response<UserResponse>?
                ) {
                    if (response?.code() == 200) {
                        username_text.text = response.body().username
                        name_text.text =
                            (response.body().firstName + " " + response.body().lastName)
                        Picasso.get().load(response.body().profilePic).into(profilePicture)
                    }

                }

                override fun onFailure(call: Call<UserResponse>?, t: Throwable?) {
                    Toast.makeText(context, t?.message, Toast.LENGTH_LONG).show()
                    getUserData()
                }
            })
        } catch (e: Exception) {
            Toast.makeText(context, "Something went wrong. Try again.", Toast.LENGTH_SHORT).show()
        }
    }


}
