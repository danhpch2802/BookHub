package com.androidrealm.bookhub.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.androidrealm.bookhub.ComicAdapter
import com.androidrealm.bookhub.Models.Book
import com.androidrealm.bookhub.Models.DownloadBook
import com.androidrealm.bookhub.R
import com.squareup.picasso.Picasso
import java.io.File
import java.io.Serializable

class DownloadBookAdapter (private var downloadBook : List<DownloadBook>
): RecyclerView.Adapter<DownloadBookAdapter.ViewHolder>(), Serializable {
    var onItemClick: ((DownloadBook) -> Unit)? = null
    var onDeleteClick: ((DownloadBook) -> Unit)? = null
    var pos:Int?=null
    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        val comicNameTV = listItemView.findViewById(R.id.comicNameTVlinear) as TextView
        var chapterNameTV =listItemView.findViewById(R.id.chapterNameTVlinear) as TextView
        val deleteDownloaded=listItemView.findViewById<ImageView>(R.id.deleteDownloaded)
        val comicIV = listItemView.findViewById(R.id.comicIVlinear) as ImageView
        init {
            listItemView.setOnClickListener {
                pos=adapterPosition
                onItemClick?.invoke(
                    downloadBook[adapterPosition]
                )
            }
            deleteDownloaded.setOnClickListener{
                pos=adapterPosition
                onDeleteClick?.invoke(
                    downloadBook[adapterPosition]
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            DownloadBookAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
// Inflate the custom layout
        val contactView = inflater.inflate(R.layout.item_download_book, parent, false)
// Return a new holder instance
        return ViewHolder(contactView)
    }

    override fun getItemCount(): Int {
        return downloadBook.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
// Get the data model based on position
        val book: DownloadBook = downloadBook.get(position)
// Set item views based on your views and data model
        val comicNameTV = holder.comicNameTV
        comicNameTV.setText(book.name)

        val chapterNameTV = holder.chapterNameTV
        chapterNameTV.setText(book.chapterName)
        comicNameTV.setText(book.name)
        val comicIV = holder.comicIV
        Picasso.get().load(File(book.imagePath)).into(comicIV);

    }



}