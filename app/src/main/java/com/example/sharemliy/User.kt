package com.example.sharemliy

data class User(val auth_id:String,val name:String,val mobile:String,val photoUrl:String,val status:String,val online:Boolean)
{
    constructor():this("","","","","",false)
}