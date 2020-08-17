package com.example.sharemliy



import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item.view.*

class peopleAdapter(val userList: Array<User>) :RecyclerView.Adapter<peopleAdapter.myViewHolder>() {

//    internal lateinit var userList:MutableList<User>
//    init{
//        this.userList=ArrayList()
//    }


    var onItemClick:((login:String)->Unit)?=null


    class myViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(user:User)=with(itemView){
            itemView.rv_name.text=user.name
            itemView.rv_status.text=user.status
        }
    }



    fun addAll(data: ArrayList<User>)
    {
//        userList.()
//        userList.addAll(data)
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


        holder.itemView.setOnClickListener {  }
    }


}





