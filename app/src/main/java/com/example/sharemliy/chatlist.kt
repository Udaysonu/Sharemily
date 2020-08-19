package com.example.sharemliy

data class ChatList(val uid:String,val name:String,val photoUrl:String,val lastMessage:String,val messagecount:Int,val time:Int,val date:String){
    constructor():this("","","","",1,-100,"")
}