package com.androidrealm.bookhub.Models

import java.io.Serializable

class Account(
    var accountName:String?=null,
    var email:String?=null,
    var badge:ArrayList<String>,
    var history:ArrayList<String>,

) : Serializable