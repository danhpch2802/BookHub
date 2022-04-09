package com.androidrealm.bookhub.Models

import com.google.firebase.firestore.DocumentId
import java.io.Serializable

class Prize(
    var id:String? = null,
    var prizeName:String?=null,
    var prizeDescrypt:String?=null
) : Serializable