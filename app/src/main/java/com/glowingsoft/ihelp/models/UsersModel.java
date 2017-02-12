package com.glowingsoft.ihelp.models;

import android.util.Log;

/**
 * Created by mg on 2/12/2017.
 */
public class UsersModel {
    String id,name,email,category_id;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    double latitude;
    double longitude;

    public UsersModel(double longitude, double latitude, String name, String id) {
        this.name = name;
        this.id = id;
        this.longitude = longitude;
        this.latitude =latitude;
    }

    public UsersModel() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    @Override
    public String toString() {
        Log.d("response User Model","id "+id+" name "+name+" latitude "+latitude+" longitude "+longitude);
        return "";
    }
}
