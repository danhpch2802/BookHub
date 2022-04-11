package com.androidrealm.bookhub.Controllers.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.*
import com.androidrealm.bookhub.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_homepage.*

class UpdateAccActivity : AppCompatActivity() {

    var uid:String = ""
    var TotalBadge:Int? = 0
    var Point:Number? = 0
    var Name:String? = ""
    var Badge:String? = ""
    var Email:String? = ""

    var Badge2 = "2"

    var Role:Long? = 3
    var AvaBtn: ImageView? = null
    var username: EditText? = null
    var badge: TextView? = null
    var badgeTV: TextView? = null
    var point: TextView? = null
    var email: TextView? = null
    var saveBtn: TextView? = null
    var ReBtn: ImageView? = null
    var passBtn: TextView? = null
    lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_acc)
        uid = FirebaseAuth.getInstance().currentUser!!.uid

        AvaBtn = findViewById(R.id.updateAccAvatar)
        saveBtn = findViewById(R.id.updateButton)
        username = findViewById(R.id.updateAccUsername)
        badge = findViewById(R.id.badge_prize)
        point = findViewById(R.id.point_prize)
        badgeTV = findViewById(R.id.badgeChosen)
        email = findViewById(R.id.emailAcc)
        passBtn = findViewById(R.id.changePass)
        getAcc()
        AvaBtn!!.setImageResource(R.drawable.amagami_cover)
        ReBtn = findViewById(R.id.returnProfile)

        AvaBtn!!.setOnClickListener{
            Toast.makeText(this@UpdateAccActivity, "Edit Successfully!", Toast.LENGTH_SHORT).show()
        }



        passBtn!!.setOnClickListener{
            val intent = Intent(this,  ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

        saveBtn!!.setOnClickListener{
            val name = username!!.text.toString().trim()
            val email = email!!.text.toString().trim()

            if(name.isEmpty()){
                Toast.makeText(this@UpdateAccActivity, "Please enter your username", Toast.LENGTH_SHORT).show()
            }
            else
            {
                Toast.makeText(this@UpdateAccActivity, "Edit Successfully!", Toast.LENGTH_SHORT).show()
                setAcc(name, email)
                finish()
            }
        }
    }

    fun getAcc () {
        val db = FirebaseFirestore.getInstance()
        db.collection("accounts").document(uid)
            .get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Document found in the offline cache
                    Name = task.result["username"] as String?
                    var cnt = 0
                    for (document in task.result["Badge"] as ArrayList<*>) {
                        cnt++
                    }
                    for (document in task.result["Badge"] as ArrayList<*>) {
                        Badge2 = document as String
                        break
                    }
                    TotalBadge = cnt
                    Point =  task.result["Point"] as Number?
                    Role = task.result["Point"] as Long?

                }
                else {onError(task.exception)}

                Email = FirebaseAuth.getInstance().currentUser!!.email
                username!!.setHint("New Username")
                badge!!.setText(TotalBadge.toString())
                point!!.setText(Point.toString())
                badgeTV!!.setText(Badge)
                email!!.setText(Email)
                if (Role == 1L) {
                    val db2 = FirebaseFirestore.getInstance()
                    db2.collection("prizes").document(Badge2).get().addOnCompleteListener { task2 ->
                        if (task2.isSuccessful) {
                            Badge = task2.result["prizeName"] as? String
                        } else {
                            onError(task2.exception)
                        }
                        badgeTV!!.setText(Badge)
                        ReBtn!!.setOnClickListener{
                            val intent = Intent(this,  ProfileActivity::class.java)
                            startActivity(intent)
                        }
                    }
                }
                else{
                    ReBtn!!.setOnClickListener{
                        val intent = Intent(this,  AdminProfileActivity::class.java)
                        startActivity(intent)
                    }
                    badgeTV!!.setText("Admin")
                }

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
    fun onError(e: Exception?) {
        if (e != null) {
            Log.d("RewardError", "onError: " + e.message)
        }
    }
}

