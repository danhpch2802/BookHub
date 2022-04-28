package com.androidrealm.bookhub.Controllers.Activities

import android.content.ContentValues
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.androidrealm.bookhub.Models.Quiz
import com.androidrealm.bookhub.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import java.util.*

class QuizActivity : AppCompatActivity() {
    var cal = Calendar.getInstance()
    var currentDate = cal.get(Calendar.DAY_OF_YEAR)
    var uid: String = ""
    var corAns: String = "123"
    var reBtn1: ImageView? = null
    var reBtn2: TextView? = null
    var nextBtn: TextView? = null
    var point: TextView? = null
    var quiz: TextView? = null
    var op1Btn: Button? = null
    var op2Btn: Button? = null
    var op3Btn: Button? = null
    var op4Btn: Button? = null
    var quizList: ArrayList<Quiz>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)
        quizList = arrayListOf()
        uid = FirebaseAuth.getInstance().currentUser!!.uid
        reBtn1 = findViewById(R.id.quizReturn)
        reBtn2 = findViewById(R.id.quizReturn2)
        nextBtn = findViewById(R.id.nextQuiz)
        point = findViewById(R.id.quizPoint)
        quiz = findViewById(R.id.quizQuestion)
        op1Btn = findViewById(R.id.option1BT)
        op2Btn = findViewById(R.id.option2BT)
        op3Btn = findViewById(R.id.option3BT)
        op4Btn = findViewById(R.id.option4BT)

        val db2 = FirebaseFirestore.getInstance()
        db2.collection("accounts").document(uid)
            .get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    var point2 = (task.result["Point"] as Long?)!!
                    point!!.text = point2.toString()
                } else {
                    onError(task.exception)
                }
            }
        getQuiz()

        reBtn2!!.setOnClickListener {
            finish()
        }
        reBtn1!!.setOnClickListener {
            finish()
        }
        op1Btn!!.setOnClickListener {
            op2Btn!!.setEnabled(false)
            op3Btn!!.setEnabled(false)
            op4Btn!!.setEnabled(false)
            verifyanswer(op1Btn!!)
        }

        op2Btn!!.setOnClickListener {
            op1Btn!!.setEnabled(false)
            op3Btn!!.setEnabled(false)
            op4Btn!!.setEnabled(false)
            verifyanswer(op2Btn!!)
        }
        op3Btn!!.setOnClickListener {
            op2Btn!!.setEnabled(false)
            op1Btn!!.setEnabled(false)
            op4Btn!!.setEnabled(false)
            verifyanswer(op3Btn!!)
        }
        op4Btn!!.setOnClickListener {
            op2Btn!!.setEnabled(false)
            op3Btn!!.setEnabled(false)
            op1Btn!!.setEnabled(false)
            verifyanswer(op4Btn!!)
        }
        nextBtn!!.setOnClickListener {
            val intent = Intent(this, QuizActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            finish()
        }
    }


    fun getQuiz() {
        op1Btn!!.visibility = View.VISIBLE
        op2Btn!!.visibility = View.VISIBLE
        op3Btn!!.visibility = View.VISIBLE
        op4Btn!!.visibility = View.VISIBLE
        op1Btn!!.setEnabled(true)
        op2Btn!!.setEnabled(true)
        op3Btn!!.setEnabled(true)
        op4Btn!!.setEnabled(true)
        val db = FirebaseFirestore.getInstance()
        db.collection("quizzes")
            .get().addOnSuccessListener { result ->
                for (document in result) {
                    quizList!!.add(document.toObject<Quiz>())
                }
                val values = quizList!!.toList()
                val random = values.random()
//                val answer = random.Answer.toList()
//                val randomAnswer = answer.random()
                quiz!!.text = random.Question
                random.Answer.shuffle()
                op1Btn!!.text = random.Answer[0]
                op2Btn!!.text = random.Answer[1]
                op3Btn!!.text = random.Answer[2]
                op4Btn!!.text = random.Answer[3]
                corAns = random.Result!!
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "Error getting documents: ", exception)
            }
    }

    fun onError(e: Exception?) {
        if (e != null) {
            Log.d("RewardError", "onError: " + e.message)
        }
    }

    private fun verifyanswer(button: Button) {
        if (corAns == button.text) {
            button.backgroundTintList =
                ColorStateList.valueOf(resources.getColor(R.color.app_green))
            setPoint()
        } else {
            button.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.darkred))
        }
        showNextBtn()
    }

    private fun showNextBtn() {
        nextBtn!!.setVisibility(View.VISIBLE)
        nextBtn!!.setEnabled(true)
    }

    fun setPoint() {
        val db2 = FirebaseFirestore.getInstance()
        db2.collection("accounts").document(uid)
            .get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    var point2 = (task.result["Point"] as Long?)!!
                    point2 = point2.plus(100)
                    db2.collection("accounts").document(uid).update("Point", point2)
                } else {
                    onError(task.exception)
                }
            }
    }
}