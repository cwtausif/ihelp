package com.glowingsoft.ihelp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.glowingsoft.ihelp.adapters.ReviewsAdapter;
import com.glowingsoft.ihelp.adapters.TutorsAdapter;
import com.glowingsoft.ihelp.models.ReviewsModel;
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
/*
IHelp project
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //region Variables
    RatingBar ratingBar;
    Dialog rankDialog;
    ReviewsAdapter reviewsAdapter;
    ReviewsModel reviewsModel;
    TextView forgotPass,totalRatings,oneTv,twoTv,threeTv,fourTv,fiveTv;
    String[] accessoryTypeService = new String[]{"Laptop/Pc","Mobile Phones"};
    String[] serviceTypeService = new String[]{"Tutor","Car Repairer","Accessory Repairer","Taxi Driver"};
    protected GoogleMap googleMap,googleMapRepair,googleMapDriverTaxi,googleMapAccessoryRepairer;
    EditText editTextEmail, editTextPassword,editTextName,carReapirNameEt,carDriverNameEt,driverEmailEt,driverCityEt,descriptionEt,locationEt,websiteEt,githubEt,linkedinEt,twitterEt;
    String carReapirName,mcity,reviewText;
    public static final int WEBVIEW_REQUEST_CODE = 100;
    protected boolean mReqFlag = true;
    Button loginBtnJoin,signupBtn,addCarRepair,addCarDriver,addAccessory,logoutBtn,buttonDistanceTutors,buttonCategoryTutors,homeBtn,saveBtn,addReviewBtn,paymentBtn;
    Intent intent;
    Context mContext;
    static public String lengthShort = "short";
    static public String lengthLong = "long";
    String signupType ="normal",social_id="";
    protected HashMap<String, String> mHashMap;
    SharedPreferences mPref;
    ArrayAdapter<String> accessoryTypeSpinnerAdapter,serviceTypeSpinnerAdapter;
    SharedPreferences.Editor mPrefEditor;
    public static final String mPrefName = "ihelpData";
    private String TAG = "MainActivity";
    ProgressBar mPb;
    ArrayAdapter adapterCategories;
    LinearLayout mRoot;
    protected  String name, age, note, email, password,website,github,linkedin,twitter,locationAddress;
    String avatar, apiKey, user_id,checkStatusLogin;
    int requestType,accessoryType=0,signupServiceType=1,userRating;
    LocationManager manager;
    int requestGpsEnabled = 3;
    LinearLayout layoutMap, layoutHome;
    TextView textViewHome,textViewTaxi,textViewRepairCar,textViewAccessories,textViewMe,meNamTextview,textViewName,ratingsTv;
    ArrayList<TutorCategoriesModel> arrayListTutorCategories;
    ArrayList<ReviewsModel> reviewsData;
    Spinner spinnerTutorCategories,accessoryTypeSpinner,serviceTypeSignup;
    ArrayList<UsersModel> tutorsData,allCarsData,allDriversData,allaccessoryRepairs;
    UsersModel usersModel;
    ListView listViewTutors,reviewsListView;
    TutorsAdapter tutorsAdapter;
    boolean firstRequst = true;
    LinearLayout carRepairLayout,homeLayout,taxiLayout,accessoriesLayout, layoutMe;
    public static TextView textViewDate, textViewTime, textViewPost;
    static String time;
    public static Calendar cal;
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
    ImageView imageViewShare,picProfile;
    String carRepairName;


    String[] arraysport;
    String[] arrayRange = new String[]{"All","1 km","5 km","10 km","20 km","100 km"};
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
    protected void userProfileRequest() {
        Log.d("response","userProfileRequest()");
        RequestParams mParams = new RequestParams();
        mParams.add("id",retrivePreferencesValues("userId"));
        requestType = 10;
        WebReq.client.addHeader("Authorizuser",retrivePreferencesValues("apiKey"));
        WebReq.get(mContext, "user", mParams, new MyTextHttpResponseHandler());
    }
    protected void saveUserProfile(){
        Log.d("response","saveUserProfile()");
        RequestParams mParams = new RequestParams();
        //mParams.add("id",retrivePreferencesValues("userId"));
        mParams.add("description",description);
        mParams.add("website",website);
        mParams.add("linkedin",linkedin);
        mParams.add("twitter",twitter);
        mParams.add("github",github);

        requestType = 11;
        WebReq.client.addHeader("Authorizuser",retrivePreferencesValues("apiKey"));
        WebReq.post(mContext, "updateprofile", mParams, new MyTextHttpResponseHandler());
    }
    protected void sendReviewToServer(){
        Log.d("response","sendReviewToServer()");
        RequestParams mParams = new RequestParams();
        //mParams.add("id",retrivePreferencesValues("userId"));
        mParams.add("update","0");
        mParams.add("user_id",user_id);
        mParams.add("review",reviewText);
        mParams.add("rating",userRating+"");
        requestType = 12;
        WebReq.client.addHeader("Authorizuser",retrivePreferencesValues("apiKey"));
        WebReq.post(mContext, "adduserrating", mParams, new MyTextHttpResponseHandler());
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
        mParams.add("categoryId",retrivePreferencesValues("categoryId"));
        mParams.add("tutorsRange",retrivePreferencesValues("tutorsRange"));
        try {
            if (retrivePreferencesValues("categorytitle") != "" && retrivePreferencesValues("categorytitle").length()> 0) {
                buttonCategoryTutors.setText(retrivePreferencesValues("categorytitle"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        try {
            if (retrivePreferencesValues("tutorsRange") != "" && retrivePreferencesValues("tutorsRange").length()> 0) {
                if (retrivePreferencesValues("tutorsRange").equals("All")){
                    buttonDistanceTutors.setText(retrivePreferencesValues("tutorsRange"));
                }else {
                    buttonDistanceTutors.setText(retrivePreferencesValues("tutorsRange") + " km");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }



        requestType = 3;
        Log.d("response params",mParams.toString());
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
            mParams.put("user_type",signupServiceType+"");

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
    protected void addCarDriverRequest(){

        RequestParams mParams = new RequestParams();
        mParams.add("name",name);
        mParams.add("email",email);
        mParams.add("city",mcity);
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
        requestType = 6;
        Log.d("response apikey",retrivePreferencesValues("apiKey"));
        WebReq.client.addHeader("Authorizuser",retrivePreferencesValues("apiKey"));
        WebReq.post(mContext, "addcardriver", mParams, new MyTextHttpResponseHandler());
    }
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
    private void allCarDriverReq() {
        Log.d("response","allCarDriverReq()");
        RequestParams mParams = new RequestParams();
        requestType = 7;
        WebReq.client.addHeader("Authorizuser",retrivePreferencesValues("apiKey"));
        WebReq.get(mContext, "allcardrivers", mParams, new MyTextHttpResponseHandler());
    }
    protected void addAccessoryRequest(){

        RequestParams mParams = new RequestParams();
        mParams.add("name",name);
        mParams.add("accessoryType",accessoryType+"");
        mParams.add("city",mcity);
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
        requestType = 8;
        Log.d("response apikey",retrivePreferencesValues("apiKey"));
        WebReq.client.addHeader("Authorizuser",retrivePreferencesValues("apiKey"));
        WebReq.post(mContext, "addaccessory", mParams, new MyTextHttpResponseHandler());
    }
    private void allAccessoryRepairsReq() {
        Log.d("response","allAccessoryRepairsReq()");
        RequestParams mParams = new RequestParams();
        requestType = 9;
        WebReq.client.addHeader("Authorizuser",retrivePreferencesValues("apiKey"));
        WebReq.get(mContext, "allaccessoryrepairs", mParams, new MyTextHttpResponseHandler());
    }
    public void getAllUserReviews() {
        Log.d("response","getAllUserReviews()");
        RequestParams mParams = new RequestParams();
        mParams.add("userId",user_id);
        requestType = 13;
        reviewsData.clear();
        WebReq.client.addHeader("Authorizuser",retrivePreferencesValues("apiKey"));
        WebReq.get(mContext, "alluserreviews", mParams, new MyTextHttpResponseHandler());
    }
    //endregion
    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id != R.id.button_range_tutors && id != R.id.add_car_repair && id !=R.id.add_car_driver && id != R.id.add_accessories && id != R.id.logoutBtn && id != R.id.button_category_tutors) {
            if (isConnected()) {
                homeLayout.setVisibility(View.GONE);
                carRepairLayout.setVisibility(View.GONE);
                taxiLayout.setVisibility(View.GONE);
                accessoriesLayout.setVisibility(View.GONE);
                layoutMe.setVisibility(View.GONE);


                textViewHome.setBackgroundColor(Color.parseColor("#ebe9ea"));
                textViewRepairCar.setBackgroundColor(Color.parseColor("#ebe9ea"));
                textViewTaxi.setBackgroundColor(Color.parseColor("#ebe9ea"));
                textViewAccessories.setBackgroundColor(Color.parseColor("#ebe9ea"));
                textViewMe.setBackgroundColor(Color.parseColor("#ebe9ea"));



                textViewRepairCar.setTextColor(Color.parseColor("#454545"));
                textViewHome.setTextColor(Color.parseColor("#454545"));
                textViewTaxi.setTextColor(Color.parseColor("#454545"));
                textViewAccessories.setTextColor(Color.parseColor("#454545"));
                textViewMe.setTextColor(Color.parseColor("#454545"));

            } else {
                networkConnectionFailed();
                return;
            }
        }

        switch (id){
            case R.id.button_range_tutors:
                Log.d("response","button_category_tutors pressed");
                //Initialize the Alert Dialog
                AlertDialog.Builder builderRange = new AlertDialog.Builder(mContext);
                builderRange.setTitle("Select One Tutor Range:-");

                final ArrayAdapter<String> arrayAdapterRange = new ArrayAdapter<>(mContext, android.R.layout.select_dialog_singlechoice,arrayRange);

                builderRange.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });


                builderRange.setAdapter(arrayAdapterRange, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        String strName = arrayAdapterRange.getItem(position);
                        Log.d("response strName",strName+"");
                        buttonDistanceTutors.setText(strName);
                        switch (position){
                            case 0:
                                strName = "All";
                                break;
                            default:
                                strName = strName.substring(0,strName.indexOf(" "));
                                break;
                        }
                        setSharedPreferences("tutorsRange",strName);
                        tutorCategoriesRequest();
                    }
                });
                builderRange.show();
                break;
            case R.id.button_category_tutors:
                Log.d("response","button_category_tutors pressed");
                //Initialize the Alert Dialog
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(mContext);
                builderSingle.setTitle("Select One Tutor Category:-");

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(mContext, android.R.layout.select_dialog_singlechoice,arraysport);

                builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strName = arrayAdapter.getItem(which);
                        Log.d("response strName",strName+"");
                        buttonCategoryTutors.setText(strName);
                        TutorCategoriesModel sportsActivityTitle = arrayListTutorCategories.get(which);
                        Log.d("response imp",sportsActivityTitle.getId()+" "+sportsActivityTitle.getTitle());
                        setSharedPreferences("categoryId",sportsActivityTitle.getId());
                        setSharedPreferences("categorytitle",strName);
                        tutorCategoriesRequest();
                    }
                });
                builderSingle.show();
                break;
            case R.id.logoutBtn:
                Log.d("response logout","logout button pressed");
                setSharedPreferences("apiKey", "");
                setSharedPreferences("email", "");
                Intent intentl = new Intent(mContext,LoginActivity.class);
                startActivity(intentl);
                finish();
                break;
            case R.id.add_accessories:
                Intent accessoryIntent = new Intent(mContext,AddAccessory.class);
                startActivity(accessoryIntent);
                break;
            case R.id.add_car_repair:
                    //showToast("Add New Car Repair","short");
                    Intent intent = new Intent(mContext,AddCarRepair.class);
                    startActivity(intent);
                break;
            case R.id.add_car_driver:
                Intent inte = new Intent(mContext,AddCarDriver.class);
                startActivity(inte);
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
                taxiLayout.setVisibility(View.VISIBLE);
                textViewTaxi.setBackgroundColor(Color.parseColor("#2ca5d2"));
                textViewTaxi.setTextColor(Color.parseColor("#ffffff"));
                if (allDriversData.size()==0) {
                    allCarDriverReq();
                }
                break;
            case R.id.textView_accessories:
                accessoriesLayout.setVisibility(View.VISIBLE);
                textViewAccessories.setBackgroundColor(Color.parseColor("#2ca5d2"));
                textViewAccessories.setTextColor(Color.parseColor("#ffffff"));
                if (allaccessoryRepairs.size()==0) {
                    allAccessoryRepairsReq();
                }
                break;
            case R.id.textView_me:
                layoutMe.setVisibility(View.VISIBLE);
                textViewMe.setBackgroundColor(Color.parseColor("#2ca5d2"));
                textViewMe.setTextColor(Color.parseColor("#ffffff"));
                setMeProfileValues();
                break;

        }
    }

    private void setMeProfileValues() {
        meNamTextview.setText(retrivePreferencesValues("name"));
    }

    class MyTextHttpResponseHandler extends JsonHttpResponseHandler {
        MyTextHttpResponseHandler() {
        }

        @Override
        public void onStart() {
            super.onStart();
            mReqFlag = false;
            if (mPb != null) {
                mPb.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onFinish() {
            super.onFinish();
            mReqFlag = true;
            if (mPb != null) {
                mPb.setVisibility(View.INVISIBLE);
            }
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
            if (mPb!=null) {
                mPb.setVisibility(View.INVISIBLE);
            }
            Log.d("response",mResponse.toString()+"");
            try{
            if (mResponse.getString("error").equals("false")) {
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
                          case 6:
                              addCarDriverResponse(mResponse);
                              break;
                          case 7:
                              allCarDrivresResponse(mResponse);
                              break;
                          case 8:
                              addAccessoryResponse(mResponse);
                          case 9:
                              allAccessoryRepairsResponse(mResponse);
                              break;
                          case 10:
                              userProfileRequestResponse(mResponse);
                              break;
                          case 11:
                              try {
                                  showToast(mResponse.getString("message"),"short");
                              } catch (JSONException e) {
                                  e.printStackTrace();
                              }
                              break;
                          case 12:
                              getAllUserReviews();
                              try {
                                  showToast(mResponse.getString("message"),"short");
                              } catch (JSONException e) {
                                  e.printStackTrace();
                              }
                              break;
                          case 13:
                                getAllUserReviewsResponse(mResponse);
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

    private void getAllUserReviewsResponse(JSONObject mResponse) {

        JSONArray reviews = new JSONArray();
        try{
            reviews = mResponse.getJSONArray("reviews");
            float theRating = 0;
            int countOne = 0,countTwo=0,countThree=0,countFour=0,countFive=0;
            for (int i=0; i<reviews.length(); i++){
                reviewsModel = new ReviewsModel();
                JSONObject eachReview = reviews.getJSONObject(i);
                reviewsModel.setReview_id(eachReview.getString("review_id"));
                reviewsModel.setName(eachReview.getString("name"));
                reviewsModel.setReview(eachReview.getString("review"));
                reviewsModel.setRating(eachReview.getString("rate"));
                reviewsData.add(reviewsModel);

                int rev = Integer.parseInt(eachReview.getString("rate"));
                if (rev==1){
                    countOne = countOne + 1;
                } else if (rev==2){
                    countTwo = countTwo + 1;
                }else if (rev==3){
                    countThree = countThree + 1;
                }else if (rev==4){
                    countFour = countFour + 1;
                }else {
                    countFive = countFive + 1;
                }
            }
            theRating = (5*countFive + 4*countFour + 3*countThree + 2*countTwo + 1*countOne) / (countFive+countFour+countThree+countTwo+countOne);

            oneTv.setText(countOne+"");
            twoTv.setText(countTwo+"");
            threeTv.setText(countThree+"");
            fourTv.setText(countFour+"");
            fiveTv.setText(countFive+"");

            reviewsAdapter.notifyDataSetChanged();
            totalRatings.setText(reviewsData.size()+" Total");

            try {
                if (theRating>0) {
                    ratingsTv.setText(theRating+"");
                    ratingBar.setRating(theRating);
                }else {
                    ratingsTv.setText("0.0");
                }

            }catch (Exception e){
                e.printStackTrace();
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void userProfileRequestResponse(JSONObject mResponse) {
        Log.d("response",mResponse.toString());
        try{
            String error = mResponse.getString("error");
            if (error.equals("false")){

                JSONArray userArray = mResponse.getJSONArray("user");
                Log.d("response","userArray"+userArray);
                JSONObject userInfo = userArray.getJSONObject(0);
                Log.d("response","userInfo"+userInfo);
                textViewName.setText(userInfo.getString("name"));
                try {
                    description = userInfo.getString("description");
                }catch (Exception e){
                    e.printStackTrace();
                }
                locationAddress = userInfo.getString("city")+" "+userInfo.getString("country");
                website = userInfo.getString("website");
                github = userInfo.getString("github");
                linkedin = userInfo.getString("linkedin");
                twitter = userInfo.getString("twitter");

                if (description != null && description != "" && description.equals("null")  == false){
                    descriptionEt.setText(description);
                }

                if (website != null && website != "" && website.equals("null")  == false){
                    websiteEt.setText(website);
                }

                if (github != null && github != "" && github.equals("null")  == false){
                    githubEt.setText(github);
                }

                if (linkedin != null && linkedin != "" && linkedin.equals("null")  == false){
                    linkedinEt.setText(linkedin);
                }

                if (twitter != null && twitter != "" && twitter.equals("null")  == false){
                    twitterEt.setText(twitter);
                }

                if (locationAddress != null && locationAddress != "" && locationAddress.equals("null")  == false){
                    locationEt.setText(locationAddress);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void addAccessoryResponse(JSONObject mResponse) {
        Log.d("response ","work in in addAccessoryResponse method"+mResponse.toString());
        try {
            showToast(mResponse.getString("message"),"short");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(mContext,HomeScreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("tab",4);
        startActivity(intent);
        finish();
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
                        googleMapRepair.addMarker(new MarkerOptions().position(latLng).title(jsonArrayUsers.getJSONObject(i).getString("name")));
                        if (i==0){
                            googleMapRepair.clear();
                            googleMapRepair.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                        }
                        usersModel = new UsersModel("",longi,lat,jsonArrayUsers.getJSONObject(i).getString("name"), jsonArrayUsers.getJSONObject(i).getString("id"));
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
    private void allCarDrivresResponse(JSONObject mResponse) {
        displayLog("response all car drivers please work   ", mResponse.toString());
        try {
            boolean error = mResponse.getBoolean("error");
            if (!error) {
                JSONArray jsonArrayUsers = mResponse.getJSONArray("cardrivers");
                if (jsonArrayUsers != null && jsonArrayUsers.length() != 0) {
                    for (int i = 0; i < jsonArrayUsers.length(); i++) {
                        double lat = jsonArrayUsers.getJSONObject(i).getDouble("latitude");
                        double longi  = jsonArrayUsers.getJSONObject(i).getDouble("longitude");
                        Log.d("response","lat,long"+lat+","+longi);
                        LatLng latLng = new LatLng(lat,longi);
                        googleMapDriverTaxi.addMarker(new MarkerOptions().position(latLng).title(jsonArrayUsers.getJSONObject(i).getString("name")));
                        if (i==0){
                            googleMapDriverTaxi.clear();
                            googleMapDriverTaxi.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                        }
                        usersModel = new UsersModel("",longi,lat,jsonArrayUsers.getJSONObject(i).getString("name"), jsonArrayUsers.getJSONObject(i).getString("id"));
                        usersModel.toString();
                        allDriversData.add(usersModel);
                    }
                    tutorsAdapter.notifyDataSetChanged();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void addCarRepairResponse(JSONObject mResponse) {
        Log.d("response ","addCarRepairRespones method"+mResponse.toString());
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
    private void addCarDriverResponse(JSONObject mResponse) {
        Log.d("response ","work in in addCarDriverResponse method"+mResponse.toString());
        try {
            showToast(mResponse.getString("message"),"short");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(mContext,HomeScreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("tab",3);
        startActivity(intent);
        finish();
    }
    private void loginOrSignup(JSONObject mResponse) {
        try {
            Log.d("response login", mResponse.toString() + "");
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
            startActivity(new Intent(MainActivity.this, UserProfileActivity.class));

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
                    arrayListTutorCategories.clear();
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
                    tutorsData.clear();
                    for (int i = 0; i < jsonArrayUsers.length(); i++) {
                       double lat = jsonArrayUsers.getJSONObject(i).getDouble("latitude");
                       double longi  = jsonArrayUsers.getJSONObject(i).getDouble("longitude");
                        Log.d("response","lat,long"+lat+","+longi);
                        LatLng latLng = new LatLng(lat,longi);
                        googleMap.addMarker(new MarkerOptions().position(latLng).title(jsonArrayUsers.getJSONObject(i).getString("name")));
                        if (i==0){
                            googleMap.clear();
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,11));
                        }
                        usersModel = new UsersModel(jsonArrayUsers.getJSONObject(i).getString("categoryTitle"),longi,lat,jsonArrayUsers.getJSONObject(i).getString("name"), jsonArrayUsers.getJSONObject(i).getString("id"));
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
    private void allAccessoryRepairsResponse(JSONObject mResponse) {
        displayLog("response allAccessoryRepairsResponse   ", mResponse.toString());

        try {
            boolean error = mResponse.getBoolean("error");
            if (!error) {
                JSONArray jsonArrayUsers = mResponse.getJSONArray("accessoryrepairs");
                if (jsonArrayUsers != null && jsonArrayUsers.length() != 0) {
                    for (int i = 0; i < jsonArrayUsers.length(); i++) {
                        double lat = jsonArrayUsers.getJSONObject(i).getDouble("latitude");
                        double longi  = jsonArrayUsers.getJSONObject(i).getDouble("longitude");
                        Log.d("response","lat,long"+lat+","+longi);
                        LatLng latLng = new LatLng(lat,longi);
                        googleMapAccessoryRepairer.addMarker(new MarkerOptions().position(latLng).title(jsonArrayUsers.getJSONObject(i).getString("name")));
                        if (i==0){
                            googleMapAccessoryRepairer.clear();
                            googleMapAccessoryRepairer.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                        }
                        usersModel = new UsersModel("",longi,lat,jsonArrayUsers.getJSONObject(i).getString("name"), jsonArrayUsers.getJSONObject(i).getString("id"));
                        usersModel.toString();
                        allaccessoryRepairs.add(usersModel);
                    }
                    tutorsAdapter.notifyDataSetChanged();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
