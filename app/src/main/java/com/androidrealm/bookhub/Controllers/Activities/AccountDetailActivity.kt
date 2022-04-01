package com.androidrealm.bookhub.Controllers.Activities

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.androidrealm.bookhub.Models.Account
import com.androidrealm.bookhub.R
import com.google.firebase.firestore.FirebaseFirestore

class AccountDetailActivity : AppCompatActivity() {
    var AvaBtn: ImageView? = null
    var username: TextView? = null
    var badge: TextView? = null
    var badgeTV: TextView? = null
    var point: TextView? = null
    var email: TextView? = null

////    val uid:String = intent.getStringExtra("uid").toString()
//    var User:Account?= null
    val uid:String = "ERQnHq5YlmL78h2wDBQX"
    var TotalBadge:Int? = 0
    var Point:Number? = 0
    var Name:String? = ""
    var Badge:String? = ""
    var Email:String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_detail)

        AvaBtn = findViewById(R.id.detailAccAvatar)
        username = findViewById(R.id.detailAccUsername)
        badge = findViewById(R.id.detail_badge_prize)
        point = findViewById(R.id.detail_point_prize)
        badgeTV = findViewById(R.id.badgeChosen)
        email = findViewById(R.id.emailAccDetail)

        getAcc()
        AvaBtn!!.setImageResource(R.drawable.amagami_cover)

        AvaBtn!!.setOnClickListener{
            Toast.makeText(this@AccountDetailActivity, "Submit Successfully!", Toast.LENGTH_SHORT).show()
        }
    }
//
    fun getAcc () {
    val db = FirebaseFirestore.getInstance()
    db.collection("accounts").document(uid)
        .get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Document found in the offline cache
                    Name = task.result["username"] as String?
                    Email = task.result["Email"] as String?
                    var cnt = 0
                    for (document in task.result["Badge"] as ArrayList<*>) {
                        cnt++
                    }
                for (document in task.result["Badge"] as ArrayList<*>) {
                    Badge = document as String
                    break
                }
                    TotalBadge = cnt
                Point =  task.result["Point"] as Number?

                }
            else {onError(task.exception)}
            username!!.setText(Name)
            badge!!.setText(TotalBadge.toString())
            point!!.setText(Point.toString())
            badgeTV!!.setText(Badge)
            email!!.setText(Email)
            }
        }


    fun onError(e: Exception?) {
        if (e != null) {
            Log.d("RewardError", "onError: " + e.message)
        }
    }
    
//    fun getAcc2 () {
//        val db = FirebaseFirestore.getInstance()
//        db.collection("accounts").document(id)
//            .get()
//            .addOnSuccessListener { documentSnapshot ->
//                val city = documentSnapshot.toObject<Account>()
//                //println(city)
//            }
////            badge!!.setText(x.toString())
////            point!!.setText(x2.toString())
////            badgeTV!!.setText(y2)
//    }
}
