package com.androidrealm.bookhub.Controllers.Activities

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.androidrealm.bookhub.Adapter.PrizeAdapter
import com.androidrealm.bookhub.ComicAdapter
import com.androidrealm.bookhub.Controllers.Fragments.ListComicFragment
import com.androidrealm.bookhub.Models.Book
import com.androidrealm.bookhub.Models.Prize
import com.androidrealm.bookhub.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

class PrizeListActivity : AppCompatActivity() {

    var uid:String = "ERQnHq5YlmL78h2wDBQX"

    var badgePrizeList: TextView? = null
    var badgeTV: TextView? = null
    var reBtn: ImageView? = null
    var TotalBadge:Int? = 0
    var Badge:String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prize_list)

        var fireStore = FirebaseFirestore.getInstance()
        uid = FirebaseAuth.getInstance().currentUser!!.uid
        reBtn = findViewById(R.id.prizelistReturn)

        reBtn!!.setOnClickListener{
            val intent = Intent(this,  PrizeActivity::class.java)
            startActivity(intent)
            finish()
        }

        val lists = ArrayList<Prize>()
        val docRef = fireStore.collection("prizes")
        docRef.get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val comicGet = document.toObject<Prize>()
                    comicGet.id = document.id
                    lists.add(comicGet)
                }

                var adapter= PrizeAdapter(lists)

                adapter.onItemClick = { prize ->
                    val intent = Intent(this, PrizeDetailActivity::class.java)
                    intent.putExtra("id",prize.id)
                    startActivity(intent)
                }
                val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
                val fragment: Fragment = PrizeFragment.newInstance(lists)
                ft.replace(R.id.fragment_prize_list_view, fragment)
                ft.commit()
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "Error getting documents: ", exception)
            }
    }
}