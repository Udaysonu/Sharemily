package com.example.sharemliy

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import chatAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.protobuf.MessageLite
import com.squareup.picasso.Picasso
import com.vanniktech.emoji.EmojiManager
import com.vanniktech.emoji.google.GoogleEmojiProvider
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.fragment_people.*
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList



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
    lateinit var childListener:ChildEventListener
    lateinit var valueListener:ValueEventListener
    val auth by lazy{
        FirebaseAuth.getInstance()
    }
    val database by lazy{
        FirebaseDatabase.getInstance().getReference()
    }
    val store by lazy{
        FirebaseFirestore.getInstance().collection("User")
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EmojiManager.install(GoogleEmojiProvider())
        setContentView(R.layout.activity_chat)

        messageList= ArrayList()

        madapter= MessagesAdapter(messageList,auth.uid.toString())


        chat_rv.adapter= madapter

        chat_rv.layoutManager= LinearLayoutManager(this)
        chat_rv.scrollToPosition(MessagesAdapter.count()-1)






        image_url=intent.getStringExtra(IMAGE)
        Picasso.get().load(image_url).into(chat_image)

        frient_name=intent.getStringExtra(NAME)

        chat_name.text= frient_name
        FRIEND_ID=intent.getStringExtra(UID)
        current_chat_id=getId(FRIEND_ID)

        send_msg.setOnClickListener {
            val string=message_send.text.toString()
          if(!string.isNullOrEmpty())
          {
              message_send.setText("")
              sendmessage(string, current_chat_id)
          }

        }

        listenToMessages()
        LocationClickListener()


    }

    private fun LocationClickListener() {
        getLocation.setOnClickListener { val intent=Intent(this,LocationActivity::class.java)
            intent.putExtra("NAME", frient_name)
            intent.putExtra("FRIEND_ID", FRIEND_ID)
            intent.putExtra("PHOTO_URL", image_url)
            startActivity(intent) }
    }

    private fun sendmessage(string: String, id: String) {

        store.document(auth.uid.toString()).get().addOnSuccessListener {
           var user= it.toObject(User::class.java)!!
            val sdf = SimpleDateFormat("HH:mm")
            val currentDate = sdf.format(Date())

            val timeinMillis=System.currentTimeMillis()
            database.child("messages").child(id).push().setValue(Message(string,currentDate!!,timeinMillis.toString(), auth.currentUser?.uid.toString(),FRIEND_ID,false))
            sendMessageNotification(id,string,currentDate,timeinMillis.toInt(),user)
            database.child("Chat").child(auth.uid.toString()).child(FRIEND_ID).setValue(ChatList(
                FRIEND_ID, frient_name, image_url,string,0,-1*timeinMillis.toInt(),currentDate))

        }.addOnFailureListener {
            Toast.makeText(this,"failed",Toast.LENGTH_LONG).show()

        }

    }

    private fun sendMessageNotification(
        id: String,
        string: String,
        currentDate: String,
        timeinMillis: Int,
        user:User
    ) {

       database.child("Chat").child(FRIEND_ID).child(auth.uid.toString()).addListenerForSingleValueEvent(object:
            ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val user=snapshot.getValue(ChatList::class.java)
                var count:Int
                if(user==null){
                    count=1
                }
                else
                {
                    count=user.messagecount+1
                }
                database.child("Chat").child(FRIEND_ID).child(auth.uid.toString()).setValue(ChatList(user?.uid.toString(),user?.name.toString() , user?.photoUrl.toString(),string, count,-1*timeinMillis,currentDate))
            }

        })

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
        childListener=database.child("messages").child("${current_chat_id}").orderByKey()
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
                    setUpLocationListener()

                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    TODO("Not yet implemented")
                }

            })
    }

    private fun addMessage(msg: Message) {
        messageList.add(msg)
        madapter.notifyDataSetChanged()
        chat_rv.scrollToPosition(MessagesAdapter.count()-1)
    }


    @SuppressLint("MissingPermission")
    private fun setUpLocationListener() {

        val lm=getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val providers = lm.getProviders(true)
        var l: Location?=null
        for( i in providers.indices.reversed())
        {

            l=lm.getLastKnownLocation(providers[i])
            if(l!=null)
            {
                l.let{
                    val sdf=SimpleDateFormat("dd/MM/yy")
                    val stf=SimpleDateFormat("HH:mm")
                    val epoctime=System.currentTimeMillis()
                    val date=sdf.format(epoctime)
                    val time=stf.format(epoctime)
                    database.child("Location").child(auth.uid.toString()).setValue(LocationDetails(
                        Latlong(it.latitude,it.longitude),epoctime.toInt(),time,date))
                }
                break;

            }
        }
    }

    override fun onBackPressed() {

        super.onBackPressed()
        database.child("messages").child("${current_chat_id}").removeEventListener(childListener)

        this.finish()

    }





}


