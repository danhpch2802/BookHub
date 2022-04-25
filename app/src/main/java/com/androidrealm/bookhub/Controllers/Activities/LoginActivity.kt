package com.androidrealm.bookhub.Controllers.Activities

import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import at.favre.lib.crypto.bcrypt.BCrypt
import com.androidrealm.bookhub.R
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException

import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {
    companion object{
        const val RC_SIGN_IN = 120
    }
    lateinit var sharedPreferences: SharedPreferences
    var isRemembered = false

    var loginBtn: TextView? = null

    var forPasBtn: TextView? = null
    var registerBtn: TextView? = null
    var emailEt: EditText? = null
    var passwordEt: EditText? = null
    var progressDialog: ProgressDialog? = null
    var loginWithFBBtn: LoginButton? = null
    var loginWithGMBtn: SignInButton? = null
    lateinit var callbackManager: CallbackManager
    private lateinit var mAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        supportActionBar?.hide()

        setContentView(R.layout.activity_login)

        // Log out of Facebook
        LoginManager.getInstance().logOut()

        sharedPreferences = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
        isRemembered = sharedPreferences.getBoolean("CHECKBOX", false)
        loginBtn = findViewById(R.id.loginBtn)

        forPasBtn = findViewById(R.id.forgotPasswordBtn)
        registerBtn = findViewById(R.id.registerBtn1)
        emailEt = findViewById(R.id.emailETLogin)
        passwordEt = findViewById(R.id.passwordETLogin)

        loginWithFBBtn = findViewById(R.id.fb_login)
        loginWithGMBtn = findViewById(R.id.gmail_login)


        if(isRemembered){
            val intent = Intent(this, HomePageActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            finish()
        }

        // Facebook
        callbackManager = CallbackManager.Factory.create()
        loginWithFBBtn!!.setReadPermissions("email")
        loginWithFBBtn!!.setOnClickListener{
            fbSignIn()
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        mAuth = FirebaseAuth.getInstance()
        // Google
        loginWithGMBtn!!.setOnClickListener {
            gmSignIn()
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
                            FirebaseFirestore.getInstance().collection("accounts").get().addOnSuccessListener { result ->
                                var flag = 0
                                for (documents in result) {
                                    if (FirebaseAuth.getInstance().currentUser!!.uid == documents.id) {
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
                                        flag = 1
                                        progressDialog!!.dismiss()
                                        startActivity(intentToHomePageActivity)
                                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                                        finish()
                                        break
                                    }
                                }
                                if (flag == 0) {
                                    Toast.makeText(
                                        this,
                                        "Error! Account Deleted Because Violate User Agreement or Account Not existed",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    progressDialog!!.dismiss()
                                }

                            }

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

    private fun fbSignIn() {
        loginWithFBBtn!!.registerCallback(callbackManager, object : FacebookCallback<LoginResult>{
            override fun onError(error: FacebookException) {
                Log.e("ERROR_SIGNIN", error.message!!)
            }

            override fun onSuccess(result: LoginResult) {
                handleFacebookAccessToken(result.accessToken)
            }

            override fun onCancel() {
                Log.d("CANCEL", "Cancelled")
            }

        })

    }

    private fun gmSignIn(){
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun handleFacebookAccessToken(accessToken: AccessToken?) {
        // Initialize Progress Dialog
        progressDialog = ProgressDialog(this)
        progressDialog!!.show()
        progressDialog!!.setContentView(R.layout.progress_dialog)
        progressDialog!!.window!!.setBackgroundDrawableResource(
            android.R.color.transparent
        )
        // Get credential
        val credential = FacebookAuthProvider.getCredential(accessToken!!.token)
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnFailureListener{ e->
                Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            }
            .addOnSuccessListener { result ->
                val badgee = arrayListOf<String>()
                val db = FirebaseFirestore.getInstance()
                db.collection("prizes").get()
                    .addOnSuccessListener { snapshot ->
                        for (document in snapshot) {
                            badgee.add(document.id)
                            if (badgee.last() == "s1")
                                badgee.removeLast()
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.d(ContentValues.TAG, "Error getting documents: ", exception)
                    }

                //Get email & Facebook username
                val email = result.user!!.email
                val name = result.user!!.displayName
                val pass = accessToken.userId
                val passHash = BCrypt.withDefaults().hashToString(12, pass.toCharArray())

                // Save Facebook User to Firestore
                val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
                val documentRef = FirebaseFirestore.getInstance().collection("accounts")
                    .document(currentUserId)
                val account: MutableMap<String, Any> = HashMap()
                account["Avatar"] = "1"
                account["Badge"] = arrayListOf("s1")
                account["BadgeUnown"] = badgee
                account["BadgeOwn"] = arrayListOf("s1")
                account["Email"] = email!!
                account["FavoriteList"] = arrayListOf("")
                account["History"] = arrayListOf("")
                account["Point"] = 0
                account["Role"] = 1
                account["password"] = passHash
                account["username"] = name!!

                documentRef.set(account)

                // Start Homepage Activity
                val intentToHomePageActivity =
                    Intent(this, HomePageActivity::class.java)
                intentToHomePageActivity.flags =
                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intentToHomePageActivity)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                finish()
                progressDialog!!.dismiss()
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == RC_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val exception = task.exception
            if(task.isSuccessful){
                try{
                    val account = task.getResult(ApiException::class.java)
                    Log.d("SignInActivity", "firebaseAuthWithGoogle: "+ account.id)
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e:ApiException){
                    Log.w("SignInActivity", "Google sign in failed", e)
                }
            }
            else Log.w("SignInActivity", exception.toString())

        }
        else callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        // Initialize Progress Dialog
        progressDialog = ProgressDialog(this)
        progressDialog!!.show()
        progressDialog!!.setContentView(R.layout.progress_dialog)
        progressDialog!!.window!!.setBackgroundDrawableResource(
            android.R.color.transparent
        )
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this){ task ->
                if(task.isSuccessful){
                    Log.d("SignInActivity", "signInWithCredential: Success")
                    progressDialog!!.dismiss()

                    val badgee = arrayListOf<String>()
                    val db = FirebaseFirestore.getInstance()
                    db.collection("prizes").get()
                        .addOnSuccessListener { snapshot ->
                            for (document in snapshot) {
                                badgee.add(document.id)
                                if (badgee.last() == "s1")
                                    badgee.removeLast()
                            }
                        }
                        .addOnFailureListener { exception ->
                            Log.d(ContentValues.TAG, "Error getting documents: ", exception)
                        }

                    //Get email & Facebook username
                    val user = mAuth.currentUser
                    val email = user?.email
                    val name = user?.displayName

                    val passHash = BCrypt.withDefaults().hashToString(12, name!!.toCharArray())

                    // Save Facebook User to Firestore
                    val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
                    val documentRef = FirebaseFirestore.getInstance().collection("accounts")
                        .document(currentUserId)
                    val account: MutableMap<String, Any> = HashMap()
                    account["Avatar"] = "1"
                    account["Badge"] = arrayListOf("s1")
                    account["BadgeUnown"] = badgee
                    account["BadgeOwn"] = arrayListOf("s1")
                    account["Email"] = email!!
                    account["FavoriteList"] = arrayListOf("")
                    account["History"] = arrayListOf("")
                    account["Point"] = 0
                    account["Role"] = 1
                    account["password"] = passHash
                    account["username"] = name!!

                    documentRef.set(account)

                    val intent = Intent(this, HomePageActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                else{
                    Log.d("SignInActivity", "signInWithCredential: Fail")
                }
            }
    }

    // disable back button pressed
    override fun onBackPressed() {
    }

}