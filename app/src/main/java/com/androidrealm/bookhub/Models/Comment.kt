package com.androidrealm.bookhub.Models

import java.io.Serializable
import java.util.*

class Comment(
    var AccountID:String?=null,
    var BookID:String?=null,
    var Content:String?=null,
    var CreatedAt:Date?=null
) : Serializable
