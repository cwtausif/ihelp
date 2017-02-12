package com.glowingsoft.ihelp.models;

/**
 * Created by mg on 2/12/2017.
 */
public class TutorCategoriesModel {
    String id;

    public TutorCategoriesModel(String title, String id) {
        this.id = id;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String title;
}
