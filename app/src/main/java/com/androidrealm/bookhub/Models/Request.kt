package com.androidrealm.bookhub.Models

import com.google.firebase.firestore.DocumentId

class Request(
    @DocumentId
    var AccId:String?=null,
    var requestName:String?=null,
    var checked:Int? = 1
)
