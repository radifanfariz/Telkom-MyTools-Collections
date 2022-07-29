package com.example.mytools_collections;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.mytools_collections.apihelper.BaseApiService;
import com.example.mytools_collections.apihelper.UtilsApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login2 extends AppCompatActivity {
    TextView imei;
    TextView imei_number;
    String IMEI_Number_Holder;

    TextView subscid;
    TextView subscid_number;
    String SUBSCID_Number_Holder;


    EditText userid;

    EditText pass;


    EditText userid_parameter;

    TelephonyManager telephonyManager;

    private static final int STORAGE_PERMISSION_CODE = 123;


    public static final String MyPREFERENCES = "MyPrefs";


    Button login;
    ProgressDialog progressDialog;
//    ConnectionClass connectionClass;

    // Date interval variables
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    String CurDate, PrevDate, NextDate;
    Date curDate, prevDate, nextDate;


    public static final String ID = "nameKey";
    public static final String Passw = "phoneKey";

    SharedPreferences sharedPreferences;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    BaseApiService mApiService;

    String useridstr1;
    String imeistr1 ;
    String subscidstr1;
    String flagging = "01";
    String apps_name = "mytoolssf";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        userid = (EditText) findViewById(R.id.userid);
        imei = (TextView) findViewById(R.id.imei);
        subscid = (TextView) findViewById(R.id.subscid);


        pass = (EditText) findViewById(R.id.pass);
        login = (Button) findViewById(R.id.login);

        //SharedPreference untuk login state
        pref = getApplicationContext().getSharedPreferences("MyPref",0);
        editor = pref.edit();

        mApiService = UtilsApi.getAPIService(); // meng-init yang ada di package apihelper


        //checkLocationPermission();

//        connectionClass = new ConnectionClass();
        progressDialog = new ProgressDialog(this);
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

//        if (pref.getBoolean("logged",false)){
//            Intent intent = new Intent(Login2.this, Maps_NavigationDrawer.class);
//            startActivity(intent);
//            finish();
//        }


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = ProgressDialog.show(Login2.this, null, "Harap Tunggu...", true, false);
                requestLogin();
            }
        });
        //zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz

        //IMEI

        imei_number = (TextView) findViewById(R.id.imei);
        subscid_number = (TextView) findViewById(R.id.subscid);


        telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        IMEI_Number_Holder = telephonyManager.getDeviceId(); //it's oke
        imei_number.setText(IMEI_Number_Holder);

        SUBSCID_Number_Holder = telephonyManager.getSubscriberId(); //it's oke
        subscid_number.setText(SUBSCID_Number_Holder);





        isPermissionGranted(); //call AndroidPermission

        requestStoragePermission1();

    }

//,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,


    //KKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKK

    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }




    private void onRequestPermissionsResult1(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {

            case 1: { //2

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_SHORT).show();
                    //do ur specific task after read phone state granted
                } else {
                    Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;

            }



        }
    }


    //KKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKK


    //,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,

    //Requesting permission
    private void requestStoragePermission1() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }







    //zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz ACCESS LOCATION PERMISSION zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz
    private static final int MY_PERMISSION_LOCATION_REQUEST_CODE = 88;
    //permission for android 6 +
    public boolean checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSION_LOCATION_REQUEST_CODE);
            }else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSION_LOCATION_REQUEST_CODE);
            }
            return false;
        }else {
            return true;
        }
    }




    //zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz END ACCESS LOCATION PERMISSION zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz




    //IMEI xxxxxxxxxxxxxxxxxxxxxxxxxxxxx ANDROID PERMISSION V6 XXXXXXXXXXXX IS WORKING PRPERLY FOR ANDROID VER 6 XXXXXXXXXXXXX
    public  boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG","Permission is granted");
                return true;
            } else {

                Log.v("TAG","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 2);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG","Permission is granted");
            return true;
        }
    }



    //xxxxxxxxxxxxxxxxxxxxxxxxxxxxx ANDROID PERMISSION V6 XXXXXXXXXXXX IS WORKING PRPERLY FOR ANDROID VER 6 XXXXXXXXXXXXX


    public void requestLogin(){
        useridstr1 = userid.getText().toString();
        imeistr1 = imei.getText().toString();
        subscidstr1 = subscid.getText().toString();
        mApiService.loginRequest(userid.getText().toString(), pass.getText().toString(),flagging,apps_name)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            progressDialog.dismiss();
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("error").equals("false")){
                                    // Jika login berhasil maka data nama yang ada di response API
                                    // akan diparsing ke activity selanjutnya.

                                    editor.putString("userid",useridstr1).apply();
                                    editor.putString("imei",imeistr1).apply();
                                    editor.putString("subscid",subscidstr1).apply();
                                    editor.putBoolean("logged",true).apply();

                                    editor.commit();

                                    Toast.makeText(Login2.this, "BERHASIL LOGIN", Toast.LENGTH_SHORT).show();
//                                    String nama = jsonRESULTS.getJSONObject("user").getString("nama");
                                    Intent intent = new Intent(Login2.this, MainMenu
                                            .class);
//                                    intent.putExtra("result_nama", nama);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // Jika login gagal
                                    String error_message = jsonRESULTS.getString("error_msg");
                                    Toast.makeText(Login2.this, error_message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("debug", "onFailure: ERROR > " + t.toString());
                        Toast.makeText(Login2.this, "Koneksi Bermasalah...", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
    }
}
