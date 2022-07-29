package com.example.mytools_collections;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.ResultReceiver;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.mytools_collections.adapter.DataCollections_GetSet;
import com.example.mytools_collections.adapter.ListCollections_Getset;
import com.example.mytools_collections.autotracking.HandleLocation;
import com.example.mytools_collections.autotracking.LocationJobService;
import com.example.mytools_collections.autotracking.LocationTrack;
import com.google.android.material.textfield.TextInputEditText;

import net.gotev.uploadservice.MultipartUploadRequest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    Bundle data;
    ListCollections_Getset collections_data;
    DataCollections_GetSet data_collections;
    TextInputEditText edtUserId,edtServNum,edtCustName,edtAlamat,edtLatLng,edtKet;
    CardView btnPicture,btnSave;
    Spinner spinStatus;
    ImageView imgPhoto;
    private String imageFilePathOri;
    private List<String> imagePathList;
    private String imagePath;
    private RequestOptions options;
    private String userid;
    ArrayAdapter<CharSequence> spinnerAdapter;
    String lat,lng;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    boolean registered = false, isServiceStarted=false;
    public static final String JOB_STATE_CHANGED = "jobStateChanged";
    public static final String LOCATION_ACQUIRED = "locAcquired";
    LocationTrack locationTrack;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //if get rom intent directly not from bundle
//        Intent intent =  getIntent();
//        collections_data = intent.getParcelableExtra("collections_data");
        //////////////////////////////////////////////////
        locationTrack = new LocationTrack(MainActivity.this);
        if (locationTrack.getLocation() == null){
            locationTrack.showSettingsAlert();
        }
        startBackgroundService();
//        latlng = HandleLocation.getLatLng();
        if (getIntent().hasExtra("collections_data")){
            data = getIntent().getExtras();
            collections_data = (ListCollections_Getset) data.getParcelable("collections_data");
        }else if(getIntent().hasExtra("data_collections")){
            data = getIntent().getExtras();
            data_collections = (DataCollections_GetSet) data.getParcelable("data_collections");
        }
        setContentView(R.layout.activity_main);

        pref = getSharedPreferences("MyPref",0);
        userid = pref.getString("userid",null);

        edtUserId = findViewById(R.id.edt_userid);
        edtServNum = findViewById(R.id.edt_serv_num);
        edtCustName = findViewById(R.id.edt_cust_name);
        edtLatLng = findViewById(R.id.edt_latlng);
        edtAlamat = findViewById(R.id.edt_alamat);
        edtKet = findViewById(R.id.edt_ket);

        btnPicture = findViewById(R.id.card_btn_picture);
        btnSave = findViewById(R.id.card_btn_save);

        spinStatus = findViewById(R.id.status);
        spinnerAdapter = ArrayAdapter.createFromResource(this,R.array.status,android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinStatus.setAdapter(spinnerAdapter);

        imgPhoto = findViewById(R.id.photo);

        imagePathList = new ArrayList<String>();

        options = new RequestOptions().fitCenter().placeholder(R.mipmap.noimage).diskCacheStrategy(DiskCacheStrategy.ALL).skipMemoryCache(true);

        if (collections_data != null){
            set_form();
        }else {
            set_form_2();
        }

        if(!registered) {
            IntentFilter i = new IntentFilter(JOB_STATE_CHANGED);
            i.addAction(LOCATION_ACQUIRED);
            LocalBroadcastManager.getInstance(this).registerReceiver(jobStateChanged, i);
        }

        btnPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                take_picture();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uploadMultipart() == true) {
                    stopBackgroundService();
                    Intent intent = new Intent(MainActivity.this, SuccessActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(), "Gagal...!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

//        Toast.makeText(getApplicationContext(), latlng.indexOf(0), Toast.LENGTH_SHORT).show();
    }

    private void set_form(){
        url = Collections_Constants.UPLOAD_URL_1;
        edtUserId.setText(userid);
        edtServNum.setText(collections_data.getService_number());
        edtCustName.setText(collections_data.getCust_name());
        edtAlamat.setText(collections_data.getCust_addr());
    }

    private void set_form_2(){
        url = Collections_Constants.UPLOAD_URL_2;
        edtUserId.setText(userid);
        edtServNum.setText(data_collections.getServ_num());
        edtCustName.setText(data_collections.getCust_name());
        edtAlamat.setText(data_collections.getAlamat());
        lat = data_collections.getLat();
        lng = data_collections.getLng();
        edtLatLng.setText(data_collections .getLat()+"/"+data_collections.getLng());
        int spinnerPosition = spinnerAdapter.getPosition(data_collections.getStatus());
        if (spinStatus != null) {
            spinStatus.setSelection(spinnerPosition);
        }
        edtKet.setText(data_collections.getKeterangan());

        Glide.with(this)
                .asBitmap()
                .apply(options)
                .load(Collections_Constants.BASIC_URL+data_collections.getPhoto_url())
                .into(imgPhoto);
//                        imagePath3 = getImgCachePath(Maps_Constants.BASIC_URL+upsfIntent.getStringExtra("url5"));
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    imagePath = getImgCachePath(Collections_Constants.BASIC_URL+data_collections.getPhoto_url());
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private String getImgCachePath(String url) throws ExecutionException, InterruptedException {
        File file = Glide.with(MainActivity.this).asFile().load(url).submit().get();
        String path = file.getAbsolutePath();
        return path;
    }

    private void take_picture(){
        String packageName = getApplicationContext().getPackageName();
        Intent photoCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (photoCaptureIntent.resolveActivity(getPackageManager()) != null){
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (photoFile != null){
                Uri photoUri = FileProvider.getUriForFile(this,getPackageName(),photoFile);
                photoCaptureIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,photoUri);
                Log.d("Filepath :",packageName.toString());
                startActivityForResult(photoCaptureIntent,PICK_IMAGE_REQUEST);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "Mytools_"+timeStamp;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(imageFileName,".jpg",storageDir);
//        File compressedImageFile = new Compressor(this).compressToFile(imageFile);
        imageFilePathOri = imageFile.getAbsolutePath();
        imagePathList.add(imageFilePathOri);
//        imageFilePathCompress = compressedImageFile.getAbsolutePath();

        return imageFile;
    };

    public void compressImage(String imageUrl,int quality){
        File file = new File(imageUrl);
        try{
            Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality , new FileOutputStream(file));
            imageUrl = file.getAbsolutePath();
            Log.d("Image Compress :","Success");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK ) {
            try {
                    Glide.with(this)
                            .asBitmap()
                            .apply(options)
                            .load(imageFilePathOri)
                            .into(imgPhoto);

                    imagePath = imageFilePathOri;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private Boolean uploadMultipart(){
        String userid = edtUserId.getText().toString();
        String servnum = edtServNum.getText().toString();
        String custname = edtCustName.getText().toString();
        String alamat = edtAlamat.getText().toString();
        String status = spinStatus.getSelectedItem().toString();
        String ket = edtKet.getText().toString();
        String picturePath = imagePath;

        try {
            String uploadId = UUID.randomUUID().toString();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.BASE){

            }

            if (collections_data == null){
                String id = data_collections.getId();
                new MultipartUploadRequest(this,uploadId,url)
                        .addFileToUpload(picturePath,"image")
                        .addParameter("id",id)
                        .addParameter("userid",userid)
                        .addParameter("servnum",servnum)
                        .addParameter("custname",custname)
                        .addParameter("alamat",alamat)
                        .addParameter("lat",lat)
                        .addParameter("lng",lng)
                        .addParameter("status",status)
                        .addParameter("ket",ket)
//                    .setNotificationConfig(new UploadNotificationConfig())
                        .setMaxRetries(2)
                        .startUpload();
            }else {
                new MultipartUploadRequest(this, uploadId, url)
                        .addFileToUpload(picturePath, "image")
                        .addParameter("userid", userid)
                        .addParameter("servnum", servnum)
                        .addParameter("custname", custname)
                        .addParameter("alamat", alamat)
                        .addParameter("lat", lat)
                        .addParameter("lng", lng)
                        .addParameter("status", status)
                        .addParameter("ket", ket)
//                    .setNotificationConfig(new UploadNotificationConfig())
                        .setMaxRetries(2)
                        .startUpload();
            }
            Toast.makeText(getApplicationContext(), "Berhasil di Save", Toast.LENGTH_SHORT).show();
            return true;
//            Intent intent = new Intent(MainActivity.this,SuccessActivity.class);
//            startActivity(intent);
//            finish();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void startBackgroundService() {
        if(!registered) {
            IntentFilter i = new IntentFilter(JOB_STATE_CHANGED);
            i.addAction(LOCATION_ACQUIRED);
            LocalBroadcastManager.getInstance(this).registerReceiver(jobStateChanged, i);
        }
        JobScheduler jobScheduler =
                (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        assert jobScheduler != null;
        jobScheduler.schedule(new JobInfo.Builder(LocationJobService.LOCATION_SERVICE_JOB_ID,
                new ComponentName(this, LocationJobService.class))
                .setOverrideDeadline(500)
                .setPersisted(true)
                .setRequiresDeviceIdle(false)
                .build());
    }

    private void stopBackgroundService() {
        if(getSharedPreferences("track",MODE_PRIVATE).getBoolean("isServiceStarted",false)){
            Log.d("registered"," on stop service");
            Intent stopJobService = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                stopJobService = new Intent(LocationJobService.ACTION_STOP_JOB);
                LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(stopJobService);
            }else{
                Toast.makeText(getApplicationContext(),"yet to be coded - stop service",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private BroadcastReceiver jobStateChanged = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction()==null){
                return;
            }
            if(intent.getAction().equals(JOB_STATE_CHANGED)) {
//                changeServiceButton(intent.getExtras().getBoolean("isStarted"));
            }else if (intent.getAction().equals(LOCATION_ACQUIRED)){
                if(intent.getExtras()!=null){
                    Bundle b = intent.getExtras();
                    Location l = b.getParcelable("location");
                    lat = Double.toString(l.getLatitude());
                    lng = Double.toString(l.getLongitude());
                    edtLatLng.setText(lat+"/"+lng);
//                    Toast.makeText(getApplicationContext(), lat+" <from Intent Service> "+lng, Toast.LENGTH_SHORT).show();
//                    updateMarker(l);
                }else{
                    Log.d("intent","null");
                }
            }
        }
    };

//    private class LatlngResultReceiver extends ResultReceiver {
//
//        /**
//         * Create a new ResultReceive to receive results.  Your
//         * {@link #onReceiveResult} method will be called from the thread running
//         * <var>handler</var> if given, or from an arbitrary thread if null.
//         *
//         * @param handler
//         */
//        public LatlngResultReceiver(Handler handler) {
//            super(handler);
//        }
//
//        @Override
//        protected void onReceiveResult(int resultCode, Bundle resultData) {
//            switch (resultCode){
//                case 2:
//                    Toast.makeText(getApplicationContext(), "No Data", Toast.LENGTH_SHORT).show();
//                    break;
//                case 1:
//                    String lat = resultData.getString("lat");
//                    String lng = resultData.getString("lng");
//                    Toast.makeText(getApplicationContext(), lat+" <from Intent Service> "+lng, Toast.LENGTH_SHORT).show();
//                    break;
//            }
//            super.onReceiveResult(resultCode, resultData);
//        }
//    }
}
