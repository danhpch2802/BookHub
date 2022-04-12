package com.androidrealm.bookhub.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.androidrealm.bookhub.Models.Chapter
import com.androidrealm.bookhub.R

class ChapterAdapter (private var listChapters : ArrayList<Chapter>
): RecyclerView.Adapter<ChapterAdapter.ViewHolder>() {

    var pos:Int?=null
    var onRowsChapterClick : ((Chapter) -> Unit)? = null
    var itemProgressBar:ProgressBar?=null
    var onRowsChapterDownloadClick : ((Chapter) -> Unit)? = null
    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        val chapterNameTV = listItemView.findViewById(R.id.chapterNameTV) as TextView
        val downloadBtn = listItemView.findViewById<ImageView>(R.id.downloadBtn)
        init {
            chapterNameTV.setOnClickListener {
                pos=adapterPosition
                onRowsChapterClick?.invoke(listChapters[adapterPosition])
            }
            downloadBtn.setOnClickListener {
                onRowsChapterDownloadClick?.invoke(listChapters[adapterPosition])
            }

        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):

            ChapterAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
// Inflate the custom layout
        val chapterView = inflater.inflate(R.layout.item_chapter, parent, false)
// Return a new holder instance
        return ViewHolder(chapterView)
    }
    override fun getItemCount(): Int {
        return listChapters.size
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
// Get the data model based on position
        val chapter: Chapter = listChapters.get(position)
// Set item views based on your views and data model
        val chapterNameTW = holder.chapterNameTV
        chapterNameTW.setText(chapter.name)

    }



}