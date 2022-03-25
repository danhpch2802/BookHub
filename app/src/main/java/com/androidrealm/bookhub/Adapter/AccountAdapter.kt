
package com.androidrealm.bookhub.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.androidrealm.bookhub.Models.Account
import com.androidrealm.bookhub.R
import java.io.Serializable

class AccountAdapter (private var listAccounts : List<Account>
): RecyclerView.Adapter<AccountAdapter.ViewHolder>(), Serializable {
    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        val accountNameTV = listItemView.findViewById(R.id.accountNameTV) as TextView

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            AccountAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
// Inflate the custom layout
        val accountView = inflater.inflate(R.layout.item_account, parent, false)
// Return a new holder instance
        return ViewHolder(accountView)
    }
    override fun getItemCount(): Int {
        return listAccounts.size
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
// Get the data model based on position
        val account: Account = listAccounts.get(position)
// Set item views based on your views and data model
        val accountNameTW = holder.accountNameTV
        accountNameTW.setText(account.accountName)
    }
}