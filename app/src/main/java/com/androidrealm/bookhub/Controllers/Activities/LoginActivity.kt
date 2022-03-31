package com.androidrealm.bookhub.Controllers.Activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.androidrealm.bookhub.R

class LoginActivity : AppCompatActivity() {
    var loginBtn: TextView? = null
    var registerBtn: TextView? = null
    var usernameEt: EditText? = null
    var passwordEt: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        supportActionBar?.hide()

        setContentView(R.layout.activity_login)
        loginBtn = findViewById(R.id.loginBtn)
        registerBtn = findViewById(R.id.registerBtn1)
        usernameEt = findViewById(R.id.usernameETLogin)
        passwordEt = findViewById(R.id.passwordETLogin)

        loginBtn!!.setOnClickListener{
            Toast.makeText(this@LoginActivity, "Login Successfully!", Toast.LENGTH_SHORT).show()
            Handler().postDelayed({
                val intent = Intent(this@LoginActivity, HomePageActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                finish()
            }, 1000)
        }

        registerBtn!!.setOnClickListener{
            val intent = Intent(this@LoginActivity, SignupActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            finish()
        }
    }
}