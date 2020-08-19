package com.example.sharemliy

data class Message(val content:String,val date:String,val time:String,val sender:String,val receiver:String,val seenstatus:Boolean){
 constructor():this("","","","","",false)
}