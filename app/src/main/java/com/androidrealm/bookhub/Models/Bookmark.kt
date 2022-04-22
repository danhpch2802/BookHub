package com.androidrealm.bookhub.Models

import java.util.*
import java.io.Serializable

class Bookmark (
    var accountID:String?= "",
    var bookID:String?= "",
    var bookName:String?= "",
    var coverUrl:String?= "",
    var chapterUrl: Int? = 0,
    var chapterName: String? = "",
    ): Serializable