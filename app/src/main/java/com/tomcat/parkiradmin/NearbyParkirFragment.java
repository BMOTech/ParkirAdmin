package com.tomcat.parkiradmin;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

/**
 * Created by albertbrucelee on 17/05/17.
 */

public class NearbyParkirFragment extends ListParkirFragment {

    public NearbyParkirFragment(){

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                boolean isParkirSaved = data.getBooleanExtra("isParkirSaved", false);
                Log.d("listFragment bool",String.valueOf(isParkirSaved));
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }
}