package com.glowingsoft.ihelp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.glowingsoft.ihelp.adapters.TutorsAdapter;
import com.glowingsoft.ihelp.models.UsersModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import glowingsoft.com.ihelp.R;

public class HomeScreen extends MainActivity implements Spinner.OnItemSelectedListener{
    ImageView imageViewCreateActivity;

    ArrayList<UsersModel> arrayListUsers;
    ImageView imageViewHideBottom, imageViewHideTop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.home_screen);
        //care repair section
        carRepairLayout = (LinearLayout) findViewById(R.id.car_repair_layout);
        addCarRepair = (Button) findViewById(R.id.add_car_repair);
        addCarDriver = (Button) findViewById(R.id.add_car_driver);
        addAccessory = (Button) findViewById(R.id.add_accessories);
        homeLayout = (LinearLayout) findViewById(R.id.layout_home);


        mContext = HomeScreen.this;
        showLog("response ApiKey", retrivePreferencesValues("apiKey") + "");
        arrayListTutorCategories = new ArrayList<>();
        arrayListUsers = new ArrayList<>();
        tutorsData = new ArrayList<>();
        allCarsData = new ArrayList<>();
        allDriversData = new ArrayList<>();
        allaccessoryRepairs = new ArrayList<>();
        listViewTutors = (ListView) findViewById(R.id.listView_tutors);
        tutorsAdapter = new TutorsAdapter(mContext, tutorsData);
        listViewTutors.setAdapter(tutorsAdapter);
        //region references
        imageViewCreateActivity = (ImageView) findViewById(R.id.imageView_create_activity);
        spinnerTutorCategories = (Spinner) findViewById(R.id.spinner_category_tutors);
        imageViewHideBottom = (ImageView) findViewById(R.id.imageview_hide_bottom);
        imageViewHideTop = (ImageView) findViewById(R.id.imageview_hide_top);
        textViewHome = (TextView) findViewById(R.id.textView_home);
        textViewMe = (TextView) findViewById(R.id.textView_me);
        textViewRepairCar = (TextView) findViewById(R.id.textView_car);
        textViewTaxi = (TextView) findViewById(R.id.textView_my_taxi);
        textViewAccessories = (TextView) findViewById(R.id.textView_accessories);


        mPb = (ProgressBar) findViewById(R.id.mPb);

        layoutMap = (LinearLayout) findViewById(R.id.layout_map);
        layoutMe = (LinearLayout) findViewById(R.id.layout_me);
        layoutHome = (LinearLayout) findViewById(R.id.layout_home);
        taxiLayout = (LinearLayout) findViewById(R.id.taxi_layout);
        accessoriesLayout = (LinearLayout) findViewById(R.id.accessories_layout);

        //region reference of me

        //region listener
        textViewRepairCar.setOnClickListener(this);
        textViewTaxi.setOnClickListener(this);
        //  listViewActivities.setOnItemClickListener(this);
        //imageViewCreateActivity.setOnClickListener(this);
        imageViewHideTop.setOnClickListener(this);
//        imageViewSetting.setOnClickListener(this);
        imageViewHideBottom.setOnClickListener(this);
        textViewMe.setOnClickListener(this);
        textViewHome.setOnClickListener(this);
        textViewAccessories.setOnClickListener(this);
        // textViewEditProfile.setOnClickListener(this);
        //textViewActivities.setOnClickListener(this);
        addCarRepair.setOnClickListener(this);
        addCarDriver.setOnClickListener(this);
        addAccessory.setOnClickListener(this);

        //endregion


        //region SpinnerListener
        spinnerTutorCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(HomeScreen.this,""+position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //endregion

        //important to show specific page after adding data


        // region google map
        try {
            // Loading map
            initilizeMap();

            // Changing map type
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            // googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            // googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            // googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            // googleMap.setMapType(GoogleMap.MAP_TYPE_NONE);

            // Showing / hiding your current location
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            googleMap.setMyLocationEnabled(true);

            // Enable / Disable zooming controls
            googleMap.getUiSettings().setZoomControlsEnabled(false);

            // Enable / Disable my location button
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);

            // Enable / Disable Compass icon
            googleMap.getUiSettings().setCompassEnabled(true);

            // Enable / Disable Rotate gesture
            googleMap.getUiSettings().setRotateGesturesEnabled(true);

            // Enable / Disable zooming functionality
            googleMap.getUiSettings().setZoomGesturesEnabled(true);

            LatLng sydney = new LatLng(-34, 151);
            googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        } catch (
                Exception e
                )

        {
            e.printStackTrace();
        }
        //endregion

        // region google map repair
        try {
            // Loading map
            initilizeMapCarRepair();

            // Changing map type
            googleMapRepair.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            // googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            // googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            // googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            // googleMap.setMapType(GoogleMap.MAP_TYPE_NONE);

            // Showing / hiding your current location
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            googleMapRepair.setMyLocationEnabled(true);

            // Enable / Disable zooming controls
            googleMapRepair.getUiSettings().setZoomControlsEnabled(false);

            // Enable / Disable my location button
            googleMapRepair.getUiSettings().setMyLocationButtonEnabled(true);

            // Enable / Disable Compass icon
            googleMapRepair.getUiSettings().setCompassEnabled(true);

            // Enable / Disable Rotate gesture
            googleMapRepair.getUiSettings().setRotateGesturesEnabled(true);

            // Enable / Disable zooming functionality
            googleMapRepair.getUiSettings().setZoomGesturesEnabled(true);

            LatLng sydney = new LatLng(-34, 151);
            googleMapRepair.addMarker(new MarkerOptions().position(sydney).title("Marker"));
            googleMapRepair.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        } catch (
                Exception e
                )

        {
            e.printStackTrace();
        }
        //endregion

        //region Google map Car Driver (Taxi)

        try {
            // Loading map
            initilizeMapCarDriver();

            // Changing map type
            googleMapDriverTaxi.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            // googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            // googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            // googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            // googleMap.setMapType(GoogleMap.MAP_TYPE_NONE);

            // Showing / hiding your current location
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            googleMapDriverTaxi.setMyLocationEnabled(true);

            // Enable / Disable zooming controls
            googleMapDriverTaxi.getUiSettings().setZoomControlsEnabled(false);

            // Enable / Disable my location button
            googleMapDriverTaxi.getUiSettings().setMyLocationButtonEnabled(true);

            // Enable / Disable Compass icon
            googleMapDriverTaxi.getUiSettings().setCompassEnabled(true);

            // Enable / Disable Rotate gesture
            googleMapDriverTaxi.getUiSettings().setRotateGesturesEnabled(true);

            // Enable / Disable zooming functionality
            googleMapDriverTaxi.getUiSettings().setZoomGesturesEnabled(true);

            LatLng sydney = new LatLng(-34, 151);
            googleMapDriverTaxi.addMarker(new MarkerOptions().position(sydney).title("car driver"));
            googleMapDriverTaxi.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        } catch (
                Exception e
                )

        {
            e.printStackTrace();
        }
        //endregion

        //region Accessories Repair (MAP)

        try {
            // Loading map
            initilizAccessoriesRepairer();

            // Changing map type
            googleMapAccessoryRepairer.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            // googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            // googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            // googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            // googleMap.setMapType(GoogleMap.MAP_TYPE_NONE);

            // Showing / hiding your current location
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            googleMapAccessoryRepairer.setMyLocationEnabled(true);

            // Enable / Disable zooming controls
            googleMapAccessoryRepairer.getUiSettings().setZoomControlsEnabled(false);

            // Enable / Disable my location button
            googleMapAccessoryRepairer.getUiSettings().setMyLocationButtonEnabled(true);

            // Enable / Disable Compass icon
            googleMapAccessoryRepairer.getUiSettings().setCompassEnabled(true);

            // Enable / Disable Rotate gesture
            googleMapAccessoryRepairer.getUiSettings().setRotateGesturesEnabled(true);

            // Enable / Disable zooming functionality
            googleMapAccessoryRepairer.getUiSettings().setZoomGesturesEnabled(true);

            LatLng sydney = new LatLng(-34, 151);
            googleMapAccessoryRepairer.addMarker(new MarkerOptions().position(sydney).title("Accessories Repairer"));
            googleMapAccessoryRepairer.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        } catch (
                Exception e
                )

        {
            e.printStackTrace();
        }
        //endregion


        try {
            if (getIntent().getExtras().getInt("tab") == 2) {
                if (isConnected()) {
                    textViewRepairCar.performClick();
                } else {
                    networkConnectionFailed();
                }
                return;
            }else if (getIntent().getExtras().getInt("tab") == 3) {
                if (isConnected()) {
                    textViewTaxi.performClick();
                } else {
                    networkConnectionFailed();
                }
                return;
            }else if (getIntent().getExtras().getInt("tab") == 4) {
                if (isConnected()) {
                    textViewAccessories.performClick();
                } else {
                    networkConnectionFailed();
                }
                return;
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        if (isConnected()) {
            tutorCategoriesRequest();
        } else {
            networkConnectionFailed();
        }

    }

    private void initilizeMap() {
        /**
         * function to load map If map is not created it will create it for you
         */
            if (googleMap == null) {
                googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                        R.id.map)).getMap();

                // check if map is created successfully or not
                if (googleMap == null) {
                    Toast.makeText(getApplicationContext(),
                            "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                            .show();
                }
            }
    }
    private void initilizeMapCarRepair() {
        /**
         * function to load map If map is not created it will create it for you
         */
        if (googleMapRepair == null) {
            googleMapRepair = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.car_repair_map)).getMap();

            // check if map is created successfully or not
            if (googleMapRepair == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }
    private void initilizeMapCarDriver() {
        /**
         * function to load map If map is not created it will create it for you
         */
        if (googleMapDriverTaxi == null) {
            googleMapDriverTaxi = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.taxi_map)).getMap();

            // check if map is created successfully or not
            if (googleMapDriverTaxi == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    private void initilizAccessoriesRepairer() {
        /**
         * function to load map If map is not created it will create it for you
         */
        if (googleMapAccessoryRepairer == null) {
            googleMapAccessoryRepairer = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.accessories_map)).getMap();

            // check if map is created successfully or not
            if (googleMapAccessoryRepairer == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //Toast.makeText(mContext,position+"",Toast.LENGTH_SHORT).show();
            //tutorCategoriesRequest();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
