import com.example.sharemliy.R


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sharemliy.ChatList
import com.example.sharemliy.Message
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item.view.*

class chatAdapter(private val userList: ArrayList<ChatList>) :RecyclerView.Adapter<chatAdapter.myViewHolder_chat>() {


    var onItemClick: ((name:String,uid:String,photoUrl:String) -> Unit)? = null;


    inner class myViewHolder_chat(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    fun setAll(list: ArrayList<ChatList>) {
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
            itemView.rv_status.text = userList[position].lastMessage
            itemView.rv_date.text=userList[position].date
            itemView.message_count.visibility=View.INVISIBLE
            if(userList[position].messagecount>0)
            {   itemView.message_count.visibility=View.VISIBLE
                itemView.message_count.text=userList[position].messagecount.toString()
            }
            Picasso.get().load(userList[position].photoUrl).placeholder(R.drawable.user)
                .error(R.drawable.user).into(itemView.rv_image)
            itemView.setOnClickListener {
                onItemClick?.invoke( userList[position].name, userList[position].uid, userList[position].photoUrl)
            }
        }


    }
}

