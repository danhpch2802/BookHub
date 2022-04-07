package com.androidrealm.bookhub.Models

import java.io.Serializable
import java.util.*

class Comment(
    var AccountID:String?= "",
    var AccountName:String?= "",
    var BookID:String?= "",
    var Content:String?="",
    var CreatedAt:Date?=null
) : Serializable
