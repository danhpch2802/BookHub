package com.androidrealm.bookhub.Controllers.Activities

import android.content.ContentValues
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.androidrealm.bookhub.R
import com.github.barteksc.pdfviewer.PDFView
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_book_read.*
import java.io.File

class OfflineBookReadActivity : AppCompatActivity()  {
    var pdfViewer: PDFView?=null
    var pageTV: TextView?=null
    var chapterName: TextView?=null
    var topBar: RelativeLayout?=null
    var topBarVisible=true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_book_read)

        val intent = intent
        val name  = intent.getStringExtra("chapterName")
        val link = intent.getStringExtra("chapterLink")
        pdfViewer = findViewById<PDFView>(R.id.bookPDF)
        pageTV=findViewById<TextView>(R.id.pagenumber)
        chapterName=findViewById(R.id.detail_book_barTV)
        topBar=findViewById(R.id.toolBar)
        loadPdfFromStrorage(link)
        val backBtn=findViewById<ImageView>(R.id.backBtn)
        backBtn.setOnClickListener {
            onBackPressed()
        }
    }

    fun loadPdfFromStrorage (pdfUri: String?){
        nextChapterBtn!!.visibility= View.INVISIBLE
        pdfViewer?.fromFile(File(pdfUri))
                    ?.enableSwipe(false)
                    ?.swipeHorizontal(true)
                    ?.onError{t->
                        Log.d("PDFError",t.message.toString())
                    }
                    ?.onPageError { page, t ->
                        Log.d("PDFPageError",t.message.toString())
                    }
                    ?.defaultPage(0)
                    ?.pageSnap(true)
                    ?.pageFling(true)
                    ?.fitEachPage(true)
                    ?.enableSwipe(true)
                    ?.enableAntialiasing(true)
                    ?.autoSpacing(true)
                    ?.onPageChange { page, pageCount ->
                        val currentPage=page+1
                        pageTV!!.setText("$currentPage/$pageCount")

                    }
                    ?.onTap {it->
                        topBarAnimation()
                        true
                    }
                    ?.load()
        findViewById<ProgressBar>(R.id.progressBar).visibility= View.GONE
    }

    private fun topBarAnimation(): Unit {
        if(topBarVisible) {
            toolBar.animate()
                .translationYBy(-160F)
                .translationY(0F)
                .setDuration(getResources().getInteger(android.R.integer.config_mediumAnimTime)
                    .toLong());
            topBarVisible=false
        }
        else {
            toolBar.animate()
                .translationYBy(0F)
                .translationY(-160F)
                .setDuration(getResources().getInteger(android.R.integer.config_mediumAnimTime)
                    .toLong());
            topBarVisible=true
        }
    }
}