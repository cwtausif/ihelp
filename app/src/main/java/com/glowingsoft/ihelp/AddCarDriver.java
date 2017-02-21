package com.glowingsoft.ihelp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import glowingsoft.com.ihelp.R;

public class AddCarDriver extends MainActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.add_driver);
        mContext = AddCarDriver.this;
        carDriverNameEt = (EditText) findViewById(R.id.driverNameEt);
        driverEmailEt = (EditText) findViewById(R.id.driverEmailEt);
        driverCityEt = (EditText) findViewById(R.id.driverCityEt);
        textViewLocation = (TextView) findViewById(R.id.textView_location);
        textViewPost = (TextView) findViewById(R.id.textView_post);
        mPb = (ProgressBar) findViewById(R.id.mPb);
        textViewLocation.setOnClickListener(this);
        textViewPost.setOnClickListener(this);

        //region google get lattitude and longitude

        buildGoogleApiClient();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        } else {
            Toast.makeText(this, "Not connected...", Toast.LENGTH_SHORT).show();
        }
        //endregion
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textView_post:
                if (streetAddress != null) {
                    location[0] = streetAddress;
                    location[1] = lattitude;
                    location[2] = longitude;
                    location[3] = country;
                    location[4] = state;
                    location[5] = city;
                    location[6] = streetAddress;
                }
                name = carDriverNameEt.getText().toString();
                if (name.length() ==0){
                    showToast("please enter valid car driver name","short");
                    break;
                } else if (driverEmailEt.length()==0 || emailValidator(driverEmailEt.getText().toString())==false){
                    showToast("please enter valid email","short");
                    break;
                } else if (driverCityEt.length()==0){
                    showToast("please enter city","short");
                    break;
                }
               email = driverEmailEt.getText().toString();
               mcity = driverCityEt.getText().toString();

                if (isConnected()) {
                    Log.d("response","lat: "+lattitude+" long: "+longitude+" name: "+name+" email: "+email+" mcity: "+mcity);
                    addCarDriverRequest();

                } else {
                    networkConnectionFailed();
                }

                break;
            case R.id.textView_location:
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult arg0) {
        Toast.makeText(this, "Failed to connect...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnected(Bundle arg0) {

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
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

        if (mLastLocation != null) {
            lattitude = String.valueOf(mLastLocation.getLatitude());
            longitude = String.valueOf(mLastLocation.getLongitude());
            displayLog("response Latitude", lattitude);
            displayLog("response Longitude", longitude);
            getLocationName(Double.parseDouble(lattitude), Double.parseDouble(longitude));
        }

    }

    @Override
    public void onConnectionSuspended(int arg0) {
        Toast.makeText(this, "Connection suspended...", Toast.LENGTH_SHORT).show();

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }
    public String getLocationName(double lattitude, double longitude) {

        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
        try {
            List<Address> addresses = gcd.getFromLocation(lattitude, longitude,
                    10);
            displayLog("Address of Lattitude and longitude", addresses.toString());

            for (Address adrs : addresses) {
                if (adrs != null) {

                    address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    city = addresses.get(0).getLocality();
                    state = addresses.get(0).getAdminArea();
                    country = addresses.get(0).getCountryName();
                    postalCode = addresses.get(0).getPostalCode();
                    streetAddress = addresses.get(0).getFeatureName();
                    displayLog("address", address);
                    displayLog("city", city);
                    displayLog("state", state);
                    displayLog("country", country);
                    displayLog("postal code", postalCode);
                    displayLog("known name", streetAddress);

                    if (city != null && !city.equals("")) {
                        textViewLocation.setText(streetAddress + "," + city);
                    } else {

                    }
                    // you should also try with addresses.get(0).toSring();
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return city;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                displayLog("Place response", place.toString());
                final CharSequence name = place.getName();
                final CharSequence address = place.getAddress();
                final LatLng location = place.getLatLng();
                lattitude = String.valueOf(location.latitude);
                longitude = String.valueOf(location.longitude);
                String attributions = PlacePicker.getAttributions(data);
                if (attributions == null) {
                    attributions = "";
                }

                textViewLocation.setText(address);
                getLocationName(Double.parseDouble(lattitude), Double.parseDouble(longitude));
            }
        }
    }

}
