package com.androidrealm.bookhub.Models

import java.io.Serializable

class Prize(
    var prizeName:String?=null,
    var prizeDescrypt:String?=null,
    var owned:Boolean?= false,
    var huge:Boolean?= false
) : Serializable