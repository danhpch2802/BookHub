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

class Request(
    var AccId:String?=null,
    var requestName:String?=null,
    var checked:Int? = 1
)

class Account(
    var accountName:String?=null,
    var email:String?=null
)

class Prize(
    var prizeName:String?=null,
    var prizeDescrypt:String?=null,
    var owned:Boolean?= false,
    var huge:Boolean?= false
)