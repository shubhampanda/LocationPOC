package com.example.shubham.locationpoc;

import android.content.Context;

import io.realm.Realm;

/**
 * Created by Shubham on 6/7/2016.
 */
public class PlaceDataHelper {
    private Context mContext;
    private Realm realm;


    public PlaceDataHelper(Context context) {
        mContext = context;
        realm = Realm.getInstance(mContext);
    }


    public void SaveProfileData(final String placeid, final String name, final float likelihood, final String address, final String phone, final String email, final double distance) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Place place = realm.createObject(Place.class);
                place.setPlaceid(placeid);
                place.setName(name);
                place.setLikelihood(likelihood);
                place.setAddress(address);
                place.setPhone(phone);
                place.setEmail(email);
                place.setDistance(distance);
            }
        }, new Realm.Transaction.Callback() {
            @Override
            public void onSuccess() {
                super.onSuccess();
            }

            @Override
            public void onError(Exception e) {
                super.onError(e);
            }
        });
    }




}
