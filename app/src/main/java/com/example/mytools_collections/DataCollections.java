package com.example.mytools_collections;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mytools_collections.adapter.DataCollections_Adapter;
import com.example.mytools_collections.adapter.DataCollections_GetSet;
import com.example.mytools_collections.adapter.Loginfo_adapter;
import com.example.mytools_collections.adapter.Loginfo_getset;
import com.example.mytools_collections.apihelper.BaseApiService;
import com.example.mytools_collections.apihelper.UtilsApi;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataCollections extends AppCompatActivity implements EditText.OnEditorActionListener {

    BaseApiService mApiService;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String userid;
    String date_x;
    String date_y;
    List<DataCollections_GetSet> datacollections_list;
    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    TextInputEditText edttxt_dt_x,edttxt_dt_y;
    TextInputLayout datetime_layout_x,datetime_layout_y;
    CardView card_datetime_x,card_datetime_y,card_datetime_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datacollections);

        recyclerView = (RecyclerView) findViewById(R.id.recylcerViewDataCollections);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Data Collections");
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);

        edttxt_dt_x = findViewById(R.id.edttxt_dt_2_x);
        card_datetime_x = findViewById(R.id.card_datetime_2_x);
        datetime_layout_x = findViewById(R.id.datetime_layout_txt_2_x);
        edttxt_dt_y = findViewById(R.id.edttxt_dt_2_y);
        card_datetime_y = findViewById(R.id.card_datetime_2_y);
        datetime_layout_y = findViewById(R.id.datetime_layout_txt_2_y);
        card_datetime_submit = findViewById(R.id.card_datetime_2_submit);
        edttxt_dt_x.setOnEditorActionListener(this);
        edttxt_dt_y.setOnEditorActionListener(this);
        edttxt_dt_x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogDatePickerX();
//                Toast.makeText(getApplicationContext(), date_x, Toast.LENGTH_SHORT).show();
            }
        });
        edttxt_dt_y.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogDatePickerY();
            }
        });
        card_datetime_x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogDatePickerX();
            }
        });
        card_datetime_y.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogDatePickerY();
            }
        });
        card_datetime_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date_x = edttxt_dt_x.getText().toString();
                date_y = edttxt_dt_y.getText().toString();
                loadDataCollections(date_x,date_y,userid);
            }
        });

        datacollections_list = new ArrayList<>();
        pref = getSharedPreferences("MyPref",0);
        mApiService = UtilsApi.getAPIService();
        userid = pref.getString("userid",null);
    }

    public void loadDataCollections(String date_x,String date_y,String userid) {
        progressDialog.show();
        mApiService.loadDataCollections(date_x,date_y,userid)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            progressDialog.dismiss();
                            try {
                                JSONArray jsonArray = new JSONArray(response.body().string());
                                Toast.makeText(getApplicationContext(), "Berhasil Dimuat", Toast.LENGTH_SHORT).show();
                                datacollections_list.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonRESULTS = jsonArray.getJSONObject(i);
                                    if (jsonRESULTS.getString("error").equals("false")) {
                                        // Jika login berhasil maka data nama yang ada di response API
                                        // akan diparsing ke activity selanjutnya.
//                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
//                                        Toast.makeText(getActivity(), jsonObject.toString(), Toast.LENGTH_SHORT).show();
//                                        Toast.makeText(getActivity(), "Berhasil Dimuat", Toast.LENGTH_SHORT).show();
                                        String id = jsonRESULTS.getJSONObject("data").getString("id");
                                        String timestamp = jsonRESULTS.getJSONObject("data").getString("timestamp");
                                        String userid = jsonRESULTS.getJSONObject("data").getString("userid");
                                        String service_number = jsonRESULTS.getJSONObject("data").getString("service_number");
                                        String cust_name = jsonRESULTS.getJSONObject("data").getString("cust_name");
                                        String alamat = jsonRESULTS.getJSONObject("data").getString("alamat");
                                        String status = jsonRESULTS.getJSONObject("data").getString("status");
                                        String keterangan = jsonRESULTS.getJSONObject("data").getString("keterangan");
                                        String lat = jsonRESULTS.getJSONObject("data").getString("lat");
                                        String lng = jsonRESULTS.getJSONObject("data").getString("lng");
                                        String photo_url = jsonRESULTS.getJSONObject("data").getString("photo_url");


                                        datacollections_list.add(new DataCollections_GetSet(id,timestamp,userid,service_number,cust_name,alamat,status,
                                                keterangan,lat,lng,photo_url));
                                        DataCollections_Adapter adapter = new DataCollections_Adapter(DataCollections.this, datacollections_list);
//                                        Toast.makeText(getContext(), Double.toString(adapter.getItemCount()), Toast.LENGTH_LONG).show();
                                        recyclerView.setAdapter(adapter);
                                    } else {
                                        // Jika login gagal
                                        String error_message = jsonRESULTS.getString("error_msg");
                                        Toast.makeText(getApplicationContext(), error_message, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                datacollections_list.clear();
                                DataCollections_Adapter adapter = new DataCollections_Adapter(DataCollections.this, datacollections_list);
                                adapter.notifyDataSetChanged();
                                recyclerView.setAdapter(adapter);
                            } catch (IOException e) {
                                e.printStackTrace();
                                datacollections_list.clear();
                                DataCollections_Adapter adapter = new DataCollections_Adapter(DataCollections.this, datacollections_list);
                                adapter.notifyDataSetChanged();
                                recyclerView.setAdapter(adapter);
                            }
                        } else {
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("debug", "onFailure: ERROR > " + t.toString());
                        progressDialog.dismiss();
                    }
                });
    }

    public void alertDialogDatePickerX(){
        LayoutInflater inflater = (LayoutInflater) getSystemService(this.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.layout_datepicker, null, false);
        final DatePicker datePicker = (DatePicker) view.findViewById(R.id.datepicker);

        new AlertDialog.Builder(this).setView(view)
                .setTitle("Date Time")
                .setCancelable(false)
                .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int month = datePicker.getMonth() + 1;
                        int day = datePicker.getDayOfMonth();
                        int year = datePicker.getYear();
                        date_x = year+"-"+month+"-"+day;
                        edttxt_dt_x.setText(date_x);
//                        datetime_layout_x.performClick();
//                        loadLoginfo(userid,date_x,date_y);

                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
                    }
                }).show();
    }

    public void alertDialogDatePickerY(){
        LayoutInflater inflater = (LayoutInflater) getSystemService(this.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.layout_datepicker, null, false);
        final DatePicker datePicker = (DatePicker) view.findViewById(R.id.datepicker);

        new AlertDialog.Builder(this).setView(view)
                .setTitle("Date Time")
                .setCancelable(false)
                .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int month = datePicker.getMonth() + 1;
                        int day = datePicker.getDayOfMonth();
                        int year = datePicker.getYear();
                        date_y = year+"-"+month+"-"+day;
                        edttxt_dt_y.setText(date_y);
//                        datetime_layout_x.performClick();
//                        loadLoginfo(userid,date_x,date_y);

                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
                    }
                }).show();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (v == edttxt_dt_x) {
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE || event != null &&
                    event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                if (event == null || !event.isShiftPressed()) {
                    try {
                        date_x = edttxt_dt_x.getText().toString();
                        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY,0);
//                        String part[] = date_x.split("-");
//                    int part3 = Integer.parseInt(part[2]) + 1;
//                    String date_y = part[0] + "-" + part[1] + "-" + part3;
//                    Toast.makeText(getApplicationContext(), date_y, Toast.LENGTH_SHORT).show();
//                    loadLoginfo(date_x, date_y, userid);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Format Yang Anda Inputkan Salah...!!!", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
            }
        }
        if (v == edttxt_dt_y){
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE || event != null &&
                    event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                if (event == null || !event.isShiftPressed()) {
                    try {
                        date_y = edttxt_dt_y.getText().toString();
                        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.RESULT_HIDDEN,0);
//                        String part[] = date_y.split("-");
//                    int part3 = Integer.parseInt(part[2]) + 1;
//                    String date_y = part[0] + "-" + part[1] + "-" + part3;
//                    Toast.makeText(getApplicationContext(), date_y, Toast.LENGTH_SHORT).show();
//                    loadLoginfo(date_x, date_y, userid);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Format Yang Anda Inputkan Salah...!!!", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
            }
        }
        return false;
    }
}
