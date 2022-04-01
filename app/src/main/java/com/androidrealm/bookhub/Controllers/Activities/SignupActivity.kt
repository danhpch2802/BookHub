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
            val passHash = BCrypt.withDefaults().hashToString(12, pass.toCharArray())

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
                //Save to Firestore
                saveToFirestore(name, passHash, email)
            }
        }
    }

    private fun saveToFirestore(name: String, pass: String, email: String) {
        val db = FirebaseFirestore.getInstance()
        val account: MutableMap<String, Any> = HashMap()
        account["Avatar"] = ""
        account["Badge"] = arrayListOf("")
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

        db.collection("accounts")
            .add(account)
            .addOnSuccessListener {
                //Output Result
                Toast.makeText(this@SignupActivity, "Register Successfully!", Toast.LENGTH_SHORT).show()
                Handler().postDelayed({
                    val intent = Intent(this@SignupActivity, LoginActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    finish()
                }, 1000)
            }
            .addOnFailureListener{ e ->
                //Output Result
                Toast.makeText(this@SignupActivity, "Register Failed due to ${e.message}!", Toast.LENGTH_SHORT).show()
            }
    }
}