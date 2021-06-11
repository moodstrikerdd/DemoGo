package com.moo.moouidservice.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class AidlBean implements Parcelable {

    private long uid;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.uid);
    }

    public AidlBean() {
    }

    public void readFromParcel(Parcel in) {
        this.uid = in.readLong();
    }

    protected AidlBean(Parcel in) {
        readFromParcel(in);
    }

    public static final Creator<AidlBean> CREATOR = new Creator<AidlBean>() {
        @Override
        public AidlBean createFromParcel(Parcel source) {
            return new AidlBean(source);
        }

        @Override
        public AidlBean[] newArray(int size) {
            return new AidlBean[size];
        }
    };

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }
}
