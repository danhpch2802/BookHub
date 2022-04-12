package com.androidrealm.bookhub.Controllers.Activities

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.androidrealm.bookhub.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class PrizeActivity : AppCompatActivity() {
    var AvaBtn: ImageView? = null
    var point:String? = ""
    var username:String? = "Danh"
    var badge:String? = "The Bookaholic"
    var uid:String = "ERQnHq5YlmL78h2wDBQX"
    var Badge2 = "2"
    var usernameTV: TextView? = null
    var badgeTV: TextView? = null
    var pointTV: TextView? = null
    var prizeBtn: TextView? = null
    var reBtn: ImageView? = null
    var smallGbBtn: ImageButton? = null
    var bigGbBtn: ImageButton? = null
    var prizeList: ArrayList<String> ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prize)

        prizeList = arrayListOf()
        val db2 = FirebaseFirestore.getInstance()
        uid = FirebaseAuth.getInstance().currentUser!!.uid

        AvaBtn = findViewById(R.id.detailPrizeAvatar)
        usernameTV = findViewById(R.id.usernamePrize)
        badgeTV = findViewById(R.id.badgePrize)
        pointTV = findViewById(R.id.enpoint_textview)
        prizeBtn = findViewById(R.id.gettoPrizeList)
        reBtn = findViewById(R.id.prizeReturn)

        smallGbBtn = findViewById(R.id.smallGBoxButton)
        bigGbBtn = findViewById(R.id.hugeGBoxButton)

        var point2:Long = 0
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
        getPrize()
    }

    override fun onResume() {
        super.onResume()
        getAcc()
    }
    fun getAcc () {
        val db = FirebaseFirestore.getInstance()
        db.collection("accounts").document(uid)
            .get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    username = task.result["username"] as String?

                    for (document in task.result["Badge"] as ArrayList<*>) {
                        Badge2 = document as String
                        break
                    }
                    var point2 =  task.result["Point"] as Number?
                    point = point2.toString()
                }
                else {onError(task.exception)}
                usernameTV!!.setText(username)
                pointTV!!.setText(point)
                badgeTV!!.setText(badge)
                val db2 = FirebaseFirestore.getInstance()
                db2.collection("prizes").document(Badge2).get().addOnCompleteListener { task2 ->
                    if (task2.isSuccessful) {
                        badge = task2.result["prizeName"] as? String
                    }
                    else {
                        onError(task2.exception)
                    }
                    badgeTV!!.setText(badge)
                }
            }
    }
    fun getPrize () {
        val db = FirebaseFirestore.getInstance()
        val db2 = FirebaseFirestore.getInstance()
        var point2:Long = 0
        db.collection("accounts").document(uid)
            .get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result["BadgeUnown"] as ArrayList<*>) {
                        prizeList!!.add(document.toString())

                    }
                } else {
                    onError(task.exception)
                }
                smallGbBtn!!.setOnClickListener{
                    db2.collection("accounts").document(uid)
                        .get().addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                point2 = (task.result["Point"] as Long?)!!
                            } else {
                                onError(task.exception)
                            }
                            Log.d(TAG, point2.toString())
                            if (point2 < 1000)
                            {
                                Toast.makeText(this, "You don't have enough Points", Toast.LENGTH_SHORT).show()
                            }
                            if (prizeList!!.size == 0)
                            {
                                Toast.makeText(this, "You already have full prize", Toast.LENGTH_SHORT).show()
                            }
                            else {
                                point2 = point2.minus(1000)
                                Log.d(TAG, point2.toString())
                                setPoint(point2)
                                val values = prizeList!!.toList()
                                val random = values.random()
                                Log.d(TAG, random)
                                setPrize(random)
                                val intent = Intent(this,  PrizeDetailActivity::class.java)
                                intent.putExtra("id",random)
                                startActivity(intent)
                            }
                        }
                }

                bigGbBtn!!.setOnClickListener{
                    db2.collection("accounts").document(uid)
                        .get().addOnCompleteListener { task ->
                            if (task.isSuccessful) {

                                point2 = (task.result["Point"] as Long?)!!
                            } else {
                                onError(task.exception)
                            }
                            Log.d(TAG, point2.toString())
                            if (point2 < 10000)
                            {
                                Toast.makeText(this, "You don't have enough Points", Toast.LENGTH_SHORT).show()
                            }

                            if (prizeList!!.size == 0)
                            {
                                Toast.makeText(this, "You already have full prize", Toast.LENGTH_SHORT).show()
                            }
                            else {
                                var flag = 0
                                val intent = Intent(this,  PrizeDetailActivity::class.java)
                                point2 = point2.minus(10000)
                                Log.d(TAG, point2.toString())
                                setPoint(point2)
                                for (i in prizeList!!){
                                    if (i == "b1" || i == "b2") {
                                        flag = 1
                                        setPrize(i)
                                        intent.putExtra("id",i)
                                        startActivity(intent)
                                        break
                                    }
                                }
                                if (flag == 0)
                                {
                                    Toast.makeText(this, "You already have full big prize", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                }

            }
    }
    fun setPrize (id: String) {
        val db = FirebaseFirestore.getInstance()
        db.collection("accounts").document(uid)
            .get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    db.collection("accounts").document(uid).update("BadgeOwn", FieldValue.arrayUnion(id))

                    db.collection("accounts").document(uid).update("BadgeUnown", FieldValue.arrayRemove(id))
                }
                else {onError(task.exception)}
            }
    }
    fun setPoint (point: Long) {
        val db = FirebaseFirestore.getInstance()
        db.collection("accounts").document(uid)
            .get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    db.collection("accounts").document(uid).update("Point", point)
                }
                else {onError(task.exception)}
            }
    }
    fun onError(e: Exception?) {
        if (e != null) {
            Log.d("RewardError", "onError: " + e.message)
        }
    }
}