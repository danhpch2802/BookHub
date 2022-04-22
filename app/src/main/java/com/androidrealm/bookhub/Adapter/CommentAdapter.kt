package com.androidrealm.bookhub.Adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.androidrealm.bookhub.Models.Book
import com.androidrealm.bookhub.Models.Comment
import com.androidrealm.bookhub.R
import com.google.firebase.firestore.FirebaseFirestore
import java.io.Serializable
import java.text.SimpleDateFormat

class CommentAdapter (private var listComments : List<Comment>
):
    RecyclerView.Adapter<CommentAdapter.ViewHolder>(), Serializable {

    var onItemClick: ((Comment) -> Unit)? = null

    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        val commentorTV = listItemView.findViewById(R.id.commentorTV) as TextView
        val dateCommentTV = listItemView.findViewById(R.id.dateCommentTV) as TextView
        val contentTV = listItemView.findViewById(R.id.contentTV) as TextView
        val numRatedTV = listItemView.findViewById<TextView>(R.id.numratedComment)
        val avaratIV = listItemView.findViewById<ImageView>(R.id.avatarpf_img)
        val badgeTV = listItemView.findViewById<TextView>(R.id.pf_prize)

        init {
            listItemView.setOnClickListener {
                onItemClick?.invoke(listComments[adapterPosition])
            }
        }

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):

            CommentAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
// Inflate the custom layout
        val commentView = inflater.inflate(R.layout.item_comment, parent, false)
// Return a new holder instance
        return ViewHolder(commentView)
    }
    override fun getItemCount(): Int {
        return listComments.size
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
// Get the data model based on position
        val comment: Comment = listComments.get(position)
// Set item views based on your views and data model
        val commentorTV = holder.commentorTV
        commentorTV.setText(comment.accountName)
        val dateCommentTV = holder.dateCommentTV

        val dateFormat = SimpleDateFormat("dd-MM-yyyy")


        dateCommentTV.setText(dateFormat.format(comment.createdAt).toString())
        val contentTV = holder.contentTV
        contentTV.setText(comment.content)

        val ratedNumTV = holder.numRatedTV

        ratedNumTV.setText(comment.starRated.toString())

        val avatarCmt = holder.avaratIV
        val badgeTV = holder.badgeTV

        var badge :String = ""

        val accountRef = FirebaseFirestore.getInstance().collection("accounts")
            .document(comment.accountID.toString())
            .get()
            .addOnCompleteListener { result ->
                if (result.isSuccessful) {
                    var avatar:String ?= ""
                    avatar = result.result["Avatar"] as String?
                    when (avatar) {
                        "1" -> avatarCmt!!.setImageResource(R.drawable.amagami_cover)
                        "2" -> avatarCmt!!.setImageResource(R.drawable.doll_cover)
                        "3" -> avatarCmt!!.setImageResource(R.drawable.fechippuru_cover)
                        "4" -> avatarCmt!!.setImageResource(R.drawable.kanojo_cover)
                        "5" -> avatarCmt!!.setImageResource(R.drawable.komi_cover)
                        "6" -> avatarCmt!!.setImageResource(R.drawable.meika_cover)
                        "7" -> avatarCmt!!.setImageResource(R.drawable.mokanojo_cover)
                        "8" -> avatarCmt!!.setImageResource(R.drawable.tonikaku_cover)
                        "9" -> avatarCmt!!.setImageResource(R.drawable.yofukashi_cover)
                        else -> avatarCmt!!.setImageResource(R.drawable.amagami_cover)
                    }

                    for (document in result.result["Badge"] as ArrayList<*>) {
                        badge = document as String
                        break
                    }
                }
                else {
                    Log.i("comment Error", result.exception!!.message.toString())
                }

                val badgeRef = FirebaseFirestore.getInstance().collection("prizes")
                    .document(badge)
                    .get()
                    .addOnCompleteListener { result ->
                        if (result.isSuccessful)
                        {
                            val temp = result.result["prizeName"] as String
                            badgeTV.setText(temp)
                            if (badge.equals("b1") || badge.equals("b2"))
                            {
                                badgeTV!!.setBackgroundColor(Color.parseColor("#f7971e"))
                            }
                        }
                        else
                        {
                            Log.i("comment Error", result.exception!!.message.toString())
                        }

                    }
            }

    }


}