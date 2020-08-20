package com.example.sharemliy

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.google.android.material.tabs.TabLayout
import com.google.firebase.database.FirebaseDatabase
import com.google.type.LatLng
import kotlinx.android.synthetic.main.activity_tab.*
import java.text.SimpleDateFormat

class TabActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tab)

        val database by lazy{
            FirebaseDatabase.getInstance().getReference()
        }
        // setting the default fragment in display

        val fragmentManager=supportFragmentManager
        val fragmentTransaction=fragmentManager.beginTransaction()
        val chatfragment=chatsFragment()
        fragmentTransaction.add(R.id.fragment_layout, chatfragment)
        fragmentTransaction.commit()



        //Handling tab change events
        tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab?) {

                //test toast
                Toast.makeText(baseContext,"${tab?.position}",Toast.LENGTH_LONG).show()

                //checking the position of the tab clicked and displaying corresponding fragments
                if(tab?.position==0)
                {
                        val fragmentManager=supportFragmentManager
                        val fragmentTransaction=fragmentManager.beginTransaction()
                        val chatfragment=chatsFragment()
                    fragmentTransaction.replace(R.id.fragment_layout, chatfragment)
                    fragmentTransaction.addToBackStack(null)
                    fragmentTransaction.commit()
                }
                else
                {
                    val newFragment = peopleFragment()
                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.fragment_layout, newFragment)
                    transaction.addToBackStack(null)
                    transaction.commit()

                }
                // Handle tab select
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // Handle tab reselect
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // Handle tab unselect
            }
        })

    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onStart() {
        super.onStart()
        when{
            isFineLocationGranted()->{setUpLocationListener()}
        else->{requestAccessFineLocation()}
        }



}

    @RequiresApi(Build.VERSION_CODES.M)
    private fun requestAccessFineLocation() {
         requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),1008)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode)
        {
            1008-> {
                if(grantResults.isNotEmpty() && isFineLocationGranted()){
                    setUpLocationListener()
                }
            }
        }
    }
    @SuppressLint("MissingPermission")
    private fun setUpLocationListener() {
        statusCheck()
        val lm=getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val providers = lm.getProviders(true)
        var l:Location?=null
        for( i in providers.indices.reversed())
        {

            l=lm.getLastKnownLocation(providers[i])
            if(l!=null)
            {
                l.let{
                    val sdf=SimpleDateFormat("dd/MM/yy")
                    val stf=SimpleDateFormat("HH:mm")
                    val epoctime=System.currentTimeMillis()
                    val date=sdf.format(epoctime)
                    val time=stf.format(epoctime)
                    database.child("Location").child(auth.uid.toString()).push().setValue(LocationDetails(
                        Latlong(it.latitude,it.longitude),epoctime.toInt(),time,date))
                }
                break;
            }
        }
    }

    fun statusCheck() {
        val manager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps()
        }
    }

    private fun buildAlertMessageNoGps() {
        val builder =
            AlertDialog.Builder(this)
        builder.setMessage("Your GPS seems to be disabled, Please enable it so that our app can show your location to your FAMILY? Do you want to enable it")
            .setCancelable(false)
            .setPositiveButton(
                "Yes"
            ) { dialog, id -> startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)) }
            .setNegativeButton(
                "No"
            ) { dialog, id -> dialog.cancel() }
        val alert = builder.create()
        alert.show()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun isFineLocationGranted() :Boolean{
        return checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED
    }

}



