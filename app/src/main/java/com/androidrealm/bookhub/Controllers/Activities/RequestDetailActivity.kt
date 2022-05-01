package com.androidrealm.bookhub.Controllers.Activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import com.androidrealm.bookhub.Controllers.Fragments.RequestListFragment
import com.androidrealm.bookhub.Controllers.Services.FirebaseService
import com.androidrealm.bookhub.Controllers.Services.NotificationData
import com.androidrealm.bookhub.Controllers.Services.PushNotification
import com.androidrealm.bookhub.Controllers.Services.RetrofitInstance
import com.androidrealm.bookhub.Models.Request
import com.androidrealm.bookhub.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.*
import com.google.firebase.iid.FirebaseInstanceIdReceiver
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RequestDetailActivity : AppCompatActivity() {
    val TAG = "RequestDetailActivity"
    var doneBtn: TextView? = null
    var username: TextView ?= null
    var request_date: TextView?=null
    var request_name: TextView? = null
    var request_detail: TextView? = null
    var addNewBookBtn: TextView ?= null
    var accept_checkbox: CheckBox? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_detail)

        FirebaseService.sharedPref = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)

        doneBtn = findViewById(R.id.doneBtn)
        username = findViewById(R.id.userID_tv)
        request_date = findViewById(R.id.date_tv)
        request_name = findViewById(R.id.requestTitle_tv)
        request_detail = findViewById(R.id.requestDetail_tv)
        addNewBookBtn = findViewById(R.id.addNewBookBtn)
        accept_checkbox = findViewById(R.id.checkbox_request)

        val intent = intent
        val docID = intent.getStringExtra("documentID")

        accept_checkbox!!.setOnClickListener{
            if(accept_checkbox!!.isChecked)
                addNewBookBtn!!.visibility = View.VISIBLE
            else addNewBookBtn!!.visibility = View.GONE
        }

        val db = FirebaseFirestore.getInstance()
        if (docID != null) {
            db.collection("requests").document(docID).get()
                .addOnSuccessListener { documentSnapshot ->
                    val data = documentSnapshot.data
                    username!!.text = data?.get("accountName") as CharSequence?
                    request_date!!.text = data?.get("time") as CharSequence?
                    request_name!!.text = data?.get("bookName") as CharSequence?
                    request_detail!!.text = data?.get("bookDetail") as CharSequence?
                    accept_checkbox!!.isChecked = data?.get("checked") as Boolean
                }
        } else {
            Toast.makeText(this, "Unable to get data from Firestore", Toast.LENGTH_SHORT).show()
        }

        doneBtn!!.setOnClickListener {
            val title = "Request Confirmed!"
            val message = "The request for ${username!!.text.toString()} has been accepted. Check out the gallery now!"

            if (accept_checkbox!!.isChecked) {
                if (docID != null) {
                    db.collection("requests").document(docID)
                        .update("checked", true)
                    // find token of user that sends the request
                    db.collection("accounts").whereEqualTo("username", username!!.text)
                        .get()
                        .addOnSuccessListener { documents ->
                            for(doc in documents){
                                var token = doc.getString("RecipientToken").toString()
                                if(token.isNotEmpty()){
                                    PushNotification(
                                        NotificationData(title, message),
                                        token
                                    ).also {
                                        sendNotification(it)
                                    }
                                }
                            }
                        }
                    Toast.makeText(this, "Request Confirmed", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Unable to get data from Firestore", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                if (docID != null) {
                    db.collection("requests").document(docID)
                        .update("checked", false)
                }
            }
            finish()
        }

        addNewBookBtn!!.setOnClickListener{
            val intentToCreateBookAct = Intent(this, BookCreateActivity::class.java)
            startActivity(intentToCreateBookAct)
        }
    }

    private fun sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {
        try{
            val response = RetrofitInstance.api.postNotification(notification)
            if(response.isSuccessful){
                Log.d(TAG, "Response: ${Gson().toJson(response)}")
            }
            else {
                Log.e(TAG, response.errorBody().toString())
            }
        } catch (e:Exception){
            Log.e(TAG, e.toString())
        }
    }

    override fun onResume() {
        super.onResume()
        val intentGet = Intent()
        val added = intentGet.getBooleanExtra("newbookadded", false)
        if(added){
            Toast.makeText(this, "New Book has been added to the Gallery", Toast.LENGTH_SHORT).show()
        }
    }
}