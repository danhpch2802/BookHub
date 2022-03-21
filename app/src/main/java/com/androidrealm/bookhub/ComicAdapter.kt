package com.androidrealm.bookhub

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class Comic(
    val imagePath:Int,
    val name:String
)
class ComicAdapter (private var listOfComic : List<Comic>
                      ):
    RecyclerView.Adapter<ComicAdapter.ViewHolder>() {

    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        val comicNameTV = listItemView.findViewById(R.id.comicNameTV) as TextView
        val comicIV = listItemView.findViewById(R.id.comicIV) as ImageView

        }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):

            ComicAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
// Inflate the custom layout
        val contactView = inflater.inflate(R.layout.item_list_comic, parent, false)
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
        val comicNameTV = holder.comicNameTV
        comicNameTV.setText(comic.name)
        val comicIV = holder.comicIV
        comicIV.setImageResource(comic.imagePath)
    }

}