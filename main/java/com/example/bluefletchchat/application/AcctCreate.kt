package com.example.bluefletchassignment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.bluefletchchat.R
import com.example.bluefletchchat.api.Create
import com.example.bluefletchchat.api.CreateResponse
import kotlinx.android.synthetic.main.acct_create_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AcctCreate : Fragment() {

    val restAPI: RestAPI = RestAPI()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.acct_create_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var text_username = view.findViewById<EditText>(R.id.text_username)
        var text_password = view.findViewById<EditText>(R.id.text_password)
        var button_create = view.findViewById<Button>(R.id.button_create)

        button_create.setOnClickListener {
            val username = text_username.text.toString().trim()
            val password = text_password.text.toString().trim()
            val firstname = text_first_name.text.toString().trim()
            val lastname = text_last_name.text.toString().trim()
            restAPI.postCreate(Create(username, password, firstname, lastname)).enqueue(object: Callback<CreateResponse> {
                    override fun onResponse(
                        call: Call<CreateResponse>?,
                        response: Response<CreateResponse>?
                    ) {
                        if (response?.code() == 200) {
                            val action = AcctCreateDirections.actionAcctCreateToMainView(response.body().token)
                            findNavController().navigate(action)
                        } else if (response?.code() == 401) {
                            showResponse("Please provide a valid username, password, " +
                                    "first name, and last name.")
                        } else if (response?.code() == 402) {
                            showResponse("Invalid username and password provided.")
                        }
                    }

                    override fun onFailure(call: Call<CreateResponse>?, t: Throwable?) {
                        showResponse(
                            "Something went wrong. There may be something " +
                                    "wrong with your internet connection."
                        )
                    }
                })
        }
    }
        fun showResponse(response: String) {
            Toast.makeText(view?.context, response, Toast.LENGTH_LONG).show()
        }

}
