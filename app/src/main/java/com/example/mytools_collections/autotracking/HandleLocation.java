package com.example.mytools_collections.autotracking;

import android.app.IntentService;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.Nullable;

import org.json.JSONArray;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HandleLocation extends IntentService {
    public static ArrayList<Location> points;
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public HandleLocation(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(intent !=null) {
            Bundle b = intent.getExtras();
            if (b != null) {
                points = intent.getParcelableArrayListExtra("points");
            }
        }
    }

    public static List<String> getLatLng(){
        List<String> latlng = null;
        String lat = "98.8229093";
        String lng = "1.6904597";
        if (points != null) {
            for (int i = 0; i < points.size(); i++) {
                lat = Double.toString(points.get(i).getLongitude());
                lng = Double.toString(points.get(i).getLatitude());
            }
        }
        latlng.add(lat);
        latlng.add(lng);
        return latlng;
    }
}
