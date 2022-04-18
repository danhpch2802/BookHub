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


class AvatarAdapter (private var listOfAva : ArrayList<String>): RecyclerView.Adapter<AvatarAdapter.ViewHolder>(), Serializable {
    var onItemClick: ((String) -> Unit)? = null
    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        val avaNameTV = listItemView.findViewById(R.id.avaNameTV) as TextView
        val avaIV = listItemView.findViewById(R.id.avaIV) as ImageView
        init {
            listItemView.setOnClickListener {
                onItemClick?.invoke(
                    listOfAva[adapterPosition]
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            AvatarAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
// Inflate the custom layout
        val contactView = inflater.inflate(R.layout.item_ava, parent, false)
// Return a new holder instance
        return ViewHolder(contactView)
    }

    override fun getItemCount(): Int {
        return listOfAva.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
// Get the data model based on position
        val ava: String = listOfAva.get(position)
// Set item views based on your views and data model
        val avaNameTV = holder.avaNameTV
        avaNameTV.setText(ava)
        val avaIV = holder.avaIV
        when (ava) {
            "1" -> avaIV.setImageResource(R.drawable.amagami_cover)
            "2" -> avaIV.setImageResource(R.drawable.doll_cover)
            "3" -> avaIV.setImageResource(R.drawable.fechippuru_cover)
            "4" -> avaIV.setImageResource(R.drawable.kanojo_cover)
            "5" -> avaIV.setImageResource(R.drawable.komi_cover)
            "6" -> avaIV.setImageResource(R.drawable.meika_cover)
            "7" -> avaIV.setImageResource(R.drawable.mokanojo_cover)
            "8" -> avaIV.setImageResource(R.drawable.tonikaku_cover)
            "9" -> avaIV.setImageResource(R.drawable.yofukashi_cover)
            // else -> comicIV.setImageResource(R.drawable.)
        }

    }
}