package com.androidrealm.bookhub.Models

import java.io.Serializable

class Comment(
    var AccountID:String?=null,
    var BookID:String?=null,
    var Content:String?=null,
    var CreatedAt:String?=null
) : Serializable
