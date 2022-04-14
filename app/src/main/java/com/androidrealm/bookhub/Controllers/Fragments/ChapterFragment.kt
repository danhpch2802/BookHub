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
import com.androidrealm.bookhub.Models.Account
import com.androidrealm.bookhub.Models.Book
import com.androidrealm.bookhub.Models.Chapter
import com.androidrealm.bookhub.Models.DownloadBook
import com.androidrealm.bookhub.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ChapterFragment(listChapter: Any?, detailBook: Book) : Fragment() {
    companion object {
        fun newInstance
                    (listChapter: ArrayList<Chapter>, detailBook: Book, userInfo: Account,): ChapterFragment
        {
            val fragment= ChapterFragment(listChapter,detailBook)
            val bundle = Bundle()
            bundle.putSerializable("detailBook", detailBook)
            bundle.putSerializable("listChapter", listChapter)
            bundle.putSerializable("userInfo", userInfo)
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
    var pdfPath:String?=null
    var imagePath:String?=null
    var chapterClick:Chapter?=null
    var detailBook:Book?=null
    var progressBar:ProgressBar?=null


    override fun onResume() {
        super.onResume()
        requireView().requestLayout()
        progressBar=requireView().findViewById<ProgressBar>(R.id.progressbar)
        val chapterRW = requireView().findViewById<RecyclerView>(R.id.chapterRW)

        val userInfo = requireArguments().getSerializable(
            "userInfo"
        ) as Account

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
        val currentUserAuth = FirebaseAuth.getInstance().currentUser

        chapterRW.layoutManager = LinearLayoutManager(activity)
        adapter.onRowsChapterClick = { chapterClick ->
            userInfo.Point = userInfo.Point!! + 10
            detailBook.viewNumber=detailBook.viewNumber!!+1
            updateViewAfterChapterClick(detailBook, detailBook!!.id!!)
            updatePointAfterChapterClick(userInfo, currentUserAuth!!.uid)
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

                requestStoragePermissionLauncher.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

            }
        }

    }
    private fun updatePointAfterChapterClick(userInfo: Account, idAccount : String) {
        val pointRef = FirebaseFirestore.getInstance().collection("accounts")
            .document(idAccount)
            .update("Point", userInfo.Point)
    }

    private fun updateViewAfterChapterClick(bookInfo: Book, idBook : String) {
        val pointRef = FirebaseFirestore.getInstance().collection("comics")
            .document(idBook)
            .update("viewNumber", bookInfo.viewNumber)
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
            referenceCover.getBytes(50000000) //50mb
                .addOnSuccessListener { bytes->
                    imageSaveToLocal(bytes, detailBook!!.name.toString())
                    val referencePDF= FirebaseStorage.getInstance().getReferenceFromUrl((chapterClick!!.links.toString()))
                    var PDFName = chapterClick.name.toString()
                    referencePDF.getBytes(50000000)
                        .addOnSuccessListener { bytes->
                            pdfSaveToLocal(bytes,PDFName,detailBook!!.name.toString())
                            var newDownloadBook = DownloadBook(
                                imagePath=imagePath.toString(),  chapterName = chapterClick!!.name.toString(),
                                chapterPath = pdfPath.toString()
                                ,name= detailBook!!.name.toString(), summary = detailBook!!.summary.toString(),author= detailBook!!.author.toString())
                            db.downloadBookDAO().insertDownloadBook(newDownloadBook)
                        }
                }
        }
        else
        {
            Toast.makeText(requireContext(),"Chapter already downloaded",Toast.LENGTH_SHORT).show()
        }
    }

     fun imageSaveToLocal (bytes:ByteArray, coverName:String) {
        var removedCoverName=coverName.replace("\\s".toRegex(),"")
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())

        var imageName="${removedCoverName}${timeStamp}Cover.jpg"
        try {

            var imagedownload_folder =  File(requireContext().getExternalFilesDir("Cover").toString())

             imagedownload_folder.mkdirs()
            var filepath=imagedownload_folder.path+"/"+imageName

            val out=FileOutputStream(filepath)
            this.imagePath=String()
            this.imagePath+=filepath.toString()
            out.write(bytes)
            out.close()

        } catch (t: Throwable) {
            Log.i("testing",t.message.toString())
            Toast.makeText(requireContext(),t.message, Toast.LENGTH_LONG).show()
        }
    }

    fun pdfSaveToLocal (bytes:ByteArray,name:String,book_name:String) {
        var removedBookName=book_name.replace("\\s".toRegex(),"")
        var removedName=name.replace("\\s".toRegex(),"")
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        var pdfName="${removedBookName}${removedName}${timeStamp}.pdf"
        try {

            var pdfdownload_folder =  File(requireContext().getExternalFilesDir("PDF").toString())

            pdfdownload_folder.mkdirs()
            var filepath=pdfdownload_folder.path+"/"+pdfName

            val out=FileOutputStream(filepath)
            out.write(bytes)
            out.close()
            Toast.makeText(requireContext(),"Download Completed",Toast.LENGTH_LONG).show()
            progressBar!!.visibility=View.INVISIBLE
            this.pdfPath=String()
            this.pdfPath+=filepath.toString()
        } catch (t: Throwable) {
            Log.i("testing",t.message.toString())
            Toast.makeText(requireContext(),t.message, Toast.LENGTH_LONG).show()
        }
    }
}