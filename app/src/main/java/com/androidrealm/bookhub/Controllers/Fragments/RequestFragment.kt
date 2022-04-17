package com.androidrealm.bookhub.Controllers.Fragments

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.androidrealm.bookhub.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class RequestFragment : Fragment() {

    var submitBtn: TextView? = null
    var request_name: EditText? = null
    var request_detail: EditText? = null
    var username: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_request, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        submitBtn = view.findViewById(R.id.rqsubmitBtn)
        request_name = view.findViewById(R.id.requestTitle)
        request_detail = view.findViewById(R.id.requestDetail)

        submitBtn!!.setOnClickListener{
            // Get input
            val rq_name = request_name!!.text.toString().trim()
            val rq_detail = request_detail!!.text.toString().trim()

            // Save to Firestore
            val db = FirebaseFirestore.getInstance()
            val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
            val request: MutableMap<String, Any> = HashMap()
            db.collection("accounts").document(currentUserId).get()
                .addOnSuccessListener { document ->
                    if (document.exists()){
                        username = document.getString("username")
                        request["accountName"] = username!!
                        request["bookDetail"] = rq_detail
                        request["bookName"] = rq_name
                        request["checked"] = false
                        db.collection("requests")
                            .add(request)
                    }
                    else{
                        Toast.makeText(context, "Cannot find username of this ID", Toast.LENGTH_SHORT).show()
                    }
                }

            // Add 10 points when submit
            db.collection("accounts").document(currentUserId)
                .update("Point", FieldValue.increment(10))

            // Toast
            Toast.makeText(context, "You have earned 10 points for making a request!", Toast.LENGTH_SHORT).show()
            Toast.makeText(context, "Submit Successfully!", Toast.LENGTH_SHORT).show()

            // Clear Edit Text
            request_name!!.text.clear()
            request_detail!!.text.clear()
        }

    }
}