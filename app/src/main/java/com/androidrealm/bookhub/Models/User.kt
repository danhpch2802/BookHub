package com.androidrealm.bookhub.Models

import android.widget.ImageView
import com.google.firebase.firestore.DocumentId

class User(@DocumentId
           var documentId:String?=null, val person: Account, val onlineState: String, val ava: ImageView?)