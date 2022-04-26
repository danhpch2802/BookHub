package com.androidrealm.bookhub.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.androidrealm.bookhub.Models.User
import com.androidrealm.bookhub.R

class UserFriendsAdapter(private val userFriendsList: ArrayList<User>) : RecyclerView.Adapter<UserFriendsAdapter.MyViewHolder>(){
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

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val friends: User = userFriendsList[position]
        holder.username.text = friends.person.username
        holder.status.text = friends.onlineState
        holder.avatar = friends.ava!!
    }

    override fun getItemCount(): Int {
        return userFriendsList.size
    }

    public class MyViewHolder(itemView: View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView){
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