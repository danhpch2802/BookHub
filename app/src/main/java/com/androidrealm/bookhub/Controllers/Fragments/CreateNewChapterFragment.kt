package com.androidrealm.bookhub.Controllers.Fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidrealm.bookhub.Adapter.CreateChapterAdapter
import com.androidrealm.bookhub.Models.Chapter
import com.androidrealm.bookhub.R

class CreateNewChapterFragment : Fragment() {

    var currentContext : Context? = null
    var currentRow : Int = -1

    companion object {
        var pdfListURIToCreate  = ArrayList<Uri>()
        var listChapterToCreate = ArrayList<Chapter>()
        fun newInstance(): CreateNewChapterFragment
        {
            return CreateNewChapterFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view =  inflater.inflate(R.layout.fragment_create_new_chapter, container, false)
        currentContext = this.context

        return view
    }

    override fun onResume() {
        super.onResume()
        val chapterRV = view?.findViewById<RecyclerView>(R.id.UploadPDFRV)

        var adapter = CreateChapterAdapter(listChapterToCreate, pdfListURIToCreate)
        chapterRV!!.addItemDecoration(
            DividerItemDecoration(currentContext,
                DividerItemDecoration.VERTICAL)
        )
        chapterRV.adapter = adapter
        chapterRV.layoutManager = LinearLayoutManager(currentContext)

        val addMoreChapter = requireView().findViewById<Button>(R.id.AddMoreChapterBtn)
        addMoreChapter.setOnClickListener {
            var newChap = Chapter()
            newChap.name = ""
            newChap.links = ""
            listChapterToCreate.add(newChap)
            var uri: Uri = Uri.EMPTY
            pdfListURIToCreate.add(uri)
            adapter.notifyItemChanged(listChapterToCreate.size)
        }

        adapter.onChoosePDFClick = {rowsNum->
            val intent = Intent()
            currentRow = rowsNum
            intent.setType("application/pdf")
            intent.action = Intent.ACTION_OPEN_DOCUMENT
            startActivityForResult(Intent.createChooser(intent, "Select PDF"),1234)
            adapter.notifyItemChanged(currentRow)
        }

        adapter.onDeleteClick = {rowsNum ->
            pdfListURIToCreate.removeAt(rowsNum)
            listChapterToCreate.removeAt(rowsNum)
            adapter.notifyItemRemoved(rowsNum)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1234 && resultCode == AppCompatActivity.RESULT_OK && data != null && data.data != null)
        {
            val tempUriPdf = data.data
            pdfListURIToCreate.set(currentRow, tempUriPdf!!)
        }
        else
        {

        }
    }

}