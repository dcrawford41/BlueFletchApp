package com.example.bluefletchassignment

import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.text.style.ClickableSpan
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.example.bluefletchchat.R
import com.example.bluefletchchat.api.Login
import com.example.bluefletchchat.api.LoginResponse


class LoginFragment : Fragment() {

    val restAPI: RestAPI = RestAPI()
    var token: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.login_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var text_username = view.findViewById<EditText>(R.id.text_username)
        var text_password = view.findViewById<EditText>(R.id.text_password)
        var button_login = view.findViewById<Button>(R.id.button_login)


        button_login.setOnClickListener {
            val username = text_username.text.toString().trim()
            val password = text_password.text.toString().trim()
            restAPI.postLogin(Login(username, password)).enqueue(object: Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>?,
                    response: Response<LoginResponse>?
                ) {
                    if (response?.code() == 200) {
                        token = response.body().token
                        retrieveToken(token)
                        val action = LoginFragmentDirections.actionLoginFragmentToMainView(token)
                        findNavController().navigate(action)
                    } else if (response?.code() == 401) {
                        showResponse("Please provide a valid username and password.")
                    } else if (response?.code() == 403) {
                        showResponse("Incorrect username or password. Please try again.")
                    }
                }

                override fun onFailure(call: Call<LoginResponse>?, t: Throwable?) {
                    showResponse("Something went wrong." +
                            " Please restart the app and try again")
                }
            })


        }


        val createView = view.findViewById<TextView>(R.id.create_text)
        val create_text = SpannableString("Don't have an account? Sign up.")
        val span: Spannable = create_text
        val create = object: ClickableSpan() {
             override fun onClick(widget: View) {
                 val action = LoginFragmentDirections.actionLoginFragmentToAcctCreate()
                 findNavController().navigate(action)
             }
        }
        span.setSpan(create, 23, 31, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        createView.text = create_text
        createView.movementMethod = LinkMovementMethod.getInstance()
        createView.highlightColor = Color.TRANSPARENT
    }

    fun showResponse(response: String) {
        Toast.makeText(view?.context, response, Toast.LENGTH_LONG).show()
    }

    fun retrieveToken(token: String) {
        this.token = token

    }

}
