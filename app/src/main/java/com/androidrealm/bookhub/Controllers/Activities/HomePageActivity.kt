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
import com.androidrealm.bookhub.Controllers.Fragments.DownloadedBookFragment
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
    var location=1
    var uid:String = "ERQnHq5YlmL78h2wDBQX"
    var role:Long = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)
        uid = FirebaseAuth.getInstance().currentUser!!.uid

        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
        val fragment: Fragment = ListComicFragment.newInstance(3)
        ft.replace(R.id.fragment_container_view, fragment)
        ft.commit()
    }

    override fun onResume() {
        super.onResume()

        uid = FirebaseAuth.getInstance().currentUser!!.uid
        var fireStore = FirebaseFirestore.getInstance()
        //bottom_navigation.menu.findItem(R.id.home_item).isChecked = true

        findViewById<BottomNavigationView>(R.id.bottom_navigation).setOnNavigationItemSelectedListener { menuItem ->
            when {
                menuItem.itemId == R.id.home_item-> {
                    if(location!=1) {

                        val ft: FragmentTransaction =
                            supportFragmentManager.beginTransaction().setCustomAnimations(
                                R.anim.slide_in_left,
                                R.anim.slide_out_right
                            )
                        val fragment: Fragment = ListComicFragment.newInstance(3)
                        ft.replace(R.id.fragment_container_view, fragment)
                        ft.commit()
                        location=1
                    }
                    return@setOnNavigationItemSelectedListener true

                }
                menuItem.itemId == R.id.profile_item -> {
                    if(location!=2) {
                        fireStore.collection("accounts").document(uid)
                            .get().addOnSuccessListener { result ->
                                role = result.get("Role") as Long
                                if (role == 1L) {
                                    val intent = Intent(this, ProfileActivity::class.java)
                                    startActivity(intent)
                                    overridePendingTransition(
                                        R.anim.slide_in_right,
                                        R.anim.slide_out_left
                                    )
                                } else {
                                    val intent = Intent(this, AdminProfileActivity::class.java)
                                    startActivity(intent)
                                    overridePendingTransition(
                                        R.anim.slide_in_right,
                                        R.anim.slide_out_left
                                    )
                                }
                            }
                            .addOnFailureListener { exception ->
                                Log.d(TAG, "Error getting documents: ", exception)
                            }
                        location=2
                    }
                    return@setOnNavigationItemSelectedListener true
                }
                menuItem.itemId == R.id.manage_book_item -> {
                    if(location!=3) {
                        fireStore.collection("accounts").document(uid)
                            .get().addOnSuccessListener { result ->
                                role = result.get("Role") as Long
                                //Log.d(TAG,  role.toString())
                                if (role == 1L) {
                                    val intent = Intent(this, RequestActivity::class.java)
                                    startActivity(intent)
                                    overridePendingTransition(
                                        R.anim.slide_in_right,
                                        R.anim.slide_out_left
                                    )
                                } else {
                                    val intent = Intent(this, RequestListActivity::class.java)
                                    startActivity(intent)
                                    overridePendingTransition(
                                        R.anim.slide_in_right,
                                        R.anim.slide_out_left
                                    )
                                }
                            }
                            .addOnFailureListener { exception ->
                                Log.d(TAG, "Error getting documents: ", exception)
                            }
                        location=3
                    }
                    return@setOnNavigationItemSelectedListener true
                }
                menuItem.itemId == R.id.downloaded_item->{
                    if(location!=4) {
                        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
                            .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                        val fragment: Fragment = DownloadedBookFragment.newInstance()
                        ft.replace(R.id.fragment_container_view, fragment)
                        ft.commit()
                        location=4
                    }
                    return@setOnNavigationItemSelectedListener true

                }
                else -> false
            }
        }
    }
}