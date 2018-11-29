package com.example.fuleehnzsolt.sapi_advertiser.Data;

public class Advertise
{
    private String title;
    private String sDescription;
    private String lDescription;
    private String phoneNumber;
    private String location;
    private String image;

    public Advertise() {
    }

    public Advertise(String title, String sDescription, String lDescription, String phoneNumber, String location, String image) {
        this.title = title;
        this.sDescription = sDescription;
        this.lDescription = lDescription;
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

    public String getsDescription() {
        return sDescription;
    }

    public void setsDescription(String sDescription) {
        this.sDescription = sDescription;
    }

    public String getlDescription() {
        return lDescription;
    }

    public void setlDescription(String lDescription) {
        this.lDescription = lDescription;
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
