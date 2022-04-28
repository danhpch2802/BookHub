package com.androidrealm.bookhub.Controllers.Activities

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidrealm.bookhub.Adapter.UserFriendsAdapter
import com.androidrealm.bookhub.Models.Account
import com.androidrealm.bookhub.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*

class UserFriendsListActivity: AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var usersList: ArrayList<Account>
    private lateinit var myAdapter: UserFriendsAdapter
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friends_list)

        findViewById<ImageView>(R.id.backIV).setOnClickListener {
            onBackPressed()
        }

        db = FirebaseFirestore.getInstance()
        recyclerView = findViewById(R.id.users_list_RV)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
    }

    override fun onResume() {
        super.onResume()
        usersList = arrayListOf()
        myAdapter = UserFriendsAdapter(usersList)

        recyclerView.adapter = myAdapter

        myAdapter.setOnItemClickListener(object : UserFriendsAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                val clickedItem = usersList[position]
                val intent = Intent(this@UserFriendsListActivity, UserDetailActivity::class.java)
                intent.putExtra("uid", clickedItem.documentId)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                finish()
            }
        })

        getFriendsList()
    }

    private fun getFriendsList() {
        val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
        val docRef = db.collection("accounts").document(currentUserId)
        docRef .get()
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    for (friend in task.result["FriendsList"] as ArrayList<*>) {
                        usersList.add(friend as Account)
                    }
                }
            }
    }


}
