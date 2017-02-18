package com.glowingsoft.ihelp;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import glowingsoft.com.ihelp.R;

public class RepairCarActivity extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);

        carRepairLayout = (LinearLayout) findViewById(R.id.car_repair_layout);
        homeLayout = (LinearLayout) findViewById(R.id.layout_home);
        //Hide other layouts than carRepair
        homeLayout.setVisibility(View.GONE);
    }
}
