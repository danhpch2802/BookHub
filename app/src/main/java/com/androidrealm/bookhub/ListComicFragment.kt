package com.androidrealm.bookhub

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class ListComicFragment : Fragment() {
    private lateinit var studentRW:RecyclerView
    companion object {
        fun newInstance
                    (adapter: ComicAdapter,gridLayoutSpanNum:Int): ListComicFragment
        {
            val fragment=ListComicFragment()
            val bundle = Bundle()
            bundle.putSerializable("adapter", adapter)
            bundle.putSerializable("gridLayoutSpanNum", gridLayoutSpanNum)//-1 means Linear Layout
            fragment.setArguments(bundle)
            return fragment
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

        var adapter=requireArguments().getSerializable(
            "adapter"
        ) as ComicAdapter

        studentRW.adapter=adapter

        var layout=requireArguments().getSerializable(
        "gridLayoutSpanNum"
        ) as Int
        if(layout>0){
            studentRW.layoutManager = GridLayoutManager(activity,layout)
        }
        else
        {
            studentRW.layoutManager = LinearLayoutManager(activity)

        }
        return view
    }

}