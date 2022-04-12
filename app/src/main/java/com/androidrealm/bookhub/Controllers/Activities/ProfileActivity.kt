package com.androidrealm.bookhub.Controllers.Activities

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.androidrealm.bookhub.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_homepage.*

class ProfileActivity : AppCompatActivity() {
    lateinit var preferences: SharedPreferences
    var PrizeBtn: ImageButton? = null
    var HistoryBtn: ImageButton? = null
    var FavBtn: ImageButton? = null
    var ProfileBtn: ImageButton? = null
    var BookmarkBtn: ImageButton? = null
    var AvaBtn: ImageView? = null
    var ReBtn: ImageView? = null
    var SignoutBtn: ImageButton? = null

    var uid:String = ""

    var Badge2 = "2"
    var TotalBadge:Int? = 0
    var Point:Number? = 0
    var Name:String? = ""
    var Badge:String ?= ""
    var role:Long = 3

    var username: TextView? = null
    var badge: TextView? = null
    var badgeTV: TextView? = null
    var point: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        preferences = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
        uid = FirebaseAuth.getInstance().currentUser!!.uid

        PrizeBtn = findViewById(R.id.prize_btn_pf)
        HistoryBtn = findViewById(R.id.history_btn_pf)
        FavBtn = findViewById(R.id.fav_btn_pf)
        ProfileBtn = findViewById(R.id.account_btn_pf)
        BookmarkBtn = findViewById(R.id.bookmark_btn_pf)
        username = findViewById(R.id.pf_username)
        badge = findViewById(R.id.badge_pf)
        point = findViewById(R.id.point_prize_pf)
        badgeTV = findViewById(R.id.pf_prize)
        AvaBtn = findViewById(R.id.avatarpf_img)
        SignoutBtn = findViewById(R.id.signout_btn_pf)

        //ReBtn = findViewById(R.id.returnHomepage)

        getAcc()

        //Log.d(TAG, Badge2)
        AvaBtn!!.setImageResource(R.drawable.amagami_cover)
        PrizeBtn!!.setOnClickListener{
            val intent = Intent(this,  PrizeActivity::class.java)
            startActivity(intent)
        }

        HistoryBtn!!.setOnClickListener{
            Toast.makeText(this@ProfileActivity, "Submit Successfully!", Toast.LENGTH_SHORT).show()
        }

        FavBtn!!.setOnClickListener{
            Toast.makeText(this@ProfileActivity, "Submit Successfully!", Toast.LENGTH_SHORT).show()
        }

        ProfileBtn!!.setOnClickListener{
            val intent = Intent(this,  UpdateAccActivity::class.java)
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
                            val intent = Intent(this,  RequestActivity::class.java)
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
                    var cnt = 0
                    for (document in task.result["BadgeOwn"] as ArrayList<*>) {
                        cnt++
                    }
                    for (document in task.result["Badge"] as ArrayList<*>) {
                        Badge2 = document as String
                        break
                    }
                    TotalBadge = cnt
                    Point =  task.result["Point"] as Number?
                }
                else {onError(task.exception)}
                username!!.setText(Name)
                badge!!.setText(TotalBadge.toString())
                point!!.setText(Point.toString())
                val db2 = FirebaseFirestore.getInstance()
                db2.collection("prizes").document(Badge2).get().addOnCompleteListener { task2 ->
                    if (task2.isSuccessful) {
                        Badge = task2.result["prizeName"] as? String
                    }
                    else {
                        onError(task2.exception)
                    }
                    badgeTV!!.setText(Badge)
                    if (Badge2 == "b1" || Badge2 =="b2")
                    {
                        badgeTV!!.setBackgroundColor(Color.parseColor("#f7971e"))
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