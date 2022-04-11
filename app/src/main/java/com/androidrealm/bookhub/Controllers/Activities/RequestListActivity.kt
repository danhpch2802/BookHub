package com.androidrealm.bookhub.Controllers.Activities


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
import com.google.firebase.firestore.*


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

        getDB()

//        val lists = ArrayList<Request>()
//        lists.add(Request("123", "sacd"))
//        lists.add(Request("123", "sachhaylam"))
//        lists.add(Request("124", "sachnhul"))
//        lists.add(Request("125", "sachnhulove"))
//
//        if (savedInstanceState == null) {
//            val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
//            val fragment: Fragment = RequestFragment.newInstance(lists)
//            ft.replace(R.id.fragment_rqlist_view, fragment)
//            ft.commit()
//        }


    }

    private fun getDB() {
        db = FirebaseFirestore.getInstance()
        db!!.collection("requests").orderBy("accountID", Query.Direction.ASCENDING)
            .addSnapshotListener(object : EventListener<QuerySnapshot>{
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null){
                    }


                }

            })
    }

}