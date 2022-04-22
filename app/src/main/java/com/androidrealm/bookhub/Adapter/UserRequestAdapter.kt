package com.androidrealm.bookhub.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.androidrealm.bookhub.Models.Request
import com.androidrealm.bookhub.R

class UserRequestAdapter(private val userRQList: ArrayList<Request>) : RecyclerView.Adapter<UserRequestAdapter.MyViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserRequestAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_user_request, parent, false)
        return MyViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: UserRequestAdapter.MyViewHolder, position: Int) {
        val userRequest: Request = userRQList[position]
        holder.rqTitle.text = userRequest.bookName
        holder.rqDetail.text = userRequest.bookDetail
        if(userRequest.checked == false){
            // Admin has not accepted
            holder.status.text = "Pending..."
            holder.statusImage.setImageResource(R.drawable.pending)
        }
        else {
            // Admin has accepted
            holder.status.text = "Done"
            holder.statusImage.setImageResource(R.drawable.check)
        }
        holder.time.text = userRequest.time
    }

    override fun getItemCount(): Int {
        return userRQList.size
    }

    public class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val status: TextView = itemView.findViewById(R.id.user_rq_status_tv)
        val rqTitle: TextView = itemView.findViewById(R.id.user_rq_title_tv)
        val rqDetail: TextView = itemView.findViewById(R.id.user_rq_detail_tv)
        var statusImage: ImageView = itemView.findViewById(R.id.status_icon)
        val time: TextView = itemView.findViewById(R.id.user_rq_time_tv)
    }
}