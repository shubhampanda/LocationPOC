package com.example.shubham.locationpoc;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Shubham on 6/7/2016.
 */
public class InfoActivity extends Activity {

    TextView info;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_activity_xml);
        info = (TextView) findViewById(R.id.text);
        sharedPreferences = getSharedPreferences("myPREF", Context.MODE_PRIVATE);
        setInfo();

    }

    private void setInfo() {
        info.setText("FetchUpdateTime : " + getDate(sharedPreferences.getLong("fetchUpdate",0)) + " at " + getTime(sharedPreferences.getLong("fetchUpdate",0)) + "\n" +
                        "LocationUpdateTime : " + getDate(sharedPreferences.getLong("locationUpdate",0)) + " at " + getTime(sharedPreferences.getLong("locationUpdate",0)) + "\n"
         + "FetchCount : " + sharedPreferences.getInt("fetchCount",0));
    }



    private String getDate(long timeStamp) {
        Calendar cal = Calendar.getInstance();
        Date date = new Date(timeStamp);
        cal.setTime(date);
        return cal.get(Calendar.DATE) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR);
    }


    private String getTime(long timeStamp) {
        Date date = new Date(timeStamp);
        String strDateFormat = "HH:mm a";
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
        return sdf.format(date);

    }
}