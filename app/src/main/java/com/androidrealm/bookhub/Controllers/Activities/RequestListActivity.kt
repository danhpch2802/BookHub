package com.androidrealm.bookhub.Controllers.Activities


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.androidrealm.bookhub.R
import com.androidrealm.bookhub.Models.Request
import com.androidrealm.bookhub.Controllers.Fragments.RequestFragment


class RequestListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_list)


        val lists = ArrayList<Request>()
        lists.add(Request("123", "sacd"))
        lists.add(Request("123", "sachhaylam"))
        lists.add(Request("124", "sachnhul"))
        lists.add(Request("125", "sachnhulove"))

        if (savedInstanceState == null) {
            val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
            val fragment: Fragment = RequestFragment.newInstance(lists)
            ft.replace(R.id.fragment_rqlist_view, fragment)
            ft.commit()
        }
    }

}