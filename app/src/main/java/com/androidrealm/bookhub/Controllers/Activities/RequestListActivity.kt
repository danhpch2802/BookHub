package com.androidrealm.bookhub.Controllers.Activities


import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidrealm.bookhub.Adapter.RequestAdapter
import com.androidrealm.bookhub.R
import com.androidrealm.bookhub.Models.Request
import com.androidrealm.bookhub.Controllers.Fragments.RequestFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.activity_homepage.*


class RequestListActivity : AppCompatActivity() {
    var recyclerView: RecyclerView ?= null
    var requestList: ArrayList<Request> ?= null
    var myAdapter: RequestAdapter ?= null
    var db: FirebaseFirestore ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_list)
        recyclerView = findViewById(R.id.rq_list)
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.layoutManager = LinearLayoutManager(this)

        requestList = arrayListOf()
        myAdapter = RequestAdapter(requestList!!)
        recyclerView!!.adapter = myAdapter

        bottom_navigation.menu.findItem(R.id.manage_book_item).isChecked = true
        findViewById<BottomNavigationView>(R.id.bottom_navigation).setOnNavigationItemSelectedListener { menuItem ->
            when {
                menuItem.itemId == R.id.home_item -> {
                    val intent = Intent(this,  HomePageActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
                    return@setOnNavigationItemSelectedListener true
                }
                menuItem.itemId == R.id.profile_item -> {
                                val intent = Intent(this,  AdminProfileActivity::class.java)
                                startActivity(intent)
                                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
                    return@setOnNavigationItemSelectedListener true
                }
                menuItem.itemId == R.id.manage_book_item -> {
                    return@setOnNavigationItemSelectedListener true
                }
                else -> false
            }
        }

        myAdapter!!.setOnItemClickListener(object : RequestAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                //Toast.makeText(this@RequestListActivity, "You click on item $position", Toast.LENGTH_SHORT).show()
                val clickedItem = requestList!![position]
                val intent = Intent(this@RequestListActivity, RequestDetailActivity::class.java)
                intent.putExtra("documentID", clickedItem.id)
//                intent.putExtra("accountID", clickedItem.AccountId)
//                intent.putExtra("bookName", clickedItem.bookName)
//                intent.putExtra("bookDetail", clickedItem.bookDetail)
//                intent.putExtra("checked", clickedItem.Checked)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                finish()
            }
        })
        getDB()
    }

    private fun getDB() {
        db = FirebaseFirestore.getInstance()
        db!!.collection("requests").orderBy("accountID", Query.Direction.ASCENDING)
            .addSnapshotListener(object : EventListener<QuerySnapshot>{
                @SuppressLint("NotifyDataSetChanged")
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null){
                        Log.e("Firestore Error", error.message.toString())
                        return
                    }

                    for (dc: DocumentChange in value?.documentChanges!!){
                        if (dc.type == DocumentChange.Type.ADDED){
                            requestList!!.add(dc.document.toObject(Request::class.java))
                        }
                    }
                    myAdapter!!.notifyDataSetChanged()
                }

            })
    }

}