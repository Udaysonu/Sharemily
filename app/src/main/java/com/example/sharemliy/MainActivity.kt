package com.example.sharemliy

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.AuthUI.IdpConfig.*
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*


class MainActivity : AppCompatActivity() {
    val auth = FirebaseAuth.getInstance()
    val RC_SIGN_IN=121
    val mRef by lazy{
        FirebaseFirestore.getInstance().collection("User")
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//
//checking if the user is signed in or not
//        if the user is not signed in ask him to sign in

        if (auth.uid != null ) {
            // already signed in
            startActivity(Intent(this,TabActivity::class.java))
            finish()
        } else {
            // not signed in
                     // Get an instance of AuthUI based on the default app
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(Arrays.asList(


                                     PhoneBuilder().build()))
                            .build(),
                    RC_SIGN_IN)

        }


    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            // Successfully signed in
            if (resultCode == Activity.RESULT_OK) {
                SignedIn()

            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    Toast.makeText(this,"Unknown Error",Toast.LENGTH_LONG).show()
                    return
                }
                if (response.error!!.errorCode == ErrorCodes.NO_NETWORK) {
                    Toast.makeText(this,"Check Your Internet Connection",Toast.LENGTH_LONG).show()
//                    showSnackbar(R.string.no_internet_connection)
                    return
                }
//                showSnackbar(R.string.unknown_error)
//                Log.e(FragmentActivity.TAG, "Sign-in error: ", response.error)
            }
        }
    }


    private fun SignedIn() {
        startActivity(Intent(this,UserDetails::class.java))
        finish()
    }
}