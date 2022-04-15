package com.androidrealm.bookhub.Controllers.Activities

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.androidrealm.bookhub.R
import com.androidrealm.bookhub.Controllers.Fragments.BookFragment
import com.androidrealm.bookhub.Models.Book

class BookCreateActivity: AppCompatActivity() {
    var ReBtn: ImageView?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_create)

        ReBtn = findViewById(R.id.reBookCreate)

        ReBtn!!.setOnClickListener{
            finish()
        }
        if (savedInstanceState == null) {
            val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
            val fragment: Fragment = BookFragment.newInstance(createNew = true,editable=false,book= Book())
            ft.replace(R.id.fragment_book, fragment)
            ft.commit()
        }
    }
}