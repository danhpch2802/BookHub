package com.androidrealm.bookhub.Controllers.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.androidrealm.bookhub.R
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordMainActivity : AppCompatActivity() {

    var confirmBtn: TextView? = null
    var emailForgotET: EditText? = null

    lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password_main)
        auth = FirebaseAuth.getInstance()
        confirmBtn = findViewById(R.id.forgotConfirmBtn)
        emailForgotET = findViewById(R.id.emailETForgot)

        val user = auth.currentUser
        confirmBtn!!.setOnClickListener {
            val mail = emailForgotET!!.text.toString().trim()

            //Validate
            if (mail.isEmpty()){
                Toast.makeText(this, "Please input Email ", Toast.LENGTH_SHORT).show()
            }
            else
            {
                auth.sendPasswordResetEmail(mail).addOnCompleteListener{ task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Verification email send to your email successfully ", Toast.LENGTH_LONG).show()
                        val intent = Intent(this@ForgotPasswordMainActivity, LoginActivity::class.java)
                        startActivity(intent)
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                        finish()
                    }
                    else
                    {
                        Toast.makeText(this, task.exception!!.message.toString(), Toast.LENGTH_LONG).show()
                    }
                }
                }
            }
    }
}