package com.androidrealm.bookhub.Controllers.Activities

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidrealm.bookhub.Adapter.ComicAdapterLinear
import com.androidrealm.bookhub.ComicAdapter
import com.androidrealm.bookhub.Controllers.Fragments.BookFragment
import com.androidrealm.bookhub.Models.Book
import com.androidrealm.bookhub.Models.Comment
import com.androidrealm.bookhub.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class BookSearchActivity: AppCompatActivity() {
    private var listCategory:ArrayList<String>?=null
    private  var searchName:String?=null
    private var searchResultRV:RecyclerView?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_search)
        listCategory = intent.getStringArrayListExtra("category")
        for (i in listCategory!!) {
            Log.i("testCat2", i.toString())
        }
        searchName = intent.getStringExtra("name")
        searchResultRV = findViewById(R.id.searchResultRV)
        var listSearchCat = ArrayList<Book>()
        var fireStore = FirebaseFirestore.getInstance()

        val docRef = fireStore
            .collection("comics")
            .whereArrayContainsAny("listCategory", listCategory!!)

        docRef.get()
            .addOnSuccessListener { result ->

                for (document in result) {
                    val comicGet = document.toObject<Book>()
                    listSearchCat.add(comicGet)

                }
                var listSearch = listSearchCat.filter {
                    it.name!!.matches("(?i)${searchName}.*".toRegex())
                }
                if (listSearch.size == 0) findViewById<TextView>(R.id.noResultTV).visibility =
                    View.VISIBLE
                var searchAdapter = ComicAdapterLinear(listSearch)
                searchAdapter.onItemClick = { book ->
                    val intent = Intent(this, BookDetailActivity::class.java)
                    intent.putExtra("id", book.id)
                    startActivity(intent)
                }
                searchResultRV!!.adapter = searchAdapter

                searchResultRV!!.layoutManager = LinearLayoutManager(this)


                val backIC = findViewById<ImageView>(R.id.backSearch)

                backIC.setOnClickListener {
                    this.finish()
                }
            }

    }

}