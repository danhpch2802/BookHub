package com.androidrealm.bookhub.Controllers.Activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.androidrealm.bookhub.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_homepage.*

class AdminProfileActivity : AppCompatActivity() {
    lateinit var preferences: SharedPreferences
    var PrizeBtn: ImageButton? = null
    var MyAccBtn: ImageButton? = null
    var AllAccBtn: ImageButton? = null
    var AvaBtn: ImageView? = null
    var SignoutBtn: ImageButton? = null

    var uid:String = ""

    var Point:Number? = 0
    var Name:String? = ""
    var Badge:String ?= ""

    var username: TextView? = null
    var badge: TextView? = null
    var badgeTV: TextView? = null
    var point: TextView? = null

    var createNewBtn:Button?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_profile)


        preferences = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
        uid = FirebaseAuth.getInstance().currentUser!!.uid

        PrizeBtn = findViewById(R.id.prize_btn_pf)
        MyAccBtn = findViewById(R.id.my_acc_btn_pf)
        AllAccBtn = findViewById(R.id.all_account_btn_pf)
        username = findViewById(R.id.pf_username)
        badge = findViewById(R.id.badge_pf)
        point = findViewById(R.id.point_prize_pf)
        badgeTV = findViewById(R.id.pf_prize)
        AvaBtn = findViewById(R.id.avatarpf_img)
        SignoutBtn = findViewById(R.id.signout_btn_pf)
        createNewBtn=findViewById<Button>(R.id.createNewBtn)

        getAcc()

        //Log.d(TAG, Badge2)
        AvaBtn!!.setImageResource(R.drawable.amagami_cover)
        PrizeBtn!!.setOnClickListener{
            val intent = Intent(this,  PrizeListActivity::class.java)
            startActivity(intent)
        }

        MyAccBtn!!.setOnClickListener{
            val intent = Intent(this,  UpdateAccActivity::class.java)
            startActivity(intent)
        }

        AllAccBtn!!.setOnClickListener{
            val intent = Intent(this,  AccountActivity::class.java)
            startActivity(intent)
        }

        //Log out
        SignoutBtn!!.setOnClickListener{
            val editor: SharedPreferences.Editor = preferences.edit()
            editor.clear()
            editor.apply()
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        bottom_navigation.menu.findItem(R.id.profile_item).isChecked = true
        bottom_navigation.setOnNavigationItemSelectedListener { menuItem ->
            when {
                menuItem.itemId == R.id.home_item -> {
                    val intent = Intent(this,  HomePageActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
                    return@setOnNavigationItemSelectedListener true
                }
                menuItem.itemId == R.id.manage_book_item -> {
                    val intent = Intent(this,  RequestListActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    return@setOnNavigationItemSelectedListener true
                }
                menuItem.itemId == R.id.profile_item -> {
                    return@setOnNavigationItemSelectedListener true
                }
                else -> false
            }
        }

        createNewBtn!!.setOnClickListener {
            val intent=Intent(this,BookCreateActivity::class.java)
            startActivity(intent)
        }
    }

    override  fun onResume ()
    {
        super.onResume()
        getAcc()
    }

    fun getAcc () {
        val db = FirebaseFirestore.getInstance()
        db.collection("accounts").document(uid)
            .get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Name = task.result["username"] as String?
                    Point =  task.result["Point"] as Number?
                }
                else {onError(task.exception)}
                username!!.setText(Name)
                badge!!.setText("1")
                point!!.setText(Point.toString())

                badgeTV!!.setText("Admin")
            }
    }

    fun onError(e: Exception?) {
        if (e != null) {
            Log.d("RewardError", "onError: " + e.message)
        }
    }
}