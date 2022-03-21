package com.androidrealm.bookhub

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView



class ListComicFragment : Fragment() {
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<ComicAdapter.ViewHolder>? = null
    private lateinit var studentRW:RecyclerView
    companion object {
        fun newInstance(): ListComicFragment {
            return ListComicFragment()
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view=inflater.inflate(R.layout.fragment_list_comic, container, false)
        studentRW=view.findViewById(R.id.comicRW)
        // set the custom adapter to the RecyclerView
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

        var adapter=ComicAdapter(listOfComic)
        studentRW.adapter=adapter
        studentRW.layoutManager = GridLayoutManager(activity,3)
        return view
    }

}