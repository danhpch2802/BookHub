package com.androidrealm.bookhub.Adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.androidrealm.bookhub.Models.Chapter
import com.androidrealm.bookhub.Models.Comment
import com.androidrealm.bookhub.Controllers.Fragments.ChapterFragment
import com.androidrealm.bookhub.Controllers.Fragments.CommentFragment
import com.androidrealm.bookhub.Controllers.Fragments.CreateNewChapterFragment
import com.androidrealm.bookhub.Controllers.Fragments.UpdateChapterFragment


class BookPageView2Adapter (activity: AppCompatActivity, val itemsCount: Int, val listChapters:List<Chapter>
                            ,val listComments:List<Comment>, val createNew : Boolean, val editable : Boolean) :
    FragmentStateAdapter(activity) {


    override fun getItemCount(): Int {
        return itemsCount
    }


    override fun createFragment(position: Int): Fragment {
        if (!createNew && !editable)
        {
            when(position) {
                0 -> return ChapterFragment.newInstance(listChapters as ArrayList<Chapter>)
                else->return CommentFragment.newInstance(listComments as ArrayList<Comment>)
            }
        }
        else if (createNew && !editable)
        {
            return CreateNewChapterFragment()
        }
        else
        {
            return UpdateChapterFragment.newInstance(listChapters  as ArrayList<Chapter>)
        }
    }

    fun getPageTitle(position: Int): CharSequence? {
        when(position) {
            0 -> return "Chapter"
            else-> return "Comment"
        }
    }
}