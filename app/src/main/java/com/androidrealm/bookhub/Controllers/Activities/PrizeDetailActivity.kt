package com.androidrealm.bookhub.Controllers.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.androidrealm.bookhub.R

class PrizeDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        var y:String? = "Unlock the book: The mythology of Bookahholic-chan"
        var y2:String? = "The Bookaholic"
        var badge: TextView? = null
        var badgeDes: TextView? = null
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prize_detail)

        badge = findViewById(R.id.prizeReceived)
        badgeDes = findViewById(R.id.prizeDescryption)
        badge!!.setText(y2)
        badgeDes!!.setText(y)
    }
}