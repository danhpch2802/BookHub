package com.androidrealm.bookhub.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.androidrealm.bookhub.Models.Request
import com.androidrealm.bookhub.R
import java.io.Serializable

class RequestAdapter (private var listRequests : List<Request>
): RecyclerView.Adapter<RequestAdapter.ViewHolder>(), Serializable {
    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        val requestNameTV = listItemView.findViewById(R.id.requestNameTV) as TextView

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            RequestAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
// Inflate the custom layout
        val requestView = inflater.inflate(R.layout.item_request, parent, false)
// Return a new holder instance
        return ViewHolder(requestView)
    }
    override fun getItemCount(): Int {
        return listRequests.size
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
// Get the data model based on position
        val request: Request = listRequests.get(position)
// Set item views based on your views and data model
        val requestNameTW = holder.requestNameTV
        requestNameTW.setText(request.requestName)
    }
}