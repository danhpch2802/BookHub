package com.androidrealm.bookhub.Controllers.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidrealm.bookhub.Adapter.ComicAdapterLinear
import com.androidrealm.bookhub.ComicAdapter
import com.androidrealm.bookhub.Models.Book
import com.androidrealm.bookhub.R
import com.androidrealm.bookhub.Controllers.Fragments.ListComicLinearFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class HistoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        val backIC = findViewById<ImageView>(R.id.icBackHistory)

        backIC.setOnClickListener {
            finish()
        }

    }

    override fun onResume() {
        super.onResume()

        val curruser = FirebaseAuth.getInstance().currentUser
        var HistoryList = ArrayList<String>()

        val historyRV = findViewById<RecyclerView>(R.id.historyListRV)

        GlobalScope.launch {
            HistoryList = getHistoryList(curruser!!.uid) as ArrayList<String>

            val listOfComicHistory = ArrayList<Book>()

            getInfoHistory(HistoryList, listOfComicHistory)

            val adapter = ComicAdapterLinear(listOfComicHistory)

            withContext(Dispatchers.Main)
            {
                adapter.onItemClick = { book ->
                    val intent = Intent(this@HistoryActivity, BookDetailActivity::class.java)
                    intent.putExtra("id", book.id)
                    startActivity(intent)
                }

                historyRV.adapter = adapter

                historyRV.layoutManager = LinearLayoutManager(this@HistoryActivity)
            }
        }

    }

    private suspend fun getInfoHistory(listHistory: ArrayList<String>, listInfoComic: ArrayList<Book>)
    {
        val comicRef = FirebaseFirestore.getInstance().collection("comics")
        for(i in listHistory)
        {
            if(!i.equals(""))
            {
                listInfoComic.add(
                    comicRef.document(i.toString())
                        .get()
                        .await()
                        .toObject<Book>()!!)
            }
        }
    }

    private suspend fun getHistoryList(uid: String): Any {
        return FirebaseFirestore.getInstance().collection("accounts")
            .document(uid)
            .get()
            .await()["History"]!!
    }
}