package com.example.mytools_collections.adapter;

import android.annotation.SuppressLint;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.mytools_collections.Collections_Constants;
import com.example.mytools_collections.MainActivity;
import com.example.mytools_collections.R;
import com.example.mytools_collections.autotracking.LocationJobService;

import java.util.List;

public class ListCollections_Adapter extends RecyclerView.Adapter<ListCollections_Adapter.ListCollectionsViewHolder> {

    private Context ctx;
    private List<ListCollections_Getset> listCollections_list;
    RequestOptions options;
    Intent intent;
//    boolean registered = false;
//    public static final String JOB_STATE_CHANGED = "jobStateChanged";
//    public static final String LOCATION_ACQUIRED = "locAcquired";

    public ListCollections_Adapter(Context ctx, List<ListCollections_Getset> listCollections_list) {
        this.ctx = ctx;
        this.listCollections_list = listCollections_list;
        options = new RequestOptions().placeholder(R.mipmap.noimage).diskCacheStrategy(DiskCacheStrategy.ALL).skipMemoryCache(true);
        intent = new Intent(ctx,MainActivity.class);
    }

    @NonNull
    @Override
    public ListCollectionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.list_collections_design,null);
        return new ListCollectionsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListCollectionsViewHolder holder, int position) {
        final ListCollections_Getset listCollections_getset = listCollections_list.get(position);


        Glide.with(ctx)
                .asBitmap()
                .apply(options)
                .load(Collections_Constants.BASIC_URL+listCollections_getset.getPhoto_url())
                .into(holder.imageView);
        holder.txtSalesName.setText(listCollections_getset.getSales_name());
        holder.txtSalesHp.setText(listCollections_getset.getSales_hp());
        holder.txtTechName.setText(listCollections_getset.getTech_name());
        holder.txtTechHp.setText(listCollections_getset.getTech_hp());
        holder.txtCustName.setText(listCollections_getset.getCust_name());
        holder.txtCustHp.setText(listCollections_getset.getCust_hp());

//        intent.putExtra("service_number",listCollections_getset.getService_number());
//        intent.putExtra("sales_name",listCollections_getset.getSales_name());
//        intent.putExtra("sales_hp",listCollections_getset.getSales_hp());
//        intent.putExtra("tech_name",listCollections_getset.getTech_name());
//        intent.putExtra("tech_hp",listCollections_getset.getTech_hp());
//        intent.putExtra("cust_name",listCollections_getset.getCust_name());
//        intent.putExtra("cust_hp",listCollections_getset.getCust_hp());
//        intent.putExtra("cust_addr",listCollections_getset.getCust_addr());

        intent.putExtra("collections_data",new ListCollections_Getset(listCollections_getset.getService_number(),listCollections_getset.getSales_name()
        ,listCollections_getset.getSales_hp(),listCollections_getset.getTech_name(),listCollections_getset.getTech_hp(),listCollections_getset.getCust_name()
        ,listCollections_getset.getCust_hp(),listCollections_getset.getCust_addr(),Collections_Constants.BASIC_URL+listCollections_getset.getPhoto_url(),
                listCollections_getset.getLat_cust(),listCollections_getset.getLng_cust()));

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ctx.startActivity(intent);
            }
        });
        holder.directionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToNavigation(listCollections_getset.getLat_cust(),listCollections_getset.getLng_cust());
            }
        });
        holder.updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ctx.startActivity(intent);
            }
        });
    }

    private void goToNavigation(String lat,String lng){
        Uri uri = Uri.parse("google.navigation:q="+lat+","+lng);
        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
        intent.setPackage("com.google.android.apps.maps");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(intent);

    }

    @Override
    public int getItemCount() {
        return listCollections_list.size();
    }

    public static class ListCollectionsViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView txtSalesName;
        TextView txtSalesHp;
        TextView txtTechName;
        TextView txtTechHp;
        TextView txtCustName;
        TextView txtCustHp;
        CardView cardView;
        Button directionBtn;
        Button updateBtn;


        public ListCollectionsViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.imageView1);
            this.txtSalesName = itemView.findViewById(R.id.sales_name);
            this.txtSalesHp = itemView.findViewById(R.id.sales_hp);
            this.txtTechName = itemView.findViewById(R.id.tech_name);
            this.txtTechHp = itemView.findViewById(R.id.tech_hp);
            this.txtCustName = itemView.findViewById(R.id.cust_name);
            this.txtCustHp = itemView.findViewById(R.id.cust_hp);
            this.cardView = itemView.findViewById(R.id.card_collections);
            this.directionBtn = itemView.findViewById(R.id.directionButton);
            this.updateBtn = itemView.findViewById(R.id.updateButton);
        }
    }
    ///////////////////////////////Start Background AutoTracking//////////////////////////
//    @SuppressLint("JobSchedulerService")
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    private void startBackgroundService() {
//        if(!registered) {
//            IntentFilter i = new IntentFilter(JOB_STATE_CHANGED);
//            i.addAction(LOCATION_ACQUIRED);
//            LocalBroadcastManager.getInstance(ctx).registerReceiver(jobStateChanged, i);
//        }
//        JobScheduler jobScheduler =
//                (JobScheduler) ctx.getSystemService(Context.JOB_SCHEDULER_SERVICE);
//        assert jobScheduler != null;
//        jobScheduler.schedule(new JobInfo.Builder(LocationJobService.LOCATION_SERVICE_JOB_ID,
//                new ComponentName(ctx, LocationJobService.class))
//                .setOverrideDeadline(500)
//                .setPersisted(true)
//                .setRequiresDeviceIdle(false)
//                .build());
//    }
//    private BroadcastReceiver jobStateChanged = new BroadcastReceiver() {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            if(intent.getAction()==null){
//                return;
//            }
//            if(intent.getAction().equals(JOB_STATE_CHANGED)) {
////                changeServiceButton(intent.getExtras().getBoolean("isStarted"));
//            }else if (intent.getAction().equals(LOCATION_ACQUIRED)){
//                if(intent.getExtras()!=null){
//                    Bundle b = intent.getExtras();
//                    Location l = b.getParcelable("location");
////                    updateMarker(l);
//                }else{
//                    Log.d("intent","null");
//                }
//            }
//        }
//    };
}
