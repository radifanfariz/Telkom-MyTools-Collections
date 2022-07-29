package com.example.mytools_collections.adapter;

import android.os.Parcel;
import android.os.Parcelable;

public class DataCollections_GetSet implements Parcelable {
    private String id;
    private String timestamp;
    private String userid;
    private String serv_num;
    private String cust_name;
    private String alamat;
    private String status;
    private String keterangan;
    private String lat;
    private String lng;
    private String photo_url;

    public DataCollections_GetSet(String id, String timestamp, String userid, String serv_num, String cust_name, String alamat, String status, String keterangan, String lat, String lng, String photo_url) {
        this.id = id;
        this.timestamp = timestamp;
        this.userid = userid;
        this.serv_num = serv_num;
        this.cust_name = cust_name;
        this.alamat = alamat;
        this.status = status;
        this.keterangan = keterangan;
        this.lat = lat;
        this.lng = lng;
        this.photo_url = photo_url;
    }

    protected DataCollections_GetSet(Parcel in) {
        id = in.readString();
        timestamp = in.readString();
        userid = in.readString();
        serv_num = in.readString();
        cust_name = in.readString();
        alamat = in.readString();
        status = in.readString();
        keterangan = in.readString();
        lat = in.readString();
        lng = in.readString();
        photo_url = in.readString();
    }

    public static final Creator<DataCollections_GetSet> CREATOR = new Creator<DataCollections_GetSet>() {
        @Override
        public DataCollections_GetSet createFromParcel(Parcel in) {
            return new DataCollections_GetSet(in);
        }

        @Override
        public DataCollections_GetSet[] newArray(int size) {
            return new DataCollections_GetSet[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getServ_num() {
        return serv_num;
    }

    public void setServ_num(String serv_num) {
        this.serv_num = serv_num;
    }

    public String getCust_name() {
        return cust_name;
    }

    public void setCust_name(String cust_name) {
        this.cust_name = cust_name;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(timestamp);
        dest.writeString(userid);
        dest.writeString(serv_num);
        dest.writeString(cust_name);
        dest.writeString(alamat);
        dest.writeString(status);
        dest.writeString(keterangan);
        dest.writeString(lat);
        dest.writeString(lng);
        dest.writeString(photo_url);
    }
}

