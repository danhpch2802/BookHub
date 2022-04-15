package com.androidrealm.bookhub.Controllers.Activities

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.androidrealm.bookhub.Models.Account
import com.androidrealm.bookhub.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class AccountDetailActivity : AppCompatActivity() {
    var AvaBtn: ImageView? = null
    var username: EditText? = null
    var badge: TextView? = null
    var badgeTV: TextView? = null
    var point: TextView? = null
    var email: TextView? = null
    var delBtn: TextView?= null
    var saveBtn: TextView?= null

    var reBtn: ImageView?= null
//    var User:Account?= null
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
        setContentView(R.layout.activity_account_detail)

        uid = intent.getStringExtra("uid").toString()
        AvaBtn = findViewById(R.id.detailAccAvatar)
        username = findViewById(R.id.detailAccUsername)
        badge = findViewById(R.id.detail_badge_prize)
        point = findViewById(R.id.detail_point_prize)
        badgeTV = findViewById(R.id.badgeChosen)
        email = findViewById(R.id.emailAccDetail)
        delBtn = findViewById(R.id.delAccButton)
        reBtn = findViewById(R.id.accDetailReturn)
        saveBtn = findViewById(R.id.saveChangePass)
        getAcc()
        AvaBtn!!.setImageResource(R.drawable.amagami_cover)
        AvaBtn!!.setOnClickListener{
            Toast.makeText(this@AccountDetailActivity, "Submit Successfully!", Toast.LENGTH_SHORT).show()
        }

        badgeTV!!.setOnClickListener{

        }
        delBtn!!.setOnClickListener{
            val db = FirebaseFirestore.getInstance()
            db.collection("accounts").document(uid)
                .get().addOnSuccessListener { result ->
                    role = result.get("Role") as Long
                    if (role == 0L)
                    {Toast.makeText(this, "This is an admin!!", Toast.LENGTH_SHORT).show()}
                    else {
                    delAcc(uid)
                    val intents = Intent(this, AccountActivity::class.java)
                    startActivity(intents)
                    finish()
                    }
                }

        }
        reBtn!!.setOnClickListener{
            val intents = Intent(this, AccountActivity::class.java)
            startActivity(intents)
            finish()
        }
        saveBtn!!.setOnClickListener{
            val db = FirebaseFirestore.getInstance()
            db.collection("accounts").document(uid)
                .get().addOnSuccessListener { result ->
                    role = result.get("Role") as Long?
                    if (role == 0L)
                    {
                        Toast.makeText(this, "This is an admin!!", Toast.LENGTH_SHORT).show()
                    }
                    else
                    {
                        val name = username!!.text.toString().trim()
                        val email = email!!.text.toString().trim()
                        if(name.isEmpty()){
                            Toast.makeText(this, "Please enter your username", Toast.LENGTH_SHORT).show()
                        }
                        else
                        {
                            setAcc(name, email)
                            Toast.makeText(this, "Edit Successfully!", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this,  AccountActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                }
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

    fun delAcc(id : String)
    {
        val db = FirebaseFirestore.getInstance()
        db.collection("accounts").document(id)
            .delete()
//        val user = Firebase.auth.getUser(id)
//        user.delete()
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    Log.d(TAG, "User account deleted.")
//                }
//            }
    }
    fun onError(e: Exception?) {
        if (e != null) {
            Log.d("RewardError", "onError: " + e.message)
        }
    }

    fun setAcc (name: String, email: String) {
        val db = FirebaseFirestore.getInstance()
        db.collection("accounts").document(uid)
            .get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    db.collection("accounts").document(uid).update("username", name)
                    db.collection("accounts").document(uid).update("email", email)
                }
                else {onError(task.exception)}
            }
    }
}

