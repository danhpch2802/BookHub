package com.androidrealm.bookhub.Controllers.Fragments

import android.app.Activity.RESULT_OK
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.viewpager2.widget.ViewPager2
import com.androidrealm.bookhub.Adapter.BookPageView2Adapter
import com.androidrealm.bookhub.ComicAdapter
import com.androidrealm.bookhub.Controllers.Fragments.CreateNewChapterFragment.Companion.listChapterToAdd
import com.androidrealm.bookhub.Controllers.Fragments.CreateNewChapterFragment.Companion.pdfList
import com.androidrealm.bookhub.Models.Book
import com.androidrealm.bookhub.Models.Chapter
import com.androidrealm.bookhub.Models.Comment
import com.androidrealm.bookhub.R
import com.google.android.gms.tasks.Task
import com.google.android.material.button.MaterialButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dmax.dialog.SpotsDialog
import java.util.*
import kotlin.collections.ArrayList


/**
 * A simple [Fragment] subclass.
 * Use the [BookFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BookFragment : Fragment() {
    private var detailComic=Book()
    private var listComment=ArrayList<Comment>()
    private var recommendList=ArrayList<Book>()
    private var createNew = false
    private var comicCoverIV : ImageView? = null

    private var imageUri : Uri? = null

    private var listCategoryChosen = ArrayList<String>()
    lateinit var alertDialog :android.app.AlertDialog

    private var bookId : String? = null

    private var fireStore : FirebaseFirestore? = null

    companion object {

        fun newInstance
                    (createNew:Boolean?=false, book: Book?=null, recommendList:ArrayList<Book>?=null, listComment:ArrayList<Comment>?=null, editable:Boolean?=null): BookFragment
        {
            val fragment= BookFragment()
            val bundle = Bundle()
            if(!createNew!!) {
                bundle.putSerializable("comic", book)
                bundle.putSerializable("recommendedList", recommendList)
                bundle.putSerializable("listComment", listComment)
                bundle.putSerializable("editable", editable)
            }
            bundle.putSerializable("createNew", createNew)
            fragment.setArguments(bundle)

            return fragment
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fireStore = FirebaseFirestore.getInstance()

        alertDialog = SpotsDialog.Builder().setContext(activity)
            .setCancelable(false)
            .setMessage("Uploading")
            .build()


        var view=inflater.inflate(R.layout.fragment_book, container, false)
        comicCoverIV = view.findViewById<ImageView>(R.id.comicCoverIV)
         val comicNameET = view.findViewById<EditText>(R.id.comicNameET)
        val comicAuthorET = view.findViewById<EditText>(R.id.comicAuthorET)
        val comicRatingTV = view.findViewById<EditText>(R.id.comicRatingTV)
        val comicSummaryET = view.findViewById<EditText>(R.id.comicSummaryET)

        val categoryContent = view.findViewById<LinearLayout>(R.id.categoriesID)
        val uploadBtn = view.findViewById<Button>(R.id.uploadComic)
        var categoryView = arrayListOf<View>()

        createNew=requireArguments().getSerializable(
            "createNew"
        ) as Boolean

        if(createNew)
        {
            val singleFrame: View = layoutInflater.inflate(R.layout.item_categories, null)
            val categoryBtn = singleFrame.findViewById<MaterialButton>(R.id.categoryBtn)

            categoryBtn.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(),R.color.app_golden))

            uploadBtn.visibility = View.VISIBLE

            comicCoverIV!!.setOnClickListener {
                val intentPickIMG = Intent(Intent.ACTION_PICK)
                intentPickIMG.type = "image/*"
                startActivityForResult(intentPickIMG, 1000)
            }
            categoryBtn.setText("Add new category")
            categoryContent.addView(singleFrame)
            categoryView.add(singleFrame)
            view.findViewById<ImageView>(R.id.recommendCoverIV).setVisibility(View.VISIBLE)

            categoryBtn.setOnClickListener {

                val listCategoryToChoose = resources.getStringArray(R.array.category_names)

                val checkedCategory = ArrayList<Int>(listCategoryToChoose.size)

                val mBuilder = AlertDialog.Builder(requireContext())

                mBuilder.setTitle("Select Category to create")


                mBuilder.setMultiChoiceItems(listCategoryToChoose, null,
                    DialogInterface.OnMultiChoiceClickListener { dialogInterface, i, b ->
                        if (b)
                        {
                            checkedCategory.add(i)
                        }
                        else if (checkedCategory.contains(i))
                        {
                            checkedCategory.remove(i)
                        }
                })
                mBuilder.setPositiveButton("OK",
                    DialogInterface.OnClickListener { dialogInterface, i ->
                        var str : String= ""
                        for (i in checkedCategory)
                            str+= listCategoryToChoose[i] + ","
                        str = str.substring(0, str.length - 1)

                        categoryContent.removeViews(1, categoryContent.childCount - 1)

                        listCategoryChosen.clear()
                        listCategoryChosen = str.split(",") as ArrayList<String>


                        for (category in listCategoryChosen) {
                            val singleFrame: View =
                                layoutInflater.inflate(R.layout.item_categories, null)
                            singleFrame.id = category.indexOf(category)
                            val categoryBtn =
                                singleFrame.findViewById<MaterialButton>(R.id.categoryBtn)
                            categoryBtn.setText(category as CharSequence)
                            categoryContent.addView(singleFrame)
                            categoryView.add(singleFrame)
                        }
                }).show()
                }

            uploadBtn.setOnClickListener {
                detailComic.name = comicNameET.text.toString()
                detailComic.author = comicAuthorET.text.toString()
                detailComic.score = 0.0

                detailComic.summary = comicSummaryET.text.toString()

                detailComic.listCategory = listCategoryChosen

                detailComic.listChapter = listChapterToAdd

                alertDialog.show()
                //create Book in firestore to get ID
                fireStore!!.collection("comics")
                    .add(detailComic)
                    .addOnSuccessListener { docRef ->
                        bookId = docRef.id
                        startUploadingImage()
                        startUploadingPDF()
                    }

            }


        }
        if(!createNew) {
            val editable = requireArguments().getSerializable(
                "editable"
            ) as Boolean

            detailComic = requireArguments().getSerializable(
                "comic"
            ) as Book

            for (category in detailComic.listCategory!!) {
                val singleFrame: View = layoutInflater.inflate(R.layout.item_categories, null)
                singleFrame.id = category.indexOf(category)
                val categoryBtn = singleFrame.findViewById<MaterialButton>(R.id.categoryBtn)
                categoryBtn.setText(category as CharSequence)
                categoryContent.addView(singleFrame)
                categoryView.add(singleFrame)
            }

            if(editable){
                val singleFrame: View = layoutInflater.inflate(R.layout.item_categories, null)
                val categoryBtn = singleFrame.findViewById<MaterialButton>(R.id.categoryBtn)
                categoryBtn.setText("Add new category")
                categoryContent.addView(singleFrame)
                categoryView.add(singleFrame)
                view.findViewById<ImageView>(R.id.recommendCoverIV).setVisibility(View.VISIBLE)
            }
            recommendList = requireArguments().getSerializable(
                "recommendedList"
            ) as ArrayList<Book>

            listComment = requireArguments().getSerializable(
                "listComment"
            ) as ArrayList<Comment>

            comicNameET.setText(detailComic.name)
            comicAuthorET.setText(detailComic.author)
            comicRatingTV.setText("${detailComic.score}/5")
            comicSummaryET.setText(detailComic.summary)
//            comicCoverIV!!.setImageResource(detailComic.imagePath!!)


            if (!editable) {
                comicNameET.setFocusable(false)
                comicAuthorET.setFocusable(false)
                comicRatingTV.setFocusable(false)
                comicSummaryET.setFocusable(false)
            }


        }
        return view
    }

    private fun startUploadingPDF() {

        val path ="Books/${bookId}/Chapters/"
        for (i in 0..detailComic.listChapter!!.size - 1)
        {
            val storageRef = FirebaseStorage.getInstance().getReference(path+ detailComic.listChapter!![i].chapterName)
            storageRef.putFile(pdfList[i])
                .addOnSuccessListener { taskSnapshot ->
                    var uriTask : Task<Uri> = taskSnapshot.storage.downloadUrl
                    while (!uriTask.isSuccessful); // while này nó chạy để đợi load xong
                    val pdfUrL = "${uriTask.result}"
                    detailComic.listChapter!![i].chapterLink = pdfUrL
                    UpdateInfoList(detailComic.listChapter!!)
                    if (i === detailComic.listChapter!!.size - 1)
                    {
                        Log.i("error", "lastone")
                        alertDialog.dismiss()
                        requireActivity().finish()
                    }
                }
                .addOnFailureListener{e->
                    Log.i("error", e.message.toString())
                }
        }
    }

    private fun UpdateInfoList(listChapter: ArrayList<Chapter>) {
        fireStore?.collection("comics")!!.document(bookId!!)
            .update("listChapter",listChapter)
    }

    private fun startUploadingImage() {
        val path = "Books/${bookId}/"
        val storageRef = FirebaseStorage.getInstance().getReference(path + "banner")
        storageRef.putFile(imageUri!!)
            .addOnSuccessListener { taskSnapshot ->
                var UriImageTask : Task<Uri> = taskSnapshot.storage.downloadUrl
                while (!UriImageTask.isSuccessful);
                val tempStringUri : String = "${UriImageTask.result}"
                fireStore?.collection("comics")!!.document(bookId!!)
                    .update("imagePath", tempStringUri)
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if(!createNew) {
            var recommendAdapter = ComicAdapter(recommendList)
            val ft: FragmentTransaction = activity?.supportFragmentManager!!.beginTransaction()
            val fragment: Fragment = ListComicFragment.newInstance(recommendAdapter, -1)
            ft.replace(R.id.recommendFragmentContainer, fragment)
            ft.commit()

            val bookViewPage2 = view.findViewById<ViewPager2>(R.id.bookViewPage2)
            var bookVPAdapter = BookPageView2Adapter(
                activity as AppCompatActivity, 2,
                detailComic.listChapter!!, listComment, createNew
            )

            bookViewPage2.adapter = bookVPAdapter
            var tabIcon = arrayListOf(R.drawable.book_icon, R.drawable.profile_icon)
            val tabLayout: TabLayout = view.findViewById<TabLayout>(R.id.bookTab)
            TabLayoutMediator(tabLayout, bookViewPage2) { tab, position ->
                tab.text = bookVPAdapter.getPageTitle(position)
                tab.icon = resources.getDrawable(tabIcon[position])
                bookViewPage2.setCurrentItem(tab.position, true)
            }.attach()
        }
        else{

            val bookViewPage2 = view.findViewById<ViewPager2>(R.id.bookViewPage2)
            var bookVPAdapter = BookPageView2Adapter(
                activity as AppCompatActivity, 2,
                emptyList(), listComment, createNew
            )

            bookViewPage2.adapter = bookVPAdapter

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1000 && resultCode == RESULT_OK)
        {
            comicCoverIV!!.setImageURI(data?.data)
            imageUri = data?.data
        }

    }

}

