package com.androidrealm.bookhub.Adapter

import android.content.ContentValues
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.androidrealm.bookhub.R
import com.google.firebase.firestore.FirebaseFirestore


class QuizAdapter(private val listQuiz: ArrayList<String>) : RecyclerView.Adapter<QuizAdapter.MyViewHolder>() {
    private lateinit var mListener: onItemClickListener

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_quiz, parent, false)
        return MyViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val quiz: String = listQuiz[position]
        var id: String? = ""
        holder.quizDetail.text = quiz
//        val db = FirebaseFirestore.getInstance()
//        db.collection("prizes").whereEqualTo("prizeName", prize)
//            .get().addOnSuccessListener { documents ->
//                for (document in documents) {
//                    id = document["id"] as String
//                    holder.prizDes.text = document["prizeDescrypt"] as String
//                }
//            }
//            .addOnFailureListener { exception ->
//                Log.w(ContentValues.TAG, "Error getting documents: ", exception)
//            }
    }

    override fun getItemCount(): Int {
        return listQuiz.size
    }

    class MyViewHolder(itemView: View, listener: onItemClickListener) :
        RecyclerView.ViewHolder(itemView) {
        val quizDetail: TextView = itemView.findViewById(R.id.quizNameTV)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }
}
