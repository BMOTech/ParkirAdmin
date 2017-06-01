package com.tomcat.parkiradmin;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
//import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tomcat.parkiradmin.DB.DB;
import com.tomcat.parkiradmin.Object.Parkir;
import com.tomcat.parkiradmin.Object.User;
//import gms.drive.*;

public class zbackup_ListParkirActivity extends AppCompatActivity  implements OnMapReadyCallback {
    private GoogleMap mMap;
    public Parkir parkir[];
    private GoogleApiClient mGoogleApiClient;
    private LatLng userLatLng;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zbackup_activity_listparkir);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();	//biar koneksi bisa dijalanin di main, karena aturannya koneksi gk boleh di Main langsung
        StrictMode.setThreadPolicy(policy);

        getListParkir();
        User user = new User(this);


//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .enableAutoManage(this /* FragmentActivity */,
//                        this /* OnConnectionFailedListener */)
//                .addApi(Drive.API)
//                .addScope(Drive.SCOPE_FILE)
//                .build();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setUpMap();
        setUpParkir();
    }


    public void getListParkir(){
        DB db = new DB(this, new User(this));
        parkir = db.getListParkir();
    }

    public void setUpMap(){
        mMap.setMyLocationEnabled(true);
        //set map type
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setRotateGesturesEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        // Show the current location in Google Map
        mMap.moveCamera(CameraUpdateFactory.newLatLng(userLatLng));
        // Zoom in the Google Map
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14));
    }
    public void setUpParkir(){
        LatLng coordinate[] = new LatLng[parkir.length];
        for(int i=0; i<coordinate.length; i++){
            coordinate[i] = new LatLng(parkir[i].getLatitude(), parkir[i].getLongitude());
            mMap.addMarker(new MarkerOptions().position(coordinate[i]).title(parkir[i].getName()));

        }
        mMap.setOnInfoWindowClickListener(getInfoWindowClickListener());
    }

    public GoogleMap.OnInfoWindowClickListener getInfoWindowClickListener()
    {
        return new GoogleMap.OnInfoWindowClickListener()
        {
            @Override
            public void onInfoWindowClick(Marker marker)
            {

                goDetail(marker.getId());
            }
        };
    }
    public void goDetail(String id){
        Intent intent = new Intent(getApplicationContext(), DetailParkirActivity.class);
        intent.putExtra("parkir_id",parkir[Integer.parseInt(id.substring(1,id.length()))].getId());
        startActivity(intent);
    }
}
