package com.androidrealm.bookhub.Controllers.Activities

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.androidrealm.bookhub.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PrizeDetailActivity : AppCompatActivity() {
    var prizeName:String? = "Unlock the book: The mythology of Bookahholic-chan"
    var prizeDes:String? = "The Bookaholic"

    var uid:String = ""
    lateinit var auth : FirebaseAuth

    var badgeChosen: ArrayList<String> ?= null
    var id:String? = "b2"
    var badge: TextView? = null
    var badgeDes: TextView? = null
    var badgeBtn: TextView? = null
    var prizeBtn: TextView?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = intent
        id = intent.getStringExtra("id")
        val db = FirebaseFirestore.getInstance()
        db.collection("prizes").whereEqualTo("prizeName", id)
            .get().addOnSuccessListener { documents ->
                for (document in documents) {
                    id = document["id"] as String?
                }
                if (id == "b1" || id =="b2") {
                    setContentView(R.layout.activity_prize_detail)
                }
                else {
                    setContentView(R.layout.activity_prize_detail2)
                }

                uid = FirebaseAuth.getInstance().currentUser!!.uid

                badgeChosen = arrayListOf(id.toString())
                badge = findViewById(R.id.prizeReceived)
                badgeDes = findViewById(R.id.prizeDescryption)
                badgeBtn = findViewById(R.id.setPrizeBadge)
                prizeBtn = findViewById(R.id.gotoPrizeList)


                prizeBtn!!.setOnClickListener{
                    val intents = Intent(this,  PrizeListActivity::class.java)
                    startActivity(intents)
                    finish()
                }

                badgeBtn!!.setOnClickListener{
                    setAcc(badgeChosen!!)
                    val intents = Intent(this,  PrizeListActivity::class.java)
                    //Log.d(TAG,badgeChosen.toString())
                    startActivity(intents)
                    finish()
                }
                getPrize()
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }


    }

    override fun onResume()
    {
        super.onResume()
        getPrize()
    }

    fun getPrize () {
        val db = FirebaseFirestore.getInstance()
        db.collection("prizes").document(id.toString())
            .get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    prizeName = task.result["prizeName"] as String?
                    prizeDes =  task.result["prizeDescrypt"] as String?
                }
                else {onError(task.exception)}
                badge!!.setText(prizeName)
                badgeDes!!.setText(prizeDes)
            }
    }

    fun setAcc (id: ArrayList<String>) {
        val db = FirebaseFirestore.getInstance()
        db.collection("accounts").document(uid)
            .get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    db.collection("accounts").document(uid).update("Badge", id)
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