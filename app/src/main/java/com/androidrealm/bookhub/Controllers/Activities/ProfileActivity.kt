package com.androidrealm.bookhub.Controllers.Activities

import android.content.Intent
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
    var PrizeBtn: ImageButton? = null
    var HistoryBtn: ImageButton? = null
    var FavBtn: ImageButton? = null
    var ProfileBtn: ImageButton? = null
    var BookmarkBtn: ImageButton? = null
    var AvaBtn: ImageView? = null
    var ReBtn: ImageView? = null
    var SignoutBtn: ImageButton? = null


    var uid:String = ""
    var TotalBadge:Int? = 0
    var Point:Number? = 0
    var Name:String? = ""
    var Badge:String? = ""

    var username: TextView? = null
    var badge: TextView? = null
    var badgeTV: TextView? = null
    var point: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

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

        ReBtn = findViewById(R.id.returnHomepage)

        getAcc()
        AvaBtn!!.setImageResource(R.drawable.amagami_cover)
        PrizeBtn!!.setOnClickListener{
            Toast.makeText(this@ProfileActivity, "Submit Successfully!", Toast.LENGTH_SHORT).show()
        }

        HistoryBtn!!.setOnClickListener{
            Toast.makeText(this@ProfileActivity, "Submit Successfully!", Toast.LENGTH_SHORT).show()
        }

        FavBtn!!.setOnClickListener{
            Toast.makeText(this@ProfileActivity, "Submit Successfully!", Toast.LENGTH_SHORT).show()
        }

        ProfileBtn!!.setOnClickListener{
            val intent = Intent(this,  UpdateAccActivity::class.java)
            intent.putExtra("uid", uid)
            startActivity(intent)
        }

        SignoutBtn!!.setOnClickListener{
            //Log out
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        ReBtn!!.setOnClickListener{
            val intent = Intent(this,  HomePageActivity::class.java)
            intent.putExtra("uid", uid)
            startActivity(intent)
        }

        bottom_navigation.menu.findItem(R.id.profile_item).isChecked = true

        bottom_navigation.setOnNavigationItemSelectedListener { menuItem ->
            when {
                menuItem.itemId == R.id.home_item -> {
                    val intent = Intent(this,  HomePageActivity::class.java)
                    intent.putExtra("uid", uid)
                    startActivity(intent)
                    return@setOnNavigationItemSelectedListener true
                }
                menuItem.itemId == R.id.manage_book_item -> {
                    val intent = Intent(this,  RequestActivity::class.java)
                    intent.putExtra("uid", uid)
                    startActivity(intent)
                    return@setOnNavigationItemSelectedListener true
                }
                menuItem.itemId == R.id.profile_item -> {
                    return@setOnNavigationItemSelectedListener true
                }
                else -> false
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
            }
    }


    fun onError(e: Exception?) {
        if (e != null) {
            Log.d("RewardError", "onError: " + e.message)
        }
    }


}