package com.androidrealm.bookhub

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

class ProfileActivity : AppCompatActivity() {
    var PrizeBtn: ImageView? = null
    var HistoryBtn: ImageView? = null
    var FavBtn: ImageView? = null
    var ProfileBtn: ImageView? = null
    var BookmarkBtn: ImageView? = null

    var x:Int? = 10
    var x2:Int? = 1000
    var y:String? = "Danh"
    var username: TextView? = null
    var badge: TextView? = null
    var point: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        PrizeBtn = findViewById(R.id.prize_btn)
        HistoryBtn = findViewById(R.id.history_btn)
        FavBtn = findViewById(R.id.fav_btn)
        ProfileBtn = findViewById(R.id.pf_btn)
        BookmarkBtn = findViewById(R.id.bookmark_btn)

        username = findViewById(R.id.pf_username)
        badge = findViewById(R.id.badge_prize)
        point = findViewById(R.id.point_prize)

    }
}