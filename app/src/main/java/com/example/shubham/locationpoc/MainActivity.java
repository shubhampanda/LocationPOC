package com.example.shubham.locationpoc;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.ActivityRecognitionApi;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceDetectionApi;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class MainActivity extends AppCompatActivity {

    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    PendingIntent pendingIntent;
    // Get a receiver for broadcasts from ActivityDetectionIntentService.
 /*   ActivityDetectionBroadcastReceiver mBroadcastReceiver;*/
    public static final String PACKAGE_NAME = "com.google.android.gms.location.activityrecognition";
    public static final String BROADCAST_ACTION = PACKAGE_NAME + ".BROADCAST_ACTION";
    TextView detectedActivityText;
    Realm realm;
    ListView rvUserProfile;
    PlaceProfileAdapter placeProfileAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(MainActivity.this,InfoActivity.class);
                startActivity(intent);
            }
        });
     // Since the change
        realm = Realm.getInstance(this);
        rvUserProfile = (ListView) findViewById(R.id.rvUserProfile);
        setupAdapter();
     /*   detectedActivityText = (TextView) findViewById(R.id.detectedactivity);
        // Get a receiver for broadcasts from ActivityDetectionIntentService.
        mBroadcastReceiver = new ActivityDetectionBroadcastReceiver();*/

        // Create an instance of GoogleAPIClient.
 /*       if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(ActivityRecognition.API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .enableAutoManage(this, this)
                    .build();
        }
        mGoogleApiClient.connect();*/
        startService(new Intent(getBaseContext(), BackgroundService.class));
    }

    private void setupAdapter() {
        RealmResults<com.example.shubham.locationpoc.Place> places = realm.where(com.example.shubham.locationpoc.Place.class).findAll();
        places.sort("distance", Sort.ASCENDING);
        placeProfileAdapter = new PlaceProfileAdapter(this, R.layout.place_item_view, places, true);
        rvUserProfile.setAdapter(placeProfileAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }





}

