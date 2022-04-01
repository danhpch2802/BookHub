package com.androidrealm.bookhub.Models

import java.io.Serializable

class Request(
    var AccId:String?=null,
    var requestName:String?=null,
    var checked:Int? = 1
) : Serializable
