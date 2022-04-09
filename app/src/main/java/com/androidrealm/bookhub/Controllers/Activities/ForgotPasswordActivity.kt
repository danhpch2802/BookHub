package com.androidrealm.bookhub.Controllers.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import at.favre.lib.crypto.bcrypt.BCrypt
import com.androidrealm.bookhub.R
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {
    var confirmBtn: TextView? = null
    var cancelBtn: TextView? = null
    var confirmPassEt: EditText? = null
    var passwordEt: EditText? = null
    //var newPassValue = "newPassword"
    lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        auth = FirebaseAuth.getInstance()
        confirmBtn = findViewById(R.id.confirmButtonChangePass)
        cancelBtn = findViewById(R.id.cancelBtnChangePass)
        confirmPassEt = findViewById(R.id.confirmPasswordETChangePass)
        passwordEt = findViewById(R.id.passwordETChangePass)
        val user = auth.currentUser
        confirmBtn!!.setOnClickListener {
            val confpass = confirmPassEt!!.text.toString().trim()
            val pass = passwordEt!!.text.toString().trim()

            //Validate
            if (confpass.isEmpty()){
                Toast.makeText(this, "Please confirm password", Toast.LENGTH_SHORT).show()
            }
            else if(pass.isEmpty()){
                Toast.makeText(this, "Please enter a password", Toast.LENGTH_SHORT).show()
            }
            else if(pass != confpass){
                Toast.makeText(this, "Passwords don't match", Toast.LENGTH_SHORT).show()
            }
            else {
                user!!.updatePassword(pass)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful()) {
                            Toast.makeText(
                                this@ForgotPasswordActivity,
                                "Change Password Successfully!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                Handler().postDelayed({
                    finish()
                }, 700)
            }
        }
    }
}