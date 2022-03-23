package com.androidrealm.bookhub

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.androidrealm.bookhub.Models.Comic
import com.androidrealm.bookhub.fragments.ListComicFragment

class FavoriteListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite_list)

        val listOfComic=ArrayList<Comic>()
        listOfComic.add(Comic(R.drawable.amagami_cover,"Amagami"))
        listOfComic.add(Comic(R.drawable.fechippuru_cover,"Fechippury"))
        listOfComic.add(Comic(R.drawable.kanojo_cover,"Kanojo Okarimasu"))
        listOfComic.add(Comic(R.drawable.komi_cover,"Komi-san"))
        listOfComic.add(Comic(R.drawable.meika_cover,"Meika"))
        listOfComic.add(Comic(R.drawable.doll_cover,"Sono bisque"))
        listOfComic.add(Comic(R.drawable.mokanojo_cover,"Kanojo mo Kanojo"))
        listOfComic.add(Comic(R.drawable.tonikaku_cover,"Tonakaku Cawaii"))
        listOfComic.add(Comic(R.drawable.yofukashi_cover,"Yofukashi no uta"))

        val adapter = ComicAdapter(listOfComic)

        if (savedInstanceState == null) {
            val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
            val fragment: Fragment = ListComicFragment.newInstance(adapter,3)
            ft.replace(R.id.fragment_Favorite_List, fragment)
            ft.commit()
        }
    }
}