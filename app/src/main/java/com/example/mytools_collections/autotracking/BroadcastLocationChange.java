package com.example.mytools_collections.autotracking;

import android.app.IntentService;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;

import androidx.annotation.Nullable;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Marty on 11/25/2017.
 */

class BroadcastLocationChange extends IntentService {

    ArrayList<Location> points;
    Bundle bundle;

    public BroadcastLocationChange(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
//        bundle = new Bundle();
//        final ResultReceiver resultReceiver = intent.getParcelableExtra("receiver");
//        if(intent !=null) {
//            Bundle b = intent.getExtras();
//            if (b != null) {
//                try {
//                    points = intent.getParcelableArrayListExtra("points");
////                bundle.putParcelableArrayList("points",points);
//                    bundle.putString("lat", Double.toString(points.get(points.size() - 1).getLatitude()));
//                    bundle.putString("lng", Double.toString(points.get(points.size() - 1).getLongitude()));
//                    resultReceiver.send(1, bundle);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    resultReceiver.send(2,Bundle.EMPTY);
//                }
//
//            }else {
//                resultReceiver.send(2,Bundle.EMPTY);
//            }
//        }
    }
}
