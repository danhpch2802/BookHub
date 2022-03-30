package com.androidrealm.bookhub.Adapter

import android.net.Uri
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.androidrealm.bookhub.Models.Chapter
import com.androidrealm.bookhub.R

class CreateChapterAdapter(private var RowsList: List<Chapter>,
                           private var pdfListUriName : ArrayList<Uri>) : RecyclerView.Adapter<CreateChapterAdapter.ViewHolder>() {

    var onDeleteClick: ((Int) -> Unit)? = null
    var onChoosePDFClick : ((Int) -> Unit)? = null

    inner class ViewHolder(ListItemViews: View) : RecyclerView.ViewHolder(ListItemViews)
    {
        val nameChap = ListItemViews.findViewById<EditText>(R.id.nameCHapEdt)
        val pdfChap = ListItemViews.findViewById<TextView>(R.id.UrlPDF)
        val deleteRow = ListItemViews.findViewById<ImageView>(R.id.DeleteRows)
        val uploadIC = ListItemViews.findViewById<ImageView>(R.id.selectPDFIC)

        init {

            nameChap.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(p0: Editable?) {
                    RowsList[adapterPosition].Name = p0.toString()
                }

            })

            deleteRow.setOnClickListener {
                onDeleteClick?.invoke(adapterPosition)
            }

            uploadIC.setOnClickListener{
                onChoosePDFClick?.invoke(adapterPosition)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreateChapterAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val itemView =inflater.inflate(R.layout.chapter_item_create, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CreateChapterAdapter.ViewHolder, position: Int) {
        val chapter: Chapter = RowsList.get(position)
        val currentPDF = pdfListUriName.get(position)

        val nameEditText = holder.nameChap

        nameEditText.text = Editable.Factory.getInstance().newEditable(chapter.Name)

        val pdfChapEdit = holder.pdfChap
        pdfChapEdit.text = currentPDF.toString()
    }

    override fun getItemCount(): Int {
        return RowsList.size
    }
}