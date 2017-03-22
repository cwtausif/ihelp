package com.glowingsoft.ihelp;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.glowingsoft.ihelp.adapters.ReviewsAdapter;
import com.glowingsoft.ihelp.models.ReviewsModel;

import java.util.ArrayList;

import glowingsoft.com.ihelp.R;

public class ReviewsAndRatingsActivity extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews_and_ratings);

        mContext = ReviewsAndRatingsActivity.this;

        Bundle extras = getIntent().getExtras();
        user_id = extras.getString("user_id");
        Log.d("response user_id",user_id+"");
        reviewsData = new ArrayList<>();

        reviewsModel = new ReviewsModel();
        reviewsModel.setName("Tauisf");
        reviewsData.add(reviewsModel);

        reviewsListView = (ListView) findViewById(R.id.reviews_listview);
        reviewsAdapter = new ReviewsAdapter(mContext,reviewsData);
        reviewsListView.setAdapter(reviewsAdapter);
    }
}
