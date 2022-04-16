package com.androidrealm.bookhub.Controllers.Fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidrealm.bookhub.Adapter.ComicAdapterLinear
import com.androidrealm.bookhub.Adapter.DownloadBookAdapter
import com.androidrealm.bookhub.Controllers.Activities.BookReadActivity
import com.androidrealm.bookhub.Controllers.Activities.OfflineBookReadActivity
import com.androidrealm.bookhub.Controllers.DAO.DownloadBookDatabase
import com.androidrealm.bookhub.Models.DownloadBook
import com.androidrealm.bookhub.R
import java.io.File

class DownloadedBookFragment:  Fragment() {
    private lateinit var listComicsRW: RecyclerView
    companion object {
        fun newInstance(): DownloadedBookFragment {
            val fragment = DownloadedBookFragment()

            return fragment
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        var view = inflater.inflate(R.layout.fragment_downloadbook, container, false)
        listComicsRW = view.findViewById(R.id.comicRW)

        // set the custom adapter to the RecyclerView

        var db = DownloadBookDatabase.getInstance(requireActivity())
        val listDownloaded=db.downloadBookDAO().getDownloadBookList() as ArrayList
        for(i in listDownloaded){
            Log.i("testing1",i.chapterName)
            Log.i("testing1",i.imagePath)
        }
        var adapter = DownloadBookAdapter(listDownloaded)

        listComicsRW.addItemDecoration(
            DividerItemDecoration(
                listComicsRW.getContext(),
                DividerItemDecoration.VERTICAL
            )
        )
        listComicsRW.adapter = adapter

        adapter.onItemClick={
                chapterClick ->
                if(!File(chapterClick.chapterPath).exists()){
                    Toast.makeText(requireActivity(),"Corrupted book, clearing.,",Toast.LENGTH_LONG).show()
                    listDownloaded.removeAt(adapter.pos!!)
                    adapter.notifyItemRemoved(adapter.pos!!)
                    deleteDownloaded(chapterClick)
                }
            else{
                    val intent = Intent(requireActivity(), OfflineBookReadActivity::class.java)
                    intent.putExtra("chapterName", chapterClick.chapterName)
                    intent.putExtra("chapterLink", chapterClick.chapterPath)
                    startActivity(intent)
                }



        }

        adapter.onDeleteClick={
                chapterClick ->
            listDownloaded.removeAt(adapter.pos!!)
            adapter.notifyItemRemoved(adapter.pos!!)
            deleteDownloaded(chapterClick)



        }
        listComicsRW.layoutManager = LinearLayoutManager(activity)

        return view
    }

    fun deleteDownloaded(dowloaded:DownloadBook){
        var db = DownloadBookDatabase.getInstance(requireActivity())
        db.downloadBookDAO().deteleDownloadBook(downloadBook =dowloaded )
        var cover= File(dowloaded.imagePath)
        cover.delete()

        var pdf=File(dowloaded.chapterPath)
    }
}
