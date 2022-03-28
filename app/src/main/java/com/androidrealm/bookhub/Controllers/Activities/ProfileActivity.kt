package com.androidrealm.bookhub.Controllers.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.androidrealm.bookhub.R

class ProfileActivity : AppCompatActivity() {
    var PrizeBtn: ImageButton? = null
    var HistoryBtn: ImageButton? = null
    var FavBtn: ImageButton? = null
    var ProfileBtn: ImageButton? = null
    var BookmarkBtn: ImageButton? = null
    var AvaBtn: ImageView? = null

    var x:Int? = 10
    var x2:Int? = 1000
    var y:String? = "Danh"
    var y2:String? = "The Bookaholic"
    var username: TextView? = null
    var badge: TextView? = null
    var badgeTV: TextView? = null
    var point: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

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

        username!!.setText(y)
        badge!!.setText(x.toString())
        point!!.setText(x2.toString())
        badgeTV!!.setText(y2)

        AvaBtn!!.setImageResource(R.drawable.meika_cover)
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
            Toast.makeText(this@ProfileActivity, "Submit Successfully!", Toast.LENGTH_SHORT).show()
        }
    }


}