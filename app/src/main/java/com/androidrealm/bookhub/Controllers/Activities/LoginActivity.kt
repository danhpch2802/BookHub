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
import at.favre.lib.crypto.bcrypt.BCrypt
import com.androidrealm.bookhub.R
import com.google.firebase.firestore.FirebaseFirestore

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
            //Get input
            val name = usernameEt!!.text.toString().trim()
            val pass = passwordEt!!.text.toString().trim()

            //Validate
            if (name.isEmpty()){
                Toast.makeText(this, "Please enter a username", Toast.LENGTH_SHORT).show()
            }
            else if(pass.isEmpty()){
                Toast.makeText(this, "Please enter a password", Toast.LENGTH_SHORT).show()
            }
            else{
                getDocumentsUserPass(name, pass)
            }
        }

        registerBtn!!.setOnClickListener{
            val intent = Intent(this@LoginActivity, SignupActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            finish()
        }
    }

    private fun getDocumentsUserPass(name: String, pass:String) {
        var countNotFound = 0
        val db = FirebaseFirestore.getInstance()
        db.collection("accounts")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    // If username exists
                    if (name == document.getString("username")){
                        // Get hashed password from firestore
                        val hashedPassword = document.getString("password")

                        val res = BCrypt.verifyer().verify(pass.toCharArray(), hashedPassword)
                        // Check pass match hashed pass from register
                        if (res.verified){
                            Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()

                            Handler().postDelayed({
                                val intent = Intent(this, HomePageActivity::class.java)
                                intent.putExtra("uid", document.id)
                                startActivity(intent)
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                                finish()
                            }, 1000)
                        }
                        else{
                            Toast.makeText(this, "Wrong password!", Toast.LENGTH_SHORT).show()
                        }
                    }
                    else countNotFound++
                }
                if (countNotFound == result.size()){
                    Toast.makeText(this, "Username not found!", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener{ e ->
                Toast.makeText(this, "Cannot get documents due to ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

}