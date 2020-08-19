package com.example.sharemliy

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import chatAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.protobuf.MessageLite
import com.squareup.picasso.Picasso
import com.vanniktech.emoji.EmojiManager
import com.vanniktech.emoji.google.GoogleEmojiProvider
import kotlinx.android.synthetic.main.activity_chat.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

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
lateinit var current_chat_id:String;
lateinit var madapter:MessagesAdapter

lateinit var messageList:ArrayList<Message>

class ChatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EmojiManager.install(GoogleEmojiProvider())
        setContentView(R.layout.activity_chat)

        messageList= ArrayList()

        madapter= MessagesAdapter(messageList,auth.uid.toString())


        chat_rv.adapter= madapter
        chat_rv.layoutManager= LinearLayoutManager(this)
        image_url=intent.getStringExtra(IMAGE)
        Picasso.get().load(image_url).into(chat_image)

        frient_name=intent.getStringExtra(NAME)

        chat_name.text= frient_name
        FRIEND_ID=intent.getStringExtra(UID)
        current_chat_id=getId(FRIEND_ID)
        send_msg.setOnClickListener {
            val string=message_send.text.toString()
            message_send.setText("")
            sendmessage(string, current_chat_id)
        }

        listenToMessages()
    }

    private fun sendmessage(string: String, id: String) {

        store.document(auth.currentUser?.uid.toString()).get().addOnSuccessListener {
           var user= it.toObject(User::class.java)!!
            Toast.makeText(this,"${user}",Toast.LENGTH_LONG).show()
            val sdf = SimpleDateFormat("HH:mm")
            val currentDate = sdf.format(Date())
            database.child("messages").child(id).push().setValue(Message(string,currentDate!!,System.currentTimeMillis().toString(), auth.currentUser?.uid.toString(),FRIEND_ID,false))
            database.child("Chat").child(auth.uid.toString()).child(FRIEND_ID).setValue(ChatList(FRIEND_ID, frient_name, image_url,string,1,-1*System.currentTimeMillis().toInt(),currentDate))
            database.child("Chat").child(FRIEND_ID).child(auth.uid.toString()).setValue(ChatList(auth.uid.toString(),user.name, user.photoUrl,string,1,-1*System.currentTimeMillis().toInt(),currentDate))

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


    private fun listenToMessages(){
        database.child("messages").child("${current_chat_id}").orderByKey()
            .addChildEventListener(object :ChildEventListener{
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    TODO("Not yet implemented")
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    TODO("Not yet implemented")
                }

                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val msg=snapshot.getValue(Message::class.java)!!
                    addMessage(msg)

                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    TODO("Not yet implemented")
                }

            })
    }

    private fun addMessage(msg: Message) {
        messageList.add(msg)
        madapter.notifyDataSetChanged()
    }
}


