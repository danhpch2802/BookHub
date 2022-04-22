package com.androidrealm.bookhub.Controllers.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.androidrealm.bookhub.ComicAdapter
import com.androidrealm.bookhub.Models.Book
import com.androidrealm.bookhub.R
import com.androidrealm.bookhub.Controllers.Fragments.ListComicFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FavoriteListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite_list)

         val backIC = findViewById<ImageView>(R.id.backFavorite)

        backIC.setOnClickListener {
            finish()
        }

    }

    override fun onResume() {
        super.onResume()

        val curruser = FirebaseAuth.getInstance().currentUser
        var favoriteList = ArrayList<String>()

        val favoriteRV = findViewById<RecyclerView>(R.id.listComicFavorRV)


        GlobalScope.launch {
            favoriteList = getFavoriteList(curruser!!.uid) as ArrayList<String>

            val listOfComicFavor = ArrayList<Book>()

            getInfoFavorite(favoriteList, listOfComicFavor)

            val adapter = ComicAdapter(listOfComicFavor)

            withContext(Dispatchers.Main)
            {
                adapter.onItemClick = { book ->
                    val intent = Intent(this@FavoriteListActivity, BookDetailActivity::class.java)
                    intent.putExtra("id", book.id)
                    startActivity(intent)
                }

                favoriteRV.adapter = adapter

                favoriteRV.layoutManager = GridLayoutManager(this@FavoriteListActivity, 3)
            }
        }
    }

    private suspend fun getInfoFavorite(listFavorite: ArrayList<String>, listInfoComic: ArrayList<Book>)
    {
        val comicRef = FirebaseFirestore.getInstance().collection("comics")
        for(i in listFavorite)
        {
            if (!i.equals(""))
            {
                listInfoComic.add(
                    comicRef.document(i.toString())
                        .get()
                        .await()
                        .toObject<Book>()!!)
            }
        }
    }

    private suspend fun getFavoriteList(uid: String): Any {
        return FirebaseFirestore.getInstance().collection("accounts")
            .document(uid)
            .get()
            .await()["FavoriteList"]!!
    }
}