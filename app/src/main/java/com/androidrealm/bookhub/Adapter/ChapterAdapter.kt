package com.androidrealm.bookhub.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.androidrealm.bookhub.Models.Chapter
import com.androidrealm.bookhub.Models.Comic
import com.androidrealm.bookhub.R
import java.io.Serializable

class ChapterAdapter (private var listChapters : List<Chapter>
):
    RecyclerView.Adapter<ChapterAdapter.ViewHolder>(), Serializable {

    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        val chapterNameTV = listItemView.findViewById(R.id.chapterNameTV) as TextView

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
        chapterNameTW.setText(chapter.chapterName)

    }



}