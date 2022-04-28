package com.androidrealm.bookhub.Models

import java.io.Serializable

class Quiz (
    var Question:String? = null,
    var Result:String?=null,
    var Answer:ArrayList<String> = ArrayList<String>()
) : Serializable