package com.androidrealm.bookhub.Adapter

import android.content.ContentValues
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.androidrealm.bookhub.Models.Book
import com.androidrealm.bookhub.Models.Prize
import com.androidrealm.bookhub.Models.Request
import com.androidrealm.bookhub.R
import com.google.firebase.firestore.FirebaseFirestore
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
        var id: String ?= ""
        //holder.userid.text = prize.AccountId
        holder.prizeDetail.text = prize
        val db = FirebaseFirestore.getInstance()
        db.collection("prizes").whereEqualTo("prizeName", prize)
            .get().addOnSuccessListener { documents ->
                for (document in documents) {
                    id = document["id"] as String
                    holder.prizeDes.text = document["prizeDescrypt"] as String
                }
                if (id == "b1" || id == "b2") {
                    holder.prizeIcon.setImageResource(R.drawable.orange_reward)
                }

                }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents: ", exception)
            }
    }

    override fun getItemCount(): Int {
        return listPrize.size
    }

    class MyViewHolder(itemView: View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView){

        val prizeDetail: TextView = itemView.findViewById(R.id.prizeNameTV)
        val prizeIcon: ImageView = itemView.findViewById(R.id.prize_icon)
        val prizeDes: TextView = itemView.findViewById(R.id.prizeDesTV)
        init{
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }
}
