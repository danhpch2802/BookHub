package com.androidrealm.bookhub.Controllers.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidrealm.bookhub.Adapter.CommentAdapter
import com.androidrealm.bookhub.Models.Comment
import com.androidrealm.bookhub.R

class CommentFragment() : Fragment() {
    private lateinit var commentRW:RecyclerView
    companion object {
        fun newInstance
                    (listComment: ArrayList<Comment>): CommentFragment
        {
            val fragment= CommentFragment()
            val bundle = Bundle()
            bundle.putSerializable("listComment", listComment)
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

        return view
    }

}