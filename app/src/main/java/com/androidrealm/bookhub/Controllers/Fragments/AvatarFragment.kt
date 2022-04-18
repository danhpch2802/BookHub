package com.androidrealm.bookhub.Controllers.Fragments

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidrealm.bookhub.Adapter.AvatarAdapter
import com.androidrealm.bookhub.Models.Book
import com.androidrealm.bookhub.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

class AvatarFragment : Fragment() {
    var flag = 0
    private lateinit var listAvaRW: RecyclerView
    companion object {
        fun newInstance
                    (gridLayoutSpanNum:Int): AvatarFragment
        {
            val fragment= AvatarFragment()
            val bundle = Bundle()
            bundle.putSerializable("gridLayoutSpanNum", gridLayoutSpanNum)//-1 means Linear Layout
            fragment.setArguments(bundle)
            return fragment
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view=inflater.inflate(R.layout.fragment_avatar, container, false)
        return view
    }

    override fun onResume() {
        super.onResume()
        listAvaRW= requireView().findViewById(R.id.avaRW)
        // set the custom adapter to the RecyclerView

        var layout=requireArguments().getSerializable(
            "gridLayoutSpanNum"
        ) as Int

        var fireStore = FirebaseFirestore.getInstance()

        val listOfAva = ArrayList<String>();

        for (i in 1..9 ){
            listOfAva.add(i.toString())
        }
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        var adapter = AvatarAdapter(listOfAva)

        adapter.onItemClick = { book ->
            val db = FirebaseFirestore.getInstance()
            db.collection("accounts").document(uid)
                .get().addOnSuccessListener { task ->
                    if (book == "9")
                    {
                        flag = 1
                        var badge: ArrayList<String> = task.get("Badge") as ArrayList<String>
                        for (i in badge)
                        {
                            if (i == "b1" )
                            {
                                flag = 0
                            }
                            else
                            {
                                Toast.makeText(context, "You must have the Wibu Badge to change to avatar " + book, Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    }
                    if (flag == 0) {
                        db.collection("accounts").document(uid).update("Avatar", book)
                        Toast.makeText(context, "Changed to avatar " + book, Toast.LENGTH_SHORT)
                            .show()
                    }
                    flag = 0
                }
                .addOnFailureListener { exception ->
                    Log.d(ContentValues.TAG, "Error getting documents: ", exception)
                }

        }
        listAvaRW.adapter=adapter

        if(layout>0){
            listAvaRW.layoutManager = GridLayoutManager(activity,layout)
        }
        else
        {
            if(layout==-1) listAvaRW.layoutManager = LinearLayoutManager(activity,
                LinearLayoutManager.HORIZONTAL,false)
            else listAvaRW.layoutManager = LinearLayoutManager(activity,
                LinearLayoutManager.VERTICAL,false)
        }
    }
    fun onError(e: Exception?) {
        if (e != null) {
            Log.d("RewardError", "onError: " + e.message)
        }
    }

}