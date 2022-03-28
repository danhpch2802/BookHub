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

        val lists = ArrayList<Account>()
        lists.add(Account("sacd"))
        lists.add(Account("Long"))
        lists.add(Account("Khôi"))
        lists.add(Account("Vu"))
        lists.add(Account("Trần"))

        if (savedInstanceState == null) {
            val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
            val fragment: Fragment = AccountFragment.newInstance(lists)
            ft.replace(R.id.fragment_acc_view, fragment)
            ft.commit()
        }
    }
}