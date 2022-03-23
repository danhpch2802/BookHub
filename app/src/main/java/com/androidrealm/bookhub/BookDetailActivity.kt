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

class BookDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_detail)

        val comic=Comic(R.drawable.amagami_cover,"Amagami-san Chi no Enmusubi",
            "Kamihate Uryuu, orphaned since childhood, receives an invitation to stay at the local Shinto shrine. All he wanted was a quiet place to study so that he can fulfill his dream of making it into a top medical school, but after arriving there, he comes across three beautiful shrine maiden sisters... and the head priest requests that he marry one of them and take over the shrine. A tale of love and miracles is about to unfold!",
            "Naito Marcey"
                ,4.75
        )
        val listCategory=arrayListOf("Romance","Harem","Comedy", "Horror", "Gore", "Drama", "Ecchi",
        "Fantasy", "Sexual")
        val listChapters= ArrayList<Chapter>()

        listChapters.add(Chapter("Hello","abc"))
        listChapters.add(Chapter("Hello1","abc"))
        listChapters.add(Chapter("Hello2","abc"))
        listChapters.add(Chapter("Hello3","abc"))
        listChapters.add(Chapter("Hello4","abc"))

        comic.listChapter=listChapters
        comic.listCategory=listCategory

        val recommendedList=ArrayList<Comic>()
        recommendedList.add(Comic(R.drawable.amagami_cover,"Amagami"))
        recommendedList.add(Comic(R.drawable.fechippuru_cover,"Fechippuuru"))
        recommendedList.add(Comic(R.drawable.kanojo_cover,"Kanojo Okarimasu"))
        recommendedList.add(Comic(R.drawable.komi_cover,"Komi-san",""))
        recommendedList.add(Comic(R.drawable.meika_cover,"Meika",""))
        recommendedList.add(Comic(R.drawable.doll_cover,"Sono bisque",""))
        recommendedList.add(Comic(R.drawable.mokanojo_cover,"Kanojo mo Kanojo",""))
        recommendedList.add(Comic(R.drawable.tonikaku_cover,"Tonakaku Cawaii",""))
        recommendedList.add(Comic(R.drawable.yofukashi_cover,"Yofukashi no uta",""))

        val listComment=ArrayList<Comment>()
        listComment.add(Comment("1234","1234","Hello","28/02/2001"))
        listComment.add(Comment("344","1234","Hello3","2/02/2001"))
        listComment.add(Comment("452","1234","Hello4","3/02/2001"))
        listComment.add(Comment("754","1234","Hello5","5/02/2001"))

        val comicBarName=findViewById<TextView>(R.id.detail_book_barTV)
        comicBarName.setText(comic.name)

        if (savedInstanceState == null) {
            val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
            val fragment: Fragment = BookFragment.newInstance(comic,recommendedList,listComment,false)
            ft.replace(R.id.fragment_book, fragment)
            ft.commit()
        }
    }
}