package com.androidrealm.bookhub

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.androidrealm.bookhub.Models.Account
import com.androidrealm.bookhub.Models.Prize
import com.androidrealm.bookhub.fragments.AccountFragment

class PrizeListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prize_list)
        val lists = ArrayList<Prize>()
        lists.add(Prize("Bookaholic"))
        lists.add(Prize("Booker"))
        lists.add(Prize("Literaler"))


        if (savedInstanceState == null) {
            val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
            val fragment: Fragment = PrizeFragment.newInstance(lists)
            ft.replace(R.id.fragment_prize_list_view, fragment)
            ft.commit()
        }
    }
}