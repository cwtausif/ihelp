package com.glowingsoft.ihelp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
            case R.id.homeBtn:
                Intent intent = new Intent(mContext,HomeScreen.class);
                startActivity(intent);
                break;
            case R.id.save_btn:
                validateUserProfile();
                break;
        }
    }

    private void validateUserProfile() {
        description = descriptionEt.getText().toString();
        website =websiteEt.getText().toString();
        github = githubEt.getText().toString();
        linkedin = linkedinEt.getText().toString();
        twitter = twitterEt.getText().toString();
        saveUserProfile();
    }

    private void getViews() {
        textViewName = (TextView) findViewById(R.id.name_tv);
        descriptionEt = (EditText) findViewById(R.id.description_et);
        locationEt = (EditText) findViewById(R.id.location_et);
        websiteEt = (EditText) findViewById(R.id.website_et);
        githubEt = (EditText) findViewById(R.id.github_et);
        linkedinEt = (EditText) findViewById(R.id.linkedin_et);
        twitterEt = (EditText) findViewById(R.id.twitter_et);
        picProfile = (ImageView) findViewById(R.id.picProfile);
        homeBtn = (Button) findViewById(R.id.homeBtn);
        saveBtn = (Button) findViewById(R.id.save_btn);


        mPb = (ProgressBar) findViewById(R.id.mPbar);
        picProfile.setOnClickListener(this);
        homeBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);

    }
}
