package com.example.mytools_collections;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytools_collections.adapter.ListCollections_Adapter;
import com.example.mytools_collections.adapter.ListCollections_Getset;
import com.example.mytools_collections.apihelper.BaseApiService;
import com.example.mytools_collections.apihelper.UtilsApi;
import com.google.android.material.textfield.TextInputEditText;

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

public class ListCollections extends AppCompatActivity implements EditText.OnEditorActionListener {

    BaseApiService mApiService;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String userid;
    String service_number;
    List<ListCollections_Getset> list_collections;
    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    TextInputEditText edttext_sn;
    CardView card_search_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_collections);

        recyclerView = (RecyclerView)  findViewById(R.id.recylcerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        edttext_sn = findViewById(R.id.edttxt_sn);
        edttext_sn.setOnEditorActionListener(this);

        card_search_btn = findViewById(R.id.card_search_btn);
        card_search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Editable service_number = edttext_sn.getText();
                getCollections(userid,service_number.toString());
            }
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Data Collections");
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);

        list_collections = new ArrayList<>();
        pref = this.getSharedPreferences("MyPref",0);
        mApiService = UtilsApi.getAPIService();
        userid = pref.getString("userid",null);
        getCollections(userid,service_number);

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE || event != null &&
        event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER){
            if (event == null || !event.isShiftPressed()) {
                Editable service_number = edttext_sn.getText();
//                Toast.makeText(getApplicationContext(), userid.toString(), Toast.LENGTH_SHORT).show();
                getCollections(userid,service_number.toString());
                return true;
            }
        }
        return false;
    }

    public void getCollections(String userid,String service_number){
        progressDialog.show();
        list_collections.clear();
        mApiService.getCollections(userid,service_number)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            progressDialog.dismiss();
                            try {
                                JSONArray jsonArray = new JSONArray(response.body().string());
                                Toast.makeText(getApplicationContext(), "Berhasil Dimuat", Toast.LENGTH_SHORT).show();
                                list_collections.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonRESULTS = jsonArray.getJSONObject(i);
                                    if (jsonRESULTS.getString("error").equals("false")) {
                                        String id = jsonRESULTS.getJSONObject("data").getString("id");
                                        String service_number = jsonRESULTS.getJSONObject("data").getString("service_number");
                                        String sales_name = jsonRESULTS.getJSONObject("data").getString("namasf");
                                        String sales_hp = jsonRESULTS.getJSONObject("data").getString("nosf");
                                        String tech_name = jsonRESULTS.getJSONObject("data").getString("namatech");
                                        String tech_hp = jsonRESULTS.getJSONObject("data").getString("notech");
                                        String cust_name = jsonRESULTS.getJSONObject("data").getString("namacust");
                                        String cust_hp = jsonRESULTS.getJSONObject("data").getString("nocust");
                                        String cust_addr = jsonRESULTS.getJSONObject("data").getString("alamatcust");
                                        String photo_url = jsonRESULTS.getJSONObject("data").getString("cust_url_image");
                                        String lat_cust = jsonRESULTS.getJSONObject("data").getString("lat_cust");
                                        String lng_cust = jsonRESULTS.getJSONObject("data").getString("lng_cust");



                                        list_collections.add(new ListCollections_Getset(service_number,sales_name, sales_hp, tech_name,tech_hp,cust_name,cust_hp,cust_addr,photo_url,
                                                lat_cust,lng_cust));
                                        ListCollections_Adapter adapter = new ListCollections_Adapter(getApplicationContext(), list_collections);
//                                        Toast.makeText(getContext(), Double.toString(adapter.getItemCount()), Toast.LENGTH_LONG).show();
                                        recyclerView.setAdapter(adapter);
                                    } else {
                                        String error_message = jsonRESULTS.getString("Data tidak ditemukan");
                                        Toast.makeText(getApplicationContext(), error_message, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), "Silahkan Input Parameter", Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
    }
}
