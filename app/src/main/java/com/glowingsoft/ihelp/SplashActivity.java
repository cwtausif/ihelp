package com.glowingsoft.ihelp;

import android.content.Intent;
import android.os.Bundle;

import glowingsoft.com.ihelp.R;

public class SplashActivity extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.splash);

        Thread background = new Thread() {
            public void run() {
                try {
                    // Thread will sleep for 5 seconds
                    sleep(3 * 1000);
                    if (isLoggedIn()) {
                        startActivity(new Intent(getApplicationContext(), UserProfileActivity.class));
                        finish();
                    }
                    else {
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finish();
                    }

                } catch (Exception e) {
                }
            }
        };
        // start thread
        background.start();
    }
}
