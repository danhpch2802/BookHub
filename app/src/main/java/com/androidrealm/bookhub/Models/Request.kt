package com.androidrealm.bookhub.Models

import com.google.firebase.firestore.DocumentId

class Request(
    @DocumentId
    var id:String?=null,
    var AccountId:String?=null,
    var bookDetail:String?=null,
    var bookName:String?=null,
    var Checked:Boolean? = false
)
