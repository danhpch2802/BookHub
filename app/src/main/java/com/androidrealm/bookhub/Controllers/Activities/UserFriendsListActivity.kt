package com.androidrealm.bookhub.Controllers.Activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidrealm.bookhub.Adapter.UserFriendsAdapter
import com.androidrealm.bookhub.Models.User
import com.androidrealm.bookhub.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*

class UserFriendsListActivity: AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var userFriendsList: ArrayList<User>
    private lateinit var myAdapter: UserFriendsAdapter
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friends_list)

        findViewById<ImageView>(R.id.backIV).setOnClickListener {
            onBackPressed()
        }

        db = FirebaseFirestore.getInstance()
        recyclerView = findViewById(R.id.user_friends_list_RV)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
    }

    override fun onResume() {
        super.onResume()
        userFriendsList = arrayListOf()
        myAdapter = UserFriendsAdapter(userFriendsList)

        recyclerView.adapter = myAdapter
    }
}