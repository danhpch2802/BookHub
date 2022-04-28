package com.androidrealm.bookhub.Controllers.Activities

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidrealm.bookhub.Adapter.PrizeAdapter
import com.androidrealm.bookhub.Controllers.Fragments.AdminProfileFragment
import com.androidrealm.bookhub.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*

class PrizeListActivity : AppCompatActivity() {

    var uid:String = "ERQnHq5YlmL78h2wDBQX"
    var reBtn: ImageView? = null
    var recyclerView: RecyclerView?= null
    var prizeList: ArrayList<String> ?= null
    var prizeNameList: ArrayList<String> ?= null
    var myAdapter: PrizeAdapter?= null
    var db: FirebaseFirestore ?= null
    var role:Long = 3
    var newPrizeBtn: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prize_list)
        recyclerView = findViewById(R.id.prize_list)
        reBtn = findViewById(R.id.prizelistReturn)
        newPrizeBtn = findViewById(R.id.newPrize)

        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.layoutManager = LinearLayoutManager(this)

        var fireStore = FirebaseFirestore.getInstance()
        uid = FirebaseAuth.getInstance().currentUser!!.uid
        prizeList = arrayListOf()
        prizeNameList = arrayListOf()

        fireStore.collection("accounts").document(uid)
            .get().addOnSuccessListener { result ->
                role = result.get("Role") as Long
                //Log.d(TAG,  role.toString())
                if (role == 1L) {
                    getAcc()
                    reBtn!!.setOnClickListener{
                        finish()
                    }
                    newPrizeBtn!!.isInvisible = true
                }
                else {
                    getAdminAcc()
                    reBtn!!.setOnClickListener{
                        finish()
                    }

                    newPrizeBtn!!.setOnClickListener{
                        val intents = Intent(this,  NewPrizeActivity::class.java)
                        startActivity(intents)
                        finish()
                    }
                }
            }
    }

    fun getAcc () {
        val db = FirebaseFirestore.getInstance()
        val db2 = FirebaseFirestore.getInstance()
        db.collection("accounts").document(uid)
            .get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result["BadgeOwn"] as ArrayList<*>) {
                        prizeList!!.add(document.toString())
                    }
                }
                else {
                    onError(task.exception)
                }
                for(i in prizeList!!.indices){
                    // Log.d(TAG, "beforedb" + i.toString())
                    db2.collection("prizes").document(prizeList!![i]).get().addOnSuccessListener { result ->
                        // Log.d(TAG, "afterdb" + i.toString())
                        prizeNameList!!.add(result.get("prizeName") as String)
                        myAdapter = PrizeAdapter(prizeNameList!!)
                        recyclerView!!.adapter = myAdapter
                        myAdapter!!.setOnItemClickListener(object : PrizeAdapter.onItemClickListener {
                            override fun onItemClick(position: Int) {
                                //Toast.makeText(this@RequestListActivity, "You click on item $position", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this@PrizeListActivity, PrizeDetailActivity::class.java)
                                intent.putExtra("id", prizeNameList!![position])
                                startActivity(intent)
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                                finish()
                            }
                            })
                    }
                }
            }
    }

    fun getAdminAcc () {
        val db = FirebaseFirestore.getInstance()
        db.collection("prizes")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    prizeNameList!!.add(document["prizeName"].toString())
                    //prizeList!!.add(document["id"].toString())
                }
                myAdapter = PrizeAdapter(prizeNameList!!)
                recyclerView!!.adapter = myAdapter

                myAdapter!!.setOnItemClickListener(object : PrizeAdapter.onItemClickListener{
                    override fun onItemClick(position: Int) {
                        //Toast.makeText(this@RequestListActivity, "You click on item $position", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@PrizeListActivity, PrizeDetailActivity::class.java)
                        intent.putExtra("id", prizeNameList!![position])
                        startActivity(intent)
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                        finish()
                    }
                })
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }
    }

    fun onError(e: Exception?) {
        if (e != null) {
            Log.d("RewardError", "onError: " + e.message)
        }
    }
}
