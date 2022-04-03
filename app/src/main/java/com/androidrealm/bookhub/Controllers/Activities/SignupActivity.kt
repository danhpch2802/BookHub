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
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class SignupActivity : AppCompatActivity() {
    var signupBtn: TextView? = null
    var usernameEt: EditText? = null
    var passwordEt: EditText? = null
    var cpasswordEt: EditText? = null
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
        cpasswordEt = findViewById(R.id.confirmPasswordET)
        emailEt = findViewById(R.id.emailETRegister)

        signupBtn!!.setOnClickListener{
            //Get input
            val name = usernameEt!!.text.toString().trim()
            val pass = passwordEt!!.text.toString().trim()
            val email = emailEt!!.text.toString().trim()
            val cpass = cpasswordEt!!.text.toString().trim()

            //Validate
            if (name.isEmpty()){
                Toast.makeText(this, "Please enter a username", Toast.LENGTH_SHORT).show()
            }
            else if(pass.isEmpty()){
                Toast.makeText(this, "Please enter a password", Toast.LENGTH_SHORT).show()
            }
            else if(pass != cpass){
                Toast.makeText(this, "Passwords don't match", Toast.LENGTH_SHORT).show()
            }
            else if(email.isEmpty()){
                Toast.makeText(this, "Please enter an email", Toast.LENGTH_SHORT).show()
            }
            else{
                // Create instance and register a user with email and password
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener { task ->
                        // If register successful
                        if (task.isSuccessful) {
                            // Firebase Register
                            val firebaseUser: FirebaseUser = task.result!!.user!!
                            Toast.makeText(this, "Register Successful!", Toast.LENGTH_SHORT).show()

                            // Save to Firestore
                            val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
                            val documentRef = FirebaseFirestore.getInstance().collection("accounts")
                                .document(currentUserId)
                            val account: MutableMap<String, Any> = HashMap()
                            account["Avatar"] = ""
                            account["Badge"] = arrayListOf("Beginner")
                            account["Email"] = email
                            account["FavoriteList"] = arrayListOf("")
                            account["History"] = arrayListOf("")
                            account["Point"] = 0
                            account["Role"] = 1
                            account["bookmark"] = hashMapOf(
                                "Chapter" to 0,
                                "Idbook" to "",
                                "Notes" to "",
                                "PageNumber" to 0
                            )
                            account["password"] = pass
                            account["username"] = name

                            documentRef.set(account)

                            // Return to Login Page
                            Handler().postDelayed({
                                val intentToLoginActivity =
                                    Intent(this@SignupActivity, LoginActivity::class.java)
                                intentToLoginActivity.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                intentToLoginActivity.putExtra("username", name)
                                startActivity(intentToLoginActivity)
                                overridePendingTransition(
                                    R.anim.slide_in_right,
                                    R.anim.slide_out_left
                                )
                                finish()
                            }, 1000)
                        } else {
                            // If register failed
                            Toast.makeText(
                                this,
                                task.exception!!.message.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }
    }
}