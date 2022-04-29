package com.androidrealm.bookhub.Controllers.Activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
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

        // back btn
        findViewById<ImageView>(R.id.backIV).setOnClickListener {
            onBackPressed()
        }
        // add friend btn
        findViewById<ImageView>(R.id.addNewFriendIV).setOnClickListener {
            val intent = Intent(this, UserAddFriendsActivity::class.java)
            startActivity(intent)
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
        db.collection("accounts").document(currentUserId)
            .get()
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    for (friendID in task.result["FriendsList"] as ArrayList<*>) {
                        //get friend list
                        db.collection("accounts").whereEqualTo(FieldPath.documentId(), friendID)
                            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                                @SuppressLint("NotifyDataSetChanged")
                                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                                    if (error != null){
                                        Log.e("Firestore Error", error.message.toString())
                                        return
                                    }
                                    for (dc: DocumentChange in value?.documentChanges!!){
                                        if (dc.type == DocumentChange.Type.ADDED){
                                            usersList.add(dc.document.toObject(Account::class.java))
                                        }
                                    }
                                    myAdapter.notifyDataSetChanged()
                                }
                            })
                    }
                }
            }
    }


}
