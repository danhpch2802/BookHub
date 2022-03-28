package com.androidrealm.bookhub.Controllers.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.androidrealm.bookhub.R

class PrizeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        var AvaBtn: ImageView? = null
        var x:Int? = 10000
        var y:String? = "Danh"
        var y2:String? = "The Bookaholic"
        var username: TextView? = null
        var badge: TextView? = null
        var point: TextView? = null

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prize)

        AvaBtn = findViewById(R.id.detailPrizeAvatar)
        username = findViewById(R.id.usernamePrize)
        badge = findViewById(R.id.badgePrize)
        point = findViewById(R.id.enpoint_textview)

        AvaBtn!!.setImageResource(R.drawable.amagami_cover)
        username!!.setText(y)
        badge!!.setText(y2)
        point!!.setText(x.toString())
    }
}