package com.androidrealm.bookhub.Controllers.Activities


import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidrealm.bookhub.Adapter.AccountAdapter
import com.androidrealm.bookhub.Models.Account
import com.androidrealm.bookhub.R
import com.google.firebase.firestore.*


class AccountActivity : AppCompatActivity() {
    var recyclerView: RecyclerView?= null
    var accountList: ArrayList<Account> ?= null
    var myAdapter: AccountAdapter?= null
    var db: FirebaseFirestore ?= null
    var reBtn: ImageView?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)
        recyclerView = findViewById(R.id.acc_list)
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.layoutManager = LinearLayoutManager(this)

        accountList = arrayListOf()
        myAdapter = AccountAdapter(accountList!!)
        recyclerView!!.adapter = myAdapter
        reBtn = findViewById(R.id.accListReturn)

        reBtn!!.setOnClickListener{
            finish()
        }

        myAdapter!!.setOnItemClickListener(object : AccountAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                val clickedItem = accountList!![position]
                val intent = Intent(this@AccountActivity, AccountDetailActivity::class.java)
                intent.putExtra("uid", clickedItem.documentId)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                finish()
            }
        })

        getDB()
    }

//    override fun onResume(){
//        super.onResume()
//    }

    private fun getDB() {
        db = FirebaseFirestore.getInstance()
        db!!.collection("accounts").orderBy("username", Query.Direction.ASCENDING)
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null){
                        Log.e("Firestore Error", error.message.toString())
                        return
                    }
                    for (dc: DocumentChange in value?.documentChanges!!){
                        if (dc.type == DocumentChange.Type.ADDED){
                            accountList!!.add(dc.document.toObject(Account::class.java))
                        }
                    }
                    myAdapter!!.notifyDataSetChanged()
                }
            })
    }

    fun onError(e: Exception?) {
        if (e != null) {
            Log.d("RewardError", "onError: " + e.message)
        }
    }
}