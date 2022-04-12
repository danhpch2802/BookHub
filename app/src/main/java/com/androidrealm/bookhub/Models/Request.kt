package com.androidrealm.bookhub.Models

import com.google.firebase.firestore.DocumentId

class Request(
    @DocumentId
    var documentId:String?=null,
    var accountID:String?=null,
    var bookDetail:String?=null,
    var bookName:String?=null,
    var checked:Boolean? = false
)
