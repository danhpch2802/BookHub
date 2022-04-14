package com.androidrealm.bookhub.Controllers.Activities

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import com.androidrealm.bookhub.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class NewPrizeActivity : AppCompatActivity() {

    var reBtn : TextView?= null
    var cfBtn :TextView?= null
    var prizeName: EditText?=null
    var prizeDes: EditText?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_prize)
        reBtn = findViewById(R.id.reNewPrizeBtn)
        cfBtn = findViewById(R.id.newPrize)
        prizeDes = findViewById( R.id.prizeDescription)
        prizeName = findViewById(R.id.prizeName)
        var totalBigGift = 2
        reBtn!!.setOnClickListener{
            val intents = Intent(this,  PrizeListActivity::class.java)
            startActivity(intents)
            finish()
        }
        cfBtn!!.setOnClickListener{
            val prize_name = prizeName!!.text.toString().trim()
            val prize_des = prizeDes!!.text.toString().trim()
            // Save to Firestore
            val db = FirebaseFirestore.getInstance()

            val newPrize: MutableMap<String, Any> = HashMap()
            newPrize["prizeDescrypt"] = prize_des
            newPrize["prizeName"] = prize_name
            newPrize["id"] = ""
            db.collection("prizes").add(newPrize).addOnSuccessListener { documentReference ->
                db.collection("accounts")
                    .get().addOnSuccessListener { result ->
                        for (documents in result) {
                            db.collection("accounts").document(documents.id).update("BadgeUnown", FieldValue.arrayUnion(documentReference.id))
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.d(ContentValues.TAG, "Error getting documents: ", exception)
                    }
                db.collection("prizes").document(documentReference.id).update("id", documentReference.id)
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }
                //.document(prize_id)
                //.set(newPrize)
            val intents = Intent(this,  PrizeListActivity::class.java)
            startActivity(intents)
            finish()
        }

    }


}