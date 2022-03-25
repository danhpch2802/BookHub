package com.androidrealm.bookhub.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidrealm.bookhub.Adapter.AccountAdapter
import com.androidrealm.bookhub.Adapter.RequestAdapter
import com.androidrealm.bookhub.Models.Account
import com.androidrealm.bookhub.Models.Request
import com.androidrealm.bookhub.R

class AccountFragment(listAccount: Any?) : Fragment() {
    private lateinit var accountRW: RecyclerView
    companion object {
        fun newInstance
                    (listAccount: ArrayList<Account>): AccountFragment
        {
            val fragment= AccountFragment(listAccount)
            val bundle = Bundle()
            bundle.putSerializable("listAccount", listAccount)
            fragment.setArguments(bundle)
            return fragment
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        var view=inflater.inflate(R.layout.fragment_account, container, false)
        accountRW=view.findViewById(R.id.accountRW)

        var listAccount=requireArguments().getSerializable(
            "listAccount"
        ) as ArrayList<Account>

        accountRW.addItemDecoration(
            DividerItemDecoration(
                accountRW.getContext(),
                DividerItemDecoration.VERTICAL
            )
        )

        var adapter= AccountAdapter(listAccount)
        accountRW.adapter=adapter

        accountRW.layoutManager = LinearLayoutManager(activity)

        return view
    }

}