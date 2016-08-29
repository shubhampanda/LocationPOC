package com.example.shubham.locationpoc;

import android.app.IntentService;
import android.content.Intent;
import android.provider.SyncStateContract;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

/**
 * Created by Shubham on 5/30/2016.
 */
public class ActivityRecognizedService extends IntentService {

    public static final String PACKAGE_NAME = "com.google.android.gms.location.activityrecognition";
    public static final String BROADCAST_ACTION = PACKAGE_NAME + ".BROADCAST_ACTION";

    public ActivityRecognizedService() {
        super("ActivityRecognizedService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
        DetectedActivity detectedActivity = result.getMostProbableActivity();
        Log.d("ActivityService", "The detected activity is " + detectedActivity.toString());
        Intent localintent = new Intent(BROADCAST_ACTION);
        localintent.putExtra("activityType", detectedActivity.getType());
        LocalBroadcastManager.getInstance(this).sendBroadcast(localintent);
    }
}
