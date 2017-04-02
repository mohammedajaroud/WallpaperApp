package com.stefan.wallpaper.demo.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Recent implements Parcelable {
    private ArrayList<String> images;

    public ArrayList<String> getImages() {
        return this.images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    protected Recent(Parcel in) {
        if (in.readByte() == 0x01) {
            images = new ArrayList<String>();
            in.readList(images, String.class.getClassLoader());
        } else {
            images = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (images == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(images);
        }
    }

    public static final Parcelable.Creator<Recent> CREATOR = new Parcelable.Creator<Recent>() {
        @Override
        public Recent createFromParcel(Parcel in) {
            return new Recent(in);
        }

        @Override
        public Recent[] newArray(int size) {
            return new Recent[size];
        }
    };
}