package com.servissoft.holebook.utils;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.TextView;

/**
 * Created by raulandrez on 21/09/15.
 */
public class GpsHoleBook extends Service implements LocationListener

    {

        private final Context ctx;
        private int i;
        private TextView texto;
        public double longitud;
        public double latitud;
        public double alatitud;
        public double velocidad;
        public long fecha;
        private Location location;
        private LocationManager locationManager;

        public GpsHoleBook(Context ctx, TextView v) {
        super();
        this.ctx = ctx;

        texto = (TextView) v;

    }

        public GpsHoleBook() {
        super();
        this.ctx = this.getApplicationContext();

    }

    public void setTextView() {
        try {
            location = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
            longitud = location.getLongitude();
            latitud = location.getLatitude();
            alatitud = location.getAltitude();
            fecha = location.getTime();
            velocidad=location.getSpeed();

         //   texto.setText("Laurita  Te Quiero Mucho\n longitud: " + longitud + "\n    latitud:" + latitud + "\naltitud:" + alatitud+"\n    fecha:"+fecha);
        } catch (Exception e) {
            texto.setText("error: gpshuecos.setTextView():"+e.getMessage());

        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {
        setTextView();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    public void getLocation() {
        try {
            locationManager = (LocationManager) this.ctx.getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER , 1 , 1 , this);

        } catch (Exception e) {
            texto.setText("error:  gpshuecos.getLocation():"+e.getMessage());

        }

     /*   if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            texto.setText("error:  getLocation()->locationManager PERMISOS");
            return;
        }

*/
        //


    }
    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
