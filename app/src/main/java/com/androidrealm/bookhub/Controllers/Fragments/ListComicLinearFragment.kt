package com.androidrealm.bookhub.Controllers.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidrealm.bookhub.Adapter.ComicAdapterLinear
import com.androidrealm.bookhub.R


class ListComicLinearFragment : Fragment() {
    private lateinit var listComicsRW:RecyclerView
    companion object {
        fun newInstance
                    (adapter: ComicAdapterLinear): ListComicLinearFragment
        {
            val fragment= ListComicLinearFragment()
            val bundle = Bundle()
            bundle.putSerializable("adapter", adapter)
            fragment.setArguments(bundle)
            return fragment
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        var view=inflater.inflate(R.layout.fragment_list_comic_linear, container, false)
        listComicsRW=view.findViewById(R.id.comicRWLinear)

        // set the custom adapter to the RecyclerView

        var adapter=requireArguments().getSerializable(
            "adapter"
        ) as ComicAdapterLinear

        listComicsRW.addItemDecoration(
            DividerItemDecoration(
                listComicsRW.getContext(),
                DividerItemDecoration.VERTICAL
            )
        )
        listComicsRW.adapter=adapter

        listComicsRW.layoutManager = LinearLayoutManager(activity)

        return view
    }

}