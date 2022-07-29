package com.example.mytools_collections.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytools_collections.MainActivity;
import com.example.mytools_collections.R;
import com.example.mytools_collections.apihelper.BaseApiService;
import com.example.mytools_collections.apihelper.UtilsApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataCollections_Adapter extends RecyclerView.Adapter<DataCollections_Adapter.DataCollectionsViewHolder> {

    private Context ctx;
    private List<DataCollections_GetSet> dataCollections_list;
    BaseApiService mApiService;
    ProgressDialog progressDialog;
    String id;
    Intent intent;

    public DataCollections_Adapter(Context ctx, List<DataCollections_GetSet> dataCollections_list) {
        this.ctx = ctx;
        this.dataCollections_list = dataCollections_list;
        intent = new Intent(ctx,MainActivity.class);
    }

    @NonNull
    @Override
    public DataCollectionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.datacollections_design,null);
        return new DataCollectionsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DataCollectionsViewHolder holder, int position) {
        final DataCollections_GetSet dataCollections_getSet = dataCollections_list.get(position);
        mApiService = UtilsApi.getAPIService();
        progressDialog = new ProgressDialog(ctx);
        progressDialog.setTitle("Data SF");
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);

        holder.txtDataCollectionsId.setText(dataCollections_getSet.getId());
        holder.txtDataCollectionsTimestamp.setText(dataCollections_getSet.getTimestamp());
        holder.txtDataCollectionsCustName.setText(dataCollections_getSet.getCust_name());
        holder.txtDataCollectionsServNum.setText(dataCollections_getSet.getServ_num());
        holder.txtDataCollectionsStatus.setText(dataCollections_getSet.getStatus());

//        id = dataCollections_getSet.getId();
//        String timestamp = dataCollections_getSet.getTimestamp();
//        String userid = dataCollections_getSet.getUserid();
//        String servnum = dataCollections_getSet.getServ_num();
//        String custname = dataCollections_getSet.getCust_name();
//        String alamat = dataCollections_getSet.getAlamat();
//        String status = dataCollections_getSet.getStatus();
//        String ket = dataCollections_getSet.getKeterangan();
//        String lat = dataCollections_getSet.getLat();
//        String lng = dataCollections_getSet.getLng();
//        String photourl = dataCollections_getSet.getPhoto_url();



//        intent.putExtra("data_collections",new DataCollections_GetSet(dataCollections_getSet.getId(),dataCollections_getSet.getTimestamp(),
//                dataCollections_getSet.getUserid(),dataCollections_getSet.getServ_num(),dataCollections_getSet.getCust_name(),dataCollections_getSet.getAlamat(),
//                dataCollections_getSet.getStatus(),dataCollections_getSet.getKeterangan(),dataCollections_getSet.getLat(),dataCollections_getSet.getLng(),
//                dataCollections_getSet.getPhoto_url()));

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        holder.idClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = holder.txtDataCollectionsId.getText().toString();

//                maps_dialogInteraction.customDialog();
//                processDB(idx,newAction,column);
//                Toast.makeText(ctx, "BERHASIL DI PROCESS", Toast.LENGTH_SHORT).show();
                showUpdatedeleteDialog();
//                ctx.startActivity(intent);
            }
        });
        holder.timestampClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = holder.txtDataCollectionsId.getText().toString();
                showUpdatedeleteDialog();
            }
        });
        holder.custnameClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = holder.txtDataCollectionsId.getText().toString();
                showUpdatedeleteDialog();
            }
        });
        holder.servnumClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = holder.txtDataCollectionsId.getText().toString();
                Toast.makeText(ctx, "BERHASIL DI PROCESS", Toast.LENGTH_SHORT).show();
                showUpdatedeleteDialog();
            }
        });
        holder.statusClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = holder.txtDataCollectionsId.getText().toString();
                showUpdatedeleteDialog();
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataCollections_list.size();
    }

    public static class DataCollectionsViewHolder extends RecyclerView.ViewHolder {
        TextView txtDataCollectionsId;
        TextView txtDataCollectionsTimestamp;
        TextView txtDataCollectionsCustName;
        TextView txtDataCollectionsServNum;
        TextView txtDataCollectionsStatus;
        CardView idClick;
        CardView timestampClick;
        CardView custnameClick;
        CardView servnumClick;
        CardView statusClick;
        public DataCollectionsViewHolder(@NonNull View itemView) {
            super(itemView);
            this.txtDataCollectionsId = itemView.findViewById(R.id.txtDataCollectionsId);
            this.txtDataCollectionsTimestamp = itemView.findViewById(R.id.txtDataCollectionsTimestamp);
            this.txtDataCollectionsCustName = itemView.findViewById(R.id.txtDataCollectionsCustName);
            this.txtDataCollectionsServNum = itemView.findViewById(R.id.txtDataCollectionsServNum);
            this.txtDataCollectionsStatus = itemView.findViewById(R.id.txtDataCollectionsStatus);
            this.idClick = itemView.findViewById(R.id.idClick);
            this.timestampClick = itemView.findViewById(R.id.timestampClick);
            this.custnameClick = itemView.findViewById(R.id.custnameClick);
            this.servnumClick = itemView.findViewById(R.id.servnumClick);
            this.statusClick = itemView.findViewById(R.id.statusClick);
        }
    }

    public void showUpdatedeleteDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctx);
        alertDialogBuilder.setTitle("Change Data");
        alertDialogBuilder.setMessage("Apakah Anda Yakin Ingin Mengubah(Update) Atau Menghapus(Delete) Data ?");
        alertDialogBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showSureDialog();
            }
        });
        alertDialogBuilder.setNeutralButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showSureDialog2();
            }
        });

        AlertDialog dialog = alertDialogBuilder.create();
        dialog.show();
    }

    public void showSureDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctx);
        alertDialogBuilder.setTitle("Peringatan");
        alertDialogBuilder.setMessage("Apakah anda yakin dengan pilihan anda ?");
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                processDB(idx,newAction,column);
//                Toast.makeText(ctx, "BERHASIL DI PROCESS", Toast.LENGTH_SHORT).show();
                progressDialog.show();
                deleteDataCollections(id);
//                Toast.makeText(ctx, idx, Toast.LENGTH_SHORT).show();
            }
        });
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(ctx, "PROCESS DI BATALKAN", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog dialog = alertDialogBuilder.create();
        dialog.show();
    }
    public void showSureDialog2(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctx);
        alertDialogBuilder.setTitle("Peringatan");
        alertDialogBuilder.setMessage("Apakah anda yakin dengan pilihan anda ?");
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                Toast.makeText(ctx, "BERHASIL DI PROCESS", Toast.LENGTH_SHORT).show();
                progressDialog.show();
                updateDataCollections(id);
//                updateIntent(idx);
            }
        });
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(ctx, "PROCESS DI BATALKAN", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog dialog = alertDialogBuilder.create();
        dialog.show();
    }
    ////////////Intent to Update_SF//////////////////////
//    private void updateIntent(String idx){
//        Intent intent = new Intent(ctx, Maps_SF_Activity.class);
//        intent.putExtra("idx",idx);
//        ctx.startActivity(intent);
//    }

    public void updateDataCollections(final String id) {
        mApiService.updateDataCollections(id)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            progressDialog.dismiss();
                            try {
                                JSONArray jsonArray = new JSONArray(response.body().string());
                                Toast.makeText(ctx, "Berhasil Dimuat", Toast.LENGTH_SHORT).show();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonRESULTS = jsonArray.getJSONObject(i);
                                    if (jsonRESULTS.getString("error").equals("false")) {
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

                                        intent.putExtra("data_collections",new DataCollections_GetSet(id,timestamp,userid
                                                ,service_number,cust_name,alamat,status,keterangan,lat,lng,photo_url));

                                        ctx.startActivity(intent);

                                    } else {
                                        String error_message = jsonRESULTS.getString("Data tidak ditemukan");
                                        Toast.makeText(ctx, error_message, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(ctx, e.toString(), Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                                Toast.makeText(ctx, e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(ctx, t.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    public void deleteDataCollections(final String id) {
        mApiService.deleteDataCollections(id)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            progressDialog.dismiss();
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("error").equals("false")) {
                                    Toast.makeText(ctx, "DATA BERHASIL DI HAPUS", Toast.LENGTH_SHORT).show();
//                                    notifyDataSetChanged();

                                } else {
                                    // Jika login gagal
                                    String error_message = jsonRESULTS.getString("error_msg");
                                    Toast.makeText(ctx, error_message, Toast.LENGTH_SHORT).show();
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
