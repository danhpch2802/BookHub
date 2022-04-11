package com.androidrealm.bookhub.Controllers.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.androidrealm.bookhub.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RequestActivity : AppCompatActivity() {

    var submitBtn: TextView? = null
    var request_name: EditText? = null
    var request_detail: EditText? = null
    lateinit var listComicFrame: FrameLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request)

        submitBtn = findViewById(R.id.rqsubmitBtn)
        request_name = findViewById(R.id.requestTitle)
        request_detail = findViewById(R.id.requestDetail)

        submitBtn!!.setOnClickListener{
            // Get input
            val rq_name = request_name!!.text.toString().trim()
            val rq_detail = request_detail!!.text.toString().trim()

            // Save to Firestore
            val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
            val documentRef = FirebaseFirestore.getInstance().collection("requests")
                .document(currentUserId)
            val request: MutableMap<String, Any> = HashMap()
            request["accountID"] = currentUserId
            request["bookDetail"] = rq_detail
            request["bookName"] = rq_name
            request["checked"] = false

            documentRef.set(request)

            Toast.makeText(this@RequestActivity, "Submit Successfully!", Toast.LENGTH_SHORT).show()
        }

    }
////
//    val listOfRequest=arrayOf("Ara Ara", "Cll", "Raiden")
//    val adapter = ArrayAdapter(this, R.layout., listOfRequest)
//
//    var adapter=ComicAdapter(listOfComic)
//
//    if (savedInstanceState == null) {
//        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
//        val fragment: Fragment = ListComicFragment.newInstance(adapter,3)
//        ft.replace(R.id.fragment_container_view, fragment)
//        ft.commit()
//    }
}