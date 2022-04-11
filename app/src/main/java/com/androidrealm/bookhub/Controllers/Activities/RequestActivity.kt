package com.androidrealm.bookhub.Controllers.Activities

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.androidrealm.bookhub.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_homepage.*

class RequestActivity : AppCompatActivity() {

    var submitBtn: TextView? = null
    var request_name: EditText? = null
    var request_detail: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request)

        submitBtn = findViewById(R.id.rqsubmitBtn)
        request_name = findViewById(R.id.requestTitle)
        request_detail = findViewById(R.id.requestDetail)


        bottom_navigation.menu.findItem(R.id.manage_book_item).isChecked = true
        findViewById<BottomNavigationView>(R.id.bottom_navigation).setOnNavigationItemSelectedListener { menuItem ->
            when {
                menuItem.itemId == R.id.home_item -> {
                    val intent = Intent(this,  HomePageActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
                    return@setOnNavigationItemSelectedListener true
                }
                menuItem.itemId == R.id.profile_item -> {
                                val intent = Intent(this,  ProfileActivity::class.java)
                                startActivity(intent)
                                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
                    return@setOnNavigationItemSelectedListener true
                }
                menuItem.itemId == R.id.manage_book_item -> {
                    return@setOnNavigationItemSelectedListener true
                }
                else -> false
            }
        }

        submitBtn!!.setOnClickListener{
            // Get input
            val rq_name = request_name!!.text.toString().trim()
            val rq_detail = request_detail!!.text.toString().trim()

            // Save to Firestore
            val db = FirebaseFirestore.getInstance()
            val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
            val request: MutableMap<String, Any> = HashMap()
            request["accountID"] = currentUserId
            request["bookDetail"] = rq_detail
            request["bookName"] = rq_name
            request["checked"] = false
            db.collection("requests")
                .add(request)


            Toast.makeText(this@RequestActivity, "Submit Successfully!", Toast.LENGTH_SHORT).show()
        }

    }
}