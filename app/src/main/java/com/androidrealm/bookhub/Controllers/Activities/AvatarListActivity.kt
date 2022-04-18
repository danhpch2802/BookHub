package com.androidrealm.bookhub.Controllers.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.androidrealm.bookhub.Controllers.Fragments.AvatarFragment
import com.androidrealm.bookhub.Controllers.Fragments.ListComicFragment
import com.androidrealm.bookhub.R
import com.google.firebase.auth.FirebaseAuth

class AvatarListActivity : AppCompatActivity() {
    var uid:String = "ERQnHq5YlmL78h2wDBQX"
    var reBtn :ImageView ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_avatar_list)
        uid = FirebaseAuth.getInstance().currentUser!!.uid
        reBtn = findViewById(R.id.avaReturn)

        reBtn!!.setOnClickListener{
            finish()
        }
        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
        val fragment: Fragment = AvatarFragment.newInstance(3)
        ft.replace(R.id.ava_fragment_container_view, fragment)
        ft.commit()
    }
}