package com.androidrealm.bookhub.Controllers.Fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.androidrealm.bookhub.Controllers.Activities.*
import com.androidrealm.bookhub.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_homepage.*

class AdminProfileFragment : Fragment() {
    lateinit var preferences: SharedPreferences
    var PrizeBtn: ImageButton? = null
    var MyAccBtn: ImageButton? = null
    var AllAccBtn: ImageButton? = null
    var AvaBtn: ImageView? = null
    var SignoutBtn: ImageButton? = null
    var uid:String = ""

    var Point:Number? = 0
    var Name:String? = ""
    var Badge:String ?= ""

    var username: TextView? = null
    var badge: TextView? = null
    var badgeTV: TextView? = null
    var point: TextView? = null

    var createNewBtn:ImageButton?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_admin_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //
        preferences = requireActivity().getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
        uid = FirebaseAuth.getInstance().currentUser!!.uid

        PrizeBtn = view.findViewById(R.id.prize_btn_pf)
        MyAccBtn = view.findViewById(R.id.my_acc_btn_pf)
        AllAccBtn = view.findViewById(R.id.all_account_btn_pf)
        username = view.findViewById(R.id.pf_username)
        badge = view.findViewById(R.id.badge_pf)
        point = view.findViewById(R.id.point_prize_pf)
        badgeTV = view.findViewById(R.id.pf_prize)
        AvaBtn = view.findViewById(R.id.avatarpf_img)
        SignoutBtn = view.findViewById(R.id.signout_btn_pf)
        createNewBtn=view.findViewById<ImageButton>(R.id.createNewBtn)
        getAcc()

        //Log.d(TAG, Badge2)
        PrizeBtn!!.setOnClickListener{
            val intent = Intent(requireActivity(),  PrizeListActivity::class.java)
            startActivity(intent)
        }

        MyAccBtn!!.setOnClickListener{
            val intent = Intent(requireActivity(),  UpdateAccActivity::class.java)
            startActivity(intent)
        }

        AllAccBtn!!.setOnClickListener{
            val intent = Intent(requireActivity(),  AccountActivity::class.java)
            startActivity(intent)
        }

        //Log out
        SignoutBtn!!.setOnClickListener{
            val editor: SharedPreferences.Editor = preferences.edit()
            editor.clear()
            editor.apply()
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(requireActivity(), LoginActivity::class.java))
//            finish()
        }

        createNewBtn!!.setOnClickListener {
            val intent=Intent(requireActivity(), BookCreateActivity::class.java)
            startActivity(intent)
        }
    }

    override  fun onResume ()
    {
        super.onResume()
        getAcc()
    }

    fun getAcc () {
        val db = FirebaseFirestore.getInstance()
        db.collection("accounts").document(uid)
            .get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Name = task.result["username"] as String?
                    Point =  task.result["Point"] as Number?
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
                }
                else {onError(task.exception)}
                username!!.setText(Name)
                badge!!.setText("1")
                point!!.setText(Point.toString())

                badgeTV!!.setText("Admin")
            }
    }

    fun onError(e: Exception?) {
        if (e != null) {
            Log.d("RewardError", "onError: " + e.message)
        }
    }
}