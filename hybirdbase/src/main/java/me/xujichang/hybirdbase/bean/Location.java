package me.xujichang.hybirdbase.bean;

import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.baidu.mapapi.model.LatLng;

/**
 * Created by xjc on 2017/6/9.
 */

public class Location implements Parcelable {
    /**
     * 维度
     */
    private double lat;
    /**
     * 经度
     */
    private double lng;
    /**
     * 位置描述
     */
    private String des;

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    protected Location(Parcel in) {
        lat = in.readDouble();
        lng = in.readDouble();
        des = in.readString();
    }

    public Location() {
    }

    public static final Creator<Location> CREATOR = new Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel in) {
            return new Location(in);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(lat);
        dest.writeDouble(lng);
        dest.writeString(des);
    }

    public boolean isEmpty() {
        return TextUtils.isEmpty(des) && lat == 0 && lng == 0;
    }

    @Override
    public String toString() {
        return "Location{" +
                "lat=" + lat +
                ", lng=" + lng +
                '}';
    }

    public void init(android.location.Location location) {
        if (null == location) {
            return;
        }
        lat = location.getLatitude();
        lng = location.getLongitude();
    }

    public void init(LatLng latLng) {
        if (null == latLng) {
            return;
        }
        lat = latLng.latitude;
        lng = latLng.longitude;
    }

    public void init(double[] doubles) {
        lat = doubles[1];
        lng = doubles[0];
    }

    public void saveInSP(SharedPreferences.Editor editor) {
        editor.putString("des", des);
        editor.putString("lat", String.valueOf(lat));
        editor.putString("lng", String.valueOf(lng));
    }

    public void init(SharedPreferences preference) {
        des = preference.getString("des", "");
        lng = Double.parseDouble(preference.getString("lng", "0"));
        lat = Double.parseDouble(preference.getString("lat", "0"));

    }
}
