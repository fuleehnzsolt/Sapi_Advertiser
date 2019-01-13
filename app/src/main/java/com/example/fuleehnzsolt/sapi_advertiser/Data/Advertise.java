package com.example.fuleehnzsolt.sapi_advertiser.Data;

public class Advertise {
    private String title;
    private String shortDescription;
    private String longDescription;
    private String phoneNumber;
    private String location;
    private String image;

    public Advertise() {
    }

    public Advertise(String title, String shortDescription, String longDescription, String phoneNumber, String location, String image) {
        this.title = title;
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;
        this.phoneNumber = phoneNumber;
        this.location = location;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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
}
