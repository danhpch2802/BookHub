package com.androidrealm.bookhub.Adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.androidrealm.bookhub.Models.Chapter
import com.androidrealm.bookhub.Models.Comment
import com.androidrealm.bookhub.Controllers.Fragments.ChapterFragment
import com.androidrealm.bookhub.Controllers.Fragments.CommentFragment
import com.androidrealm.bookhub.Controllers.Fragments.CreateNewChapterFragment


class BookPageView2Adapter (activity: AppCompatActivity, val itemsCount: Int, val listChapters:List<Chapter>
                            ,val listComments:List<Comment>, val createNew : Boolean) :
    FragmentStateAdapter(activity) {


    override fun getItemCount(): Int {
        return itemsCount
    }


    override fun createFragment(position: Int): Fragment {
        if (!createNew)
        {
            when(position) {
                0 -> return ChapterFragment.newInstance(listChapters as ArrayList<Chapter>)
                else->return CommentFragment.newInstance(listComments as ArrayList<Comment>)
            }
        }
        else
        {
            return CreateNewChapterFragment()
        }
    }

    fun getPageTitle(position: Int): CharSequence? {
        when(position) {
            0 -> return "Chapter"
            else-> return "Comment"
        }
    }
}