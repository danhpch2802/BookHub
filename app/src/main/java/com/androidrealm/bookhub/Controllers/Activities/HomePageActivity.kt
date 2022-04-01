package com.androidrealm.bookhub.Controllers.Activities

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.androidrealm.bookhub.ComicAdapter
import com.androidrealm.bookhub.Models.Book
import com.androidrealm.bookhub.R
import com.androidrealm.bookhub.Controllers.Fragments.ListComicFragment
import com.androidrealm.bookhub.Models.Chapter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import java.io.Serializable


class HomePageActivity : AppCompatActivity(),Serializable {

    lateinit var listComicFrame:FrameLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)

        var fireStore = FirebaseFirestore.getInstance()

        val listOfComic=ArrayList<Book>();

        val docRef = fireStore.collection("comics")
        docRef.get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val comicGet = document.toObject<Book>()
                    comicGet.id = document.id
                    listOfComic.add(comicGet)
                }

                var adapter= ComicAdapter(listOfComic)
                adapter.onItemClick = { book ->
                    val intent = Intent(this, BookDetailActivity::class.java)
                    intent.putExtra("id",book.id)
                    startActivity(intent)
                }
                if (savedInstanceState == null) {
                    val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
                    val fragment: Fragment = ListComicFragment.newInstance(adapter,3)
                    ft.replace(R.id.fragment_container_view, fragment)
                    ft.commit()
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }


    }
}