package com.androidrealm.bookhub.Controllers.Fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidrealm.bookhub.Adapter.CreateChapterAdapter
import com.androidrealm.bookhub.Models.Chapter
import com.androidrealm.bookhub.R
import com.google.firebase.storage.FirebaseStorage

class UpdateChapterFragment(listChapter: Any?) : Fragment() {

    var currentContext : Context? = null
    var currentRow : Int = -1

    companion object {

        var listChapterToEdit = ArrayList<Chapter>()
        var pdfListUriToEdit  = ArrayList<Uri>()
        fun newInstance
                    (listChapter: ArrayList<Chapter>): UpdateChapterFragment
        {
            val fragment = UpdateChapterFragment(listChapter)
            val bundle = Bundle()
            bundle.putSerializable("listChapter", listChapter)
            fragment.setArguments(bundle)
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_update_chapter, container, false)

        currentContext = this.context

        return view
    }

    override fun onResume() {
        super.onResume()

        listChapterToEdit = requireArguments().getSerializable(
            "listChapter"
        ) as ArrayList<Chapter>

        for (i in 0..listChapterToEdit.size - 1)
            pdfListUriToEdit.add(Uri.parse("Currently Have PDF file"))

        val chapterRVedt = requireView().findViewById<RecyclerView>(R.id.UploadPDFRVEdtFragment)

        val chapterAdapter = CreateChapterAdapter(listChapterToEdit, pdfListUriToEdit)

        chapterRVedt.adapter = chapterAdapter
        chapterRVedt.layoutManager = LinearLayoutManager(currentContext)

        val addMoreChapter = requireView().findViewById<Button>(R.id.AddMoreChapterBtnEdtFragment)
        addMoreChapter.setOnClickListener {
            var newChap = Chapter()
            newChap.name = ""
            newChap.links = ""
            listChapterToEdit.add(newChap)
            var uri: Uri = Uri.EMPTY
            pdfListUriToEdit.add(uri)
            chapterAdapter.notifyItemChanged(listChapterToEdit.size)
        }

        chapterAdapter.onChoosePDFClick = {rowsNum->
            val intent = Intent()
            currentRow = rowsNum
            intent.setType("application/pdf")
            intent.action = Intent.ACTION_OPEN_DOCUMENT
            startActivityForResult(Intent.createChooser(intent, "Select PDF"),1234)
            chapterAdapter.notifyItemChanged(currentRow)
        }

        chapterAdapter.onDeleteClick = {rowsNum ->
            // delete pdf if already in storage
            if (!listChapterToEdit[rowsNum].links.equals(""))
            {
                delPdfInEdit(listChapterToEdit[rowsNum].links)
            }
            pdfListUriToEdit.removeAt(rowsNum)
            listChapterToEdit.removeAt(rowsNum)
            chapterAdapter.notifyItemRemoved(rowsNum)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1234 && resultCode == AppCompatActivity.RESULT_OK && data != null && data.data != null)
        {
            val tempUriPdf = data.data
            pdfListUriToEdit.set(currentRow, tempUriPdf!!)
        }
        else
        {

        }
    }

    private fun delPdfInEdit(UrlPdf : String?)
    {
        val delRef = FirebaseStorage.getInstance().getReferenceFromUrl(UrlPdf!!)
        delRef.delete()
    }

}

