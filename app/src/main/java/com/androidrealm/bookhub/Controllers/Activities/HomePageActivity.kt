package com.androidrealm.bookhub.Controllers.Activities

import PrizeFragment
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
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_homepage.*


class HomePageActivity : AppCompatActivity() {

    lateinit var listComicFrame:FrameLayout

//    val uid:String = intent.getStringExtra("uid").toString()
val uid:String = "ERQnHq5YlmL78h2wDBQX"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)

        bottom_navigation.setOnNavigationItemSelectedListener { menuItem ->
            when {
                menuItem.itemId == R.id.profile_item -> {
                    val intent = Intent(this,  ProfileActivity::class.java)
                    intent.putExtra("uid", uid)
                    startActivity(intent)
                    return@setOnNavigationItemSelectedListener true
                }
                menuItem.itemId == R.id.manage_book_item -> {
                    val intent = Intent(this,  RequestActivity::class.java)
                    intent.putExtra("uid", uid)
                    startActivity(intent)
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
                    var chapter=document.get("listChapter") as ArrayList<Chapter>

                    var book= Book(name= document.get("name") as String,
                    author=document.get("author") as String?,
                    summary = document.get("summary") as String,
                    viewNumber = (document.get("viewNumber") as Long).toInt(),
                    locked=document.get("locked") as Boolean,
                    ratedAccount = document.get("ratedAccount") as ArrayList<String>,
                    imagePath = document.get("imagePath") as String,
                    listChapter = chapter,
                    listCategory = document.get("listCategory") as ArrayList<String>,
                    score =  document.get("score") as ArrayList<Int>)
                    listOfComic.add(book)
                }

                var adapter= ComicAdapter(listOfComic)

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