package com.androidrealm.bookhub.Controllers.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidrealm.bookhub.Adapter.RequestAdapter
import com.androidrealm.bookhub.Controllers.Activities.ProfileActivity
import com.androidrealm.bookhub.Controllers.Activities.RequestActivity
import com.androidrealm.bookhub.Models.Request
import com.androidrealm.bookhub.R

class RequestFragment (listRequest: Any?) : Fragment() {
    private lateinit var requestRW:RecyclerView
    companion object {
        fun newInstance
                    (listRequest: ArrayList<Request>): RequestFragment
        {
            val fragment= RequestFragment(listRequest)
            val bundle = Bundle()
            bundle.putSerializable("listRequest", listRequest)
            fragment.setArguments(bundle)
            return fragment
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        var view=inflater.inflate(R.layout.fragment_request, container, false)
        requestRW=view.findViewById(R.id.requestRW)

        // set the custom adapter to the RecyclerView

        var listRequest=requireArguments().getSerializable(
            "listRequest"
        ) as ArrayList<Request>

        requestRW.addItemDecoration(
            DividerItemDecoration(
                requestRW.getContext(),
                DividerItemDecoration.VERTICAL
            )
        )

        var adapter= RequestAdapter(listRequest)
        requestRW.adapter=adapter

        requestRW.layoutManager = LinearLayoutManager(activity)

        return view
    }

}