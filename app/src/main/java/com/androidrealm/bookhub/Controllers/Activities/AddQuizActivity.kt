package com.androidrealm.bookhub.Controllers.Activities

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import com.androidrealm.bookhub.R
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class AddQuizActivity : AppCompatActivity() {
    var reBtn : TextView?= null
    var cfBtn : TextView?= null
    var quizName: EditText?=null
    var quizDes1: EditText?=null
    var quizDes3: EditText?=null
    var quizDes4: EditText?=null
    var quizDes2: EditText?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_quiz)
        reBtn = findViewById(R.id.reNewQuizBtn)
        cfBtn = findViewById(R.id.newQuizConfirm)
        quizName = findViewById(R.id.newQuizQuestion)
        quizDes1 = findViewById(R.id.quiz1Description)
        quizDes2 = findViewById(R.id.quiz2Description)
        quizDes3 = findViewById(R.id.quiz3Description)
        quizDes4 = findViewById(R.id.quiz4Description)
        cfBtn!!.setOnClickListener {
            val quiz_name = quizName!!.text.toString().trim()
            val quiz_des = quizDes1!!.text.toString().trim()
            val quiz_des2 = quizDes2!!.text.toString().trim()
            val quiz_des3 = quizDes3!!.text.toString().trim()
            val quiz_des4 = quizDes4!!.text.toString().trim()
            val quiz_answer:ArrayList<String> = ArrayList<String>()
            quiz_answer.add(quiz_des)
            quiz_answer.add(quiz_des2)
            quiz_answer.add(quiz_des3)
            quiz_answer.add(quiz_des4)

            // Save to Firestore
            val db = FirebaseFirestore.getInstance()

            val newPrize: MutableMap<String, Any> = HashMap()
            newPrize["Result"] = quiz_des4
            newPrize["Question"] = quiz_name
            newPrize["Answer"] = quiz_answer
            db.collection("quizzes").add(newPrize)
//                .addOnSuccessListener { documentReference ->
//                db.collection("quizzes").document(documentReference.id).update("id", documentReference.id)
//            }
//                .addOnFailureListener { e ->
//                    Log.w(ContentValues.TAG, "Error adding document", e)
//                }
            //.document(prize_id)
            //.set(newPrize)
            val intents = Intent(this,  QuizListActivity::class.java)
            startActivity(intents)
            finish()
        }

        reBtn!!.setOnClickListener {
            finish()
        }
    }
}