package com.example.shubham.locationpoc;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by Shubham on 6/7/2016.
 */
public class Place extends RealmObject {
    @PrimaryKey
    private String placeid;
    @Required
    private String name;
    private byte[] decodedString;
    private String about;
    private float likelihood;
    private String address;
    private String phone;
    private String email;
    private double distance;
    private int placeScore;


    public void setAbout(String about) {
        this.about = about;
    }


    public void setName(String name) {
        this.name = name;
    }


    public void setPlaceid(String placeid) {
        this.placeid = placeid;
    }


    public void setDecodedString(byte[] decodedString) {
        this.decodedString = decodedString;
    }

    public void setLikelihood(float likelihood) {
        this.likelihood = likelihood;
    }


    public String getPlaceid() {
        return placeid;
    }


    public byte[] getDecodedString() {
        return decodedString;
    }


    public String getAbout() {
        return about;
    }


    public float getLikelihood() {
        return likelihood;
    }


    public String getName() {
        return name;
    }


    public String getEmail() {
        return email;
    }


    public String getAddress() {
        return address;
    }


    public String getPhone() {
        return phone;
    }


    public void setEmail(String email) {
        this.email = email;
    }


    public void setAddress(String address) {
        this.address = address;
    }


    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }


    public double getDistance() {
        return distance;
    }


    public void setPlaceScore(int placeScore) {
        this.placeScore = placeScore;
    }

    public int getPlaceScore() {
        return placeScore;
    }
}
