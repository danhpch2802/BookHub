package com.androidrealm.bookhub.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.androidrealm.bookhub.Models.Book
import com.androidrealm.bookhub.Models.Prize
import com.androidrealm.bookhub.R
import java.io.Serializable

class PrizeAdapter (private var listPrizes : List<Prize>
): RecyclerView.Adapter<PrizeAdapter.ViewHolder>(), Serializable {
    var onItemClick: ((Prize) -> Unit)? = null

    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView), Serializable {
        val prizeNameTV = listItemView.findViewById(R.id.prizeNameTV) as TextView
        init {
            listItemView.setOnClickListener {
                onItemClick?.invoke(
                    listPrizes[adapterPosition]
                )
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            PrizeAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
// Inflate the custom layout
        val prizeView = inflater.inflate(R.layout.item_prize, parent, false)
// Return a new holder instance
        return ViewHolder(prizeView)
    }
    override fun getItemCount(): Int {
        return listPrizes.size
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
// Get the data model based on position
        val prize: Prize = listPrizes.get(position)
// Set item views based on your views and data model
        val prizeNameTW = holder.prizeNameTV
        prizeNameTW.setText(prize.prizeName)
    }
}