package com.example.shubham.locationpoc;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.PlaceDetectionApi;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Shubham on 5/31/2016.
 */
public class BackgroundService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, SharedPreferences.OnSharedPreferenceChangeListener, com.google.android.gms.location.LocationListener {
    GoogleApiClient mGoogleApiClient;
    PendingIntent pendingIntent;
    SharedPreferences sharedPreferences;
    LocationRequest mLocationRequest;
    UpdateCountDownTimer updateCountDownTimer;
    int activityType;
    Realm realm;
    boolean isLocationUpdate;
    private static final int REQUEST_CHECK_SETTINGS = 1;


    @Override
    public void onConnected(Bundle bundle) {
        startActivityUpdates();
        createLocationRequest();
        startLocationUpdates();
        updateCountDownTimer = new UpdateCountDownTimer(30000, 50);
        updateCountDownTimer.start();
        isLocationUpdate = true;
    }

    private void startLocationUpdates() {
        Log.d("BackgroundService", "The location updates have started");
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        isLocationUpdate = true;
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void startActivityUpdates() {
        ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(mGoogleApiClient, 3000, getActivityDetectionPendingIntent());
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startGoogleApiClient();
        ActivityDetectionBroadcastReceiver mBroadcastReceiver = new ActivityDetectionBroadcastReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver, new IntentFilter(ActivityRecognizedService.BROADCAST_ACTION));
        sharedPreferences = getSharedPreferences("myPREF", Context.MODE_PRIVATE);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        realm = Realm.getInstance(this);
        return START_STICKY;
    }

    private void startGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(ActivityRecognition.API)
                .addApi(LocationServices.API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
        mGoogleApiClient.connect();
    }


    private PendingIntent getActivityDetectionPendingIntent() {
        Intent intent = new Intent(this, ActivityRecognizedService.class);
        pendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        Log.d("BackgroundService", "The activityType is " + sharedPreferences.getInt("activityType", 0));
        int activityType = sharedPreferences.getInt("activityType", 0);
        String activity = null;
        switch (activityType) {
            case DetectedActivity.IN_VEHICLE:
                activity = "IN VEHICLE";
                if (isLocationUpdate) {
                    stopLocationUpdates();
                }
                break;

            case DetectedActivity.ON_FOOT:
                activity = "ON FOOT";
                if (isLocationUpdate) {
                    stopLocationUpdates();
                }
                break;


            case DetectedActivity.RUNNING:
                activity = "RUNNING";
                if (isLocationUpdate) {
                    stopLocationUpdates();
                }
                break;


            case DetectedActivity.TILTING:
                activity = "TILTING";
                if (isLocationUpdate) {
                    stopLocationUpdates();
                }
                break;


            case DetectedActivity.WALKING:
                activity = "WALKING";
                if (isLocationUpdate) {
                    stopLocationUpdates();
                }
                break;


            case DetectedActivity.ON_BICYCLE:
                activity = "ON BICYCLE";
                if (isLocationUpdate) {
                    stopLocationUpdates();
                }
                break;


            case DetectedActivity.STILL:
                activity = "STILL";
                if (!isLocationUpdate) {
                    startLocationUpdates();
                    //
                    updateCountDownTimer = new UpdateCountDownTimer(30000, 50);
                    updateCountDownTimer.start();
                }
                break;

            case DetectedActivity.UNKNOWN:
                activity = "UNKNOWN";
                if (isLocationUpdate) {
                    stopLocationUpdates();
                }
                break;

            default:
                activity = "UNKNOWN";
                if (isLocationUpdate) {
                    stopLocationUpdates();
                }
                break;

        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this).setContentTitle("Activity Recognition").setContentText("The activity recognised is " + activity).setSmallIcon(R.drawable.ic_cast_light);
        Notification notification = builder.build();
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("TAG", "The location has changed");
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("locationUpdate", System.currentTimeMillis());
        LatLng newLatlng = new LatLng(location.getLatitude(), location.getLongitude());
        double oldLatitude = Double.parseDouble(sharedPreferences.getString("lat", "0"));
        double oldLongitude = Double.parseDouble(sharedPreferences.getString("long", "0"));
        editor.putString("currentlat", String.valueOf(location.getLatitude()));
        editor.putString("currentlong", String.valueOf(location.getLongitude()));
        LatLng oldLatLng = new LatLng(oldLatitude, oldLongitude);
        double distance = SphericalUtil.computeDistanceBetween(oldLatLng, newLatlng);
        Log.d("BackgroundService", "The new Latitude is " + location.getLatitude() + "The new Longitude is " + location.getLongitude());
        Log.d("BackgroundService", "The oldLatitude is " + oldLatitude + "The old longitude is " + oldLongitude);
        Log.d("BackgroundService", "The distance is " + SphericalUtil.computeDistanceBetween(oldLatLng, newLatlng));
        if (distance > 1000) {
            fetchPlaces(newLatlng);
            editor.putString("lat", String.valueOf(location.getLatitude()));
            editor.putString("long", String.valueOf(location.getLongitude()));
            editor.commit();
        }
    }


    private void fetchPlaces(final LatLng newLatlng) {
        final PlaceDataHelper placeDataHelper = new PlaceDataHelper(this);
        final PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi
                .getCurrentPlace(mGoogleApiClient, null);
        result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
            @Override
            public void onResult(PlaceLikelihoodBuffer likelyPlaces) {
                Log.d("TAG", "The likelyPlaces is " + likelyPlaces);
                Log.d("TAG", "The fetchStatus is " + likelyPlaces.getStatus().getStatusMessage());
                if (likelyPlaces.getStatus().isSuccess()) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    int fetchCount = sharedPreferences.getInt("fetchCount", 0);
                    fetchCount++;
                    editor.putInt("fetchCount", fetchCount);
                    editor.putLong("fetchUpdate", System.currentTimeMillis());
                    editor.commit();
                    realm.beginTransaction();
                    RealmResults<Place> places = realm.where(Place.class).findAll();
                    places.clear();
                    realm.commitTransaction();
                }
                for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                    Log.i("Background Service", String.format("Place '%s' has likelihood: %g",
                            placeLikelihood.getPlace().getName(),
                            placeLikelihood.getLikelihood()));
                    String placeName = placeLikelihood.getPlace().getName().toString();
                    float likehood = placeLikelihood.getLikelihood();
                    String placeid = placeLikelihood.getPlace().getId();
                    String address = "";
                    String phone = "";
                    String email = "";
                    double distance = 0;
                    if (placeLikelihood.getPlace().getWebsiteUri() != null) {
                        email = placeLikelihood.getPlace().getWebsiteUri().toString();
                    }
                    if (placeLikelihood.getPlace().getPhoneNumber() != null) {
                        phone = placeLikelihood.getPlace().getPhoneNumber().toString();

                    }
                    if (placeLikelihood.getPlace().getAddress() != null) {
                        address = placeLikelihood.getPlace().getAddress().toString();
                    }

                    if (placeLikelihood.getPlace().getLatLng() != null) {
                        LatLng placeLatLng = placeLikelihood.getPlace().getLatLng();
                        distance = SphericalUtil.computeDistanceBetween(newLatlng, placeLatLng);
                    }
                    List<Integer> placeTypes = placeLikelihood.getPlace().getPlaceTypes();
                    placeLikelihood.getPlace().get
                    computePlaceScore();
                    placeDataHelper.SaveProfileData(placeid, placeName, likehood, address, phone, email, distance);
                }
                likelyPlaces.release();
            }
        });
    }


    public class ActivityDetectionBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            activityType = intent.getIntExtra("activityType", 0);
            Log.d("BackgroundService", "The activityType is " + activityType);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("activityType", activityType);
            editor.commit();
        }
    }


    private void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        Log.d("BackgroundService", "Location updates have stopped");
        isLocationUpdate = false;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public class UpdateCountDownTimer extends CountDownTimer {

        public UpdateCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }


        @Override
        public void onTick(long l) {

        }


        @Override
        public void onFinish() {
            stopLocationUpdates();
            updateCountDownTimer.cancel();
        }

    }

    public void computePlaceScore() {

    }

}

