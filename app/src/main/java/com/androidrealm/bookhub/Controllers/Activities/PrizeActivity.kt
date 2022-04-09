package com.androidrealm.bookhub.Controllers.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.androidrealm.bookhub.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PrizeActivity : AppCompatActivity() {
    var AvaBtn: ImageView? = null
    var point:String? = ""
    var username:String? = "Danh"
    var badge:String? = "The Bookaholic"
    var uid:String = "ERQnHq5YlmL78h2wDBQX"
    var usernameTV: TextView? = null
    var badgeTV: TextView? = null
    var pointTV: TextView? = null
    var prizeBtn: TextView? = null
    var reBtn: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prize)

        var fireStore = FirebaseFirestore.getInstance()
        uid = FirebaseAuth.getInstance().currentUser!!.uid

        AvaBtn = findViewById(R.id.detailPrizeAvatar)
        usernameTV = findViewById(R.id.usernamePrize)
        badgeTV = findViewById(R.id.badgePrize)
        pointTV = findViewById(R.id.enpoint_textview)
        prizeBtn = findViewById(R.id.gettoPrizeList)
        reBtn = findViewById(R.id.prizeReturn)

        reBtn!!.setOnClickListener{
            val intent = Intent(this,  ProfileActivity::class.java)
            startActivity(intent)
            finish()
        }

        prizeBtn!!.setOnClickListener{
            val intent = Intent(this,  PrizeListActivity::class.java)
            startActivity(intent)
        }

        AvaBtn!!.setImageResource(R.drawable.amagami_cover)
        getAcc()
    }

    fun getAcc () {
        val db = FirebaseFirestore.getInstance()
        db.collection("accounts").document(uid)
            .get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    username = task.result["username"] as String?

                    for (document in task.result["Badge"] as ArrayList<*>) {
                        badge = document as String
                        break
                    }
                    var point2 =  task.result["Point"] as Number?
                    point = point2.toString()
                }
                else {onError(task.exception)}
                usernameTV!!.setText(username)
                pointTV!!.setText(point)
                badgeTV!!.setText(badge)
            }
    }
    fun onError(e: Exception?) {
        if (e != null) {
            Log.d("RewardError", "onError: " + e.message)
        }
    }
}