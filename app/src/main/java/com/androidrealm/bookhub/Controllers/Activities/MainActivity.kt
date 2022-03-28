package com.androidrealm.bookhub.Controllers.Activities

import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.androidrealm.bookhub.ComicAdapter
import com.androidrealm.bookhub.Models.Book
import com.androidrealm.bookhub.R
import com.androidrealm.bookhub.Controllers.Fragments.ListComicFragment


class MainActivity : AppCompatActivity() {

    lateinit var listComicFrame:FrameLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)

        val listOfComic=ArrayList<Book>()
        listOfComic.add(Book(R.drawable.amagami_cover,"Amagami"))
        listOfComic.add(Book(R.drawable.fechippuru_cover,"Fechippuuru"))
        listOfComic.add(Book(R.drawable.kanojo_cover,"Kanojo Okarimasu"))
        listOfComic.add(Book(R.drawable.komi_cover,"Komi-san",""))
        listOfComic.add(Book(R.drawable.meika_cover,"Meika",""))
        listOfComic.add(Book(R.drawable.doll_cover,"Sono bisque",""))
        listOfComic.add(Book(R.drawable.mokanojo_cover,"Kanojo mo Kanojo",""))
        listOfComic.add(Book(R.drawable.tonikaku_cover,"Tonakaku Cawaii",""))
        listOfComic.add(Book(R.drawable.yofukashi_cover,"Yofukashi no uta",""))
        var adapter= ComicAdapter(listOfComic)

        if (savedInstanceState == null) {
            val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
            val fragment: Fragment = ListComicFragment.newInstance(adapter,3)
            ft.replace(R.id.fragment_container_view, fragment)
            ft.commit()
        }
    }
}