package com.androidrealm.bookhub.Controllers.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import com.androidrealm.bookhub.R
import com.google.firebase.firestore.FirebaseFirestore

class RequestDetailActivity : AppCompatActivity() {
    var doneBtn: TextView? = null
    var userId: TextView ?= null
    var request_name: TextView? = null
    var request_detail: TextView? = null
    var accept_checkbox: CheckBox? = null
    var accountId: String ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_detail)

        doneBtn = findViewById(R.id.doneBtn)
        userId = findViewById(R.id.userID_tv)
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
                    accountId = (data?.get("accountID") as CharSequence?).toString()
                    db.collection("accounts").document(accountId!!).get()
                        .addOnSuccessListener { document ->
                            if (document.exists()){
                                userId!!.text = document.getString("username")
                            }
                        }
                    request_name!!.text = data?.get("bookName") as CharSequence?
                    request_detail!!.text = data?.get("bookDetail") as CharSequence?
                    accept_checkbox!!.isChecked = data?.get("checked") as Boolean
                }
        }
        else{
            Toast.makeText(this, "Unable to get data from Firestore", Toast.LENGTH_SHORT).show()
        }

        doneBtn!!.setOnClickListener{
            if(accept_checkbox!!.isChecked){

                if (docID != null) {
                    db.collection("requests").document(docID)
                        .update("checked", true)
                    Toast.makeText(this, "Request Confirmed", Toast.LENGTH_LONG).show()
                }
                else{
                    Toast.makeText(this, "Unable to get data from Firestore", Toast.LENGTH_SHORT).show()
                }
            }

            val intentBack = Intent(this, RequestListActivity::class.java)
            startActivity(intentBack)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
            finish()
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this@RequestDetailActivity, RequestListActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        finish()
    }
}