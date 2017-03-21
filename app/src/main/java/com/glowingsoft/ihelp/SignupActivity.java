package com.glowingsoft.ihelp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import glowingsoft.com.ihelp.R;

public class SignupActivity extends MainActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        mContext = SignupActivity.this;

        loginBtnJoin = (Button) findViewById(R.id.loginBtn);
        signupBtn = (Button) findViewById(R.id.signupBtn);
        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPassword = (EditText) findViewById(R.id.password);
        editTextName = (EditText) findViewById(R.id.name);
        mPb = (ProgressBar) findViewById(R.id.mPb);
        mRoot = (LinearLayout) findViewById(R.id.mRoot);
        serviceTypeSignup = (Spinner) findViewById(R.id.serviceType);

        serviceTypeSpinnerAdapter = new ArrayAdapter<>(mContext,android.R.layout.simple_spinner_item,serviceTypeService);
        serviceTypeSignup.setAdapter(serviceTypeSpinnerAdapter);
        serviceTypeSignup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                signupServiceType = position+1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //region listenr
        loginBtnJoin.setOnClickListener(this);
        signupBtn.setOnClickListener(this);
        //endregion

        //region google get lattitude and longitude
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        } else {
            buildGoogleApiClient();
            if (mGoogleApiClient != null) {
                mGoogleApiClient.connect();
            } else {
                Toast.makeText(this, "Not connected...", Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginBtn:
                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.signupBtn:
                checkStatusLogin = "mannual";
                email = editTextEmail.getText().toString();
                password = editTextPassword.getText().toString();
                if (editTextName.getText().toString().equals("")) {
                    showToast("Name can't be empty", "short");
                } else if (editTextEmail.getText().toString().equals("")) {
                    showToast("Please enter email", "short");
                } else if (!emailValidator(editTextEmail.getText().toString())) {
                    showToast("Please enter correct email", "short");
                } else if (editTextPassword.getText().toString().equals("")) {
                    showToast("Please enter password", "short");
                } else if (editTextPassword.getText().toString().length() < 6) {
                    showToast("Password should be atleast 6 character", "short");
                } else {
                    email = editTextEmail.getText().toString();
                    password = editTextPassword.getText().toString();
                    name = editTextName.getText().toString();
                    reqSignup();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == requestGpsEnabled && resultCode == 0) {
            String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            if (provider != null && !provider.equals("")) {
                buildGoogleApiClient();
                if (mGoogleApiClient != null) {
                    mGoogleApiClient.connect();
                } else {
                    Toast.makeText(this, "Not connected...", Toast.LENGTH_SHORT).show();
                }
            } else {
                buildAlertMessageNoGps();
            }


        }
    }

    @Override
    public void onConnected(Bundle bundle) {
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
            displayLog("Lattitude", lattitude);
            displayLog("Longitude", longitude);
            getLocationName(Double.parseDouble(lattitude), Double.parseDouble(longitude));
        }
    }
    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

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
                    if (address == null) {
                        address = " ";
                    }
                    city = addresses.get(0).getLocality();
                    if (city == null) {
                        city = " ";
                    }
                    state = addresses.get(0).getAdminArea();
                    if (state == null) {
                        state = " ";
                    }
                    country = addresses.get(0).getCountryName();
                    if (country == null) {
                        country = " ";
                    }
                    postalCode = addresses.get(0).getPostalCode();
                    if (postalCode == null) {
                        postalCode = " ";
                    }
                    streetAddress = addresses.get(0).getFeatureName();
                    if (streetAddress == null) {
                        streetAddress = " ";
                    }
                    displayLog("address", address);
                    displayLog("city", city);
                    displayLog("state", state);
                    displayLog("country", country);
                    displayLog("postal code", postalCode);
                    displayLog("known name", streetAddress);
                    // you should also try with addresses.get(0).toSring();

                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return city;

    }
}
