package com.example.sharemliy

import android.app.Activity
import com.example.sharemliy.R


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.sharemliy.ChatList
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item.view.*
import kotlinx.android.synthetic.main.list_item_chat_recv_message.view.*

class MessagesAdapter(private val list: MutableList<Message>,private val mCurrentUid:String) :RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val inflate={layout:Int->
            LayoutInflater.from(parent.context).inflate(layout,parent,false)
        }


        return when(viewType){
             TEXT_MESSAGE_RECEIVED->{
                        MessageViewHolder(inflate(R.layout.list_item_chat_recv_message))
             }
             else->{
                 MessageViewHolder(inflate(R.layout.list_item_chat_sent_message))

             }

         }
    }

    override fun getItemCount(): Int {
         return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(val item=list[position])
        {
            is Message->{
                holder.itemView.apply{
                     content.text=item.content
                      msg_time.text=item.date.toString()
                }
            }
        }
     }

    override fun getItemViewType(position: Int): Int {
        return  when(val event=list[position])
        {
            is Message->{
                if(event.sender==mCurrentUid)
                {
                    TEXT_MESSAGE_SENT
                }
                else{
                    TEXT_MESSAGE_RECEIVED
                }
            }
            else-> UNSUPPORT
        }
    }

    class DateViewHolder(view:View):RecyclerView.ViewHolder(view){}

    class MessageViewHolder(view:View):RecyclerView.ViewHolder(view){}


    companion object{
        fun count(): Int {
           return  messageList.size

        }

        private const val UNSUPPORT=-1
        private const val TEXT_MESSAGE_RECEIVED=0
        private const val TEXT_MESSAGE_SENT=1
    }


}

