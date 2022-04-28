package com.androidrealm.bookhub.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.androidrealm.bookhub.Models.Account
import com.androidrealm.bookhub.R

class UserFriendsAdapter(private val userFriendsList: ArrayList<Account>) : RecyclerView.Adapter<UserFriendsAdapter.MyViewHolder>(){
    private lateinit var mListener: onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: UserFriendsAdapter.onItemClickListener){
        mListener = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_users_friends, parent, false)
        return MyViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val friends: Account = userFriendsList[position]
        holder.username.text = friends.username
        holder.status.text = friends.status
        when (friends.Avatar) {
            "1" -> holder.avatar.setImageResource(R.drawable.amagami_cover)
            "2" -> holder.avatar.setImageResource(R.drawable.doll_cover)
            "3" -> holder.avatar.setImageResource(R.drawable.fechippuru_cover)
            "4" -> holder.avatar.setImageResource(R.drawable.kanojo_cover)
            "5" -> holder.avatar.setImageResource(R.drawable.komi_cover)
            "6" -> holder.avatar.setImageResource(R.drawable.meika_cover)
            "7" -> holder.avatar.setImageResource(R.drawable.mokanojo_cover)
            "8" -> holder.avatar.setImageResource(R.drawable.tonikaku_cover)
            "9" -> holder.avatar.setImageResource(R.drawable.yofukashi_cover)
            else -> holder.avatar.setImageResource(R.drawable.amagami_cover)
        }
    }

    override fun getItemCount(): Int {
        return userFriendsList.size
    }

    class MyViewHolder(itemView: View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView){
        val username: TextView = itemView.findViewById(R.id.nameTV)
        val status: TextView = itemView.findViewById(R.id.profile_status)
        var avatar: ImageView = itemView.findViewById(R.id.profile_picture)

        init{
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }
}