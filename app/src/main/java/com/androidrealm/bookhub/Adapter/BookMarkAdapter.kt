package com.androidrealm.bookhub.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.androidrealm.bookhub.Models.Book
import com.androidrealm.bookhub.Models.Bookmark
import com.androidrealm.bookhub.R
import com.squareup.picasso.Picasso
import java.io.Serializable

class BookmarkAdapter (private var listBookMark : List<Bookmark>
):
    RecyclerView.Adapter<BookmarkAdapter.ViewHolder>(), Serializable {

    var onItemClick: ((Bookmark) -> Unit)? = null

    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        val comicNameTVlinear = listItemView.findViewById(R.id.comicNameTVlinear) as TextView
        val chapterNameTVLinear = listItemView.findViewById(R.id.chapterNameTVLinear) as TextView

        val comicIVlinear = listItemView.findViewById(R.id.comicIVlinear) as ImageView
        init {
            listItemView.setOnClickListener {
                onItemClick?.invoke(
                    listBookMark[adapterPosition]
                )
            }
        }

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):

            BookmarkAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
// Inflate the custom layout
        val contactView = inflater.inflate(R.layout.item_list_bookmark_linear, parent, false)
// Return a new holder instance
        return ViewHolder(contactView)
    }
    override fun getItemCount(): Int {
        return listBookMark.size
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val bookmark: Bookmark = listBookMark.get(position)

        val comicNameTV = holder.comicNameTVlinear
        comicNameTV.setText(bookmark.bookName)
        val comicIV = holder.comicIVlinear
        Picasso.get().load(bookmark.coverUrl).into(comicIV);

        val chapterNameTV = holder.chapterNameTVLinear
        chapterNameTV.setText(bookmark.chapterName)

    }

}