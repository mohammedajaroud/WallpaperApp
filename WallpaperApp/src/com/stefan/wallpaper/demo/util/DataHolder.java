package com.stefan.wallpaper.demo.util;

import android.os.Parcel;
import android.os.Parcelable;

import com.stefan.wallpaper.demo.models.Category;
import com.stefan.wallpaper.demo.models.Recent;

import java.util.ArrayList;
import java.util.List;

public class DataHolder implements Parcelable {
    private Recent mRecent;
    private ArrayList<String> mFavourites;
    private List<Category> mCategories;

    protected DataHolder(Parcel in) {
        mRecent = (Recent) in.readValue(Recent.class.getClassLoader());
        if (in.readByte() == 0x01) {
            mFavourites = new ArrayList<String>();
            in.readList(mFavourites, String.class.getClassLoader());
        } else {
            mFavourites = null;
        }
        if (in.readByte() == 0x01) {
            mCategories = new ArrayList<Category>();
            in.readList(mCategories, Category.class.getClassLoader());
        } else {
            mCategories = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(mRecent);
        if (mFavourites == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(mFavourites);
        }
        if (mCategories == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(mCategories);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<DataHolder> CREATOR = new Parcelable.Creator<DataHolder>() {
        @Override
        public DataHolder createFromParcel(Parcel in) {
            return new DataHolder(in);
        }

        @Override
        public DataHolder[] newArray(int size) {
            return new DataHolder[size];
        }
    };

    public DataHolder() {
        super();
    }

    public DataHolder(Recent recent, ArrayList<String> favourites,
                      List<Category> categories) {
        this.mRecent = recent;
        this.mFavourites = favourites;
        this.mCategories = categories;
    }

    public Recent getRecent() {
        return mRecent;
    }

    public void setRecent(Recent mRecent) {
        this.mRecent = mRecent;
    }

    public ArrayList<String> getFavourites() {
        return mFavourites;
    }

    public void setFavourites(ArrayList<String> mFavourites) {
        this.mFavourites = mFavourites;
    }

    public List<Category> getCategories() {
        return mCategories;
    }

    public void setCategories(List<Category> mCategories) {
        this.mCategories = mCategories;
    }

}
