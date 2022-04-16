package com.androidrealm.bookhub.Controllers.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidrealm.bookhub.ComicAdapter
import com.androidrealm.bookhub.Controllers.Activities.BookDetailActivity
import com.androidrealm.bookhub.Models.Book
import com.androidrealm.bookhub.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

class ListComicFragment : Fragment() {
    private lateinit var listComicsRW:RecyclerView
    companion object {
        fun newInstance
                    (gridLayoutSpanNum:Int): ListComicFragment
        {
            val fragment= ListComicFragment()
            val bundle = Bundle()
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
        return view
    }

    override fun onResume() {
        super.onResume()
        listComicsRW= requireView().findViewById(R.id.comicRW)
        // set the custom adapter to the RecyclerView

        var layout=requireArguments().getSerializable(
            "gridLayoutSpanNum"
        ) as Int

        var fireStore = FirebaseFirestore.getInstance()

        val listOfComic = ArrayList<Book>();
        val docRef = fireStore.collection("comics")
        docRef.get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val comicGet = document.toObject<Book>()
                    comicGet.id = document.id
                    listOfComic.add(comicGet)
                }

                var adapter = ComicAdapter(listOfComic)
                adapter.onItemClick = { book ->
                    val intent = Intent(activity, BookDetailActivity::class.java)
                    intent.putExtra("id", book.id)
                    startActivity(intent)
                }

                listComicsRW.adapter=adapter

                if(layout>0){
                    listComicsRW.layoutManager = GridLayoutManager(activity,layout)
                }
                else
                {
                    if(layout==-1) listComicsRW.layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
                    else listComicsRW.layoutManager = LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false)
                }
            }

    }
}