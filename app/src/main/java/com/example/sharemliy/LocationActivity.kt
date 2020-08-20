package com.example.sharemliy

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase


class LocationActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    val database by lazy{
        FirebaseDatabase.getInstance().getReference()
    }
    lateinit var friend_name:String
    lateinit var friend_id:String
    lateinit var friend_photo:String
      var marker: Marker? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        friend_name=intent.getStringExtra("NAME")
        friend_id=intent.getStringExtra("FRIEND_ID")
        friend_photo=intent.getStringExtra("PHOTO_URL")
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera


        var oldline: LatLng? =null
        var newline:LatLng

        database.child("Location").child(friend_id).addChildEventListener(object:ChildEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {

                val loc=snapshot.getValue(LocationDetails::class.java)!!
                if(oldline==null)
                {
                    oldline= LatLng(loc.latlng.latitude,loc.latlng.longitude)
                }

                newline=LatLng(loc.latlng.latitude,loc.latlng.longitude)

                marker?.remove()

                mMap.addPolyline(PolylineOptions().add(oldline,newline).color(Color.GREEN)).width=2f
               marker= mMap.addMarker(MarkerOptions().position(newline).title(loc.time))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newline,15f))
                oldline=newline
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

        })
    }



}