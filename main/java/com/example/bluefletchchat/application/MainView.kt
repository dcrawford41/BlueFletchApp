package com.example.bluefletchassignment

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.EditText
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.navigation.fragment.navArgs
import androidx.navigation.fragment.findNavController
import androidx.navigation.navArgs
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.fragment_main_view.*
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Callback
import retrofit2.Call
import retrofit2.converter.gson.GsonConverterFactory
import rx.Observable
import rx.schedulers.Schedulers
import java.lang.Exception
import java.lang.reflect.GenericArrayType
import java.lang.reflect.Type
import androidx.navigation.navArgs
import androidx.navigation.findNavController
import com.example.bluefletchchat.R
import com.example.bluefletchchat.api.FeedResponse
import com.example.bluefletchchat.api.LogoutResponse
import com.example.bluefletchchat.api.Post
import com.example.bluefletchchat.api.UserResponse
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_user_profile.*
import kotlinx.android.synthetic.main.post_item.*


/**
 * A simple [Fragment] subclass.
 */
class MainView : RxBaseFragment() {

    val restAPI: RestAPI = RestAPI()
   val args: MainViewArgs by navArgs()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return container?.inflate(R.layout.fragment_main_view)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.options_menu, menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        try {
            val action = MainViewDirections.actionMainViewToUserProfile(args.token)
            val action2 = MainViewDirections.actionMainViewToLoginFragment()
            when (item.itemId) {
                R.id.user_settings -> findNavController().navigate(action)
                R.id.logout_item -> (restAPI.putLogout(args.token).enqueue(object: Callback<LogoutResponse> {
                    override fun onResponse(
                        call: Call<LogoutResponse>?,
                        response: Response<LogoutResponse>?
                    ) {
                        findNavController().navigate(action2)
                    }

                    override fun onFailure(call: Call<LogoutResponse>?, t: Throwable?) {
                       Snackbar.make(view!!,
                           "Could not log out. Try again.",
                           Snackbar.LENGTH_LONG).show()
                    }
                }))
            }
        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
        }
        return true
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        post_list.setHasFixedSize(true)
        post_list.layoutManager = LinearLayoutManager(context)
        /*restAPI.getFeedInfo(args.token, "", "5").enqueue(object: Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                textView.text = "Fail:" + t?.message
            }

            override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                if (response?.code() == 200 ) {
                    val rawResponseBody = response.body().string()
                    val errorLessBody = rawResponseBody.subSequence(0, rawResponseBody.length - 218).toString() + "]"
                        val gson = Gson()
                        val feedResponse = gson.fromJson(errorLessBody, FeedResponse::class.java)
                        textView.text = feedResponse[0].text

                } else {
                    Toast.makeText(context, response?.errorBody()?.string(), Toast.LENGTH_LONG).show()
                }
            }


        })


        post_list.layoutManager = linearLayout
        post_list.clearOnScrollListeners()
        post_list.addOnScrollListener(InfiniteScrollListener({requestPosts()}, linearLayout))
        */
        initAdapter()

        //(post_list.adapter as PostAdapter).addPosts(posts)
        if (savedInstanceState == null) {
           getPosts()
        }
        swipetorefresh.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(
            context!!, R.color.backgroundColor))
        swipetorefresh.setColorSchemeColors(Color.WHITE)
        swipetorefresh.setOnRefreshListener {
            val action = MainViewDirections.actionMainViewSelf(args.token)
            findNavController().navigate(action)
        }
        add_post_button.setOnClickListener {
            showAddItemDialog(context)
        }

    }



    private fun showAddItemDialog(c: Context?) {
        val taskEditText = EditText(c)
        val builder = AlertDialog.Builder(c)
            builder.setTitle("Add a new post")
            builder.setView(taskEditText)
            builder.setPositiveButton("Add") {dialog, which ->
                restAPI.postFeed(args.token, Post(taskEditText.text.toString())).enqueue(object: Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>?,
                        response: Response<ResponseBody>?
                    ) {
                       if (response?.code() == 200) {
                           Toast.makeText(context, "Successful!", Toast.LENGTH_SHORT).show()
                       } else {
                           Toast.makeText(context,
                               response?.errorBody()?.string(),
                               Toast.LENGTH_SHORT).show()
                       }
                    }

                    override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                        Toast.makeText(context, t?.message, Toast.LENGTH_LONG).show()
                    }
                })
                val action = MainViewDirections.actionMainViewSelf(args.token)
                findNavController().navigate(action)
            }
            builder.setNegativeButton("Cancel") {dialog, which ->
                null
            }

        builder.show()
    }


    /*
    private fun retrievePosts(feedResponse: FeedResponse) {
        val subscription = getPosts("")
            .subscribeOn(Schedulers.io())
            .subscribe(
                { retrievedPosts ->
                    (post_list.adapter as PostAdapter).addPosts(retrievedPosts)
                },
                {e ->
                    Snackbar.make(post_list, e.message.toString(), Snackbar.LENGTH_LONG).show()
                }
            )
        subscriptions.add(subscription)


    }

     */






    private fun initAdapter() {
        if (post_list.adapter == null) {
            post_list.adapter = PostAdapter()
        }
    }

    fun viewComments() {
        val action =
            MainViewDirections.actionMainViewToUserProfile(args.token)
        findNavController().navigate(action)
    }

    fun getPosts() {
        /*return Observable.create {
                subscriber ->
            val feedResponse = restAPI.getFeedInfo(args.token, "",
                limit)
            val response = feedResponse.execute()
            if (response.isSuccessful) {
                val rawResponseBody = response.body().string()
                val errorLessBody = rawResponseBody.subSequence(0, rawResponseBody.length - 218).toString() + "]"
                val gson = Gson()
                val postsResponse = gson.fromJson(errorLessBody, FeedResponse::class.java).toList()

                val posts = postsResponse.map {
                    FeedItem(it.comments, it.createdAt, it.id, it.text, it.updatedAt, it.user, it.username)
                }
                subscriber.onNext(posts)
                subscriber.onCompleted()
            } else {
                subscriber.onError(Throwable(response.message()))

            }


        }
         */

        restAPI.getFeedInfo(args.token, "", "50").enqueue(object: Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                Toast.makeText(context,
                    "Something went wrong. Try to refresh.",
                    Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                if (response?.code() == 200) {
                    val rawResponseBody = response.body().string()
                    val errorLessBody = rawResponseBody.subSequence(
                        0,
                        rawResponseBody.length - 218
                    ).toString() + "]"
                    val gson = Gson()
                    val feedResponse = gson.fromJson(errorLessBody, FeedResponse::class.java)
                    val posts = feedResponse.toList()
                    (post_list.adapter as PostAdapter).addPosts(posts, 50)
                } else {
                    Toast.makeText(context, response?.errorBody()?.string(), Toast.LENGTH_LONG)
                        .show()
                }
            }
        })


    }


}
