package com.tomcat.parkiradmin;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.tomcat.parkiradmin.DB.DB;
import com.tomcat.parkiradmin.Object.Parkir;
import com.tomcat.parkiradmin.Object.User;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Parkir parkir[];
    private Parkir parkir2[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();	//biar koneksi bisa dijalanin di main, karena aturannya koneksi gk boleh di Main langsung
        StrictMode.setThreadPolicy(policy);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new RequestedParkirFragment(), "Requested");
        adapter.addFragment(new NearbyParkirFragment(), "List Parkir");
        //adapter.addFragment(new ThreeFragment(), "THREE");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    Bundle bundle = new Bundle();
                    parkir = getListRequestedParkir();
                    bundle.putSerializable("Parkir", parkir);
                    bundle.putInt("indexTab",1);
                    mFragmentList.get(position).setArguments(bundle);
                    break;
                case 1:
                    Bundle bundle2 = new Bundle();
                    parkir2 = getListParkir();
                    bundle2.putSerializable("Parkir", parkir2);
                    bundle2.putInt("indexTab",2);
                    mFragmentList.get(position).setArguments(bundle2);
                    break;
                default:
                    break;
            }
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Intent i;
        switch (item.getItemId()) {
            case R.id.action_settings:
                i = new Intent(getApplicationContext(), PreferenceActivity.class);
                startActivity(i);
                return true;
            case R.id.action_add_parkir:
                i = new Intent(getApplicationContext(), AddParkirActivity.class);
                startActivity(i);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public Parkir[] getListRequestedParkir(){
        DB db = new DB(this, new User(this));
        return db.getListRequestedParkir();
    }
    public Parkir[] getListParkir(){
        DB db = new DB(this, new User(this));
        return db.getListParkir();
    }
}