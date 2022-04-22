package com.androidrealm.bookhub.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
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
        holder.username.text = request.accountName
        holder.requestDetail.text = request.bookDetail
        holder.requestTitle.text = request.bookName
        holder.time.text = request.time
        holder.checked.text = request.checked.toString()
        if (holder.checked.text == "true"){
            holder.check_image.setImageResource(R.drawable.check)
        }
        else holder.check_image.setImageResource(R.drawable.multiply)
    }

    override fun getItemCount(): Int {
        return listRequest.size
    }

    class MyViewHolder(itemView: View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView){
        val username: TextView = itemView.findViewById(R.id.rq_id_tv)
        val requestTitle: TextView = itemView.findViewById(R.id.rq_title_tv)
        val requestDetail: TextView = itemView.findViewById(R.id.rq_detail_tv)
        val checked: TextView = itemView.findViewById(R.id.rq_checked_tv)
        var check_image: ImageView = itemView.findViewById(R.id.check_icon)
        val time: TextView = itemView.findViewById(R.id.rq_time_tv)

        init{
            itemView.setOnClickListener { 
                listener.onItemClick(adapterPosition)
            }
        }
    }
}