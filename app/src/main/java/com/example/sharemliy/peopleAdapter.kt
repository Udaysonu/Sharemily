package com.example.sharemliy



import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item.view.*

class peopleAdapter(val userList: ArrayList<ChatList>) :RecyclerView.Adapter<peopleAdapter.myViewHolder>() {


    class myViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(user:User,onclick:(name:String,photoUrl:String,id:String)->Unit)=with(itemView){
            itemView.rv_name.text=user.name
            itemView.rv_status.text=user.status
            itemView.rv_date.visibility=View.INVISIBLE
            Picasso.get().load(user.photoUrl).placeholder(R.drawable.user).error(R.drawable.user).into(itemView.rv_image)
            setOnClickListener {
                onclick.invoke(user.name,user.photoUrl,user.auth_id)
            }
        }
        fun avoid(user:User)=with(itemView){
           itemView.visibility=View.INVISIBLE
            itemView.layoutParams.height=0
        }

    }


    fun setAll(list: ArrayList<ChatList>)
    {
        userList.clear()
        userList.addAll(list)
        notifyDataSetChanged()
    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false) as View
        return myViewHolder(itemView)
    }


    override fun getItemCount(): Int {
        return userList.size
    }



    override fun onBindViewHolder(holder: myViewHolder, position: Int) {

    }


}





