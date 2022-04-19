package com.androidrealm.bookhub.Controllers.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidrealm.bookhub.Adapter.CommentAdapter
import com.androidrealm.bookhub.Models.Account
import com.androidrealm.bookhub.Models.Comment
import com.androidrealm.bookhub.R
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.io.Serializable

class CommentFragment() : Fragment(), Serializable {
    private lateinit var commentRW:RecyclerView
    companion object {
        fun newInstance
                    (listComment: ArrayList<Comment>, userInfo: Account, bookId: String): CommentFragment
        {
            val fragment= CommentFragment()
            val bundle = Bundle()
            bundle.putSerializable("listComment", listComment)
            bundle.putSerializable("userInfo", userInfo)
            bundle.putSerializable("bookId", bookId)
            fragment.setArguments(bundle)
            return fragment
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        var view=inflater.inflate(R.layout.fragment_comment, container, false)
        commentRW=view.findViewById(R.id.commentRW)

        // set the custom adapter to the RecyclerView
        val currentUserAuth = FirebaseAuth.getInstance().currentUser

        val commentSection = view.findViewById<LinearLayout>(R.id.commentSection)

        var listComment=requireArguments().getSerializable(
            "listComment"
        ) as ArrayList<Comment>

        if (listComment.any { it.accountID == currentUserAuth!!.uid })
        {
            commentSection.visibility = View.GONE
        }

        commentRW.addItemDecoration(
            DividerItemDecoration(
                commentRW.getContext(),
                DividerItemDecoration.VERTICAL
            )
        )

        var adapter= CommentAdapter(listComment)
        commentRW.adapter=adapter

        commentRW.layoutManager = LinearLayoutManager(activity)

        val userInfo = requireArguments().getSerializable(
            "userInfo"
        ) as Account

        val currentBookId = requireArguments().getSerializable(
            "bookId"
        ) as String


        val commentEdt = view.findViewById<EditText>(R.id.commentBox)
        val sendBtn = view.findViewById<Button>(R.id.sendBtn)
        val ratingBar = view.findViewById<RatingBar>(R.id.ratingForComment)





        sendBtn.setOnClickListener {
            if (commentEdt.text.isEmpty())
            {
                Toast.makeText(requireActivity(),"Please input comment",Toast.LENGTH_SHORT)
                    .show()
            }
            else
            {
                val contentComment = commentEdt.text.toString()
                commentEdt.text.clear()

                userInfo.Point = userInfo.Point!! + 10

                updatePointAfterComment(userInfo, currentUserAuth!!.uid)

                commentSection.visibility = View.GONE

                val commentTemp = Comment()
                commentTemp.accountName = userInfo.username
                commentTemp.bookID = currentBookId
                commentTemp.content = contentComment
                commentTemp.createdAt = Timestamp.now().toDate()
                commentTemp.accountID = currentUserAuth!!.uid
                commentTemp.starRated = ratingBar.rating.toLong()

                addCommentInFireStore(commentTemp)
                addScoreInFireStoreComic(currentBookId, commentTemp.starRated!!)

                val myFragment = requireActivity().supportFragmentManager
                    .findFragmentById(R.id.fragment_book) as BookFragment

                myFragment.reFreshStar(commentTemp.starRated!!)

                listComment.add(0,commentTemp)
                adapter.notifyItemInserted(0)
            }
        }

        return view
    }

    private fun addScoreInFireStoreComic(currentBookId: String, index: Long) {
        val comicRef = FirebaseFirestore.getInstance().collection("comics")
            .document(currentBookId)
            .get()
            .addOnSuccessListener { doc->
                val scoreList = doc.data!!["score"] as ArrayList<Long>
                scoreList[index.toInt() - 1] = scoreList[index.toInt() - 1] + 1
                val updateComicRef = FirebaseFirestore.getInstance().collection("comics")
                    .document(currentBookId)
                    .update("score", scoreList)
            }
    }

    private fun updatePointAfterComment(userInfo: Account, idAccount : String) {
        val pointRef = FirebaseFirestore.getInstance().collection("accounts")
            .document(idAccount)
            .update("Point", userInfo.Point)
    }

    override fun onResume() {
        super.onResume()
        requireView().requestLayout()
    }

    private fun addCommentInFireStore(commentToAdd: Comment)
    {
        val commentRef = FirebaseFirestore.getInstance().collection("comments")
            .add(commentToAdd)
            .addOnFailureListener { exception ->
                Log.e("Comment error", exception.message.toString())
            }
    }

}