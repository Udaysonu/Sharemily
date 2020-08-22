package com.example.sharemliy

import android.util.Log
import com.example.sharemliy.R


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.sharemliy.ChatList
import com.example.sharemliy.Message
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item.view.*

class familyAdapter(private val userList: ArrayList<User>) :RecyclerView.Adapter<familyAdapter.myViewHolder_chat>() {


    var onItemClick: ((name:String,uid:String,photoUrl:String) -> Unit)? = null;


    inner class myViewHolder_chat(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    fun setAll(list: ArrayList<User>) {
        userList.clear()
        userList.addAll(list)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder_chat {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false) as View


        return myViewHolder_chat(itemView)
    }


    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: myViewHolder_chat, position: Int) {

        with(holder) {
            itemView.rv_name.text = userList[position].name
            itemView.rv_status.text = userList[position].status

            itemView.message_count_box.visibility=View.INVISIBLE


            Picasso.get().load(userList[position].photoUrl).placeholder(R.drawable.default_image)
                .error(R.drawable.default_image).into(itemView.rv_image)
            itemView.setOnClickListener {
                onItemClick?.invoke( userList[position].name, userList[position].auth_id, userList[position].photoUrl)
            }
        }


    }
}

