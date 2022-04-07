package com.androidrealm.bookhub.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.androidrealm.bookhub.Models.Chapter
import com.androidrealm.bookhub.Models.Comment
import com.androidrealm.bookhub.R
import java.io.Serializable

class CommentAdapter (private var listComments : List<Comment>
):
    RecyclerView.Adapter<CommentAdapter.ViewHolder>(), Serializable {

    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        val commentorTV = listItemView.findViewById(R.id.commentorTV) as TextView
        val dateCommentTV = listItemView.findViewById(R.id.dateCommentTV) as TextView
        val contentTV = listItemView.findViewById(R.id.contentTV) as TextView

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
        commentorTV.setText(comment.AccountName)
        val dateCommentTV = holder.dateCommentTV
        dateCommentTV.setText(comment.CreatedAt.toString())
        val contentTV = holder.contentTV
        contentTV.setText(comment.Content)

    }



}