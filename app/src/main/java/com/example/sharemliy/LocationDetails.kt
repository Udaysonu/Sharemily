package com.example.sharemliy

import com.google.android.gms.maps.model.LatLng
data class Latlong(val latitude:Double,val longitude:Double)
{


    constructor():this(0.00,0.00)
}
data class LocationDetails(val latlng:Latlong,val epoctime:Int,val time:String,val date:String)
{
    constructor():this( Latlong(0.00,1.11),0,"sa","dat")
}