package com.androidrealm.bookhub.Controllers.Activities

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.androidrealm.bookhub.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class UserDetailActivity : AppCompatActivity() {
    var avatar: ImageView? = null
    var username: TextView? = null
    var badge: TextView? = null
    var badgeTV: TextView? = null
    var delBtn: TextView ?= null
    var point: TextView? = null
    var email: TextView? = null
    private lateinit var db : FirebaseFirestore

    var uid:String = ""
    var Name:String? = ""
    var Badge:String? = ""
    var Email:String? = ""
    var TotalBadge:Int? = 0
    var Point:Number? = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detail)

        uid = intent.getStringExtra("uid").toString()
        avatar = findViewById(R.id.detailAccAvatar)
        username = findViewById(R.id.detailAccUsername)
        badge = findViewById(R.id.detail_badge_prize)
        point = findViewById(R.id.detail_point_prize)
        badgeTV = findViewById(R.id.badgeChosen)
        email = findViewById(R.id.emailAccDetail)
        delBtn = findViewById(R.id.deleteFriendBtn)

        db = FirebaseFirestore.getInstance()

        delBtn!!.setOnClickListener{
            val currentUserID = FirebaseAuth.getInstance().currentUser!!.uid
            db.collection("accounts").document(currentUserID)
                .update("FriendsList", FieldValue.arrayRemove(uid))

            Toast.makeText(this, "Friend has been deleted from List", Toast.LENGTH_LONG).show()
        }
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
                    // Document found in the offline cache
                    Name = task.result["username"] as String?
                    Email = task.result["Email"] as String?
                    var ava:String ?= ""
                    ava = task.result["Avatar"] as String?
                    when (ava) {
                        "1" -> avatar!!.setImageResource(R.drawable.amagami_cover)
                        "2" -> avatar!!.setImageResource(R.drawable.doll_cover)
                        "3" -> avatar!!.setImageResource(R.drawable.fechippuru_cover)
                        "4" -> avatar!!.setImageResource(R.drawable.kanojo_cover)
                        "5" -> avatar!!.setImageResource(R.drawable.komi_cover)
                        "6" -> avatar!!.setImageResource(R.drawable.meika_cover)
                        "7" -> avatar!!.setImageResource(R.drawable.mokanojo_cover)
                        "8" -> avatar!!.setImageResource(R.drawable.tonikaku_cover)
                        "9" -> avatar!!.setImageResource(R.drawable.yofukashi_cover)
                        else -> avatar!!.setImageResource(R.drawable.amagami_cover)
                    }
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

            }
    }

    fun onError(e: Exception?) {
        if (e != null) {
            Log.d("RewardError", "onError: " + e.message)
        }
    }
}