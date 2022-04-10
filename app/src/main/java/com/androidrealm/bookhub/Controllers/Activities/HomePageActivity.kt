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
import com.androidrealm.bookhub.Controllers.Fragments.ProfileFragment
import com.androidrealm.bookhub.Controllers.Fragments.RequestFragment
import com.androidrealm.bookhub.Models.Chapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.android.synthetic.main.activity_homepage.*
import java.io.Serializable


class HomePageActivity : AppCompatActivity(),Serializable {

    lateinit var listComicFrame:FrameLayout

    // Uncomment the line below if starting the proj from login
    //var uid:String = ""

    // Comment this if  starting the proj from login
var uid:String = "ERQnHq5YlmL78h2wDBQX"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)
        uid = FirebaseAuth.getInstance().currentUser!!.uid
        // Uncomment the line below if start from login
        //uid = intent.getStringExtra("uid").toString()
    }

    override fun onResume() {
        super.onResume()

        //bottom_navigation.menu.findItem(R.id.home_item).isChecked = true
        findViewById<BottomNavigationView>(R.id.bottom_navigation).setOnNavigationItemSelectedListener { menuItem ->
            when {
                menuItem.itemId == R.id.profile_item -> {
                    val intent = Intent(this,  ProfileActivity::class.java)
                    intent.putExtra("uid", uid)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    return@setOnNavigationItemSelectedListener true
                }
                menuItem.itemId == R.id.manage_book_item -> {
                    val intent = Intent(this,  RequestActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    return@setOnNavigationItemSelectedListener true
                }
                else -> false
            }
        }
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
                val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
                val fragment: Fragment = ListComicFragment.newInstance(adapter,3)
                ft.replace(R.id.fragment_container_view, fragment)
                ft.commit()
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }
    }
}