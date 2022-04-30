package com.androidrealm.bookhub.Controllers.Activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidrealm.bookhub.Adapter.RankAdapter
import com.androidrealm.bookhub.Models.Account
import com.androidrealm.bookhub.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*

class RankActivity : AppCompatActivity() {
    var uid:String = "ERQnHq5YlmL78h2wDBQX"
    var reBtn: ImageView? = null
    var top1Ava: ImageView? = null
    var myAva: ImageView? = null
    var myRank: TextView? = null
    var pointRank: TextView? = null
    var badgeRank: TextView? = null
    var recyclerView: RecyclerView?= null
    var rankList: ArrayList<Account> ?= null
    var myAdapter: RankAdapter?= null
    var db: FirebaseFirestore?= null
    var top1Badge: TextView? = null
    var top1Rank :TextView?= null
    var top1Name :TextView?= null
    var top1Point :TextView?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rank)
        uid = FirebaseAuth.getInstance().currentUser!!.uid
        recyclerView = findViewById(R.id.rank_list)
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.layoutManager = LinearLayoutManager(this)
        reBtn = findViewById(R.id.returnRank)
        top1Rank = findViewById(R.id.top1)
        top1Badge = findViewById(R.id.top1Badge)
        top1Name = findViewById(R.id.top1Name)
        top1Point = findViewById(R.id.top1Point)
        top1Ava = findViewById(R.id.top1Avatar)
        myRank = findViewById(R.id.myRank)
        pointRank = findViewById(R.id.pointRankingBtn)
        badgeRank = findViewById(R.id.badgeRankingBtn)
        myAva = findViewById(R.id.myRankAvatar)
        reBtn!!.setOnClickListener {
            finish()
        }

        val db = FirebaseFirestore.getInstance()
        db.collection("accounts").document(uid)
            .get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    var avatar:String ?= ""
                    avatar = task.result["Avatar"] as String?
                    when (avatar) {
                        "1" -> myAva!!.setImageResource(R.drawable.amagami_cover)
                        "2" -> myAva!!.setImageResource(R.drawable.doll_cover)
                        "3" -> myAva!!.setImageResource(R.drawable.fechippuru_cover)
                        "4" -> myAva!!.setImageResource(R.drawable.kanojo_cover)
                        "5" -> myAva!!.setImageResource(R.drawable.komi_cover)
                        "6" -> myAva!!.setImageResource(R.drawable.meika_cover)
                        "7" -> myAva!!.setImageResource(R.drawable.mokanojo_cover)
                        "8" -> myAva!!.setImageResource(R.drawable.tonikaku_cover)
                        "9" -> myAva!!.setImageResource(R.drawable.yofukashi_cover)
                        else -> myAva!!.setImageResource(R.drawable.amagami_cover)
                    }
                }
                else {onError(task.exception)}
            }
        rankList = arrayListOf()
        myAdapter = RankAdapter(rankList!!)
        recyclerView!!.adapter = myAdapter

        myAdapter!!.setOnItemClickListener(object : RankAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                val clickedItem = rankList!![position]
                val intent = Intent( this@RankActivity,UserAccountDetailActivity::class.java)
                intent.putExtra("uid", clickedItem.documentId)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
        })

        getDB("Point")

        badgeRank!!.setOnClickListener {
            rankList!!.clear()
            getDB("Point")
            pointRank!!.visibility = View.VISIBLE
            badgeRank!!.visibility = View.INVISIBLE
        }

        pointRank!!.setOnClickListener {
            rankList!!.clear()
            getDB("BadgeOwn")
            badgeRank!!.visibility = View.VISIBLE
            pointRank!!.visibility = View.INVISIBLE
        }

    }

    private fun getDB(fields: String) {
        db = FirebaseFirestore.getInstance()
        db!!.collection("accounts").orderBy(fields, Query.Direction.DESCENDING)
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null){
                        Log.e("Firestore Error", error.message.toString())
                        return
                    }
                    for (dc: DocumentChange in value?.documentChanges!!){
                        if (dc.type == DocumentChange.Type.ADDED){
                            rankList!!.add(dc.document.toObject(Account::class.java))
                            top1Name!!.text = rankList!![0].username
                            top1Point!!.text = rankList!![0].Point.toString()
                            top1Badge!!.text = rankList!![0].BadgeOwn.size.toString()
                            when (rankList!![0].Avatar) {
                                "1" -> top1Ava!!.setImageResource(R.drawable.amagami_cover)
                                "2" -> top1Ava!!.setImageResource(R.drawable.doll_cover)
                                "3" -> top1Ava!!.setImageResource(R.drawable.fechippuru_cover)
                                "4" -> top1Ava!!.setImageResource(R.drawable.kanojo_cover)
                                "5" -> top1Ava!!.setImageResource(R.drawable.komi_cover)
                                "6" -> top1Ava!!.setImageResource(R.drawable.meika_cover)
                                "7" -> top1Ava!!.setImageResource(R.drawable.mokanojo_cover)
                                "8" -> top1Ava!!.setImageResource(R.drawable.tonikaku_cover)
                                "9" -> top1Ava!!.setImageResource(R.drawable.yofukashi_cover)
                                else -> top1Ava!!.setImageResource(R.drawable.amagami_cover)
                            }
                            for (acc in rankList!!.indices)
                            {
                                if (rankList!![acc].documentId == uid)
                                {
                                    myRank!!.text = "My Rank: "+ (acc + 1).toString()
                                }
                            }
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