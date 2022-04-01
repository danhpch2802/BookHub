package com.androidrealm.bookhub.Models

import java.io.Serializable

class Book(
    var id:String?="",
    var imagePath:String?= "",
    var listChapter:ArrayList<Chapter>?= ArrayList<Chapter>(),

    var ratedAccount: ArrayList<String>? = ArrayList<String>(),

    var viewNumber: Int = 0,
    var locked: Boolean = false,

    var name:String?="", //
    var summary:String?="", //
    var author:String?="", //
    var score: ArrayList<Int> = arrayListOf<Int>(0,0,0,0,0), //
    var listCategory:ArrayList<String>?= ArrayList<String>()
) : Serializable


