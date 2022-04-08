package com.androidrealm.bookhub.Controllers.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
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

        var listComment=requireArguments().getSerializable(
            "listComment"
        ) as ArrayList<Comment>

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
        val currentUserAuth = FirebaseAuth.getInstance().currentUser

        sendBtn.setOnClickListener {
            if (commentEdt.text.equals(""))
            {
                Toast.makeText(requireActivity(),"Please input comment",Toast.LENGTH_SHORT)
                    .show()
            }
            else
            {
                val contentComment = commentEdt.text.toString()
                commentEdt.text.clear()

                val commentTemp = Comment()
                commentTemp.accountName = userInfo.username
                commentTemp.bookID = currentBookId
                commentTemp.content = contentComment
                commentTemp.createdAt = Timestamp.now().toDate()
                commentTemp.accountID = currentUserAuth!!.uid
                addCommentInFireStore(commentTemp)
                listComment.add(0,commentTemp)
                adapter.notifyItemInserted(0)
            }
        }

        return view
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