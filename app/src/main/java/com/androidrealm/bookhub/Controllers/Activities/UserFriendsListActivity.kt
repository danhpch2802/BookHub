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
import com.google.firebase.firestore.EventListener
import java.util.*
import kotlin.collections.ArrayList

class UserFriendsListActivity: AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var usersList: ArrayList<Account>
    private lateinit var tempList: ArrayList<Account>
    private lateinit var myAdapter: UserFriendsAdapter
    private lateinit var searchView: androidx.appcompat.widget.SearchView
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
        recyclerView = findViewById(R.id.friends_RV)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        searchView = findViewById(R.id.user_friends_searchView)
    }

    override fun onResume() {
        super.onResume()
        searchView.clearFocus()
        usersList = arrayListOf()
        tempList = arrayListOf()
        myAdapter = UserFriendsAdapter(tempList)

        recyclerView.adapter = myAdapter

        myAdapter.setOnItemClickListener(object : UserFriendsAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                val clickedItem = tempList[position]
                val intent = Intent(this@UserFriendsListActivity, UserDetailActivity::class.java)
                intent.putExtra("uid", clickedItem.documentId)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
        })

        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                searchView.clearFocus()
                return false
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onQueryTextChange(p0: String?): Boolean {
                tempList.clear()
                val searchText = p0!!.toLowerCase(Locale.getDefault())
                if(searchText.isNotEmpty()){
                    usersList.forEach{
                        if(it.username!!.toLowerCase(Locale.getDefault()).contains(searchText)){
                            tempList.add(it)
                        }
                    }
                    myAdapter.notifyDataSetChanged()
                }
                else{
                    tempList.clear()
                    tempList.addAll(usersList)
                    myAdapter.notifyDataSetChanged()
                }
                return false
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
                                    tempList.addAll(usersList)
                                    myAdapter.notifyDataSetChanged()
                                }
                            })
                    }
                }
            }
    }


}
