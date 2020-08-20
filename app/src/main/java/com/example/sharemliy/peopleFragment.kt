package com.example.sharemliy

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.paging.FirestorePagingAdapter
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirestoreRegistrar
import kotlinx.android.synthetic.main.fragment_chats.*
import kotlinx.android.synthetic.main.fragment_people.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [peopleFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class peopleFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var myadapter:FirestorePagingAdapter<User,peopleAdapter.myViewHolder>
    val store by lazy{
        FirebaseFirestore.getInstance().collection("User")
    }
    val auth by lazy{
        FirebaseAuth.getInstance()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setupAdapter()
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

    recycler_view.apply{
        layoutManager=LinearLayoutManager(requireContext())
        adapter=myadapter
    }

    }

    private fun setupAdapter() {
        val config=PagedList.Config.Builder()
            .setPageSize(8)
            .setPrefetchDistance(2)
            .setEnablePlaceholders(false)
            .build()
        val options=FirestorePagingOptions.Builder<User>()
            .setLifecycleOwner(this)
            .setQuery(store,config,User::class.java)
            .build()
        myadapter=object:FirestorePagingAdapter<User,peopleAdapter.myViewHolder>(options){
            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): peopleAdapter.myViewHolder {

                val view=layoutInflater.inflate(R.layout.list_item,parent,false)
                return peopleAdapter.myViewHolder(view)
             }

            override fun onBindViewHolder(
                holder: peopleAdapter.myViewHolder,
                position: Int,
                model: User
            ) {
               if(model.auth_id==auth.uid){
                   holder.avoid(user=model)
               }
                else{
                   holder.bind(user=model){ name: String, photoUrl: String, auth_id: String ->
                       val intent= Intent(requireContext(),ChatActivity::class.java)
                       intent.putExtra(UID,auth_id)
                       intent.putExtra(IMAGE,photoUrl)
                       intent.putExtra(NAME,name)
                       startActivity(intent)
                   }
               }


             }

        }
     }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_people, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment peopleFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            peopleFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}