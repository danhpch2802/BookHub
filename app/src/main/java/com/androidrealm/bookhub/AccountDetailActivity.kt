package com.androidrealm.bookhub

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

class AccountDetailActivity : AppCompatActivity() {
    var AvaBtn: ImageView? = null
    var x:Int? = 10
    var x2:Int? = 1000
    var y:String? = "Danh"
    var y2:String? = "The Bookaholic"
    var username: TextView? = null
    var badge: TextView? = null
    var badgeTV: TextView? = null
    var point: TextView? = null
    var email: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_detail)

        AvaBtn = findViewById(R.id.detailAccAvatar)
        username = findViewById(R.id.detailAccUsername)
        badge = findViewById(R.id.detail_badge_prize)
        point = findViewById(R.id.detail_point_prize)
        badgeTV = findViewById(R.id.badgeChosen)

        email = findViewById(R.id.emailAccDetail)

        AvaBtn!!.setImageResource(R.drawable.amagami_cover)
        username!!.setText(y)
        badge!!.setText(x.toString())
        point!!.setText(x2.toString())
        badgeTV!!.setText(y2)
        email!!.setText("danh1@gmail.com")
        AvaBtn!!.setOnClickListener{
            Toast.makeText(this@AccountDetailActivity, "Submit Successfully!", Toast.LENGTH_SHORT).show()
        }
    }
}