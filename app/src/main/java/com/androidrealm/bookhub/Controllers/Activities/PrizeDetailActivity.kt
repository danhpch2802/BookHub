package com.androidrealm.bookhub.Controllers.Activities

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.androidrealm.bookhub.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class PrizeDetailActivity : AppCompatActivity() {
    var prizeName:String? = "Unlock the book: The mythology of Bookahholic-chan"
    var prizeDes:String? = "The Bookaholic"
    var role:Long = 3
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

        var fireStore = FirebaseFirestore.getInstance()
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
                fireStore.collection("accounts").document(uid)
                    .get().addOnSuccessListener { result ->
                        role = result.get("Role") as Long
                        if (role == 1L) {
                            badgeBtn!!.setOnClickListener {
                            setAcc(badgeChosen!!)
                            val intents = Intent(this, ProfileActivity::class.java)
                            startActivity(intents)
                            finish()
                            }
                        }
                        else {
                            badgeBtn!!.setText("Delete Badge")
                            badgeBtn!!.setOnClickListener {
                                if (badgeChosen!![0] == "s1")
                                {
                                    Toast.makeText(this, "Can delete this badge. It 's the standard badge!", Toast.LENGTH_SHORT).show()
                                }
                                else {
                                    delBad(badgeChosen!![0])
                                    val intents = Intent(this, PrizeListActivity::class.java)
                                    startActivity(intents)
                                    finish()
                                }
                            }
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.w(TAG, "Error getting documents: ", exception)
                    }
                getPrize()
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

    fun delBad(id : String)
    {
        val db = FirebaseFirestore.getInstance()
        db.collection("prizes").document(id)
            .delete()
        db.collection("accounts")
            .get().addOnSuccessListener { result ->
                for (documents in result) {
                            db.collection("accounts").document(documents.id).update("BadgeOwn", FieldValue.arrayRemove(id))
                            db.collection("accounts").document(documents.id).update("BadgeUnown", FieldValue.arrayRemove(id))
                            db.collection("accounts").document(documents.id).update("Badge", FieldValue.arrayRemove(id))
                    }
                }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }
    }
    fun onError(e: Exception?) {
        if (e != null) {
            Log.d("RewardError", "onError: " + e.message)
        }
    }
}