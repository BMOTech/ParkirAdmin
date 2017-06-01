package com.tomcat.parkiradmin;

import android.os.Bundle;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.Window;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);     //menghilangkan heading title
        setContentView(R.layout.activity_main);

    }

    public void ClickDaftar(View V){
        Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(i);
    }

    public void ClickLogin(View V){
        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(i);
    }


}
