package com.androidrealm.bookhub.Models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import java.io.Serializable

class Account(
    @DocumentId
    var documentId:String?=null,
    var Avatar:String?= "",
    var Badge:ArrayList<String> = ArrayList<String>(),
    var BadgeOwn:ArrayList<String> = ArrayList<String>(),
    var BadgeUnown:ArrayList<String> = ArrayList<String>(),
    var Email:String?= "",
    var FavoriteList:ArrayList<String> = ArrayList<String>(),
    var FriendsList:ArrayList<String> = ArrayList<String>(),
    var History:ArrayList<String> = ArrayList<String>(),
    var LastLogin:Timestamp?= null,
    var Point:Long?=1,
    var RecipientToken:String?="",
    var Role:Long?=1,
    var password:String?=  "",
    var quizCnt:Long?=1,
    val status: String?="",
    var username:String?= "",
    //var bookmark:Map<String, String>?=null
) : Serializable