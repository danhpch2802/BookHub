package com.androidrealm.bookhub.Models

import com.google.firebase.firestore.DocumentId

class Account(
    //@DocumentId
    var Avatar:String?=null,
    var Email:String?=null,
    var Badge:ArrayList<String>,
    var History:ArrayList<String>,
    var FavoriteList:ArrayList<String>,
    var Point:Int?=1,
    var password:String?= null,
    var username:String?=null,
    var bookmark:Map<String, String>?=null
)