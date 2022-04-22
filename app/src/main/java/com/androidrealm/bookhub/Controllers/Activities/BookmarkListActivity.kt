
package com.androidrealm.bookhub.Controllers.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidrealm.bookhub.Adapter.BookmarkAdapter
import com.androidrealm.bookhub.Adapter.ComicAdapterLinear
import com.androidrealm.bookhub.Models.Book
import com.androidrealm.bookhub.R
import com.androidrealm.bookhub.Controllers.Fragments.ListComicLinearFragment
import com.androidrealm.bookhub.Models.Account
import com.androidrealm.bookhub.Models.Bookmark
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

class BookmarkListActivity : AppCompatActivity() {
    private var bookmarkRW: RecyclerView?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bookmark_list)

        bookmarkRW = findViewById(R.id.bookmarkRW)

        var fireStore = FirebaseFirestore.getInstance()

        val currentUserAuth = FirebaseAuth.getInstance().currentUser


        val docRef = fireStore
            .collection("bookmarks")
            .whereEqualTo("accountID", currentUserAuth!!.uid)

        var listBookmark=ArrayList<Bookmark>()
        docRef.get()
            .addOnSuccessListener { result ->

                for (document in result) {
                    val bookmarkGet = document.toObject<Bookmark>()
                    listBookmark.add(bookmarkGet)
                    Log.i("testBookmark", bookmarkGet.chapterName.toString())
                }
                  var bookmarkAdapter = BookmarkAdapter(listBookmark)
                bookmarkAdapter.onItemClick={
                        bookmark->

                    updateViewAfterChapterClick(bookmark.bookID!!)
                    updatePointAfterChapterClick(currentUserAuth!!.uid)
                    Log.i("testingBookmark",bookmark.bookName.toString())
                    val intent= Intent(this, BookReadActivity::class.java)
                    intent.putExtra("ChapterPos",bookmark.chapterUrl)
                    intent.putExtra("id", bookmark.bookID)
                    startActivity(intent)

                }
                bookmarkRW!!.adapter = bookmarkAdapter
                bookmarkRW!!.layoutManager = LinearLayoutManager(this)
        }
        val backIC = findViewById<ImageView>(R.id.backBookmarkIV)

        backIC.setOnClickListener {
            this.finish()
        }
    }

    private fun updatePointAfterChapterClick( idAccount : String) {
        val pointRef = FirebaseFirestore.getInstance().collection("accounts")
            .document(idAccount)
            .update("Point", FieldValue.increment(10))

    }

    private fun updateViewAfterChapterClick( idBook : String) {
        val pointRef = FirebaseFirestore.getInstance().collection("comics")
            .document(idBook)
            .update("viewNumber", FieldValue.increment(1))
    }
}