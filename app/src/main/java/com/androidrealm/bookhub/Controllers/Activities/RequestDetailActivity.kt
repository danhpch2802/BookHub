package com.androidrealm.bookhub.Controllers.Activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import com.androidrealm.bookhub.Controllers.Fragments.RequestListFragment
import com.androidrealm.bookhub.Models.Request
import com.androidrealm.bookhub.R
import com.google.firebase.firestore.*

class RequestDetailActivity : AppCompatActivity() {
    var doneBtn: TextView? = null
    var username: TextView ?= null
    var request_name: TextView? = null
    var request_detail: TextView? = null
    var accept_checkbox: CheckBox? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_detail)

        doneBtn = findViewById(R.id.doneBtn)
        username = findViewById(R.id.userID_tv)
        request_name = findViewById(R.id.requestTitle_tv)
        request_detail = findViewById(R.id.requestDetail_tv)
        accept_checkbox = findViewById(R.id.checkbox_request)

        val intent = intent
        val docID = intent.getStringExtra("documentID")

        val db = FirebaseFirestore.getInstance()
        if (docID != null) {
            db.collection("requests").document(docID).get()
                .addOnSuccessListener { documentSnapshot ->
                    val data = documentSnapshot.data
                    username!!.text = data?.get("accountName") as CharSequence?
                    request_name!!.text = data?.get("bookName") as CharSequence?
                    request_detail!!.text = data?.get("bookDetail") as CharSequence?
                    accept_checkbox!!.isChecked = data?.get("checked") as Boolean
                }
        } else {
            Toast.makeText(this, "Unable to get data from Firestore", Toast.LENGTH_SHORT).show()
        }

        doneBtn!!.setOnClickListener {
            if (accept_checkbox!!.isChecked) {
                if (docID != null) {
                    db.collection("requests").document(docID)
                        .update("checked", true)
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
    }
}