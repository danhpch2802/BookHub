package com.androidrealm.bookhub.Controllers.Fragments

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.androidrealm.bookhub.Models.Book
import com.androidrealm.bookhub.Models.Comment
import com.androidrealm.bookhub.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
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

    private var listCategoryChosen = ArrayList<String>()

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

        var view=inflater.inflate(R.layout.fragment_book, container, false)

         val comicNameET = view.findViewById<EditText>(R.id.comicNameET)
        val comicAuthorET = view.findViewById<EditText>(R.id.comicAuthorET)
        val comicRatingTV = view.findViewById<EditText>(R.id.comicRatingTV)
        val comicSummaryET = view.findViewById<EditText>(R.id.comicSummaryET)
        val comicCoverIV=view.findViewById<ImageView>(R.id.comicCoverIV)
        val categoryContent = view.findViewById<LinearLayout>(R.id.categoriesID)
        var categoryView = arrayListOf<View>()

        createNew=requireArguments().getSerializable(
            "createNew"
        ) as Boolean

        if(createNew)
        {
            val singleFrame: View = layoutInflater.inflate(R.layout.item_categories, null)
            val categoryBtn = singleFrame.findViewById<MaterialButton>(R.id.categoryBtn)

            categoryBtn.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(),R.color.app_golden))


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

                        if (categoryContent.childCount > 0)
                        {
                            categoryContent.removeViews(1, categoryContent.childCount - 1)
                        }


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
            comicCoverIV.setImageResource(detailComic.imagePath!!)


            if (!editable) {
                comicNameET.setFocusable(false)
                comicAuthorET.setFocusable(false)
                comicRatingTV.setFocusable(false)
                comicSummaryET.setFocusable(false)
            }


        }
        return view
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
                detailComic.listChapter!!, listComment
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
    }

    fun GetDataFromView()
    {

    }

}

