package com.androidrealm.bookhub.Controllers.Activities

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidrealm.bookhub.Adapter.PrizeAdapter
import com.androidrealm.bookhub.Adapter.QuizAdapter
import com.androidrealm.bookhub.R
import com.google.firebase.firestore.FirebaseFirestore

class QuizListActivity : AppCompatActivity() {
    var reBtn: ImageView? = null
    var recyclerView: RecyclerView?= null
    var quizNameList: ArrayList<String> ?= null
    var myAdapter: QuizAdapter?= null
    var db: FirebaseFirestore?= null
    var role:Long = 3
    var newQuizBtn: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_list)
        recyclerView = findViewById(R.id.quiz_list)
        reBtn = findViewById(R.id.quizlistReturn)
        newQuizBtn = findViewById(R.id.newQuiz)

        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.layoutManager = LinearLayoutManager(this)
        quizNameList = arrayListOf()
        getAdminAcc()
        reBtn!!.setOnClickListener{
            finish()
        }

        newQuizBtn!!.setOnClickListener{
            val intents = Intent(this,  AddQuizActivity::class.java)
            startActivity(intents)
        }
    }


    fun getAdminAcc () {
        val db = FirebaseFirestore.getInstance()
        db.collection("quizzes")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    quizNameList!!.add(document["Question"].toString())
                    //prizeList!!.add(document["id"].toString())
                }
                myAdapter = QuizAdapter(quizNameList!!)
                recyclerView!!.adapter = myAdapter

                myAdapter!!.setOnItemClickListener(object : QuizAdapter.onItemClickListener{
                    override fun onItemClick(position: Int) {
                        //Toast.makeText(this@RequestListActivity, "You click on item $position", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@QuizListActivity, QuizDetailActivity::class.java)
                        intent.putExtra("id", quizNameList!![position])
                        startActivity(intent)
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                        finish()
                    }
                })
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "Error getting documents: ", exception)
            }
    }
}