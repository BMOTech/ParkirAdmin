package com.tomcat.parkiradmin.Object;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by albertbrucelee on 12/05/17.
 */

public class User {
    private String username;
    private String password;
    private LatLng latLng;
    private Context context;
    //GPSTracker gps;

    public User(Context context){
        this.context = context;
        getAuth();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    /*
    public void setLatLng(Context context){
        gps = new GPSTracker(context);
        if (gps.canGetLocation()) {
            latLng = new LatLng(gps.getLatitude(), gps.getLongitude());
        }
    }
    public LatLng getLatLng(){
        return latLng;
    }
    */

    public void setAuth(){
        SharedPreferences sharedPref= context.getSharedPreferences("auth", 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("id", username);
        editor.putString("password", password);
        editor.commit();
    }

    public void getAuth(){
        SharedPreferences sharedPref= context.getSharedPreferences("auth", 0);
        username = sharedPref.getString("id", "");
        password = sharedPref.getString("password", "");
        Log.d("auth_id",username);
        Log.d("auth_password",password);
    }
    public boolean deleteAuth(){
        SharedPreferences sharedPref= context.getSharedPreferences("auth", 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        if(editor.commit())
            return true;
        return false;
    }
}
