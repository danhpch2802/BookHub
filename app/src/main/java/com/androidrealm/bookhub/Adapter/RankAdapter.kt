package com.androidrealm.bookhub.Adapter

import android.content.ContentValues
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.androidrealm.bookhub.Models.Account
import com.androidrealm.bookhub.Models.Book
import com.androidrealm.bookhub.Models.Request
import com.androidrealm.bookhub.R
import com.google.firebase.firestore.FirebaseFirestore
import java.io.Serializable

class RankAdapter(private val listRank: ArrayList<Account>) : RecyclerView.Adapter<RankAdapter.MyViewHolder>(){
    private lateinit var mListener: onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_rank, parent, false)
        return MyViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val rank: Account = listRank[position]
        holder.rankName.text = rank.username
        holder.rankPoint.text = rank.Point.toString()
        holder.rankBadge.text = rank.BadgeOwn.size.toString()
        holder.rankRank.text = (position+1).toString()
        when (rank.Avatar) {
            "1" -> holder.rankIcon.setImageResource(R.drawable.amagami_cover)
            "2" -> holder.rankIcon.setImageResource(R.drawable.doll_cover)
            "3" -> holder.rankIcon.setImageResource(R.drawable.fechippuru_cover)
            "4" -> holder.rankIcon.setImageResource(R.drawable.kanojo_cover)
            "5" -> holder.rankIcon.setImageResource(R.drawable.komi_cover)
            "6" -> holder.rankIcon.setImageResource(R.drawable.meika_cover)
            "7" -> holder.rankIcon.setImageResource(R.drawable.mokanojo_cover)
            "8" -> holder.rankIcon.setImageResource(R.drawable.tonikaku_cover)
            "9" -> holder.rankIcon.setImageResource(R.drawable.yofukashi_cover)
            else -> holder.rankIcon.setImageResource(R.drawable.amagami_cover)
        }
    }

    override fun getItemCount(): Int {
        return listRank.size
    }

    class MyViewHolder(itemView: View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView){

        val rankName: TextView = itemView.findViewById(R.id.topName)
        val rankIcon: ImageView = itemView.findViewById(R.id.topAvatar)
        val rankPoint: TextView = itemView.findViewById(R.id.topPoint)
        val rankBadge: TextView = itemView.findViewById(R.id.topBadge)
        val rankRank: TextView = itemView.findViewById(R.id.topRank)
        init{
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }
}
