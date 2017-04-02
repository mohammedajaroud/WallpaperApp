package com.stefan.wallpaper.demo;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.stefan.wallpaper.demo.adapters.FullScreenGalleryAdapter;
import com.stefan.wallpaper.demo.fragments.FullScreenGalleryFragment;
import com.stefan.wallpaper.demo.models.Category;
import com.stefan.wallpaper.demo.models.Recent;
import com.stefan.wallpaper.demo.util.DataHolder;

import java.util.ArrayList;
import java.util.List;

public class FullScreenGalleryActivity extends BaseActivity {


    private ArrayList<String> mFavouritesList;

    private Recent mRecent;
    private List<String> mFavourites;
    private Category mCategory;

    private DataHolder mDataHolder;

    private ViewPager mViewPager;
    int position;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_screen_gallery);

        mRecent = getIntent().getExtras().getParcelable(MainActivity.PARC_RECENT);
        mFavourites = getIntent().getExtras().getStringArrayList(MainActivity.PARC_FAVOURITES);
        mCategory = getIntent().getExtras().getParcelable(MainActivity.PARC_CATEGORIES);
        position = getIntent().getExtras().getInt(MainActivity.PARC_POSITION);

        mDataHolder = getIntent().getExtras().getParcelable(MainActivity.PARC_DATA_HOLDER);
        mFavouritesList = mDataHolder.getFavourites();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getActionBar().hide();
        }

        mViewPager = (ViewPager) findViewById(R.id.image_pager);

        setFragmentAdapter();

        mViewPager.setAdapter(new FullScreenGalleryAdapter(getSupportFragmentManager(), getImageFragments()));
        mViewPager.setCurrentItem(position);
    }

    @Override
    public void onBackPressed() {
        mDataHolder.setFavourites(mFavouritesList);
        super.onBackPressed();
    }

    public void setFragmentAdapter() {
        mViewPager.setAdapter(new FullScreenGalleryAdapter(getSupportFragmentManager(), getImageFragments()));
        mViewPager.setCurrentItem(position);
    }


    private List<Fragment> getImageFragments() {
        List<Fragment> fragments = new ArrayList<Fragment>(0);
        if (mRecent != null) {
            for (String image : mRecent.getImages()) {
                fragments.add(FullScreenGalleryFragment.newInstance(image.split("/")[0], image.split("/")[1]));
            }
        } else if (mFavourites != null) {
            for (String image : mFavourites) {
                String catandimage = image.split("/")[image.split("/").length - 1];
                String[] spl = catandimage.split("--");
                fragments.add(FullScreenGalleryFragment.newInstance(spl[spl.length - 2], spl[spl.length - 1]));
            }
        } else if (mCategory != null) {
            for (String image : mCategory.getImages()) {
                fragments.add(FullScreenGalleryFragment.newInstance(mCategory.getName(), image));
            }
        }
        return fragments;
    }

    public int getPosition() {
        return position;
    }

    public ViewPager getMPager() {
        return mViewPager;
    }

    public ArrayList<String> getFavourites() {
        return mFavouritesList;
    }

    public void fillFavourites(String cat, String img) {
        String dir = Environment.getExternalStorageDirectory() + "/" + this.getString(R.string.app_name) + "/favourites/";
        mFavouritesList.add(dir + cat + "--" + img);
        mDataHolder.setFavourites(mFavouritesList);
    }

    public void removeFavourites(String cat, String img) {
        String dir = Environment.getExternalStorageDirectory() + "/" + this.getString(R.string.app_name) + "/favourites/";
        mFavouritesList.remove(dir + cat + "--" + img);
        mDataHolder.setFavourites(mFavouritesList);
    }
}
