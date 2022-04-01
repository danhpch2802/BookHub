package com.androidrealm.bookhub.Controllers.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.*
import com.androidrealm.bookhub.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_homepage.*

class UpdateAccActivity : AppCompatActivity() {

    var uid:String = ""
    var TotalBadge:Int? = 0
    var Point:Number? = 0
    var Name:String? = ""
    var Badge:String? = ""
    var Email:String? = ""

    var AvaBtn: ImageView? = null
    var username: EditText? = null
    var badge: TextView? = null
    var badgeTV: TextView? = null
    var point: TextView? = null
    var email: EditText? = null
    var saveBtn: Button? = null
    var ReBtn: ImageView? = null

    var pass: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_acc)

        uid = intent.getStringExtra("uid").toString()

        AvaBtn = findViewById(R.id.updateAccAvatar)
        saveBtn = findViewById(R.id.updateButton)
        username = findViewById(R.id.updateAccUsername)
        badge = findViewById(R.id.badge_prize)
        point = findViewById(R.id.point_prize)
        badgeTV = findViewById(R.id.badgeChosen)
        email = findViewById(R.id.emailAcc)
        pass = findViewById(R.id.passAcc)
        getAcc()
        AvaBtn!!.setImageResource(R.drawable.amagami_cover)

        ReBtn = findViewById(R.id.returnProfile)


        AvaBtn!!.setOnClickListener{
            Toast.makeText(this@UpdateAccActivity, "Edit Successfully!", Toast.LENGTH_SHORT).show()
        }

        ReBtn!!.setOnClickListener{
            val intent = Intent(this,  ProfileActivity::class.java)
            intent.putExtra("uid", uid)
            startActivity(intent)
        }
        saveBtn!!.setOnClickListener{
            Toast.makeText(this@UpdateAccActivity, "Edit Successfully!", Toast.LENGTH_SHORT).show()
            val name = username!!.text.toString().trim()
            val pass = pass!!.text.toString().trim()

            val email = email!!.text.toString().trim()

            if(pass.isEmpty()){
                Toast.makeText(this, "Please enter a password", Toast.LENGTH_SHORT).show()
            }
            if(email.isEmpty()){
                Toast.makeText(this, "Please enter an email", Toast.LENGTH_SHORT).show()
            }
            if(name.isEmpty()){
                Toast.makeText(this, "Please enter your username", Toast.LENGTH_SHORT).show()
            }
            else
            {
                setAcc(name, email)
                Handler().postDelayed({
                val intent = Intent(this,  ProfileActivity::class.java)
                intent.putExtra("uid", uid)
                startActivity(intent)
                }, 700)
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
                    Email = task.result["email"] as String?
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
                username!!.setHint(Name)
                badge!!.setText(TotalBadge.toString())
                point!!.setText(Point.toString())
                badgeTV!!.setText(Badge)
                email!!.setHint(Email)

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

