package com.androidrealm.bookhub.Controllers.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.androidrealm.bookhub.R
import com.google.firebase.firestore.FirebaseFirestore

class PrizeDetailActivity : AppCompatActivity() {
    var prizeName:String? = "Unlock the book: The mythology of Bookahholic-chan"
    var prizeDes:String? = "The Bookaholic"

    var id:String? = "b2"
    var badge: TextView? = null
    var badgeDes: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prize_detail)

        val intent = intent
        id = intent.getStringExtra("id")

        badge = findViewById(R.id.prizeReceived)
        badgeDes = findViewById(R.id.prizeDescryption)
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
    fun onError(e: Exception?) {
        if (e != null) {
            Log.d("RewardError", "onError: " + e.message)
        }
    }
}