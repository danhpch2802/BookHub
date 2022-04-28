package com.androidrealm.bookhub.Controllers.Activities

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.androidrealm.bookhub.R
import com.google.firebase.firestore.FirebaseFirestore

class UserAccountDetailActivity : AppCompatActivity() {
    var AvaBtn: ImageView? = null
    var username: TextView? = null
    var badge: TextView? = null
    var badgeTV: TextView? = null
    var point: TextView? = null
    var email: TextView? = null
    var friendBtn: TextView?= null
    var reBtn: ImageView?= null
    var delBtn: TextView?= null
    var uid:String = "ERQnHq5YlmL78h2wDBQX"
    var TotalBadge:Int? = 0
    var Point:Number? = 0
    var Name:String? = ""
    var Badge:String? = ""
    var Badge2:String? = ""
    var Email:String? = ""
    var role :Long?=3L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_account_detail)

        uid = intent.getStringExtra("uid").toString()
        AvaBtn = findViewById(R.id.detailAccAvatar2)
        username = findViewById(R.id.detailAccUsername2)
        badge = findViewById(R.id.detail_badge_prize2)
        point = findViewById(R.id.detail_point_prize2)
        badgeTV = findViewById(R.id.badgeChosen2)
        email = findViewById(R.id.emailAccDetail2)
        delBtn = findViewById(R.id.delAccButton2)
        reBtn = findViewById(R.id.accDetailReturn2)
        friendBtn = findViewById(R.id.saveFriendPass2)

        reBtn!!.setOnClickListener{
            finish()
        }

        getAcc()
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
                    var avatar:String ?= ""
                    avatar = task.result["Avatar"] as String?
                    when (avatar) {
                        "1" -> AvaBtn!!.setImageResource(R.drawable.amagami_cover)
                        "2" -> AvaBtn!!.setImageResource(R.drawable.doll_cover)
                        "3" -> AvaBtn!!.setImageResource(R.drawable.fechippuru_cover)
                        "4" -> AvaBtn!!.setImageResource(R.drawable.kanojo_cover)
                        "5" -> AvaBtn!!.setImageResource(R.drawable.komi_cover)
                        "6" -> AvaBtn!!.setImageResource(R.drawable.meika_cover)
                        "7" -> AvaBtn!!.setImageResource(R.drawable.mokanojo_cover)
                        "8" -> AvaBtn!!.setImageResource(R.drawable.tonikaku_cover)
                        "9" -> AvaBtn!!.setImageResource(R.drawable.yofukashi_cover)
                        else -> AvaBtn!!.setImageResource(R.drawable.amagami_cover)
                    }
                    var cnt = 0
                    for (document in task.result["BadgeOwn"] as ArrayList<*>) {
                        cnt++
                    }
                    for (document in task.result["Badge"] as ArrayList<*>) {
                        Badge = document as String
                        break
                    }
                    TotalBadge = cnt
                    Point =  task.result["Point"] as Number?
                    role = task.result["Role"] as Long?
                }
                else {onError(task.exception)}
                username!!.setText(Name)
                badge!!.setText(TotalBadge.toString())
                point!!.setText(Point.toString())
                val db2 = FirebaseFirestore.getInstance()
                email!!.setText(Email)
                if(role == 0L)
                {
                    badgeTV!!.setText("Admin")
                    badgeTV!!.setBackgroundColor(Color.parseColor("#f7971e"))
                }
                else{
                    db2.collection("prizes").document(Badge!!).get().addOnCompleteListener { task2 ->
                        if (task2.isSuccessful) {
                            Badge2 = task2.result["prizeName"] as? String
                        } else {
                            onError(task2.exception)
                        }
                        badgeTV!!.setText(Badge2)
                    }
                }
            }

    }
    fun onError(e: Exception?) {
        if (e != null) {
            Log.d("RewardError", "onError: " + e.message)
        }
    }
}