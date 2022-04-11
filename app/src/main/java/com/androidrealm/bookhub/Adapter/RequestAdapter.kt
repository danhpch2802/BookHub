package com.androidrealm.bookhub.Adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.androidrealm.bookhub.Models.Request
import com.androidrealm.bookhub.R

class RequestAdapter(private val listRequest: ArrayList<Request>) : RecyclerView.Adapter<RequestAdapter.MyViewHolder>(){
    private lateinit var mListener: onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_request, parent, false)
        return MyViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val request: Request = listRequest[position]
        holder.userid.text = request.accountID
        holder.requestDetail.text = request.bookDetail
        holder.requestTitle.text = request.bookName
        holder.checked.text = request.checked.toString()
    }

    override fun getItemCount(): Int {
        return listRequest.size
    }

    class MyViewHolder(itemView: View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView){
        val userid: TextView = itemView.findViewById(R.id.rq_id_tv)
        val requestTitle: TextView = itemView.findViewById(R.id.rq_title_tv)
        val requestDetail: TextView = itemView.findViewById(R.id.rq_detail_tv)
        val checked: TextView = itemView.findViewById(R.id.rq_checked_tv)

        init{
            itemView.setOnClickListener { 
                listener.onItemClick(adapterPosition)
            }
        }
    }
}