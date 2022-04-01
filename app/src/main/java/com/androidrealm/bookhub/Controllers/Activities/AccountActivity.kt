package com.androidrealm.bookhub.Controllers.Activities


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.androidrealm.bookhub.Models.Account
import com.androidrealm.bookhub.R
import com.androidrealm.bookhub.Controllers.Fragments.AccountFragment


class AccountActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        val badge = ArrayList<String>()//Creating an empty arraylist.
        badge.add("Top5")//Adding object in arraylist.
        badge.add("Bookaholic")

        val his = ArrayList<String>()//Creating an empty arraylist.
        his.add("OP")//Adding object in arraylist.
        his.add("Nar")

        val lists = ArrayList<Account>()
        lists.add(Account("sacd","", badge, his))
        lists.add(Account("Long","", badge, his))

        if (savedInstanceState == null) {
            val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
            val fragment: Fragment = AccountFragment.newInstance(lists)
            ft.replace(R.id.fragment_acc_view, fragment)
            ft.commit()
        }
    }
}