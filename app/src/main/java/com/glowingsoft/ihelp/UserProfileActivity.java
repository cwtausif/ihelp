package com.glowingsoft.ihelp;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import glowingsoft.com.ihelp.R;

public class UserProfileActivity extends MainActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        mContext = UserProfileActivity.this;

        getViews();
        setValues();

    }

    protected void setValues() {
        userProfileRequest();
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){

        }
    }

    private void getViews() {
        textViewName = (TextView) findViewById(R.id.name_tv);
        descriptionEt = (EditText) findViewById(R.id.description_et);
        locationEt = (EditText) findViewById(R.id.location_et);
        websiteEt = (EditText) findViewById(R.id.website_et);
        githubEt = (EditText) findViewById(R.id.github_et);
        linkedinEt = (EditText) findViewById(R.id.linkedin_et);
        twitterEt = (EditText) findViewById(R.id.twitter_et);


        mPb = (ProgressBar) findViewById(R.id.mPbar);

    }
}
