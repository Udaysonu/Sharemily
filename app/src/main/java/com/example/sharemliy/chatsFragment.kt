package com.example.sharemliy

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import chatAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.fragment_chats.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [chatsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class chatsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    val mRef by lazy{
        FirebaseDatabase.getInstance().getReference()
            .child("Chat").child(auth.uid.toString())

    }

    val auth by lazy{
        FirebaseAuth.getInstance()
    }


    lateinit var adapter:chatAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }



    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter= chatAdapter(ArrayList())
        adapter.onItemClick={name,uid,photoUrl->
            val intent= Intent(this.requireContext(),ChatActivity::class.java)
            intent.putExtra(NAME,name)
            intent.putExtra(UID,uid)
            intent.putExtra(IMAGE,photoUrl)
            startActivity(intent)
        }
        rv_chat.adapter=adapter
        rv_chat.layoutManager=LinearLayoutManager(this.context)

        mRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                Toast.makeText(context,"changed",Toast.LENGTH_LONG).show()
                val userList =ArrayList<ChatList>()
                 for(i in snapshot.children){

                                    userList.add(i.getValue(ChatList::class.java)!!)
                 }
                adapter.setAll(userList)
                adapter.notifyDataSetChanged()

            }

        })

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_chats, container, false)
    }









    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment chatsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            chatsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}