package com.androidrealm.bookhub.Controllers.Activities

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.androidrealm.bookhub.Models.Book
import com.androidrealm.bookhub.Models.Bookmark
import com.androidrealm.bookhub.Models.Chapter
import com.androidrealm.bookhub.R
import com.github.barteksc.pdfviewer.PDFView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.firestore.model.mutation.Precondition.exists
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_book_read.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class BookReadActivity : AppCompatActivity()  {
    var fireStore:FirebaseFirestore?=null
    var pdfViewer:PDFView?=null
    var pageTV:TextView?=null
    var chapterName:TextView?=null
    var topBar:RelativeLayout?=null
    var topBarVisible=true
    var chapterSpinner:Spinner?=null
    var nextChapterBtn:Button?=null
    var currentChap=0
    var bookmarkBtn:ImageView?=null
    var bookmarked=false
    var book:Book?=null

    var bookmarkID:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_read)

        val intent = intent
        val id = intent.getStringExtra("id")
        val ChapterPos = intent.getIntExtra("ChapterPos",0)

        Log.i("chapterTest",ChapterPos.toString())
        pdfViewer = findViewById<PDFView>(R.id.bookPDF)
        pageTV=findViewById<TextView>(R.id.pagenumber)
        chapterName=findViewById(R.id.detail_book_barTV)
        chapterSpinner=findViewById(R.id.chapterSpinner)
        topBar=findViewById(R.id.toolBar)
        nextChapterBtn=findViewById(R.id.nextChapterBtn)
        bookmarkBtn=findViewById(R.id.bookmarkBtn)
        fireStore = FirebaseFirestore.getInstance()

        val docRef = fireStore!!.collection("comics").document(id.toString())
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    book = document.toObject<Book>()
                    currentChap=ChapterPos
                    chapterName!!.setText(book?.listChapter?.get(currentChap)!!.name)

                    setBookmark()
                    bookmarkBtn!!.setOnClickListener{
                        val fireStore = FirebaseFirestore.getInstance()
                        val currentUserAuth = FirebaseAuth.getInstance().currentUser
                        if(!bookmarked){
                            val bookmark=Bookmark(accountID=currentUserAuth!!.uid,bookID = book!!.id,bookName = book!!.name, coverUrl = book!!.imagePath
                            ,chapterUrl = currentChap,chapterName = book!!.listChapter!!.get(currentChap)!!.name,

                            )
                            bookmarkBtn!!.isClickable=false
                            fireStore!!.collection("bookmarks")
                                .add(bookmark)
                                .addOnCompleteListener{documentReference->
                                    Toast.makeText(this,"Chapter has been bookmarked",Toast.LENGTH_LONG).show()
                                    bookmarkBtn!!.isClickable=true
                                    bookmarked=true
                                    bookmarkBtn!!.isSelected=true
                                    bookmarkBtn!!.setImageResource(R.drawable.bookmarkon)
                                    bookmarkID=documentReference.result.id
                                }

                        }
                        else{
                            bookmarkBtn!!.isClickable=false

                            val docRef = fireStore
                                ?.collection("bookmarks")
                                ?.document(bookmarkID!!)
                                ?.delete()
                                .addOnCompleteListener{
                                    Toast.makeText(this,"Chapter has been unbookmark",Toast.LENGTH_LONG).show()
                                    bookmarkBtn!!.isClickable=true
                                    bookmarked=false
                                    bookmarkBtn!!.isSelected=false
                                    bookmarkBtn!!.setImageResource(R.drawable.bookmarkoff)
                                }

                        }
                    }


                    loadSpinner(book!!.listChapter!!)
                    chapterSpinner!!.setSelection(currentChap,false)
                    chapterSpinner!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(p0: AdapterView<*>?) {
                                // TODO: do nothing ??? needed by the interface
                        }
                        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                            currentChap=p2
                            chapterName!!.setText(book?.listChapter?.get(currentChap)!!.name)
                            findViewById<ProgressBar>(R.id.progressBar).visibility= View.VISIBLE
                            pdfViewer!!.recycle()
                            loadPdfFromUri(book!!.listChapter?.get(currentChap)!!.links)
                        }
                    }
                    nextChapterBtn!!.setOnClickListener{
                        currentChap++
                        chapterName!!.setText(book?.listChapter?.get(currentChap)!!.name)
                        findViewById<ProgressBar>(R.id.progressBar).visibility= View.VISIBLE
                        pdfViewer!!.recycle()
                        loadPdfFromUri(book!!.listChapter?.get(currentChap)!!.links)
                    }
                    loadPdfFromUri(book!!.listChapter?.get(ChapterPos)!!.links)
                } else {
                    finish()
                }
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "get failed with ", exception)
            }


        val backBtn=findViewById<ImageView>(R.id.backBtn)
        backBtn.setOnClickListener {
            finish()
        }
    }
    fun loadSpinner(listChapter:ArrayList<Chapter>){
        val listChapterToString = ArrayList<String>()
        for(chapter in listChapter) {
            listChapterToString.add(chapter.name.toString())
        }
        ArrayAdapter(this, android.R.layout.simple_spinner_item, listChapterToString)
            .also { adapter ->
// Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(
                    android.R.layout.simple_spinner_dropdown_item)
// Apply the adapter to the spinner
                chapterSpinner!!.adapter = adapter
            }
    }
    fun loadPdfFromUri (pdfUri: String?){
        nextChapterBtn!!.visibility=View.INVISIBLE
        val reference= FirebaseStorage.getInstance().getReferenceFromUrl(pdfUri.toString())
        reference.getBytes(50000000) //Max PDF Bytes
            .addOnSuccessListener { bytes->
                pdfViewer?.fromBytes(bytes)
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
                        if(currentPage==pageCount)
                        {
                            if(currentChap + 1 < book!!.listChapter!!.size) nextChapterBtn!!.visibility=View.VISIBLE
                        }
                        else
                        {
                            nextChapterBtn!!.visibility=View.GONE
                        }
                    }
                    ?.onTap {it->
                        topBarAnimation()
                        true
                    }
                    ?.load()
                findViewById<ProgressBar>(R.id.progressBar).visibility= View.GONE

        }

            .addOnFailureListener{e->
                Log.d(ContentValues.TAG, "get failed with ", e)
            }
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

    private fun setBookmark(){

        val currentUserAuth = FirebaseAuth.getInstance().currentUser
        val docRef = fireStore
            ?.collection("bookmarks")
            ?.whereEqualTo("accountID", currentUserAuth!!.uid)
            ?.whereEqualTo("bookID", book!!.id)
            ?.whereEqualTo("chapterUrl",currentChap)

        docRef!!.get()
            .addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    if (task.result.size()>0) {
                        var document=task.result.documents[0].toObject<Bookmark>()
                        bookmarked = true
                        bookmarkBtn!!.isSelected = true
                        bookmarkBtn!!.setImageResource(R.drawable.bookmarkon)
                        bookmarkID= task.result.documents[0].id
                    } else {
                        Log.i("testDoc", "Error")

                        bookmarked = false
                        bookmarkBtn!!.isSelected = false
                        bookmarkBtn!!.setImageResource(R.drawable.bookmarkoff)

                    }
                }
            }
            .addOnFailureListener { exception ->
                bookmarked=false
            }
    }

}