package com.example.sharemliy

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_user_details.*

class UserDetails : AppCompatActivity() {
    var downloadUrl:String?=null

    val Storage by lazy{
        FirebaseStorage.getInstance()
    }
    val auth by lazy{
        FirebaseAuth.getInstance()
    }
    val myRef by lazy{
        FirebaseFirestore.getInstance().collection("User")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_details)
        profile_image.setOnClickListener {
            val intent=Intent(Intent.ACTION_PICK)
            intent.type="image/*"
            startActivityForResult(intent,1000)
        }
        profile_button.setOnClickListener{
            if(profile_name.text.isNullOrEmpty() or profile_status.text.isNullOrEmpty())
            {
                Toast.makeText(this,"Text Fields Should not be empty",Toast.LENGTH_LONG).show()
            }
            else
            {
                if(downloadUrl==null)
                {
                    Toast.makeText(this,"Profile Picture cannot be empty",Toast.LENGTH_LONG).show()
                }
                else
                {   profile_button.text="Please Wait"
                    profile_button.isEnabled=false
                    myRef.document(auth.uid.toString()).set(User(auth.uid.toString(),profile_name.text.toString(),auth.currentUser?.phoneNumber.toString(),downloadUrl.toString(),profile_status.text.toString(),false)).addOnCompleteListener {
                        startActivity(Intent(this,TabActivity::class.java))
                        finish()
                    }

                }
            }

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode==Activity.RESULT_OK)
        {
           data?.data?.let{
               profile_image.setImageURI(it)
               uploadImage(it)
           }
        }
        else{
            Toast.makeText(this,"Uploading Failed",Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadImage(it: Uri) {
        profile_button.text="Please Wait"
        profile_button.isEnabled=false
        val ref=Storage.reference.child("uploads/"+auth.uid.toString())
        val uploadTask=ref.putFile(it)
        val urlTask=uploadTask.continueWithTask{
            if(!it.isSuccessful)
            {
                it.exception?.let{
                    throw it
                }
            }
            ref.downloadUrl
        }.addOnCompleteListener {
            profile_button.text="Next"
            profile_button.isEnabled=true
            if(it.isSuccessful){
                downloadUrl=it.result.toString()
                Toast.makeText(this,"Profile Picture uploaded",Toast.LENGTH_SHORT).show()
            }
        }
            .addOnCanceledListener {
                Toast.makeText(this,"Profile Picture uploaded failed",Toast.LENGTH_SHORT).show()
            }
    }
}