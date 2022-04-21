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
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.androidrealm.bookhub.Adapter.BookPageView2Adapter
import com.androidrealm.bookhub.ComicAdapter
import com.androidrealm.bookhub.Controllers.Activities.BookDetailActivity
import com.androidrealm.bookhub.Controllers.Fragments.CreateNewChapterFragment.Companion.listChapterToCreate
import com.androidrealm.bookhub.Controllers.Fragments.CreateNewChapterFragment.Companion.pdfListURIToCreate
import com.androidrealm.bookhub.Controllers.Fragments.UpdateChapterFragment.Companion.listChapterToEdit
import com.androidrealm.bookhub.Controllers.Fragments.UpdateChapterFragment.Companion.pdfListUriToEdit
import com.androidrealm.bookhub.Controllers.Fragments.UpdateChapterFragment.Companion.pdfListUrlToDel
import com.androidrealm.bookhub.Models.Account
import com.androidrealm.bookhub.Models.Book
import com.androidrealm.bookhub.Models.Comment
import com.androidrealm.bookhub.R
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import dmax.dialog.SpotsDialog
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.math.RoundingMode
import java.text.DecimalFormat


/**
 * A simple [Fragment] subclass.
 * Use the [BookFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BookFragment : Fragment() {
    private var detailComic=Book()
    private var listComment : ArrayList<Comment>? =ArrayList<Comment>()
    private var recommendList : ArrayList<Book>? =ArrayList<Book>()
    private var createNew = false
    private var editable = false
    private var comicCoverIV : ImageView? = null
    private var comicNameET : EditText? = null
    private var comicAuthorET : EditText? = null
    private var comicSummaryET:EditText?=null
    private var imageUri : Uri? = null

    private var listCategoryChosen = ArrayList<String>()
    lateinit var alertDialog :android.app.AlertDialog

    private var bookId : String? = null

    private var fireStore : FirebaseFirestore? = null

    private var userInfo : Account? = Account()

    private var ratingLL:RatingBar?=null


    private var recommendTV:TextView?=null
    private var recommendFragmentContainer:RecyclerView?=null
    private var cover_EditRL:RelativeLayout?=null
    private var favoriteBtn:Button?=null

    companion object {

        fun newInstance
                    (createNew:Boolean?=false, book: Book?=null, recommendList:ArrayList<Book>?=null, listComment:ArrayList<Comment>?=null, editable:Boolean?=null): BookFragment
        {
            val fragment= BookFragment()
            val bundle = Bundle()
            if(!createNew!!) {

                bundle.putSerializable("recommendedList", recommendList)
                bundle.putSerializable("listComment", listComment)
            }
            bundle.putSerializable("recommendedList", recommendList)
            bundle.putSerializable("comic", book)
            bundle.putSerializable("editable", editable)
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
        comicNameET = view.findViewById<EditText>(R.id.comicNameET)
        comicAuthorET = view.findViewById<EditText>(R.id.comicAuthorET)
        cover_EditRL=view.findViewById(R.id.cover_EditRL)
        recommendTV=view.findViewById(R.id.recommendTV)
        recommendFragmentContainer=view.findViewById<RecyclerView>(R.id.recommendFragmentContainer)
        favoriteBtn = view.findViewById<Button>(R.id.markAsFavoriteBtn)

        ratingLL=view.findViewById(R.id.ratingLL)

        comicSummaryET = view.findViewById<EditText>(R.id.comicSummaryET)

        val categoryContent = view.findViewById<LinearLayout>(R.id.categoriesID)
        val uploadBtn = view.findViewById<Button>(R.id.uploadComic)

        var categoryView = arrayListOf<View>()

        createNew=requireArguments().getSerializable(
            "createNew"
        ) as Boolean
        editable= requireArguments().getSerializable(
            "editable"
        ) as Boolean
        if(createNew) // create new here
        {
            hideWhenEdit()
            val singleFrame: View = layoutInflater.inflate(R.layout.item_categories, null)
            val categoryBtn = singleFrame.findViewById<MaterialButton>(R.id.categoryBtn)

            categoryBtn.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(),R.color.app_golden))

            uploadBtn.visibility = View.VISIBLE

            cover_EditRL!!.setOnClickListener {
                val intentPickIMG = Intent(Intent.ACTION_OPEN_DOCUMENT)
                intentPickIMG.type = "image/*"
                startActivityForResult(intentPickIMG, 1000)
            }
            categoryBtn.setText("Add new category")
            categoryContent.addView(singleFrame)
            categoryView.add(singleFrame)
            //view.findViewById<ImageView>(R.id.recommendCoverIV).setVisibility(View.VISIBLE)


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
                        listCategoryChosen.clear()

                        for (i in checkedCategory)
                            listCategoryChosen.add(listCategoryToChoose[i])
                        if (categoryContent.childCount > 0)
                        {
                            categoryContent.removeViews(1, categoryContent.childCount - 1)
                        }


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
                detailComic.name = comicNameET!!.text.toString()
                detailComic.author = comicAuthorET!!.text.toString()

                detailComic.summary = comicSummaryET!!.text.toString()

                detailComic.listCategory = listCategoryChosen

                detailComic.listChapter = listChapterToCreate

                alertDialog.show()
                //create Book in firestore to get ID

                fireStore!!.collection("comics")
                    .add(detailComic)
                    .addOnSuccessListener { docRef ->
                        bookId = docRef.id
                        detailComic.id = docRef.id
                        GlobalScope.launch {
                            try {
                                startUploadingImage()
                                startUploadingPDF()
                            }
                            catch (e : Throwable)
                            {

                            }
                        }
                    }

            }


        }
        // Edit here
       else  {
            val editable = requireArguments().getSerializable(
                "editable"
            ) as Boolean

            detailComic = requireArguments().getSerializable(
                "comic"
            ) as Book

            if(editable)
            {
                val singleFrame: View = layoutInflater.inflate(R.layout.item_categories, null)
                val categoryBtn = singleFrame.findViewById<MaterialButton>(R.id.categoryBtn)
                categoryBtn.setText("Add new category")
                categoryContent.addView(singleFrame)
                categoryView.add(singleFrame)
                categoryBtn.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(),R.color.app_golden))

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
                            listCategoryChosen.clear()
                            detailComic.listCategory?.clear()

                            for (i in checkedCategory)
                                listCategoryChosen.add(listCategoryToChoose[i])

                            if (categoryContent.childCount > 0)
                            {
                                categoryContent.removeViews(1, categoryContent.childCount - 1)
                            }

                            detailComic.listCategory = listCategoryChosen


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

                cover_EditRL!!.setOnClickListener {
                    val intentPickIMG = Intent(Intent.ACTION_OPEN_DOCUMENT)
                    intentPickIMG.type = "image/*"
                    startActivityForResult(intentPickIMG, 1000)
                }

                val topAppbar= requireActivity().findViewById<MaterialToolbar>(R.id.topAppbar)

                topAppbar.setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.saveInfoBook -> {

                            detailComic.name = comicNameET!!.text.toString()
                            detailComic.author = comicAuthorET!!.text.toString()

                            detailComic.summary = comicSummaryET!!.text.toString()
                            bookId = detailComic.id
                            Log.i("id", bookId.toString())
                            //change Cover
                            alertDialog.show()
                            GlobalScope.launch {
                            if (imageUri != null)
                            {

                                    try {
                                        deleteCover()
                                        startUploadingImage()
                                    }
                                    catch (e : Throwable)
                                    {

                                    }
                            }
                                if(!pdfListUrlToDel.size.equals(0))
                                {
                                    startDelPDFList(pdfListUrlToDel)
                                }
                                detailComic.listChapter = listChapterToEdit
                                try {
                                    startUploadingPDF()
                                } catch (e: Throwable) {

                                }
                            }
                            true
                        }
                        R.id.deleteBook -> {
                            bookId = detailComic.id
                            alertDialog.setMessage("Deleting")
                            alertDialog.show()
                            deleteAllFileInStorage(detailComic)
                            GlobalScope.launch {
                                deleteCover()
                                deleteDocumentInFireStore(bookId)
                            }
                            deleteCommentInFireStore(bookId)
                            deleteInAccountHistory(bookId)
                            deleteInAccountFavorite(bookId)
                            listChapterToEdit.clear()
                            pdfListUriToEdit.clear()
                            pdfListUrlToDel.clear()
                            true
                        }
                        else -> false
                    }
                }
            }
            //Read
            else {
                comicNameET!!.setFocusable(false)
                comicAuthorET!!.setFocusable(false)
                comicSummaryET!!.setFocusable(false)

            }

            for (category in detailComic.listCategory!!) {
                val singleFrame: View = layoutInflater.inflate(R.layout.item_categories, null)
                singleFrame.id = category.indexOf(category)
                val categoryBtn = singleFrame.findViewById<MaterialButton>(R.id.categoryBtn)
                categoryBtn.setText(category as CharSequence)
                categoryContent.addView(singleFrame)
                categoryView.add(singleFrame)
            }


            comicNameET!!.setText(detailComic.name)
            comicAuthorET!!.setText(detailComic.author)

            var calScore = 0.0F

            if (!detailComic.score.sum().equals(0))
            {
                for (i in 0..4)
                {
                    calScore = calScore + detailComic.score[i] * (i+1)
                }

                calScore= calScore/detailComic.score.sum()
            }


            calRating(calScore)

            comicSummaryET!!.setText(detailComic.summary)
            Picasso.get()
                .load(detailComic.imagePath)
                .into(comicCoverIV);
       }
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val editable = requireArguments().getSerializable(
            "editable"
        ) as Boolean

        //no create && no edit
        if(!createNew && !editable) {
            listComment = requireArguments().getSerializable(
                "listComment"
            ) as ArrayList<Comment>

            detailComic = requireArguments().getSerializable(
                "comic"
            ) as Book

            recommendList = requireArguments().getSerializable(
                "recommendedList"
            ) as ArrayList<Book>

            bookId = detailComic.id


            var recommendAdapter = ComicAdapter(recommendList!!)
            recommendAdapter.onItemClick = { book ->
                val intent = Intent(requireContext(), BookDetailActivity::class.java)
                intent.putExtra("id", book.id)
                requireActivity().finish()
                startActivity(intent)
            }

            recommendFragmentContainer!!.adapter=recommendAdapter
            recommendFragmentContainer!!.layoutManager=GridLayoutManager(requireContext(),3)

            val currentUserAuth = FirebaseAuth.getInstance().currentUser

            val accountRef =FirebaseFirestore.getInstance().collection("accounts")
                .document(currentUserAuth!!.uid)
                .get()
                .addOnSuccessListener { document ->
                    userInfo!!.username = document.data!!["username"] as String
                    userInfo!!.Point = document.data!!["Point"] as Long
                    userInfo!!.FavoriteList = document.data!!["FavoriteList"] as ArrayList<String>
                    userInfo!!.History = document.data!!["History"] as ArrayList<String>

                    val role = document.data!!["Role"] as Long

                    if (userInfo!!.FavoriteList.contains(bookId))
                    {
                        favoriteBtn!!.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.btn_star_big_on,0,0,0)
                        favoriteBtn!!.isSelected = true
                    }
                    else
                    {
                        favoriteBtn!!.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.btn_star,0,0,0)
                        favoriteBtn!!.isSelected = false
                    }

                    favoriteBtn!!.setOnClickListener {

                        if (favoriteBtn!!.isSelected)
                        {
                            val position = userInfo!!.FavoriteList.indexOf(bookId)
                            userInfo!!.FavoriteList.removeAt(position)
                            updateFavoriteList(currentUserAuth!!.uid, userInfo!!.FavoriteList)

                            favoriteBtn!!.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.btn_star,0,0,0)
                            favoriteBtn!!.isSelected = false
                        }
                        else
                        {
                            userInfo!!.FavoriteList.add(0, bookId!!)
                            updateFavoriteList(currentUserAuth!!.uid, userInfo!!.FavoriteList)

                            favoriteBtn!!.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.btn_star_big_on,0,0,0)
                            favoriteBtn!!.isSelected = true
                        }
                    }

                    val bookViewPage2 = view.findViewById<ViewPager2>(R.id.bookViewPage2)
                    var bookVPAdapter = BookPageView2Adapter(
                        activity as AppCompatActivity, 2,
                        detailComic.listChapter!!, listComment!!, userInfo!!, bookId, createNew, editable,detailComic,
                        role
                    )
                    Log.d("chapter",detailComic.listChapter.toString())
                    bookViewPage2.adapter = bookVPAdapter
                    var tabIcon = arrayListOf(R.drawable.book_icon, R.drawable.profile_icon)
                    val tabLayout: TabLayout = view.findViewById<TabLayout>(R.id.bookTab)
                    TabLayoutMediator(tabLayout, bookViewPage2) { tab, position ->
                        tab.text = bookVPAdapter.getPageTitle(position)
                        tab.icon = resources.getDrawable(tabIcon[position])
                        bookViewPage2.setCurrentItem(tab.position, true)
                    }.attach()

                }
                .addOnFailureListener {exception ->
                    Log.d("Error", "get failed with ", exception)
                }
        }
        //create && no edit
        else if (createNew && !editable){
            hideWhenEdit()
            val bookViewPage2 = view.findViewById<ViewPager2>(R.id.bookViewPage2)
            var bookVPAdapter = BookPageView2Adapter(
                activity as AppCompatActivity, 2,
                emptyList(), emptyList(), null, bookId, createNew, editable,null,
                -1
            )
            bookViewPage2.adapter = bookVPAdapter
        }
        //no create && edit
        else
        {
            hideWhenEdit()
            detailComic = requireArguments().getSerializable(
                "comic"
            ) as Book

            bookId = detailComic.id
            val bookViewPage2 = view.findViewById<ViewPager2>(R.id.bookViewPage2)
            var bookVPAdapter = BookPageView2Adapter(
                activity as AppCompatActivity, 2,
                detailComic.listChapter!!,emptyList()!!, null, bookId, createNew, editable,null,
                -1
            )
            bookViewPage2.adapter = bookVPAdapter
        }
    }

    fun updateFavoriteList(userInfoId: String, listFavorite: ArrayList<String>)
    {
        val accountRef = FirebaseFirestore.getInstance().collection("accounts")
            .document(userInfoId)
            .update("FavoriteList", listFavorite)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1000 && resultCode == RESULT_OK)
        {
            comicCoverIV!!.setImageURI(data?.data)
            imageUri = data?.data
        }

    }

    fun reFreshStarAdminDel(currentListComment: ArrayList<Comment>) {
        var calScore = 0.0F

        for (i in currentListComment) {
            calScore = calScore + i.starRated!!
        }

        calScore= calScore / (currentListComment.size)

        calRating(calScore)
    }

    fun reFreshStar(indexNewStar: Long) {
        var calScore = 0.0F

        if (!detailComic.score.sum().equals(0))
        {
            for (i in 0..4)
            {
                calScore = calScore + detailComic.score[i] * (i+1)
            }

            calScore = calScore + (indexNewStar)

            calScore= calScore / (detailComic.score.sum()+1)

        }
        else
        {
            calScore = calScore + (indexNewStar)
        }

        calRating(calScore)
    }


    private fun deleteInAccountHistory(bookId: String?) {
        fireStore!!.collection("accounts")
            .whereArrayContains("History", bookId.toString())
            .get()
            .addOnSuccessListener { querySnapshot->
                for (document in querySnapshot)
                {
                    document.reference.update("History", FieldValue.arrayRemove(bookId))
                }
            }
    }



    private fun deleteInAccountFavorite(bookId: String?) {
        fireStore!!.collection("accounts")
            .whereArrayContains("FavoriteList", bookId.toString())
            .get()
            .addOnSuccessListener { querySnapshot->
                for (document in querySnapshot)
                {
                    document.reference.update("FavoriteList", FieldValue.arrayRemove(bookId))
                }
            }
    }

    private fun deleteCommentInFireStore(bookId: String?) {
        fireStore!!.collection("comments")
            .whereEqualTo("BookID", bookId.toString())
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot)
                {
                    fireStore!!.collection("comments").document(document.id)
                        .delete()
                }
            }
    }

    private suspend fun deleteDocumentInFireStore(bookId: String?) {
        fireStore!!.collection("comics")!!.document(bookId!!)
            .delete()
            .await()
        alertDialog.dismiss()
        requireActivity().finish()
    }

    private suspend fun startDelPDFList(pdfListUrlToDel: ArrayList<String>)
    {
        for (i in pdfListUrlToDel)
        {
            val delRef = FirebaseStorage.getInstance().getReferenceFromUrl(i)
            try {
                delRef.delete()
                    .await()
            }
            catch (e: Throwable)
            {
                Log.e("error delete pdf list", e.message.toString())
            }
        }
    }

    private suspend fun startUploadingPDF(){

        val path ="Books/${bookId}/Chapters/"
        if(createNew)
        {
                for (i in 0..detailComic.listChapter!!.size - 1) {

                    UploadingFile(path, i, pdfListURIToCreate)
                    if (i === detailComic.listChapter!!.size - 1) {
                        Log.i("Upload success", "lastone")
                        UpdateInfoList()
                        pdfListURIToCreate.clear()
                        listChapterToCreate.clear()
                        alertDialog.dismiss()
                        requireActivity().finish()

                    }
                }
        }
        else{
            for (i in 0..detailComic.listChapter!!.size - 1) {
                if (!pdfListUriToEdit[i].toString()
                        .equals("Currently Have PDF file")
                ) // change PDF chapter
                {
                    if (!detailComic.listChapter!![i].links!!.equals("")) {
                        deletePdf(detailComic.listChapter!![i].links!!)
                    }

                    UploadingFile(path, i, pdfListUriToEdit)

                    if (i === detailComic.listChapter!!.size - 1) {
                        Log.i("error1", "lastone")
                        UpdateInfoList()
                        listChapterToEdit.clear()
                        pdfListUriToEdit.clear()
                        pdfListUrlToDel.clear()
                        alertDialog.dismiss()
                        requireActivity().finish()
                    }
                } else {
                    if (i === detailComic.listChapter!!.size - 1) {
                        Log.i("error2", "lastone")
                        UpdateInfoList()
                        listChapterToEdit.clear()
                        pdfListUriToEdit.clear()
                        pdfListUrlToDel.clear()
                        alertDialog.dismiss()
                        requireActivity().finish()
                    }
                }
            }
        }
    }

    private suspend fun deleteCover(){
        val delRef = FirebaseStorage.getInstance().getReferenceFromUrl(detailComic.imagePath!!)
        try {
            delRef.delete()
                .await()
        }
        catch (e: Throwable)
        {
            Log.e("error delete image", e.message.toString())
        }
    }

    private fun deleteAllFileInStorage(detailComic: Book) {
        for (i in detailComic.listChapter!!)
        {
            val delRef = FirebaseStorage.getInstance().getReferenceFromUrl(i.links!!)
            try {
                    delRef.delete()
            }
            catch (e: Throwable)
            {
                Log.e("error file, chapter", e.message.toString())
            }
        }
    }

    private suspend fun deletePdf(UrlPDF: String){
        val delRef = FirebaseStorage.getInstance().getReferenceFromUrl(UrlPDF!!)
        try {
            delRef.delete()
                .await()
        }
        catch (e: Throwable)
        {
            Log.e("error delete file", e.message.toString())
        }
    }

    private suspend fun UploadingFile(path : String,position: Int, listURI : ArrayList<Uri>){
        val storageRef = FirebaseStorage.getInstance().getReference(path+ detailComic.listChapter!![position].name)
        try {
            val temp = storageRef.putFile(listURI[position])
                .await()
                .storage
                .downloadUrl
                .await()
            detailComic.listChapter!![position].links = temp.toString()
            Log.i("Upload file", "")
        }
        catch (e: Throwable)
        {
            Log.e("error up file", e.message.toString())
        }
    }
    private suspend fun UpdateInfoList() {
        try {
            fireStore?.collection("comics")!!.document(bookId!!)
                .set(detailComic).await()
        }
        catch (e: Throwable)
        {
            Log.e("error up Info", e.message.toString())
        }
    }

    private suspend fun startUploadingImage() {
        val path = "Books/${bookId}/"
        val storageRef = FirebaseStorage.getInstance().getReference(path + "banner")
        try {
            val temp = storageRef.putFile(imageUri!!)
                .await()
                .storage
                .downloadUrl
                .await()
            detailComic.imagePath = temp.toString()
            Log.i("Upload image", "")
        }
        catch (e: Throwable)
        {
            Log.e("error up image", e.message.toString())
        }
    }

    private fun calRating(rating: Float){
        ratingLL!!.setRating(rating)
    }

    private fun hideWhenEdit(){
        recommendTV!!.visibility=View.GONE
        recommendFragmentContainer!!.visibility=View.GONE
        comicNameET!!.setBackgroundResource(R.drawable.edittextbackground)

        comicAuthorET!!.setBackgroundResource(R.drawable.edittextbackground)

        favoriteBtn!!.visibility=View.GONE
        cover_EditRL!!.visibility=View.VISIBLE
        ratingLL!!.visibility=View.GONE
    }

    override fun onResume() {
        super.onResume()

        if(!createNew && !editable) {

        }
    }
}

