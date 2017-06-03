package com.tomcat.parkiradmin;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tomcat.parkiradmin.DB.DB;
import com.tomcat.parkiradmin.Object.Parkir;
import com.tomcat.parkiradmin.Object.User;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static android.R.attr.value;
import static com.tomcat.parkiradmin.R.id.inputCapacity;
import static com.tomcat.parkiradmin.R.id.inputPrice;
import static com.tomcat.parkiradmin.R.id.map;


public class EditParkirActivity extends AddParkirActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();

        if(bundle != null) {
            String parkirId = "" + bundle.getInt("idParkir");
            int indexTab = bundle.getInt("indexTab");

            Button btnSubmit = (Button) findViewById(R.id.btnSubmit);
            if (indexTab == 1) {
                action = 3;
                getSupportActionBar().setTitle(R.string.title_filter_parkir);
                btnSubmit.setText(R.string.btnConfirm);
                getDetailRequestedParkir(parkirId);
            } else {
                action = 2;
                getSupportActionBar().setTitle(R.string.title_edit_parkir);
                btnSubmit.setText(R.string.btnSave);
                getDetailParkir(parkirId);
            }
        }

        setView();
    }


    public void setView(){
        textInputName.setText(parkir.getName());
        textInputAddress.setText(parkir.getAddress());
        textInputPrice.setText(parkir.getPrice());
        textInputCapacity.setText(String.valueOf(parkir.getCapacity()));
    }

    public void getDetailRequestedParkir(String parkirId){
        DB db = new DB(this, new User(this));
        parkir = db.getDetailRequestedParkir(parkirId);
    }
    public void getDetailParkir(String parkirId){
        DB db = new DB(this, new User(this));
        parkir = db.getDetailParkir(parkirId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_parkir, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_delete_parkir:
                delete();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void delete(){
        Delete delete = new Delete();

        int corePoolSize = 60;
        int maximumPoolSize = 80;
        int keepAliveTime = 10;

        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);
        Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);
        delete.executeOnExecutor(threadPoolExecutor);
    }
    class Delete extends AsyncTask<Integer, Integer, Integer> {
        //        double lat; double lng; String name; String address; String price; int capacity;
//        Submit(double lat, double lng, String name, String address, String price, int capacity) {
//            this.lat = lat;
//            this.lng = lng;
//            this.name = name;
//            this.address = address;
//            this.price = price;
//            this.capacity = capacity;
//        }
        // ### Before starting background thread Show Progress Dialog ###
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EditParkirActivity.this);
            pDialog.setMessage("Delete...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

        }

        protected Integer doInBackground(Integer... args) {
            Log.d("addparkiraction2",""+action);
            DB db = new DB(getApplicationContext(),new User(getApplicationContext()));
            int signal=1;
            switch (action){
                case 2:
                    signal = db.deleteParkir(parkir.getId());
                    setReturnIntent(signal);
                    break;
                case 3:
                    signal = db.deleteRequestedParkir(parkir.getId());
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
}
