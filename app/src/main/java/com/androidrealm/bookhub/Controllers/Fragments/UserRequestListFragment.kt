package com.androidrealm.bookhub.Controllers.Fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidrealm.bookhub.Adapter.RequestAdapter
import com.androidrealm.bookhub.Controllers.Activities.RequestDetailActivity
import com.androidrealm.bookhub.R
import com.androidrealm.bookhub.Models.Request
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.ktx.Firebase

class UserRequestListFragment : Fragment() {
    var recyclerView: RecyclerView?= null
    var requestList: ArrayList<Request> ?= null
    var myAdapter: RequestAdapter?= null
    var db: FirebaseFirestore ?= null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_user_request_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.rq_list)
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.layoutManager = LinearLayoutManager(context)
    }

    override fun onResume() {
        super.onResume()

        requestList = arrayListOf()
        myAdapter = RequestAdapter(requestList!!)
        recyclerView!!.adapter = myAdapter

        myAdapter!!.setOnItemClickListener(object : RequestAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                val clickedItem = requestList!![position]
                val intent = Intent(context, RequestDetailActivity::class.java)
                intent.putExtra("documentID", clickedItem.documentId)
                startActivity(intent)
            }
        })
        getDB()
    }

    private fun getDB() {
        db = FirebaseFirestore.getInstance()
        val userid = FirebaseAuth.getInstance().currentUser!!.uid
        db!!.collection("requests").document(userid).get()
            .addOnSuccessListener {

            }

    }
}