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

    //var uid:String = ""
    var uid:String = "ERQnHq5YlmL78h2wDBQX"
    var role:Long = 3
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)
        uid = FirebaseAuth.getInstance().currentUser!!.uid
        // Uncomment the line below if start from login
        //uid = intent.getStringExtra("uid").toString()
    }

    override fun onResume() {
        super.onResume()

        uid = FirebaseAuth.getInstance().currentUser!!.uid
        var fireStore = FirebaseFirestore.getInstance()
        //bottom_navigation.menu.findItem(R.id.home_item).isChecked = true
        findViewById<BottomNavigationView>(R.id.bottom_navigation).setOnNavigationItemSelectedListener { menuItem ->
            when {
                menuItem.itemId == R.id.profile_item -> {
                    fireStore.collection("accounts").document(uid)
                        .get().addOnSuccessListener { result ->
                            role = result.get("Role") as Long
                            //Log.d(TAG,  role.toString())
                            if (role == 1L) {
                                val intent = Intent(this,  ProfileActivity::class.java)
                                startActivity(intent)
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                            }
                            else{
                                val intent = Intent(this,  AdminProfileActivity::class.java)
                                startActivity(intent)
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                            }
                        }
                        .addOnFailureListener { exception ->
                            Log.d(TAG, "Error getting documents: ", exception)
                        }
                    return@setOnNavigationItemSelectedListener true
                }
                menuItem.itemId == R.id.manage_book_item -> {
                        fireStore.collection("accounts").document(uid)
                            .get().addOnSuccessListener { result ->
                                    role = result.get("Role") as Long
                                //Log.d(TAG,  role.toString())
                                if (role == 1L) {
                                    val intent = Intent(this,  RequestActivity::class.java)
                                    startActivity(intent)
                                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                                }
                                else{
                                    val intent = Intent(this,  RequestListActivity::class.java)
                                    startActivity(intent)
                                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                                }
                            }
                            .addOnFailureListener { exception ->
                                Log.d(TAG, "Error getting documents: ", exception)
                            }
                    return@setOnNavigationItemSelectedListener true
                }
                else -> false
            }
        }

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