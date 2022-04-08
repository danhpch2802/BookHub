package com.androidrealm.bookhub.Models

import java.io.Serializable
import java.util.*

class Comment(
    var accountID:String?= "",
    var accountName:String?= "",
    var bookID:String?= "",
    var content:String?="",
    var createdAt:Date?=null
) : Serializable
