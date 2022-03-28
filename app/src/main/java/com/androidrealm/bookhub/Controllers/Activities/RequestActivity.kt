package com.androidrealm.bookhub.Controllers.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.androidrealm.bookhub.R

class RequestActivity : AppCompatActivity() {

    var submitBtn: TextView? = null
    var RqEt: EditText? = null
    lateinit var listComicFrame: FrameLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request)

        submitBtn = findViewById(R.id.rqsubmitBtn)
        RqEt = findViewById(R.id.rqbook)

        submitBtn!!.setOnClickListener{
            Toast.makeText(this@RequestActivity, "Submit Successfully!", Toast.LENGTH_SHORT).show()
        }

    }
////
//    val listOfRequest=arrayOf("Ara Ara", "Cll", "Raiden")
//    val adapter = ArrayAdapter(this, R.layout., listOfRequest)
//
//    var adapter=ComicAdapter(listOfComic)
//
//    if (savedInstanceState == null) {
//        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
//        val fragment: Fragment = ListComicFragment.newInstance(adapter,3)
//        ft.replace(R.id.fragment_container_view, fragment)
//        ft.commit()
//    }
}