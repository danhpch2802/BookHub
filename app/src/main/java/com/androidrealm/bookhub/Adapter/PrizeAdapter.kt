package com.androidrealm.bookhub.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.androidrealm.bookhub.Models.Book
import com.androidrealm.bookhub.Models.Prize
import com.androidrealm.bookhub.Models.Request
import com.androidrealm.bookhub.R
import java.io.Serializable

class PrizeAdapter(private val listPrize: ArrayList<String>) : RecyclerView.Adapter<PrizeAdapter.MyViewHolder>(){
    private lateinit var mListener: onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_prize, parent, false)
        return MyViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val prize: String = listPrize[position]
        //holder.userid.text = prize.AccountId
        holder.prizeDetail.text = prize
    }

    override fun getItemCount(): Int {
        return listPrize.size
    }

    class MyViewHolder(itemView: View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView){

        val prizeDetail: TextView = itemView.findViewById(R.id.prizeNameTV)

        init{
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }
}