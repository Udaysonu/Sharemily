package com.example.sharemliy

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import chatAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_chats.*
import kotlinx.android.synthetic.main.fragment_family.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FamilyFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FamilyFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    val mRef by lazy{
        FirebaseDatabase.getInstance().getReference()
            .child("Friend").child(auth.uid.toString())
    }

    val auth by lazy{
        FirebaseAuth.getInstance()
    }
    val database by lazy{
        FirebaseDatabase.getInstance().getReference()
    }


    lateinit var adapter:familyAdapter


    override fun onAttach(context: Context) {
        super.onAttach(context)
        adapter= familyAdapter(ArrayList())
        adapter.onItemClick={name,uid,photoUrl->
            val intent= Intent(this.requireContext(),ChatActivity::class.java)
            intent.putExtra(NAME,name)
            intent.putExtra(UID,uid)
            intent.putExtra(IMAGE,photoUrl)
            startActivity(intent)
        }
        mRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val userList =ArrayList<User>()
                for(i in snapshot.children){

                    userList.add(i.getValue(User::class.java)!!)
                }
                adapter.setAll(userList)
                adapter.notifyDataSetChanged()

            }

        })
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_fam.adapter=adapter
        rv_fam.layoutManager= LinearLayoutManager(this.context)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_family, container, false)
    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FamilyFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FamilyFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}