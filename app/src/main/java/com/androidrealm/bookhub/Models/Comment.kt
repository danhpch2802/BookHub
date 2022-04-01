package com.androidrealm.bookhub.Models

import com.google.firebase.firestore.DocumentId

class Comment(
    @DocumentId
    var AccountID:String?=null,
    var BookID:String?=null,
    var Content:String?=null,
    var CreatedAt:String?=null
)
