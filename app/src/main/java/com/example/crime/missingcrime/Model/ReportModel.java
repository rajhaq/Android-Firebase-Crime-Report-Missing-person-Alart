package com.example.crime.missingcrime.Model;

public class ReportModel {
    String id, title, type, location, image, time, user_id;

    public ReportModel() {
    }

    public ReportModel(String id, String title, String type, String location, String image, String time, String user_id) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.location = location;
        this.image = image;
        this.time = time;
        this.user_id = user_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
