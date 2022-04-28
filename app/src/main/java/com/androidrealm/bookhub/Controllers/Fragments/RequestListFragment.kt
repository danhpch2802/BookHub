package com.androidrealm.bookhub.Controllers.Fragments


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidrealm.bookhub.Adapter.RequestAdapter
import com.androidrealm.bookhub.Controllers.Activities.RequestDetailActivity
import com.androidrealm.bookhub.R
import com.androidrealm.bookhub.Models.Request
import com.google.firebase.firestore.*


class RequestListFragment : Fragment() {
    var recyclerView: RecyclerView ?= null
    var requestList: ArrayList<Request> ?= null
    var tempRQList: ArrayList<Request> ?= null
    var myAdapter: RequestAdapter ?= null
    var db: FirebaseFirestore ?= null
    lateinit var option: Spinner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_request_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.rq_list)
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.layoutManager = LinearLayoutManager(context)

        option = view.findViewById(R.id.spinner) as Spinner
        var options = arrayOf("Sort by...", "Date", "Checked Status")
        option.adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1, options)
        option.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                var choice = p2
                when (choice) {
                    1 -> {
                        requestList!!.sortByDescending { it.time }
                    }
                    2 -> {
                        requestList!!.sortByDescending { it.checked }
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                getDB()
            }

        }
    }

    override fun onResume() {
        super.onResume()
        getDB()
        requestList = arrayListOf()

        myAdapter = RequestAdapter(requestList!!)
        recyclerView!!.adapter = myAdapter

        myAdapter!!.setOnItemClickListener(object : RequestAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                val clickedItem = requestList!![position]
                val intent = Intent(context, RequestDetailActivity::class.java)
                intent.putExtra("documentID", clickedItem.documentId)
                startActivity(intent)
//                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
//                finish()
            }
        })
    }

    private fun getDB() {
        db = FirebaseFirestore.getInstance()
        db!!.collection("requests")
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