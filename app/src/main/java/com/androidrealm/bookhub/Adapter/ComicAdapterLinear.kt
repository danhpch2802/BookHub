package com.androidrealm.bookhub.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.androidrealm.bookhub.Models.Book
import com.androidrealm.bookhub.R
import com.squareup.picasso.Picasso
import java.io.Serializable

class ComicAdapterLinear (private var listOfBook : List<Book>
):
    RecyclerView.Adapter<ComicAdapterLinear.ViewHolder>(), Serializable {

    var onItemClick: ((Book) -> Unit)? = null

    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        val comicNameTVlinear = listItemView.findViewById(R.id.comicNameTVlinear) as TextView
        val comicIVlinear = listItemView.findViewById(R.id.comicIVlinear) as ImageView
        init {
            listItemView.setOnClickListener {
                onItemClick?.invoke(
                    listOfBook[adapterPosition]
                )
            }
        }

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
        return listOfBook.size
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val book: Book = listOfBook.get(position)

        val comicNameTV = holder.comicNameTVlinear
        comicNameTV.setText(book.name)
        val comicIV = holder.comicIVlinear
        Picasso.get().load(book.imagePath).into(comicIV);

    }

}