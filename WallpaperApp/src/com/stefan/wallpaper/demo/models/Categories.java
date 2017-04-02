package com.stefan.wallpaper.demo.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Categories implements Parcelable {
    private ArrayList<Category> category;
    private Recent recent;

    public ArrayList<Category> getCategories() {
        return this.category;
    }

    public void setCategory(ArrayList<Category> category) {
        this.category = category;
    }

    public Recent getRecent() {
        return this.recent;
    }

    public void setRecent(Recent recent) {
        this.recent = recent;
    }

    protected Categories(Parcel in) {
        if (in.readByte() == 0x01) {
            category = new ArrayList<Category>();
            in.readList(category, Category.class.getClassLoader());
        } else {
            category = null;
        }
        recent = (Recent) in.readValue(Recent.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (category == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(category);
        }
        dest.writeValue(recent);
    }

    public static final Parcelable.Creator<Categories> CREATOR = new Parcelable.Creator<Categories>() {
        @Override
        public Categories createFromParcel(Parcel in) {
            return new Categories(in);
        }

        @Override
        public Categories[] newArray(int size) {
            return new Categories[size];
        }
    };
}