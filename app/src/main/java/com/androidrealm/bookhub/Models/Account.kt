package com.androidrealm.bookhub.Models

import com.google.firebase.firestore.DocumentId
import java.io.Serializable

class Account(
    //@DocumentId
    var Avatar:String?= "",
    var Email:String?= "",
    var Badge:ArrayList<String> = ArrayList<String>(),
    var BadgeUnown:ArrayList<String> = ArrayList<String>(),
    var BadgeOwn:ArrayList<String> = ArrayList<String>(),
    var History:ArrayList<String> = ArrayList<String>(),
    var FavoriteList:ArrayList<String> = ArrayList<String>(),
    var Point:Int?=1,
    var password:String?=  "",
    var username:String?= "",
    var bookmark:Map<String, String>?=null
) : Serializable