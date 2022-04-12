package com.androidrealm.bookhub.Controllers.Activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import at.favre.lib.crypto.bcrypt.BCrypt
import com.androidrealm.bookhub.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    lateinit var sharedPreferences: SharedPreferences
    var isRemembered = false

    var loginBtn: TextView? = null

    var forPasBtn: TextView? = null
    var registerBtn: TextView? = null
    var emailEt: EditText? = null
    var passwordEt: EditText? = null
    var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        supportActionBar?.hide()

        setContentView(R.layout.activity_login)

        sharedPreferences = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
        isRemembered = sharedPreferences.getBoolean("CHECKBOX", false)
        loginBtn = findViewById(R.id.loginBtn)

        forPasBtn = findViewById(R.id.forgotPasswordBtn)
        registerBtn = findViewById(R.id.registerBtn1)
        emailEt = findViewById(R.id.emailETLogin)
        passwordEt = findViewById(R.id.passwordETLogin)

        if(isRemembered){
            val intent = Intent(this, HomePageActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            finish()
        }

        loginBtn!!.setOnClickListener{
            //Get input
            val email = emailEt!!.text.toString().trim()
            val pass = passwordEt!!.text.toString().trim()
            val checked: Boolean = checkBox.isChecked

            //Edit Shared Preference
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putString("EMAIL", email)
            editor.putString("PASS", pass)
            editor.putBoolean("CHECKBOX", checked)
            editor.apply()

            //Validate
            if (email.isEmpty()){
                Toast.makeText(this, "Please enter an email", Toast.LENGTH_SHORT).show()
            }
            else if(pass.isEmpty()){
                Toast.makeText(this, "Please enter a password", Toast.LENGTH_SHORT).show()
            }
            else{
                // Initialize Progress Dialog
                progressDialog = ProgressDialog(this)
                progressDialog!!.show()
                progressDialog!!.setContentView(R.layout.progress_dialog)
                progressDialog!!.window!!.setBackgroundDrawableResource(
                    android.R.color.transparent
                )
                // Create instance and login a user with email and password
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener { task ->
                        // If login successful
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()
                            // Send user to Homepage Activity
                            val intentToHomePageActivity =
                                Intent(this, HomePageActivity::class.java)
                            intentToHomePageActivity.flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            intentToHomePageActivity.putExtra(
                                "uid",
                                FirebaseAuth.getInstance().currentUser!!.uid
                            )
                            progressDialog!!.dismiss()
                            startActivity(intentToHomePageActivity)
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                            finish()
                        } else {
                            // If login failed
                            progressDialog!!.dismiss()
                            Toast.makeText(
                                this,
                                task.exception!!.message.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }

        registerBtn!!.setOnClickListener{
            val intent = Intent(this@LoginActivity, SignupActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            finish()
        }

        forPasBtn!!.setOnClickListener{
            val intent = Intent(this@LoginActivity, ForgotPasswordMainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            finish()
        }
    }
    
    // disable back button pressed
    override fun onBackPressed() {
    }

}