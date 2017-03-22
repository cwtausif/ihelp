package com.glowingsoft.ihelp.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.glowingsoft.ihelp.CircleTransform;
import com.glowingsoft.ihelp.ReviewsAndRatingsActivity;
import com.glowingsoft.ihelp.models.UsersModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import glowingsoft.com.ihelp.R;

/**
 * Created by mg on 2/12/2017.
 */
public class TutorsAdapter extends BaseAdapter {
    ArrayList<UsersModel> usersData;
    LayoutInflater layoutInflater;
    Context mContext;
    UsersModel usersModel;
    public TutorsAdapter(Context mContext, ArrayList<UsersModel> tutorsData){
        this.mContext = mContext;
        usersData = tutorsData;
        layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return usersData.size();
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
        View view = layoutInflater.inflate(R.layout.tutor_row,null);
        TextView title = (TextView) view.findViewById(R.id.name);
        ImageView tutorImage = (ImageView) view.findViewById(R.id.tutorImage);
        TextView tutorCategory = (TextView) view.findViewById(R.id.tutorCategory);
        usersModel = new UsersModel();
        usersModel = usersData.get(position);
        title.setText(usersModel.getName()+"");
        Picasso.with(mContext).load("https://avatars.io/twitter/"+usersModel.getName()+"")  .transform(new CircleTransform()).into(tutorImage);

        try{
            tutorCategory.setText(usersModel.getCategoryTitle());
        }catch (Exception e){
            e.printStackTrace();
        }
        view.setTag(position);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = (int) v.getTag();
                Log.d("response position",pos+"");
                Intent intent = new Intent(mContext, ReviewsAndRatingsActivity.class);
                intent.putExtra("user_id",usersData.get(pos).getId());
                mContext.startActivity(intent);
            }
        });
        return view;
    }
}
