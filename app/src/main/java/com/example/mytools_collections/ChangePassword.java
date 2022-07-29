package com.example.mytools_collections;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.mytools_collections.apihelper.BaseApiService;
import com.example.mytools_collections.apihelper.UtilsApi;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePassword extends AppCompatActivity {
    TextInputEditText edtUserid;
    TextInputEditText edtOldPassword;
    TextInputEditText edtNewPassword;
    CardView btnSavePassword;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String userid;
    BaseApiService mApiService;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        pref = getSharedPreferences("MyPref",0);
        userid = pref.getString("userid",null);

        mApiService = UtilsApi.getAPIService();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Change Password");
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);

        edtUserid = findViewById(R.id.edt_userid_password);
        edtOldPassword = findViewById(R.id.edt_old_password);
        edtNewPassword = findViewById(R.id.edt_new_password);
        btnSavePassword = findViewById(R.id.card_btn_save_password);

        edtUserid.setText(userid);
        btnSavePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userid = edtUserid.getText().toString();
                String old_password = edtOldPassword.getText().toString();
                String new_password = edtNewPassword.getText().toString();
                if (old_password.isEmpty()) {
                    edtOldPassword.setError("Tidak Boleh Kosong...!!!");
                }
                else if (new_password.isEmpty()){
                    edtNewPassword.setError("Tidak Boleh Kosong...!!!");
                }
                else {
                    changePassword(userid, old_password, new_password);
                }
            }
        });
    }

    public void changePassword(final String userid,String old_password,String new_password) {
        mApiService.changePassword(userid,old_password,new_password)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            progressDialog.dismiss();
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("error").equals("false")) {
                                    String success_message = jsonRESULTS.getString("success_msg");
                                    Toast.makeText(getApplicationContext(), success_message, Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(),SuccessActivity.class));
//                                    notifyDataSetChanged();

                                } else {
                                    // Jika login gagal
                                    String error_message = jsonRESULTS.getString("error_msg");
                                    Toast.makeText(getApplicationContext(), error_message, Toast.LENGTH_SHORT).show();
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
                        progressDialog.dismiss();
                    }
                });
    }
}
