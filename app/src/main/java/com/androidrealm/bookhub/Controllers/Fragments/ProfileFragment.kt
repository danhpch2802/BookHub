package com.androidrealm.bookhub.Controllers.Fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.androidrealm.bookhub.Controllers.Activities.*
import com.androidrealm.bookhub.R
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class ProfileFragment : Fragment() {
    lateinit var preferences: SharedPreferences
    var PrizeBtn: ImageButton? = null
    var HistoryBtn: ImageButton? = null
    var FavBtn: ImageButton? = null
    var ProfileBtn: ImageButton? = null
    var BookmarkBtn: ImageButton? = null
    var AvaBtn: ImageView? = null
    var SignoutBtn: ImageButton? = null
    var QuizBtn: ImageButton? = null
    var RankBtn: ImageButton? = null
    var uid:String = ""
    var Badge2 = "2"
    var TotalBadge:Int? = 0
    var Point:Number? = 0
    var Name:String? = ""
    var Badge:String ?= ""

    var role:Long = 3

    var username: TextView? = null
    var badge: TextView? = null
    var badgeTV: TextView? = null
    var point: TextView? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//
        preferences = requireActivity().getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
        uid = FirebaseAuth.getInstance().currentUser!!.uid

        PrizeBtn = view.findViewById(R.id.prize_btn_pf)
        HistoryBtn = view.findViewById(R.id.history_btn_pf)
        FavBtn = view.findViewById(R.id.fav_btn_pf)
        ProfileBtn = view.findViewById(R.id.account_btn_pf)
        BookmarkBtn = view.findViewById(R.id.bookmark_btn_pf)
        username = view.findViewById(R.id.pf_username)
        badge = view.findViewById(R.id.badge_pf)
        point = view.findViewById(R.id.point_prize_pf)
        badgeTV = view.findViewById(R.id.pf_prize)
        AvaBtn = view.findViewById(R.id.avatarpf_img)
        QuizBtn = view.findViewById(R.id.quiz_btn_pf)
        RankBtn = view.findViewById(R.id.rank_btn_pf)
        SignoutBtn = view.findViewById(R.id.signout_btn_pf)
//        //ReBtn = findViewById(R.id.returnHomepage)
//
        getAcc()

        BookmarkBtn!!.setOnClickListener{
            val intent = Intent (getActivity(), BookmarkListActivity::class.java)
            getActivity()?.startActivity(intent)
        }

        PrizeBtn!!.setOnClickListener{
            val intent = Intent (getActivity(), PrizeActivity::class.java)
            getActivity()?.startActivity(intent)
        }
//
        HistoryBtn!!.setOnClickListener{
            val intent = Intent(requireActivity(), HistoryActivity::class.java)
            startActivity(intent)
        }

        FavBtn!!.setOnClickListener{
            val intent = Intent(requireActivity(), FavoriteListActivity::class.java)
            startActivity(intent)
        }

        RankBtn!!.setOnClickListener{
            val intent = Intent(requireActivity(), RankActivity::class.java)
            startActivity(intent)
        }

        ProfileBtn!!.setOnClickListener{
            val intent = Intent(getActivity(),  UpdateAccActivity::class.java)
            getActivity()?.startActivity(intent)

        }

        //Log out
        SignoutBtn!!.setOnClickListener{
            val editor: SharedPreferences.Editor = preferences.edit()
            editor.clear()
            editor.apply()
            updateUserStatus("Offline")
            FirebaseAuth.getInstance().signOut()
            LoginManager.getInstance().logOut()
            startActivity(Intent(getActivity(), LoginActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        getAcc()
    }

    private fun updateUserStatus(s: String) {
        val currentUserID = FirebaseAuth.getInstance().currentUser!!.uid
        val dbRef = FirebaseFirestore.getInstance().collection("accounts")
            .document(currentUserID)
        dbRef.update("status", s)
    }

    fun getAcc () {
        val db = FirebaseFirestore.getInstance()
        db.collection("accounts").document(uid)
            .get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Name = task.result["username"] as String?
                    var avatar:String ?= ""
                    avatar = task.result["Avatar"] as String?
                    when (avatar) {
                        "1" -> AvaBtn!!.setImageResource(R.drawable.amagami_cover)
                        "2" -> AvaBtn!!.setImageResource(R.drawable.doll_cover)
                        "3" -> AvaBtn!!.setImageResource(R.drawable.fechippuru_cover)
                        "4" -> AvaBtn!!.setImageResource(R.drawable.kanojo_cover)
                        "5" -> AvaBtn!!.setImageResource(R.drawable.komi_cover)
                        "6" -> AvaBtn!!.setImageResource(R.drawable.meika_cover)
                        "7" -> AvaBtn!!.setImageResource(R.drawable.mokanojo_cover)
                        "8" -> AvaBtn!!.setImageResource(R.drawable.tonikaku_cover)
                        "9" -> AvaBtn!!.setImageResource(R.drawable.yofukashi_cover)
                        else -> AvaBtn!!.setImageResource(R.drawable.amagami_cover)
                    }
                    var cnt = 0
                    for (document in task.result["BadgeOwn"] as ArrayList<*>) {
                        cnt++
                    }
                    for (document in task.result["Badge"] as ArrayList<*>) {
                        Badge2 = document as String
                        break
                    }
                    TotalBadge = cnt
                    Point =  task.result["Point"] as Number?
                }
                else {onError(task.exception)}
                username!!.setText(Name)
                badge!!.setText(TotalBadge.toString())
                point!!.setText(Point.toString())
                val db2 = FirebaseFirestore.getInstance()
                db2.collection("prizes").document(Badge2).get().addOnCompleteListener { task2 ->
                    if (task2.isSuccessful) {
                        Badge = task2.result["prizeName"] as? String
                    }
                    else {
                        onError(task2.exception)
                    }
                    badgeTV!!.setText(Badge)
                    if (Badge2 == "b1" || Badge2 =="b2")
                    {
                        badgeTV!!.setBackgroundColor(Color.parseColor("#f7971e"))
                    }
                }
            }
    }

    fun onError(e: Exception?) {
        if (e != null) {
            Log.d("RewardError", "onError: " + e.message)
        }
    }
}