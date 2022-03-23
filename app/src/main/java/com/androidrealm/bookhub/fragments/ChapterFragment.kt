package com.androidrealm.bookhub.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidrealm.bookhub.Adapter.ChapterAdapter
import com.androidrealm.bookhub.Models.Chapter
import com.androidrealm.bookhub.R

class ChapterFragment(listChapter: Any?) : Fragment() {
    private lateinit var chapterRW:RecyclerView
    companion object {
        fun newInstance
                    (listChapter: ArrayList<Chapter>): ChapterFragment
        {
            val fragment= ChapterFragment(listChapter)
            val bundle = Bundle()
            bundle.putSerializable("listChapter", listChapter)
            fragment.setArguments(bundle)
            return fragment
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        var view=inflater.inflate(R.layout.fragment_chapter, container, false)
        chapterRW=view.findViewById(R.id.chapterRW)

        // set the custom adapter to the RecyclerView

        var listChapter=requireArguments().getSerializable(
            "listChapter"
        ) as ArrayList<Chapter>

        chapterRW.addItemDecoration(
            DividerItemDecoration(
                chapterRW.getContext(),
                DividerItemDecoration.VERTICAL
            )
        )

        var adapter=ChapterAdapter(listChapter)
        chapterRW.adapter=adapter

        chapterRW.layoutManager = LinearLayoutManager(activity)

        return view
    }

}