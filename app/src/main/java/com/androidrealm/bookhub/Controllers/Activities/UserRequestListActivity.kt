package com.androidrealm.bookhub.Controllers.Activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidrealm.bookhub.Adapter.UserRequestAdapter
import com.androidrealm.bookhub.Models.Request
import com.androidrealm.bookhub.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*

class UserRequestListActivity: AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var userRQList: ArrayList<Request>
    private lateinit var myAdapter: UserRequestAdapter
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_request_list)

        findViewById<ImageView>(R.id.backIV).setOnClickListener {
            onBackPressed()
        }

        db = FirebaseFirestore.getInstance()
        recyclerView = findViewById(R.id.user_request_list_RV)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        userRQList = arrayListOf()
        myAdapter = UserRequestAdapter(userRQList)

        recyclerView.adapter = myAdapter

        EventChangeListener()
    }

    private fun EventChangeListener() {
        val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
        var username : String? = null
        db.collection("accounts").document(currentUserId).get()
            .addOnSuccessListener { document ->
                if (document.exists()){
                    username = document.getString("username")
                    db.collection("requests").whereEqualTo("accountName", username)
                        .addSnapshotListener(object : EventListener<QuerySnapshot>{
                            @SuppressLint("NotifyDataSetChanged")
                            override fun onEvent(
                                value: QuerySnapshot?,
                                error: FirebaseFirestoreException?
                            ) {
                                if(error!=null){
                                    Log.e("Firestore Error: ", error.message.toString())
                                }

                                for(dc: DocumentChange in value?.documentChanges!!){
                                    if(dc.type == DocumentChange.Type.ADDED){
                                        userRQList.add(dc.document.toObject(Request::class.java))
                                    }
                                }
                                myAdapter.notifyDataSetChanged()
                            }

                        })
                }
                else{
                    Toast.makeText(this, "Cannot find username of this ID", Toast.LENGTH_SHORT).show()
                }
            }
    }

}
