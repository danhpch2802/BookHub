package com.androidrealm.bookhub.Controllers.Activities

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.androidrealm.bookhub.Controllers.Fragments.BookFragment
import com.androidrealm.bookhub.Models.Book
import com.androidrealm.bookhub.Models.Chapter
import com.androidrealm.bookhub.R
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject


class BookDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_detail)

        val intent = intent
        val id = intent.getStringExtra("id")

        var fireStore = FirebaseFirestore.getInstance()

        val docRef = fireStore.collection("comics").document(id.toString())
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val comicGet = document.toObject<Book>()
                    findViewById<TextView>(R.id.detail_book_barTV).setText(comicGet!!.name)
                    if (savedInstanceState == null) {
                        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
                        val fragment: Fragment = BookFragment.newInstance(book=comicGet,recommendList=null, listComment = null, editable = false)
                        ft.replace(R.id.fragment_book, fragment)
                        ft.commit()
                    }
                } else {
                    Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }
        val topAppbar=findViewById<MaterialToolbar>(R.id.topAppbar)

        topAppbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.editIT -> {
                    val intent = Intent(this, BookUpdateActivity::class.java)
                    intent.putExtra("id",id)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        findViewById<ImageView>(R.id.backIV).setOnClickListener {
            onBackPressed()
        }
    }
}