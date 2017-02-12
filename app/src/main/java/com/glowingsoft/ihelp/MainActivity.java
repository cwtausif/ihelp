package com.glowingsoft.ihelp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.glowingsoft.ihelp.adapters.TutorsAdapter;
import com.glowingsoft.ihelp.models.TutorCategoriesModel;
import com.glowingsoft.ihelp.models.UsersModel;
import com.glowingsoft.ihelp.utils.GlobalClass;
import com.glowingsoft.ihelp.web.WebReq;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;
import glowingsoft.com.ihelp.R;

public class MainActivity extends AppCompatActivity {
    TextView forgotPass;
    protected GoogleMap googleMap;
    EditText editTextEmail, editTextPassword,editTextName;
    public static final int WEBVIEW_REQUEST_CODE = 100;
    protected boolean mReqFlag = true;
    Button loginBtnJoin,signupBtn;
    Intent intent;
    Context mContext;
    static public String lengthShort = "short";
    static public String lengthLong = "long";
    String signupType ="normal",social_id="";
    protected HashMap<String, String> mHashMap;
    SharedPreferences mPref;
    SharedPreferences.Editor mPrefEditor;
    public static final String mPrefName = "ihelpData";
    private String TAG = "MainActivity";
    ProgressBar mPb;
    LinearLayout mRoot;
    Location mLastLocation;
    String lattitude, longitude;
    String address, city, state, country, postalCode, streetAddress;
    protected  String name, age, gender, note, email, password;
    String avatar, apiKey, user_id,checkStatusLogin;
    int requestType;
    LocationManager manager;
    int requestGpsEnabled = 3;
    GoogleApiClient mGoogleApiClient;
    TextView textViewHome,textViewTaxi,textViewRepairCar;
    ArrayList<TutorCategoriesModel> arrayListTutorCategories;
    String[] arraysport;
    Spinner spinnerTutorCategories;
    ArrayList<UsersModel> tutorsData;
    UsersModel usersModel;
    ListView listViewTutors;
    TutorsAdapter tutorsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    protected void loginRequest() {
        RequestParams mParams = new RequestParams();
        mParams.add("email",email);
        mParams.add("password",password);
        requestType = 1;
        WebReq.post(mContext, "login", mParams, new MyTextHttpResponseHandler());
    }
    protected void tutorCategoriesRequest() {
        RequestParams mParams = new RequestParams();

        requestType = 3;
        WebReq.client.addHeader("Authorizuser",retrivePreferencesValues("apiKey"));
        WebReq.get(mContext, "tutorscat", mParams, new MyTextHttpResponseHandler());
    }

    public boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
    public void networkConnectionFailed() {

        showToast("Network Connection Failed", lengthShort);
    }
    public  boolean isLoggedIn() {
        String session = retrivePreferencesValues("apiKey");
        boolean loginStatus = false;
        if (session != null && !session.equals("")) {
            loginStatus = true;
        }
        return loginStatus;
    }
    public void displayLog(String tag, String value) {
        Log.e(tag + " = ", " " + value);
    }
    public void hideKeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public void showToast(final String message, final String length) {
        runOnUiThread(new Runnable() {
            public void run() {
                if (length.equals("short")) {
                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                } else if (length.equals("long")) {
                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public String retrivePreferencesValues(String key) {
        mPref = getSharedPreferences(mPrefName, Context.MODE_PRIVATE);
        String storeValue = mPref.getString(key, "");
        return storeValue;
    }

    public boolean isConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }
    public void showLog(String message, String output){
        try{
            Log.d(message,output+"");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void setSharedPreferences(String key, String value) {
        mPref = getSharedPreferences(mPrefName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString(key, value);
        editor.commit();
    }
    //Region Signup
    protected void reqSignup() {
        if (mReqFlag) {
            RequestParams mParams = new RequestParams();

            mParams.put("pushType", "android");
            mParams.put("name", name);
            mParams.put("email", email);
            mParams.put("password",password);

            mHashMap = new HashMap<>();
            mHashMap.put("location",address);
            mHashMap.put("latitude", lattitude);
            mHashMap.put("longitude", longitude);
            mHashMap.put("formatted_address", address+", "+city+" "+country);
            mHashMap.put("country", country);
            mHashMap.put("city",city);
            mHashMap.put("address", address);

            requestType = 2;

            mParams.put("location", mHashMap);
            //Params while social signup
            Log.d(TAG,"signup type "+signupType+mParams.toString());
            WebReq.post(mContext, "signup", mParams, new MyTextHttpResponseHandler());
        } else {
            showToast("Request Already In Progress","short");
        }
    }

    //endregion
    class MyTextHttpResponseHandler extends JsonHttpResponseHandler {
        MyTextHttpResponseHandler() {
        }

        @Override
        public void onStart() {
            super.onStart();
            mReqFlag = false;
            mPb.setVisibility(View.VISIBLE);
        }

        @Override
        public void onFinish() {
            super.onFinish();
            mReqFlag = true;
            mPb.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onFailure(int mStatusCode, Header[] headers, Throwable mThrow, JSONObject e) {
            if (!((Activity) mContext).isFinishing()) {
                GlobalClass.getInstance().snakbar(mRoot, GlobalClass.getInstance().webError(mStatusCode, mThrow), R.color.white, R.color.error);
            }
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, final JSONObject mResponse) {
            mPb.setVisibility(View.INVISIBLE);
            Log.d("response",mResponse.toString()+"");
            try{
            if (mResponse.getString("error").equals("false")) {
                Log.d("response",mResponse.toString()+"");
                      switch (requestType){
                          case 3:
                              tutorCategoriesResponse(mResponse);
                              break;
                          case 1:
                          case 2:
                              loginOrSignup(mResponse);
                      }
            } else {
                GlobalClass.getInstance().snakbar(mRoot, mResponse.getString("message"), R.color.white, R.color.error);
            }
            }catch (Exception e){
                e.printStackTrace();
                GlobalClass.getInstance().snakbar(mRoot, "Request is Success But Empty Data", R.color.white, R.color.error);
            }
        }
    }

    private void loginOrSignup(JSONObject mResponse) {
        try {
            Log.d("response", mResponse.toString() + "");
            user_id = mResponse.getString("id");
            apiKey = mResponse.getString("apiKey");

            if (requestType == 1) {
                //When user login
                name = mResponse.getString("name");
                avatar = mResponse.getString("avatar");
            }

            setSharedPreferences("apiKey", apiKey);
            setSharedPreferences("email", email);
            setSharedPreferences("name", name);
            setSharedPreferences("avatar", avatar);
            setSharedPreferences("userId", user_id);
            showToast(mResponse.getString("message"), "long");
            startActivity(new Intent(MainActivity.this, HomeScreen.class));
            finish();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void tutorCategoriesResponse(JSONObject mResponse) {
        displayLog("response Tutor Categories & Tutors", mResponse.toString());
        try {
            boolean error = mResponse.getBoolean("error");
            if (!error) {
                JSONArray jsonArrayCategories = mResponse.getJSONArray("categories");
                JSONArray jsonArrayUsers = mResponse.getJSONArray("tutors");
                if (jsonArrayCategories != null && jsonArrayCategories.length() != 0) {
                    for (int i = 0; i < jsonArrayCategories.length(); i++) {
                        TutorCategoriesModel sportsActivityTitle = new TutorCategoriesModel(jsonArrayCategories.getJSONObject(i).getString("title"), jsonArrayCategories.getJSONObject(i).getString("id"));
                        arrayListTutorCategories.add(sportsActivityTitle);
                    }
                    arraysport = new String[arrayListTutorCategories.size()];
                    for (int i = 0; i < arrayListTutorCategories.size(); i++) {
                        arraysport[i] = arrayListTutorCategories.get(i).getTitle();
                    }
                }
                if (jsonArrayUsers != null && jsonArrayUsers.length() != 0) {
                    for (int i = 0; i < jsonArrayUsers.length(); i++) {
                       double lat = jsonArrayUsers.getJSONObject(i).getDouble("latitude");
                       double longi  = jsonArrayUsers.getJSONObject(i).getDouble("longitude");
                        Log.d("response","lat,long"+lat+","+longi);
                        usersModel = new UsersModel(longi,lat,jsonArrayUsers.getJSONObject(i).getString("name"), jsonArrayUsers.getJSONObject(i).getString("id"));
                        usersModel.toString();
                        tutorsData.add(usersModel);
                    }
                    tutorsAdapter.notifyDataSetChanged();
                }

                //Show categories on spinner
                ArrayAdapter adapterCategories = new ArrayAdapter(mContext, R.layout.spinner_item, arraysport);
                adapterCategories.setDropDownViewResource(R.layout.spinner_dropdown_item);
                spinnerTutorCategories.setAdapter(adapterCategories);
                drawTutorsOnMap();
            }
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    private void drawTutorsOnMap() {
        if (googleMap != null) {
            googleMap.clear();
        }
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.pin);
        for (int i = 0; i < tutorsData.size(); i++) {
            usersModel = new UsersModel();
            usersModel = tutorsData.get(i);
            usersModel.toString();
            // Adding a marker
            MarkerOptions marker = new MarkerOptions().position(
                    new LatLng(usersModel.getLatitude(), usersModel.getLongitude()))
                    .title(googleMap.getCameraPosition().toString());
            // changing marker color
            marker.icon(icon);
            googleMap.addMarker(marker);
            if (i == tutorsData.size() - 1) {
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(tutorsData.get(i).getLatitude(),
                                tutorsData.get(i).getLongitude())).zoom(11).build();
                googleMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(cameraPosition));
            }
        }
    }

    protected void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, please enable it?")
                .setCancelable(false)
                .setPositiveButton("Enable", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), requestGpsEnabled);
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}
