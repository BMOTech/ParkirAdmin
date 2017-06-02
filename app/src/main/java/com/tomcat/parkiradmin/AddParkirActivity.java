package com.tomcat.parkiradmin;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tomcat.parkiradmin.DB.DB;
import com.tomcat.parkiradmin.Object.Parkir;
import com.tomcat.parkiradmin.Object.User;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.tomcat.parkiradmin.R.id.inputCapacity;
import static com.tomcat.parkiradmin.R.id.inputPrice;
import static com.tomcat.parkiradmin.R.id.map;


public class AddParkirActivity extends AppCompatActivity implements OnMapReadyCallback{

    protected ProgressDialog pDialog;
    MapView mMapView;
    private GoogleMap mMap;
    private Marker marker;
    public GPSTracker gps;
    int PLACE_PICKER_REQUEST = 1;
    protected EditText textInputName;
    protected EditText textInputAddress;
    protected EditText textInputPrice;
    protected EditText textInputCapacity;

    protected Parkir parkir;
    protected int action=1;   //1=add, 2=list, 3=requested
    protected Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_parkir);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_new_parkir);

        parkir = new Parkir();

        textInputName = (EditText) findViewById(R.id.inputName);
        textInputAddress = (EditText) findViewById(R.id.inputAddress);
        textInputPrice = (EditText) findViewById(inputPrice);
        textInputCapacity = (EditText) findViewById(inputCapacity);

        gps = new GPSTracker(this);
        if (gps.canGetLocation()) {
            parkir.setLatitude(gps.getLatitude());
            parkir.setLongitude(gps.getLongitude());
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);


        Button btnPick = (Button)findViewById(R.id.btnPick);
        btnPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(AddParkirActivity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });
//        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
//        try {
//            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
//        } catch (GooglePlayServicesRepairableException e) {
//            e.printStackTrace();
//        } catch (GooglePlayServicesNotAvailableException e) {
//            e.printStackTrace();
//        }



        Button btnRequestParkir = (Button) findViewById(R.id.btnSubmit);
        btnRequestParkir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parkir.setName(textInputName.getText().toString());
                parkir.setAddress(textInputAddress.getText().toString());
                parkir.setPrice(textInputPrice.getText().toString());
                parkir.setCapacity(Integer.parseInt(textInputCapacity.getText().toString()));
                //new Submit(userLatLng.latitude,userLatLng.longitude,textInputName.getText().toString(),textInputAddress.getText().toString(),textInputPrice.getText().toString(),Integer.parseInt(textInputCapacity.getText().toString())).execute();
                Submit submit = new Submit();

                int corePoolSize = 60;
                int maximumPoolSize = 80;
                int keepAliveTime = 10;

                BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);
                Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);
                submit.executeOnExecutor(threadPoolExecutor);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class Submit extends AsyncTask<Integer, Integer, Integer> {
//        double lat; double lng; String name; String address; String price; int capacity;
//        Submit(double lat, double lng, String name, String address, String price, int capacity) {
//            this.lat = lat;
//            this.lng = lng;
//            this.name = name;
//            this.address = address;
//            this.price = price;
//            this.capacity = capacity;
//        }
        Submit(){
        }
        // ### Before starting background thread Show Progress Dialog ###
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AddParkirActivity.this);
            pDialog.setMessage("Submit...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

        }

        protected Integer doInBackground(Integer... args) {
            Log.d("addparkiraction2",""+action);
            DB db = new DB(getApplicationContext(),new User(getApplicationContext()));
            Log.d("addparkiraction2",""+action);
            int signal=1;
            switch (action){
                case 1:
                    signal = db.addParkir(parkir);
                    break;
                case 2:
                    signal = db.editParkir(parkir);
                    break;
                case 3:
                    signal = db.confirmRequestedParkir(parkir);
                    setReturnIntent(signal);
                    break;
            }

            return signal;
        }

        // ### After completing background task ###
        protected void onPostExecute(final Integer signal) {
            // dismiss the dialog once done
            //pDialog.dismiss();

            super.onPostExecute(signal);
            switch (signal) {
                case 0:
                    Toast.makeText(getApplicationContext(), getString(R.string.textSuccess), Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(getApplicationContext(), getString(R.string.textRequestFailed), Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(getApplicationContext(), getString(R.string.signal2), Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(getApplicationContext(), getString(R.string.signal3), Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    Toast.makeText(getApplicationContext(), getString(R.string.signal4), Toast.LENGTH_SHORT).show();
                    break;
            }
            finish();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setUpMarker();
        setUpMap();
    }

    @Override
    //result from pick location
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                //Log.i("Place ",""+place.getName()+ " "+place.getId()+ " " + place.getAddress()+ " "+place.getAttributions());
                LatLng userLatLng = place.getLatLng();
                textInputAddress.setText(place.getAddress());
                marker.setPosition(userLatLng);
                parkir.setLatitude(place.getLatLng().latitude);
                parkir.setLongitude(place.getLatLng().longitude);
            }
        }
    }

    public void setUpMap(){
        double currentLatitude=0;
        double currentLongitude=0;
        mMap.setMyLocationEnabled(true);
        //set map type
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setRotateGesturesEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        if (gps.canGetLocation()) {
            currentLatitude = gps.getLatitude();
            currentLongitude = gps.getLongitude();
        }
        Log.d("currentlatitude",""+currentLatitude);
        // Show the current location in Google Map
        mMap.moveCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
        // Zoom in the Google Map
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14));

    }

    public void setUpMarker(){
        LatLng coord = new LatLng(parkir.getLatitude(), parkir.getLongitude());
        marker = mMap.addMarker(new MarkerOptions().position(coord));
    }

    public void setReturnIntent(int signal){
        boolean isSuccess = false;
        if(signal==0)
            isSuccess = true;
        Intent returnIntent = new Intent();
        returnIntent.putExtra("idParkir",parkir.getId());
        returnIntent.putExtra("isSuccess",isSuccess);
        setResult(Activity.RESULT_OK, returnIntent);
    }
}
