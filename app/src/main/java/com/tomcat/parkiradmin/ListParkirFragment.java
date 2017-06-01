package com.tomcat.parkiradmin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tomcat.parkiradmin.Object.Parkir;

import java.util.ArrayList;
import java.util.HashMap;

import static android.os.Build.VERSION_CODES.M;

/**
 * Created by albertbrucelee on 17/05/17.
 */

public class ListParkirFragment extends Fragment {

    MapView mMapView;
    protected GoogleMap mMap;
    public Parkir parkir[];
    private LatLng userLatLng;
    public GPSTracker gps;
    HashMap<String,Marker> markers = new HashMap<>();
    public int indexTab;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();	//biar koneksi bisa dijalanin di main, karena aturannya koneksi gk boleh di Main langsung
        StrictMode.setThreadPolicy(policy);

        parkir = (Parkir[]) getArguments().getSerializable("Parkir");
        indexTab = getArguments().getInt("indexTab");

        gps = new GPSTracker(getActivity());
        if (gps.canGetLocation()) {
            userLatLng = new LatLng(gps.getLatitude(), gps.getLongitude());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list_parkir, container, false);

        setMapView(rootView, savedInstanceState);

        return rootView;
    }

    public void setMapView(View rootView, Bundle savedInstanceState){
        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;

                setUpMap();
                setUpParkir();
                /*
                // For showing a move to my location button
                mMap.setMyLocationEnabled(true);

                // For dropping a marker at a point on the Map
                LatLng sydney = new LatLng(-34, 151);
                mMap.addMarker(new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description"));

                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                */
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
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
        if(parkir!=null){
            LatLng coordinate[] = new LatLng[parkir.length];
            for(int i=0; i<coordinate.length; i++){
                coordinate[i] = new LatLng(parkir[i].getLatitude(), parkir[i].getLongitude());
                Marker marker = mMap.addMarker(new MarkerOptions().position(coordinate[i]).title(parkir[i].getName()));
                markers.put(String.valueOf(parkir[i].getId()),marker);
            }
            mMap.setOnInfoWindowClickListener(getInfoWindowClickListener());
        }
    }

    public GoogleMap.OnInfoWindowClickListener getInfoWindowClickListener()
    {
        return new GoogleMap.OnInfoWindowClickListener()
        {
            @Override
            public void onInfoWindowClick(Marker marker)
            {
                goDetail(marker.getId(),indexTab);
            }
        };
    }
    public void goDetail(String id, int indexTab){
        Intent intent = new Intent(getActivity(), EditParkirActivity.class);
        intent.putExtra("idParkir",parkir[Integer.parseInt(id.substring(1,id.length()))].getId());
        intent.putExtra("indexTab",indexTab);
        startActivityForResult(intent,1);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                boolean isSuccess = data.getBooleanExtra("isSuccess", false);
                int idParkir = data.getIntExtra("idParkir", 0);
                if(isSuccess){
                    Marker marker = markers.get(String.valueOf(idParkir));
                    marker.remove();
                    markers.remove(String.valueOf(idParkir));
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }
}