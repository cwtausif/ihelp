package com.glowingsoft.ihelp;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

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

        ratingsTv = (TextView) findViewById(R.id.ratings_tv);
        totalRatings = (TextView) findViewById(R.id.totalRatings);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        paymentBtn = (Button) findViewById(R.id.paymentBtn);

        paymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,Payment.class);
                startActivity(intent);
            }
        });

        oneTv = (TextView) findViewById(R.id.one_tv);
        twoTv = (TextView) findViewById(R.id.two_tv);
        threeTv = (TextView) findViewById(R.id.three_tv);
        fourTv = (TextView) findViewById(R.id.four_tv);
        fiveTv = (TextView) findViewById(R.id.five_tv);

        addReviewBtn = (Button) findViewById(R.id.addReview_btn);
        addReviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showDialogBox();
            }
        });

        reviewsListView = (ListView) findViewById(R.id.reviews_listview);
        reviewsAdapter = new ReviewsAdapter(mContext,reviewsData);
        reviewsListView.setAdapter(reviewsAdapter);

        getAllUserReviews();
    }

    private void showDialogBox() {
        rankDialog = new Dialog(mContext, R.style.FullHeightDialog);
        rankDialog.setContentView(R.layout.rank_dialog);
        rankDialog.setCancelable(true);
        final RatingBar ratingBar = (RatingBar)rankDialog.findViewById(R.id.dialog_ratingbar);
        ratingBar.setRating(5);

        final TextView text = (TextView) rankDialog.findViewById(R.id.rank_dialog_text1);
        final EditText reviewET = (EditText) rankDialog.findViewById(R.id.reviewText_et);
        //text.setText(name);

        Button updateButton = (Button) rankDialog.findViewById(R.id.rank_dialog_button);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reviewText = reviewET.getText().toString();

                if (reviewText.length()==0){
                 showToast("Review Text can't be empty!","short");
                }else {
                    userRating = (int) ratingBar.getRating();
                    Log.d("response ","rating "+userRating+" review: "+reviewText);
                    sendReviewToServer();
                    rankDialog.dismiss();
                }
            }
        });
        //now that the dialog is set up, it's time to show it
        rankDialog.show();
    }
}
