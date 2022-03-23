package com.androidrealm.bookhub.Models

import java.io.Serializable

class Comic(
    var imagePath:Int?=null,
    var name:String?=null,
    var summary:String?=null,
    var author:String?=null,
    var score: Double? =null,
    var listCategory:ArrayList<String>?=null,
    var listChapter:ArrayList<Chapter>?=null
) : Serializable

class Chapter(
    var chapterName:String?=null,
    var chapterLink:String?=null
)

class Comment(
    var AccountID:String?=null,
    var BookID:String?=null,
    var Content:String?=null,
    var CreatedAt:String?=null
    )