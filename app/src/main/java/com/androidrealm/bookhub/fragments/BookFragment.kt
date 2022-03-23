package com.androidrealm.bookhub.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.HorizontalScrollView
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.viewpager2.widget.ViewPager2
import com.androidrealm.bookhub.Adapter.BookPageView2Adapter
import com.androidrealm.bookhub.ComicAdapter
import com.androidrealm.bookhub.Models.Comic
import com.androidrealm.bookhub.Models.Comment
import com.androidrealm.bookhub.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


/**
 * A simple [Fragment] subclass.
 * Use the [BookFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BookFragment : Fragment() {
    private var detailComic=Comic()
    private var listComment=ArrayList<Comment>()
    private var recommendList=ArrayList<Comic>()
    companion object {
        fun newInstance
                    (comic: Comic,recommendList:ArrayList<Comic>,listComment:ArrayList<Comment>,editable:Boolean): BookFragment
        {
            val fragment= BookFragment()
            val bundle = Bundle()
            bundle.putSerializable("comic", comic)
            bundle.putSerializable("recommendedList", recommendList)
            bundle.putSerializable("listComment", listComment)
            bundle.putSerializable("editable", editable)
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

        detailComic=requireArguments().getSerializable(
            "comic"
        ) as Comic


        val categoryContent=view.findViewById<ConstraintLayout>(R.id.categoriesID)
        var categoryView= arrayListOf<View>()

        for (category in detailComic.listCategory!!) {
//frame-0, frame-1, frame-2, ... and so on
            val singleFrame: View = layoutInflater.inflate(R.layout.item_categories, null)
            singleFrame.id = category.indexOf(category)
            val categoryBtn = singleFrame.findViewById<MaterialButton>(R.id.categoryBtn)
            categoryBtn.setText(category as CharSequence)
            categoryContent.addView(singleFrame)
            categoryView.add(singleFrame)
        }

        recommendList=requireArguments().getSerializable(
            "recommendedList"
        ) as ArrayList<Comic>

        listComment=requireArguments().getSerializable(
            "listComment"
        ) as ArrayList<Comment>

        comicNameET.setText(detailComic.name)
        comicAuthorET.setText(detailComic.author)
        comicRatingTV.setText("${detailComic.score}/5")
        comicSummaryET.setText(detailComic.summary)
        comicCoverIV.setImageResource(detailComic.imagePath!!)

        val editable=requireArguments().getSerializable(
            "editable"
        ) as Boolean
        if(!editable){
            comicNameET.setFocusable(false)
            comicAuthorET.setFocusable(false)
            comicRatingTV.setFocusable(false)
            comicSummaryET.setFocusable(false)
        }



        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //Recommend fragments
        var recommendAdapter= ComicAdapter(recommendList)
        val ft: FragmentTransaction = activity?.supportFragmentManager!!.beginTransaction()
        val fragment: Fragment = ListComicFragment.newInstance(recommendAdapter, -1)
        ft.replace(R.id.recommendFragmentContainer, fragment)
        ft.commit()

        val bookViewPage2=view.findViewById<ViewPager2>(R.id.bookViewPage2)
        var bookVPAdapter=BookPageView2Adapter(
            activity as AppCompatActivity, 2,
            detailComic.listChapter!!,listComment
        )

        bookViewPage2.adapter=bookVPAdapter
        var tabIcon= arrayListOf(R.drawable.book_icon,R.drawable.profile_icon)
        val tabLayout: TabLayout = view.findViewById<TabLayout>(R.id.bookTab)
        TabLayoutMediator(tabLayout, bookViewPage2) { tab, position ->
            tab.text = bookVPAdapter.getPageTitle(position)
            tab.icon = resources.getDrawable(tabIcon[position])
            bookViewPage2.setCurrentItem(tab.position, true)
        }.attach()

    }

}