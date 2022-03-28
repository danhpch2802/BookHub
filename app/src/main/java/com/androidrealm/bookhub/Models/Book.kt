package com.androidrealm.bookhub.Models

import java.io.Serializable

class Book(
    var imagePath:Int?=null,
    var name:String?=null,
    var summary:String?=null,
    var author:String?=null,
    var score: Double? =null,
    var listCategory:ArrayList<String>?=null,
    var listChapter:ArrayList<Chapter>?=null
) : Serializable


