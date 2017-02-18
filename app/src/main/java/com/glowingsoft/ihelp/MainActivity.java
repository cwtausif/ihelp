package com.glowingsoft.ihelp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import android.widget.ImageView;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;
import glowingsoft.com.ihelp.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //region Variables
    TextView forgotPass;
    protected GoogleMap googleMap,googleMapRepair;
    EditText editTextEmail, editTextPassword,editTextName,carReapirNameEt;
    String carReapirName;
    public static final int WEBVIEW_REQUEST_CODE = 100;
    protected boolean mReqFlag = true;
    Button loginBtnJoin,signupBtn,addCarRepair;
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
    ArrayAdapter adapterCategories;
    LinearLayout mRoot;
    protected  String name, age, note, email, password;
    String avatar, apiKey, user_id,checkStatusLogin;
    int requestType;
    LocationManager manager;
    int requestGpsEnabled = 3;
    TextView textViewHome,textViewTaxi,textViewRepairCar;
    ArrayList<TutorCategoriesModel> arrayListTutorCategories;
    Spinner spinnerTutorCategories;
    ArrayList<UsersModel> tutorsData,allCarsData;
    UsersModel usersModel;
    ListView listViewTutors;
    TutorsAdapter tutorsAdapter;
    boolean firstRequst = true;
    LinearLayout carRepairLayout,homeLayout,taxiLayout;
    public static TextView textViewDate, textViewTime, textViewPost;
    static String time;
    public static Calendar cal;
    Spinner spinnerSports, spinnerOpponentTeam, spinnerNumbers, spinnerAgeFirst, spinnerAgeEnd, spinnerSkillsPrefrence, spinnerGender;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    String lattitude, longitude;
    TextView textViewLocation;
    private static final int DATE_DIALOG_ID = 1;
    private int year;
    private int month;
    private int day;
    String sportsCategory, activityCategory, numbers, ageFirst, skillsPrefrence, gender, description;
    String ageEnd = "Any";
    String[] location = new String[7];
    Context context;
    EditText editTextDescription;
    String address, city, state, country, postalCode, streetAddress;
    int PLACE_PICKER_REQUEST = 1;
    ImageView imageViewShare;
    String carRepairName;


    String[] arraysport;
    String[] arrayActivity;

    //endregion
    //region Model
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                } else if (length.equals("long")) {
                    Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
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
    //endregion

    //region Requests
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

    protected void addCarRepairRequest(){
        RequestParams mParams = new RequestParams();
        mParams.add("name",carRepairName);
        mHashMap = new HashMap<>();
        mHashMap.put("location",address);
        mHashMap.put("latitude", lattitude);
        mHashMap.put("longitude", longitude);
        mHashMap.put("formatted_address", address+", "+city+" "+country);
        mHashMap.put("country", country);
        mHashMap.put("city",city);
        mHashMap.put("address", address);
        mParams.put("location", mHashMap);

        //Params while social signup
        Log.d(TAG,"response"+mParams.toString());
        requestType = 4;
        Log.d("response apikey",retrivePreferencesValues("apiKey"));
        WebReq.client.addHeader("Authorizuser",retrivePreferencesValues("apiKey"));
        WebReq.post(mContext, "addcarrepair", mParams, new MyTextHttpResponseHandler());
    }

    private void allCarRepairReq() {
        Log.d("response","allCarRepairReq()");
        RequestParams mParams = new RequestParams();
        requestType = 5;
        WebReq.client.addHeader("Authorizuser",retrivePreferencesValues("apiKey"));
        WebReq.get(mContext, "allcarrepair", mParams, new MyTextHttpResponseHandler());
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id != R.id.add_car_repair) {
            if (isConnected()) {
                homeLayout.setVisibility(View.GONE);
                carRepairLayout.setVisibility(View.GONE);
                taxiLayout.setVisibility(View.GONE);

                textViewHome.setBackgroundColor(Color.parseColor("#ebe9ea"));
                textViewRepairCar.setBackgroundColor(Color.parseColor("#ebe9ea"));
                textViewTaxi.setBackgroundColor(Color.parseColor("#ebe9ea"));

                textViewRepairCar.setTextColor(Color.parseColor("#454545"));
                textViewHome.setTextColor(Color.parseColor("#454545"));
                textViewTaxi.setTextColor(Color.parseColor("#454545"));
            } else {
                networkConnectionFailed();
                return;
            }
        }

        switch (id){
            case R.id.add_car_repair:
                    //showToast("Add New Car Repair","short");
                    Intent intent = new Intent(mContext,AddCarRepair.class);
                    startActivity(intent);
                break;
            case R.id.textView_home:
                //showToast("home","short");
                homeLayout.setVisibility(View.VISIBLE);
                textViewHome.setBackgroundColor(Color.parseColor("#2ca5d2"));
                textViewHome.setTextColor(Color.parseColor("#ffffff"));
                if (tutorsData.size()==0){
                    tutorCategoriesRequest();
                }
                break;
            case R.id.textView_car:
                //showToast("car","short");
                    carRepairLayout.setVisibility(View.VISIBLE);
                    textViewRepairCar.setBackgroundColor(Color.parseColor("#2ca5d2"));
                    textViewRepairCar.setTextColor(Color.parseColor("#ffffff"));
                    if (allCarsData.size()==0) {
                        allCarRepairReq();
                    }
                break;
            case R.id.textView_my_taxi:
                showToast("home","short");
                taxiLayout.setVisibility(View.VISIBLE);
                textViewTaxi.setBackgroundColor(Color.parseColor("#2ca5d2"));
                textViewTaxi.setTextColor(Color.parseColor("#ffffff"));
                break;
        }
    }



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
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            super.onFailure(statusCode, headers, responseString, throwable);
            Log.d("response error on fail",responseString+" "+throwable);
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, final JSONObject mResponse) {
            mPb.setVisibility(View.INVISIBLE);
            Log.d("response",mResponse.toString()+"");
            try{
            if (mResponse.getString("error").equals("false")) {
                Log.d("response",mResponse.toString()+"");
                      switch (requestType){
                          case 1:
                          case 2:
                              loginOrSignup(mResponse);
                              break;
                          case 3:
                              tutorCategoriesResponse(mResponse);
                              break;
                          case 4:
                              addCarRepairResponse(mResponse);
                              break;
                          case 5:
                              allCarRepairResponse(mResponse);
                              break;

                      }
            } else {
                showToast(mResponse.getString("message"),"short");
                //GlobalClass.getInstance().snakbar(mRoot, mResponse.getString("message"), R.color.white, R.color.error);
            }
            }catch (Exception e){
                e.printStackTrace();
                GlobalClass.getInstance().snakbar(mRoot, "Request is Success But Empty Data", R.color.white, R.color.error);
            }
        }
    }

    private void allCarRepairResponse(JSONObject mResponse) {
        displayLog("response all car repairs please work   ", mResponse.toString());
        try {
            boolean error = mResponse.getBoolean("error");
            if (!error) {
                JSONArray jsonArrayUsers = mResponse.getJSONArray("carrepairs");
                if (jsonArrayUsers != null && jsonArrayUsers.length() != 0) {
                    for (int i = 0; i < jsonArrayUsers.length(); i++) {
                        double lat = jsonArrayUsers.getJSONObject(i).getDouble("latitude");
                        double longi  = jsonArrayUsers.getJSONObject(i).getDouble("longitude");
                        Log.d("response","lat,long"+lat+","+longi);
                        LatLng latLng = new LatLng(lat,longi);
                        googleMapRepair.addMarker(new MarkerOptions().position(latLng).title("Marker"));
                        if (i==0){
                            googleMapRepair.clear();
                            googleMapRepair.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                        }
                        usersModel = new UsersModel(longi,lat,jsonArrayUsers.getJSONObject(i).getString("name"), jsonArrayUsers.getJSONObject(i).getString("id"));
                        usersModel.toString();
                        allCarsData.add(usersModel);
                    }
                    tutorsAdapter.notifyDataSetChanged();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void addCarRepairResponse(JSONObject mResponse) {
        Log.d("response ","work in in addCarRepairRespones method"+mResponse.toString());
        try {
            showToast(mResponse.getString("message"),"short");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(mContext,HomeScreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("tab",2);
        startActivity(intent);
        finish();
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
                        LatLng latLng = new LatLng(lat,longi);
                        googleMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
                        if (i==0){
                            googleMap.clear();
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,12));
                        }
                        usersModel = new UsersModel(longi,lat,jsonArrayUsers.getJSONObject(i).getString("name"), jsonArrayUsers.getJSONObject(i).getString("id"));
                        usersModel.toString();
                        tutorsData.add(usersModel);
                    }
                    tutorsAdapter.notifyDataSetChanged();
                }

                //Show categories on spinner
                 adapterCategories = new ArrayAdapter(mContext, R.layout.spinner_item, arraysport);
                adapterCategories.setDropDownViewResource(R.layout.spinner_dropdown_item);
                spinnerTutorCategories.setAdapter(adapterCategories);

            }
        }catch (Exception e){
            e.printStackTrace();
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
