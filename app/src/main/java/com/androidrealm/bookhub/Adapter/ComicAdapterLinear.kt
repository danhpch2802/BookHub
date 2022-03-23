package com.androidrealm.bookhub.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.androidrealm.bookhub.Models.Comic
import com.androidrealm.bookhub.R
import java.io.Serializable

class ComicAdapterLinear (private var listOfComic : List<Comic>
):
    RecyclerView.Adapter<ComicAdapterLinear.ViewHolder>(), Serializable {

    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        val comicNameTVlinear = listItemView.findViewById(R.id.comicNameTVlinear) as TextView
        val comicIVlinear = listItemView.findViewById(R.id.comicIVlinear) as ImageView

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):

            ComicAdapterLinear.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
// Inflate the custom layout
        val contactView = inflater.inflate(R.layout.item_list_comic_linear, parent, false)
// Return a new holder instance
        return ViewHolder(contactView)
    }
    override fun getItemCount(): Int {
        return listOfComic.size
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
// Get the data model based on position
        val comic: Comic = listOfComic.get(position)
// Set item views based on your views and data model
        val comicNameTV = holder.comicNameTVlinear
        comicNameTV.setText(comic.name)
        val comicIV = holder.comicIVlinear
        comicIV.setImageResource(comic.imagePath!!)
    }

}