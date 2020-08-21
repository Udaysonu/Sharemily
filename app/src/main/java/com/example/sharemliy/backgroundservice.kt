package com.example.sharemliy

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.location.Criteria
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.*
import java.lang.Runnable
import java.text.SimpleDateFormat

class backgroundservice: Service() {

    val database by lazy { FirebaseDatabase.getInstance().getReference() }
    val auth by lazy { FirebaseAuth.getInstance() }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {


        val runnable= Runnable {
            while (true)
            {
                Log.d("checkrunning","running")
                Thread.sleep(100000)
                setUpLocationListener()
            }
        }

        val channel=NotificationChannel("channel1","bestchannel",NotificationManager.IMPORTANCE_DEFAULT)
        val manager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)

        val notification=NotificationCompat.Builder(this,"channel1")
            .setContentTitle("Example Service")
            .setContentText("welcome")
            .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
            .build()
        startForeground(1,notification)

        Thread(runnable).start()
        return START_STICKY
    }



    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }



    @SuppressLint("MissingPermission")
    private fun setUpLocationListener() {
        val lm=getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val providers = lm.getProviders(true)
        val criteria= Criteria()
        var l: Location?=null
        for( i in providers.indices.reversed())
        {

            l=lm.getLastKnownLocation(providers[i])
            if(l!=null)
            { l.let{
                val sdf= SimpleDateFormat("dd/MM/yy")
                val stf= SimpleDateFormat("HH:mm")
                val epoctime=System.currentTimeMillis()
                val date=sdf.format(epoctime)
                val time=stf.format(epoctime)
                database.child("Location").child(auth.uid.toString()).setValue(LocationDetails(
                    Latlong(it.latitude,it.longitude),epoctime.toInt(),time,date))
            }
                break;

            }
        }
    }




}