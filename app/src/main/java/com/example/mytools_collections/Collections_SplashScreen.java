package com.example.mytools_collections;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Collections_SplashScreen extends AppCompatActivity {

    private static final int MY_PERMISSION_REQUEST_CODE = 101;

    String[] appPermissions = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.CAMERA,
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collections_splashscreen);

        if (checkLocationPermission()) {
            startActivity(new Intent(Collections_SplashScreen.this, Login2.class));
            finish();
        }
    }


    //..............................................................................................
    public boolean checkLocationPermission() {
        List<String> listPermissionNeeded = new ArrayList<>();
        for (String permit : appPermissions) {
            if (ContextCompat.checkSelfPermission(this, permit) != PackageManager.PERMISSION_GRANTED) {
                listPermissionNeeded.add(permit);
            }
        }
        if (!listPermissionNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionNeeded.toArray(new String[listPermissionNeeded.size()]),
                    MY_PERMISSION_REQUEST_CODE);
            return false;
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_CODE: {
                HashMap<String, Integer> permissionResult = new HashMap<>();
                int deniedCount = 0;
                for (int i = 0; i < grantResults.length; i++) {
                    // If request is cancelled, the result arrays are empty.
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        permissionResult.put(permissions[i], grantResults[i]);
                        deniedCount++;
                    }
                }
                if (deniedCount == 0) {
                    startActivity(new Intent(Collections_SplashScreen.this, Login2.class));
                    finish();
                } else {
                    for (Map.Entry<String, Integer> entry : permissionResult.entrySet()) {
                        String permName = entry.getKey();
                        int permResult = entry.getValue();

                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permName)) {
                            showDialog("", "Aplikasi Ini Butuh Akses Lokasi Dan Penyimpana Untuk Bekerja Dengan Baik",
                                    "Ya, Izinkan",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                            checkLocationPermission();
                                        }
                                    }, "Tidak, Keluar Aplikasi", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                            finish();
                                        }
                                    }, false);
                        } else {
                            showDialog("", "Semua Akses Ditolak. Selanjutnya Untuk Memberi Akses pergi ke [Settings] -> [Permission]",
                                    "Go to Settings",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                                    Uri.fromParts("package", getPackageName(), null));
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }, "Tidak, Keluar Aplikasi", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                            finish();
                                        }
                                    }, false);
                            break;
                        }
                    }
                }
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    private AlertDialog showDialog(String title, String msg, String positiveLabel,
                                   DialogInterface.OnClickListener onClickListenerPositive,
                                   String negativeLabel, DialogInterface.OnClickListener onClickListenerNegative, boolean isCancelAble) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton(positiveLabel, onClickListenerPositive);
        builder.setNegativeButton(negativeLabel, onClickListenerNegative);
        builder.setCancelable(isCancelAble);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        return alertDialog;
    }
//..............................................................................................
}