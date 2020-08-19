package com.example.sharemliy

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import com.vanniktech.emoji.EmojiManager
import com.vanniktech.emoji.EmojiPopup
import com.vanniktech.emoji.google.GoogleEmojiProvider
import com.vanniktech.emoji.listeners.OnEmojiClickListener
import com.vanniktech.emoji.listeners.OnSoftKeyboardCloseListener
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.list_item_chat_sent_message.*
import java.text.SimpleDateFormat
import java.util.*

val auth by lazy{
    FirebaseAuth.getInstance()
}
val database by lazy{
    FirebaseDatabase.getInstance().getReference()
}
val store by lazy{
    FirebaseFirestore.getInstance().collection("User")
}


const val UID="auth_id"
const val NAME="name"
const val IMAGE="photoUrl"


lateinit var FRIEND_ID:String;
lateinit var image_url:String;
lateinit var frient_name:String;
class ChatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EmojiManager.install(GoogleEmojiProvider())


        setContentView(R.layout.activity_chat)
        image_url=intent.getStringExtra(IMAGE)
        Picasso.get().load(image_url).into(chat_image)

        frient_name=intent.getStringExtra(NAME)

        chat_name.text= frient_name
        FRIEND_ID=intent.getStringExtra(UID)
        val id=getId(FRIEND_ID)
        send_msg.setOnClickListener {
            val string=message_send.text.toString()
            message_send.setText("")
            sendmessage(string,id)
        }
    }

    private fun sendmessage(string: String, id: String) {

        store.document(auth.currentUser?.uid.toString()).get().addOnSuccessListener {
           var user= it.toObject(User::class.java)!!
            Toast.makeText(this,"${user}",Toast.LENGTH_LONG).show()
            val sdf = SimpleDateFormat("HH:mm")
            val currentDate = sdf.format(Date())
            database.child("messaages").child(id).push().setValue(Message(string,"none",System.currentTimeMillis().toString(), FRIEND_ID,auth.currentUser?.uid.toString(),false))
            database.child("Chat").child(auth.uid.toString()).child(FRIEND_ID).setValue(ChatList(FRIEND_ID, frient_name, image_url,string,1,System.currentTimeMillis().toString(),currentDate))
            database.child("Chat").child(FRIEND_ID).child(auth.uid.toString()).setValue(ChatList(auth.uid.toString(),user.name, user.photoUrl,string,1,System.currentTimeMillis().toString(),currentDate))

        }.addOnFailureListener {
            Toast.makeText(this,"failed",Toast.LENGTH_LONG).show()

        }

    }

    private fun getId(friendId: String?):String {
        var id:String;

            if(friendId!! > auth.currentUser?.uid.toString())
            {
                id=friendId+ auth.currentUser?.uid.toString()
            }
            else
            {
                id=auth.currentUser?.uid.toString() + friendId
            }
        return id
    }
}


