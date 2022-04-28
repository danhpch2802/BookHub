package com.androidrealm.bookhub.Controllers.Activities

import android.content.ContentValues
import android.content.Intent
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.androidrealm.bookhub.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class QuizDetailActivity : AppCompatActivity() {
    var quizDes:String? = ""
    var role:Long = 3
    var uid:String = ""
    lateinit var auth : FirebaseAuth
    var quizAns: ArrayList<String> ?= null
    var id:String? = "b2"
    var reBtn1: ImageView? = null
    var reBtn2: TextView? = null
    var delBtn: TextView? = null
    var point: TextView? = null
    var quiz: TextView? = null
    var op1Btn: Button? = null
    var op2Btn: Button? = null
    var op3Btn: Button? = null
    var op4Btn: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_detail)
        quizAns = arrayListOf()
        uid = FirebaseAuth.getInstance().currentUser!!.uid
        reBtn1 = findViewById(R.id.quizReturnX)
        reBtn2 = findViewById(R.id.quizReturn2X)
        delBtn = findViewById(R.id.delQuizX)
        point = findViewById(R.id.quizPointX)
        quiz = findViewById(R.id.quizQuestionX)
        op1Btn = findViewById(R.id.option1BTX)
        op2Btn = findViewById(R.id.option2BTX)
        op3Btn = findViewById(R.id.option3BTX)
        op4Btn = findViewById(R.id.option4BTX)
        var fireStore = FirebaseFirestore.getInstance()
        val intent = intent
        id = intent.getStringExtra("id")
        val db = FirebaseFirestore.getInstance()
        db.collection("quizzes").whereEqualTo("Question", id)
            .get().addOnSuccessListener { documents ->
                for (document in documents) {
                    id = document.id
                }
                getQuiz()
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents: ", exception)
            }


        delBtn!!.setOnClickListener {
            delQuiz(id.toString())
            val intents = Intent(this, QuizListActivity::class.java)
            startActivity(intents)
            finish()
        }
        reBtn1!!.setOnClickListener {
            val intent = Intent(this,  QuizListActivity::class.java)
            startActivity(intent)
            finish()
        }
        reBtn2!!.setOnClickListener {
            val intent = Intent(this,  QuizListActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun delQuiz(id : String)
    {
        val db = FirebaseFirestore.getInstance()
        db.collection("quizzes").document(id)
            .delete()
    }
    fun getQuiz () {
        val db = FirebaseFirestore.getInstance()
        db.collection("quizzes").document(id.toString())
            .get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result["Answer"] as ArrayList<*>) {
                        quizAns!!.add(document.toString())
                    }
                    quiz!!.text = task.result["Question"] as String?
                    quizDes =  task.result["Result"] as String?

                }
                else {onError(task.exception)}
                op1Btn!!.text = quizAns!![0]
                op2Btn!!.text = quizAns!![1]
                op3Btn!!.text = quizAns!![2]
                op4Btn!!.text = quizAns!![3]
                if (quizDes == op1Btn!!.text) {
                    op1Btn!!.backgroundTintList =
                        ColorStateList.valueOf(resources.getColor(R.color.app_green))
                }
                if (quizDes == op2Btn!!.text) {
                    op2Btn!!.backgroundTintList =
                        ColorStateList.valueOf(resources.getColor(R.color.app_green))
                }
                if (quizDes == op3Btn!!.text) {
                    op3Btn!!.backgroundTintList =
                        ColorStateList.valueOf(resources.getColor(R.color.app_green))
                }
                if (quizDes == op4Btn!!.text) {
                    op4Btn!!.backgroundTintList =
                        ColorStateList.valueOf(resources.getColor(R.color.app_green))
                }
            }
    }
    fun onError(e: Exception?) {
        if (e != null) {
            Log.d("RewardError", "onError: " + e.message)
        }
    }
}