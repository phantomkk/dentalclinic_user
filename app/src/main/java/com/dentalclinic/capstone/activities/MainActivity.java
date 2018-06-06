package com.dentalclinic.capstone.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import com.dentalclinic.capstone.R;

import com.dentalclinic.capstone.fragment.AppointmentFragment;
import com.dentalclinic.capstone.fragment.DentalFragment;
import com.dentalclinic.capstone.fragment.HistoryAppointmentFragment;
import com.dentalclinic.capstone.fragment.HistoryTreatmentFragment;
import com.dentalclinic.capstone.fragment.MyAccoutFragment;
import com.dentalclinic.capstone.fragment.NewFragment;
import com.dentalclinic.capstone.fragment.NewsFragment;
import com.dentalclinic.capstone.fragment.PromotionFragment;
import com.rupins.drawercardbehaviour.CardDrawerLayout;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {
   FragmentManager fragmentManager = getSupportFragmentManager();
    private CardDrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getResources().getString(R.string.new_fragment_title));
        NewFragment newFragment = new NewFragment();
        fragmentManager.beginTransaction().replace(R.id.main_fragment, newFragment).commit();

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        drawer.setViewScale(Gravity.START, 0.9f);
        drawer.setRadius(Gravity.START, 35);
        drawer.setViewElevation(Gravity.START, 20);
    }

    @Override
    public String getMainTitle() {
        return null;
    }

    @Override
    public void onCancelLoading() {

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
//        switch (id){
//            case R.id.nav_new:
//                break;
//            case R.id.nav_dental:
//                break;
//            case R.id.nav_appointment:
//                break;
//            case R.id.nav_promotion:
//            case R.id.nav_history_treatment:
//            case R.id.nav_history_appointment:
//            case R.id.nav_log_out:
//                default:
//
//
//        }
        if (id == R.id.nav_new) {
            setTitle(getResources().getString(R.string.new_fragment_title));
            NewsFragment newFragment = new NewsFragment();
            fragmentManager.beginTransaction().replace(R.id.main_fragment, newFragment).commit();
        } else if (id == R.id.nav_dental) {
            setTitle(getResources().getString(R.string.dental_fragment_title));
            DentalFragment dentalFragment = new DentalFragment();
            fragmentManager.beginTransaction().replace(R.id.main_fragment, dentalFragment).commit();
        } else if (id == R.id.nav_appointment) {
//            setTitle(getResources().getString(R.string.appointment_fragment_title));
////            AppointmentFragment dentalFragment = new AppointmentFragment();
////            fragmentManager.beginTransaction().replace(R.id.main_fragment, dentalFragment).commit();
            Intent intent = new Intent(MainActivity.this, QuickRegisterActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_promotion) {
            setTitle(getResources().getString(R.string.promotion_fragment_title));
            PromotionFragment dentalFragment = new PromotionFragment();
            fragmentManager.beginTransaction().replace(R.id.main_fragment, dentalFragment).commit();
        } else if (id == R.id.nav_history_treatment) {
            setTitle(getResources().getString(R.string.history_treatment_fragment_title));
            HistoryTreatmentFragment dentalFragment = new HistoryTreatmentFragment();
            fragmentManager.beginTransaction().replace(R.id.main_fragment, dentalFragment).commit();
        } else if (id == R.id.nav_history_appointment) {
            setTitle(getResources().getString(R.string.history_appointment_title));
            HistoryAppointmentFragment dentalFragment = new HistoryAppointmentFragment();
            fragmentManager.beginTransaction().replace(R.id.main_fragment, dentalFragment).commit();
        } else if (id == R.id.nav_my_accout) {
            setTitle(getResources().getString(R.string.my_accout_fragment_title));
            MyAccoutFragment myAccoutFragment = new MyAccoutFragment();
            fragmentManager.beginTransaction().replace(R.id.main_fragment, myAccoutFragment).commit();
        }else if (id == R.id.nav_log_out) {
            showMessage("Đăng xuất");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
