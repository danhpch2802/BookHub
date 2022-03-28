package com.androidrealm.bookhub.Controllers.Activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.androidrealm.bookhub.Adapter.ComicAdapterLinear
import com.androidrealm.bookhub.Models.Book
import com.androidrealm.bookhub.R
import com.androidrealm.bookhub.Controllers.Fragments.ListComicLinearFragment

class ManageBookActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_book)

        val listOfComic=ArrayList<Book>()
        listOfComic.add(Book(R.drawable.amagami_cover,"Amagami"))
        listOfComic.add(Book(R.drawable.fechippuru_cover,"Fechippury"))
        listOfComic.add(Book(R.drawable.kanojo_cover,"Kanojo Okarimasu"))
        listOfComic.add(Book(R.drawable.komi_cover,"Komi-san"))
        listOfComic.add(Book(R.drawable.meika_cover,"Meika"))
        listOfComic.add(Book(R.drawable.doll_cover,"Sono bisque"))
        listOfComic.add(Book(R.drawable.mokanojo_cover,"Kanojo mo Kanojo"))
        listOfComic.add(Book(R.drawable.tonikaku_cover,"Tonakaku Cawaii"))
        listOfComic.add(Book(R.drawable.yofukashi_cover,"Yofukashi no uta"))

        val adapter = ComicAdapterLinear(listOfComic)

        if (savedInstanceState == null) {
            val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
            val fragment: Fragment = ListComicLinearFragment.newInstance(adapter)
            ft.replace(R.id.fragment_mange_book, fragment)
            ft.commit()
        }
    }

}