package com.example.mytools_collections.autotracking;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.mytools_collections.Collections_Constants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.UUID;


/**
 * Created by Marty on 12/20/2017.
 */

public class UploadLocationService extends IntentService {

    ArrayList<Location> points;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private String userid;

    public UploadLocationService(){
        super("UploadLocationService");
    }
    public UploadLocationService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(intent !=null) {
            Bundle b = intent.getExtras();
            if (b != null) {
                points = intent.getParcelableArrayListExtra("points");
                pref = getSharedPreferences("MyPref",0);
                userid = pref.getString("userid",null);
                URL url;
                try {
                        url = new URL(Collections_Constants.UPLOAD_URL_AUTOTRACKING); // set your server url
                        sendLocation(url, UUID.randomUUID().toString());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void sendLocation(URL url, String token){
        try {
            String jsonResp;
            int code = 0;

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id","userid");
            JSONArray pointsArray = new JSONArray();
            for (int i = 0; i < points.size(); i++) {
                pointsArray.put(new JSONArray().put(points.get(i).getLongitude()).put(points.get(i).getLatitude()).put(userid));
            }
            jsonObject.put("coordinates",pointsArray);
            Log.d("data sent", jsonObject.toString());
            if (pointsArray.length() != 0) {
                jsonResp = ServiceCall.doServerCall("POST", url, pointsArray.toString(), token);
                if (jsonResp != null && jsonResp.equals(ServiceCall.TIME_OUT)) {
                    code = 500;
                    Toast.makeText(getApplicationContext(),"Lokasi Gagal di Insert (TIME OUT)", Toast.LENGTH_SHORT).show();
                    return;
                } else if (jsonResp == null) {
                    code = 500;
                    Toast.makeText(getApplicationContext(),"Lokasi Gagal di Insert (404)", Toast.LENGTH_SHORT).show();
                    return;
                }
//                Log.d("resp", jsonResp);
//                JSONObject json = new JSONObject(jsonResp);
//                code = Integer.parseInt(json.getString("code"));
            } else {
                code = 100;
            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
