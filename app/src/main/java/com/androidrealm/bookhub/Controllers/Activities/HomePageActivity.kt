package com.androidrealm.bookhub.Controllers.Activities

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.androidrealm.bookhub.Controllers.Fragments.*
import com.androidrealm.bookhub.R
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.io.Serializable


class HomePageActivity : AppCompatActivity(),Serializable {

    lateinit var appBarLayout2: MaterialToolbar
    lateinit var listComicFrame:FrameLayout
    var location=1
    var uid:String = "ERQnHq5YlmL78h2wDBQX"
    var role:Long = 3
    val profileFragment = ProfileFragment()
    val requestFragment = RequestFragment()
    val adminProfileFragment = AdminProfileFragment()
    val adminRequestListFragment = RequestListFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)
        uid = FirebaseAuth.getInstance().currentUser!!.uid
        appBarLayout2 = findViewById(R.id.topAppbar)
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
                        appBarLayout2.setTitle("Home")

                        val fragment: Fragment = ListComicFragment.newInstance(3)
                        replaceFragment(fragment, location, 1)
                        location=1
                    }
                    return@setOnNavigationItemSelectedListener true

                }
                menuItem.itemId == R.id.profile_item -> {
                    if(location!=2) {
                        appBarLayout2.setTitle("Profile")
                        fireStore.collection("accounts").document(uid)
                            .get().addOnSuccessListener { result ->
                                role = result.get("Role") as Long
                                if (role == 1L) {

                                    Log.d(TAG, "Location Value: " + location.toString())
                                    replaceFragment(profileFragment, location, 2)
//
                                } else {
                                    Log.d(TAG, "Location Value: " + location.toString())
                                    replaceFragment(adminProfileFragment, location,2)
                                }
                                location=2
                            }
                            .addOnFailureListener { exception ->
                                Log.d(TAG, "Error getting documents: ", exception)
                            }
                    }
                    return@setOnNavigationItemSelectedListener true
                }
                menuItem.itemId == R.id.manage_book_item -> {
                    if(location!=3) {
                        appBarLayout2.setTitle("Request")
                        fireStore.collection("accounts").document(uid)
                            .get().addOnSuccessListener { result ->
                                role = result.get("Role") as Long
                                //Log.d(TAG,  role.toString())
                                if (role == 1L) {
                                    Log.d(TAG, "Location Value: " + location.toString())
                                    replaceFragment(requestFragment, location,3)
                                } else {
                                    Log.d(TAG, "Location Value: " + location.toString())
                                    replaceFragment(adminRequestListFragment, location,3 )
                                }
                                location=3
                            }
                            .addOnFailureListener { exception ->
                                Log.d(TAG, "Error getting documents: ", exception)
                            }
                    }
                    return@setOnNavigationItemSelectedListener true
                }
                menuItem.itemId == R.id.downloaded_item->{
                    if(location!=4) {
                        appBarLayout2.setTitle("Download")
                        val fragment: Fragment = DownloadedBookFragment.newInstance()
                        replaceFragment(fragment, location, 4)
                        location=4
                    }
                    return@setOnNavigationItemSelectedListener true

                }
                else -> false
            }
        }
    }

    private fun replaceFragment (fragment: Fragment, Location: Int, Cmp: Int)
    {
        if (fragment!= null ){
            //Log.d(TAG, "Location In-Value: " + Location.toString())

            if (Location > Cmp) {
                val transaction = supportFragmentManager.beginTransaction().setCustomAnimations(
                    R.anim.slide_in_left,
                    R.anim.slide_out_right
                )
                transaction.replace(R.id.fragment_container_view, fragment)
                transaction.commit()
            }
            else {
                val transaction = supportFragmentManager.beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                transaction.replace(R.id.fragment_container_view, fragment)
                transaction.commit()
            }
        }
        }
}