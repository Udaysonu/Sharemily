package com.example.sharemliy

import android.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_search_family.*
import kotlinx.android.synthetic.main.list_item.view.*

class searchFamily : AppCompatActivity() {
    val mobRef by lazy{
        FirebaseFirestore.getInstance().collection("Mobile")
    }
    val database by lazy{
        FirebaseDatabase.getInstance()
    }
    val auth by lazy{
        FirebaseAuth.getInstance()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_family)

        search_btn.setOnClickListener{
            search_cardview.visibility=View.INVISIBLE
            search_not_found.visibility= View.INVISIBLE


            if(edit_search.text.isNullOrEmpty()){
               Toast.makeText(this,"Please Enter A valid Number",Toast.LENGTH_SHORT).show()
           }
           else
           {
               search_btn.text="Please wait"
               search_btn.isEnabled=false

               val param="+91"+edit_search.text
            mobRef.document(param).get().addOnSuccessListener {
                search_btn.text="Search User"
                search_btn.isEnabled=true

                if(!it.exists())
                {
                    Toast.makeText(this,"User Not Found",Toast.LENGTH_LONG).show()
                    search_not_found.visibility= View.VISIBLE
                }
                else{
                    Toast.makeText(this,"User Found",Toast.LENGTH_LONG).show()

                    val document=it.toObject(User::class.java)!!

                   if(document.auth_id!=auth.uid.toString())
                   {
                       search_cardview.visibility=View.VISIBLE
                       search_name.text=document.name.toString()
                       search_status.text=document.status
                       Picasso.get().load(document.photoUrl).placeholder(R.drawable.default_image)
                           .error(R.drawable.default_image).into(search_image)
                       send_request.setOnClickListener{
                           search_btn.text="Please Wait"
                           send_request.text="Please Wait"
                           search_btn.isEnabled=false
                           send_request.isEnabled=false
                           database.reference.child("Friend").child(auth.uid.toString()).push().setValue(document)
                           mobRef.document(auth.currentUser?.phoneNumber.toString()).get().addOnSuccessListener {
                               val currUser=it.toObject(User::class.java)
                               database.reference.child("Friend").child(document.auth_id).push().setValue(currUser).addOnCompleteListener {
                                   Toast.makeText(this,"User Added to Family",Toast.LENGTH_LONG).show()
                                   finish()
                               }
                           }

                       }
                   }
                    else
                   {
                       search_cardview.visibility=View.VISIBLE
                       search_name.text=document.name.toString()
                       search_status.text=document.status
                       Picasso.get().load(document.photoUrl).placeholder(R.drawable.default_image)
                           .error(R.drawable.default_image).into(search_image)
                     send_request.text="This Is You"
                       send_request.isEnabled=false
                   }

                }

            }
           }
        }

    }
}