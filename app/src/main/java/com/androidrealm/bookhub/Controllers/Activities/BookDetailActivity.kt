package com.androidrealm.bookhub.Controllers.Activities

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.androidrealm.bookhub.Controllers.Fragments.BookFragment
import com.androidrealm.bookhub.Models.Book
import com.androidrealm.bookhub.Models.Comment
import com.androidrealm.bookhub.R
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlin.collections.ArrayList


class BookDetailActivity : AppCompatActivity() {
    private var id:String? ="";
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_detail)
        val intent = intent
        id = intent.getStringExtra("id")
    }

    override fun onResume() {
        super.onResume()

        var fireStore = FirebaseFirestore.getInstance()

        val docRef = fireStore.collection("comics").document(id.toString())
        docRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val comicGet = document.toObject<Book>()
                    findViewById<TextView>(R.id.detail_book_barTV).setText(comicGet!!.name)
                    GlobalScope.launch {
                        val listcommentGet: ArrayList<Comment> = findListComment(id)

                        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
                        val fragment: Fragment = BookFragment.newInstance(
                            book = comicGet,
                            recommendList = null,
                            listComment = listcommentGet,
                            editable = false
                        )
                        ft.replace(R.id.fragment_book, fragment)
                        ft.commit()
                    }
                } else {
                    finish()
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }

        val topAppbar=findViewById<MaterialToolbar>(R.id.topAppbar)

        val currentIDAccount = FirebaseAuth.getInstance().currentUser!!.uid

        val accref = fireStore.collection("accounts").document(currentIDAccount)
        accref.get()
            .addOnSuccessListener { document->
                Log.i("role", document.data!!["Role"].toString())
                if (document.data!!["Role"]!!.equals(0L))
                {
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
                }
                else if (document.data!!["Role"]!!.equals(1L))
                {
                    Log.i("cc","")
                    topAppbar.menu.clear()
                }
            }

        findViewById<ImageView>(R.id.backIV).setOnClickListener {
            onBackPressed()
        }
    }


    private suspend fun findListComment(idBook: String?): ArrayList<Comment> {
        var tempListComment = ArrayList<Comment>()
        try {
            FirebaseFirestore.getInstance().collection("comments")
                .whereEqualTo("bookID", idBook.toString())
                .get()
                .await()
                .documents
                .forEach {
                    val tempTimestamp = it.data!!["createdAt"] as Timestamp
                    val tempDate = tempTimestamp.toDate()
                    tempListComment.add(
                    Comment(
                        it.data!!["accountID"] as String,
                        it.data!!["accountName"] as String,
                        it.data!!["bookID"] as String,
                        it.data!!["content"] as String,
                        it.data!!["starRated"] as Long,
                        tempDate
                    ))
                }
        }
        catch (e: Throwable)
        {
            Log.i("Error 1234", e.message.toString())
        }
        if (!tempListComment.isEmpty())
            tempListComment.sortByDescending { it.createdAt }
        return tempListComment
    }


}