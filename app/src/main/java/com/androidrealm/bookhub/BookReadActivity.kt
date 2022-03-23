package com.androidrealm.bookhub

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.androidrealm.bookhub.Models.Chapter
import com.androidrealm.bookhub.Models.Comic
import com.androidrealm.bookhub.Models.Comment
import com.androidrealm.bookhub.fragments.BookFragment

class BookReadActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_read)


    }
}