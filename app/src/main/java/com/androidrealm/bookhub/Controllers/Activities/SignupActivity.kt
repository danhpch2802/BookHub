package com.androidrealm.bookhub.Controllers.Activities

import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.androidrealm.bookhub.R

class SignupActivity : AppCompatActivity() {
    var signupBtn: TextView? = null
    var usernameEt: EditText? = null
    var passwordEt: EditText? = null
    var emailEt: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        supportActionBar?.hide()

        setContentView(R.layout.activity_signup)

        signupBtn = findViewById(R.id.registerBtn2)
        usernameEt = findViewById(R.id.usernameETRegister)
        passwordEt = findViewById(R.id.passwordETRegister)
        emailEt = findViewById(R.id.emailETRegister)

        signupBtn!!.setOnClickListener{
            Toast.makeText(this@SignupActivity, "Register Successfully!", Toast.LENGTH_SHORT).show()
        }
    }
}