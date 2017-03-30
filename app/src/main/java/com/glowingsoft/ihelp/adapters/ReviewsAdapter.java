package com.glowingsoft.ihelp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.glowingsoft.ihelp.CircleTransform;
import com.glowingsoft.ihelp.models.ReviewsModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import glowingsoft.com.ihelp.R;

/**
 * Created by mg on 2/12/2017.
 */
public class ReviewsAdapter extends BaseAdapter {
    ArrayList<ReviewsModel> mData;
    LayoutInflater layoutInflater;
    Context mContext;
    ReviewsModel reviewsModel;
    public ReviewsAdapter(Context mContext, ArrayList<ReviewsModel> Data){
        this.mContext = mContext;
        this.mData = Data;
        layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.review_layout,null);
        TextView title = (TextView) view.findViewById(R.id.name);
        ImageView tutorImage = (ImageView) view.findViewById(R.id.image);
        TextView review_tv = (TextView) view.findViewById(R.id.review_tv);
        RatingBar rateBar = (RatingBar) view.findViewById(R.id.rateBar);
        reviewsModel = new ReviewsModel();
        reviewsModel = mData.get(position);
        title.setText(reviewsModel.getName()+"");
        review_tv.setText(reviewsModel.getReview()+"");
        try {
            rateBar.setRating(Float.parseFloat(reviewsModel.getRating()));
        }catch (Exception e){
            e.printStackTrace();
        }
        Picasso.with(mContext).load("https://avatars.io/twitter/"+reviewsModel.getName()+"")  .transform(new CircleTransform()).into(tutorImage);

        return view;
    }
}
