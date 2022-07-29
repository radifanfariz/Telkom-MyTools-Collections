package com.example.mytools_collections.adapter;

import android.os.Parcel;
import android.os.Parcelable;

public class ListCollections_Getset implements Parcelable {

    public String service_number;
    public String cust_addr;
    public String sales_name;
    public String sales_hp;
    public String tech_name;
    public String tech_hp;
    public String cust_name;
    public String cust_hp;
    public String photo_url;
    public String lat_cust;
    public String lng_cust;


    protected ListCollections_Getset(Parcel in) {
        service_number = in.readString();
        cust_addr = in.readString();
        sales_name = in.readString();
        sales_hp = in.readString();
        tech_name = in.readString();
        tech_hp = in.readString();
        cust_name = in.readString();
        cust_hp = in.readString();
        photo_url = in.readString();
    }

    public static final Creator<ListCollections_Getset> CREATOR = new Creator<ListCollections_Getset>() {
        @Override
        public ListCollections_Getset createFromParcel(Parcel in) {
            return new ListCollections_Getset(in);
        }

        @Override
        public ListCollections_Getset[] newArray(int size) {
            return new ListCollections_Getset[size];
        }
    };

    public String getService_number() {
        return service_number;
    }

    public void setService_number(String service_number) {
        this.service_number = service_number;
    }

    public String getCust_addr() {
        return cust_addr;
    }

    public void setCust_addr(String cust_addr) {
        this.cust_addr = cust_addr;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }

    public ListCollections_Getset(String service_number,String sales_name, String sales_hp, String tech_name, String tech_hp, String cust_name, String cust_hp,String cust_addr,String photo_url,
                                  String lat_cust,String lng_cust) {
        this.service_number = service_number;
        this.sales_name = sales_name;
        this.sales_hp = sales_hp;
        this.tech_name = tech_name;
        this.tech_hp = tech_hp;
        this.cust_name = cust_name;
        this.cust_hp = cust_hp;
        this.cust_addr = cust_addr;
        this.photo_url  = photo_url;
        this.lat_cust = lat_cust;
        this.lng_cust = lng_cust;
    }

    public String getLat_cust() {
        return lat_cust;
    }

    public void setLat_cust(String lat_cust) {
        this.lat_cust = lat_cust;
    }

    public String getLng_cust() {
        return lng_cust;
    }

    public void setLng_cust(String lng_cust) {
        this.lng_cust = lng_cust;
    }

    public String getSales_name() {
        return sales_name;
    }

    public void setSales_name(String sales_name) {
        this.sales_name = sales_name;
    }

    public String getSales_hp() {
        return sales_hp;
    }

    public void setSales_hp(String sales_hp) {
        this.sales_hp = sales_hp;
    }

    public String getTech_name() {
        return tech_name;
    }

    public void setTech_name(String tech_name) {
        this.tech_name = tech_name;
    }

    public String getTech_hp() {
        return tech_hp;
    }

    public void setTech_hp(String tech_hp) {
        this.tech_hp = tech_hp;
    }

    public String getCust_name() {
        return cust_name;
    }

    public void setCust_name(String cust_name) {
        this.cust_name = cust_name;
    }

    public String getCust_hp() {
        return cust_hp;
    }

    public void setCust_hp(String cust_hp) {
        this.cust_hp = cust_hp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(service_number);
        dest.writeString(cust_addr);
        dest.writeString(sales_name);
        dest.writeString(sales_hp);
        dest.writeString(tech_name);
        dest.writeString(tech_hp);
        dest.writeString(cust_name);
        dest.writeString(cust_hp);
        dest.writeString(photo_url);
    }
}
