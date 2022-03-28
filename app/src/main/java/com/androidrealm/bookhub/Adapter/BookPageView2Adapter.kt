package com.androidrealm.bookhub.Adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.androidrealm.bookhub.Models.Chapter
import com.androidrealm.bookhub.Models.Comment
import com.androidrealm.bookhub.Controllers.Fragments.ChapterFragment
import com.androidrealm.bookhub.Controllers.Fragments.CommentFragment


class BookPageView2Adapter (activity: AppCompatActivity, val itemsCount: Int,val listChapters:List<Chapter>
                            ,val listComments:List<Comment>) :
    FragmentStateAdapter(activity) {


    override fun getItemCount(): Int {
        return itemsCount
    }


    override fun createFragment(position: Int): Fragment {
        when(position) {
            0 -> return ChapterFragment.newInstance(listChapters as ArrayList<Chapter>)
            else->return CommentFragment.newInstance(listComments as ArrayList<Comment>)
        }
    }

    fun getPageTitle(position: Int): CharSequence? {
        when(position) {
            0 -> return "Chapter"
            else-> return "Comment"
        }
    }
}