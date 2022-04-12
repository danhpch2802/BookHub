package com.androidrealm.bookhub.Controllers.Fragments

import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidrealm.bookhub.Adapter.ChapterAdapter
import com.androidrealm.bookhub.Controllers.Activities.BookReadActivity
import com.androidrealm.bookhub.Controllers.DAO.DownloadBookDatabase
import com.androidrealm.bookhub.Models.Book
import com.androidrealm.bookhub.Models.Chapter
import com.androidrealm.bookhub.Models.DownloadBook
import com.androidrealm.bookhub.R
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ChapterFragment(listChapter: Any?, detailBook: Book) : Fragment() {
    companion object {
        fun newInstance
                    (listChapter: ArrayList<Chapter>,detailBook: Book): ChapterFragment
        {
            val fragment= ChapterFragment(listChapter,detailBook)
            val bundle = Bundle()
            bundle.putSerializable("detailBook", detailBook)
            bundle.putSerializable("listChapter", listChapter)
            fragment.setArguments(bundle)
            return fragment
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        var view=inflater.inflate(R.layout.fragment_chapter, container, false)

        return view
    }
    var chapterClick:Chapter?=null
    var detailBook:Book?=null
    var progressBar:ProgressBar?=null
    override fun onResume() {
        super.onResume()
        requireView().requestLayout()
        progressBar=requireView().findViewById<ProgressBar>(R.id.progressbar)
        val chapterRW = requireView().findViewById<RecyclerView>(R.id.chapterRW)

        // set the custom adapter to the RecyclerView

        var listChapter=requireArguments().getSerializable(
            "listChapter"
        ) as ArrayList<Chapter>

        var detailBook=requireArguments().getSerializable(
            "detailBook"
        ) as Book

        chapterRW.addItemDecoration(
            DividerItemDecoration(
                chapterRW.getContext(),
                DividerItemDecoration.VERTICAL
            )
        )

        var adapter=ChapterAdapter(listChapter)
        chapterRW.adapter=adapter

        chapterRW.layoutManager = LinearLayoutManager(activity)
        adapter.onRowsChapterClick = { chapterClick ->

            val intent= Intent(requireActivity(), BookReadActivity::class.java)
            intent.putExtra("ChapterPos",adapter.pos)
            intent.putExtra("id",detailBook.id)
            startActivity(intent)
        }
        adapter.onRowsChapterDownloadClick={chapterClick ->
            if(ContextCompat.checkSelfPermission(requireContext(),android.Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED){
                Log.d("External","Permission already granted!")
                downloadChapter(chapterClick,detailBook)
            }
            else{
                Log.d("External","Permission not granted!")
                this.chapterClick=chapterClick
                this.detailBook=detailBook
                requestStoragePermissionLauncher.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

            }
        }

    }

    private val requestStoragePermissionLauncher= registerForActivityResult(ActivityResultContracts.RequestPermission()){
        isGranted:Boolean ->
        if(isGranted)
        {
            Log.d("External","Permission is granted!")
            downloadChapter(chapterClick!!,detailBook)
        }
        else
        {
            Log.d("External","Permission not granted!")
            Toast.makeText(requireContext(),"Permission denied",Toast.LENGTH_SHORT)
        }
    }

    fun downloadChapter(chapterClick:Chapter,detailBook: Book){
        var db = DownloadBookDatabase.getInstance(requireContext())
        var existDownloadedBook = db.downloadBookDAO().findByNameAndChapter(detailBook.name.toString(),
            chapterClick.name.toString()
        )
        if(existDownloadedBook==null){
            progressBar!!.visibility=View.VISIBLE
            val referenceCover= FirebaseStorage.getInstance().getReferenceFromUrl((detailBook!!.imagePath.toString()))
            var coverName=referenceCover.getName().split(".")[0]
            var imagePath=String()
            referenceCover.getBytes(50000000) //50mb
                .addOnSuccessListener { bytes->
                    imagePath = imageSaveToLocal(bytes, detailBook!!.name.toString())
                }
            Log.d("testing",imagePath)

            val referencePDF= FirebaseStorage.getInstance().getReferenceFromUrl((chapterClick!!.links.toString()))
            var PDFName = chapterClick.name.toString()
            var pdfPath=String()
            referencePDF.getBytes(50000000)
                .addOnSuccessListener { bytes->
                    pdfPath=pdfSaveToLocal(bytes,PDFName,detailBook!!.name.toString())
                }

            var newDownloadBook = DownloadBook(imagePath=imagePath,  chapterName = chapterClick!!.name.toString(),
                chapterPath = pdfPath
                ,name= detailBook!!.name.toString(), summary = detailBook!!.summary.toString(),author= detailBook!!.author.toString())
            db.downloadBookDAO().insertDownloadBook(newDownloadBook)
        }
        else
        {
            Toast.makeText(requireContext(),"Chapter already downloaded",Toast.LENGTH_SHORT).show()
        }
    }

    private fun imageSaveToLocal (bytes:ByteArray, coverName:String) :String{
        var removedCoverName=coverName.replace("\\s".toRegex(),"")
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())

        var imageName="${removedCoverName}${timeStamp}Cover.jpg"
        var path=""
        try {

            var imagedownload_folder =  File(requireContext().getExternalFilesDir("Cover").toString())

             imagedownload_folder.mkdirs()
            var filepath=imagedownload_folder.path+"/"+imageName

            val out=FileOutputStream(filepath)
            out.write(bytes)
            out.close()
            path=filepath
            Log.i("testing",filepath.toString())

        } catch (t: Throwable) {
            Log.i("testing",t.message.toString())
            Toast.makeText(requireContext(),t.message, Toast.LENGTH_LONG).show()
        }
        return path
    }

    private fun pdfSaveToLocal (bytes:ByteArray,name:String,book_name:String) :String{
        var removedBookName=book_name.replace("\\s".toRegex(),"")
        var removedName=name.replace("\\s".toRegex(),"")
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        var pdfName="${removedBookName}${removedName}${timeStamp}.pdf"
        var path=""
        try {

            var pdfdownload_folder =  File(requireContext().getExternalFilesDir("PDF").toString())

            pdfdownload_folder.mkdirs()
            var filepath=pdfdownload_folder.path+"/"+pdfName

            val out=FileOutputStream(filepath)
            out.write(bytes)
            out.close()
            path=filepath
            Log.i("testing",path)
            Toast.makeText(requireContext(),"Download Completed",Toast.LENGTH_LONG).show()
            progressBar!!.visibility=View.INVISIBLE

        } catch (t: Throwable) {
            Log.i("testing",t.message.toString())
            Toast.makeText(requireContext(),t.message, Toast.LENGTH_LONG).show()
        }
        return path
    }
}