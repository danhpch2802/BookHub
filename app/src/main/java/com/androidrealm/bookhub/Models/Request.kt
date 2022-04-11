package com.androidrealm.bookhub.Models

import com.google.firebase.firestore.DocumentId

class Request(
    @DocumentId
    var AccountId:String?=null,
    var requestName:String?=null,
    var requestDetail:String?=null,
    var Checked:Int? = 1
)
