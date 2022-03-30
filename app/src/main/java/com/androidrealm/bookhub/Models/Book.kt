package com.androidrealm.bookhub.Models

import java.io.Serializable

class Book(
    var imagePath:String?= "",
    var listChapter:ArrayList<Chapter>?= ArrayList<Chapter>(),

    var RatedAccount: ArrayList<String>? = ArrayList<String>(),

    var ViewNumber: Int = 0,
    var locked: Boolean = false,

    var name:String?="", //
    var summary:String?="", //
    var author:String?="", //
    var score: Int = 0, //
    var listCategory:ArrayList<String>?= ArrayList<String>()
) : Serializable


