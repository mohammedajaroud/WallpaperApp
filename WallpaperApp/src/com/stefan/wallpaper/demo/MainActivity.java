package com.stefan.wallpaper.demo;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.stefan.wallpaper.demo.adapters.GridImageAdapter;
import com.stefan.wallpaper.demo.models.Categories;
import com.stefan.wallpaper.demo.models.Category;
import com.stefan.wallpaper.demo.models.Recent;
import com.stefan.wallpaper.demo.util.Controller;
import com.stefan.wallpaper.demo.util.DataHolder;
import com.stefan.wallpaper.demo.util.DialogUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends NavigationDrawerActivity implements
        OnItemClickListener {

    public static final String PARC_RECENT = "com.stefan.wallpaper.demo.Recent";
    public static final String PARC_FAVOURITES = "com.stefan.wallpaper.demo.Favourites";
    public static final String PARC_CATEGORIES = "com.stefan.wallpaper.demo.Categories";
    public static final String PARC_POSITION = "com.stefan.wallpaper.demo.Position";
    public static final String PARC_DATA_HOLDER = "com.stefan.wallpaper.demo.DataHolder";

    private Dialog mSplashScreenDialog;
    private boolean mSplashScreenOnScreen = true;
    private SyncData mSyncData;
    private DialogUtils dialog;

    public void setFavourites(ArrayList<String> favourites) {
        this.mDataHolder.setFavourites(favourites);
    }

    public ArrayList<String> getFavourites() {
        return this.mDataHolder.getFavourites();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataHolder = new DataHolder();
        mSyncData = new SyncData();
        mSyncData.execute();

        mGrid.setOnItemClickListener(this);

        dialog = new DialogUtils(this);

        showSplashScreen();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        super.onConfigurationChanged(newConfig);
        mGrid.setNumColumns(getResources().getInteger(
                R.integer.grid_num_columns));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSyncData != null) {
            mSyncData.cancel(true);
        }
    }

    private void showSplashScreen() {
        mSplashScreenDialog = new Dialog(this, R.style.SplashScreenStyle);
        mSplashScreenDialog.setContentView(R.layout.splash_screen);
        mSplashScreenDialog.show();
        mSplashScreenDialog.setOnCancelListener(new OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mDataHolder != null && mSplashScreenOnScreen == false) {
            Dialog progressDialog = ProgressDialog.show(MainActivity.this, "",
                    getString(R.string.please_wait));
            SyncData data;
            if (currentSelectedItem == 0) {
                data = new SyncData(progressDialog, 2, "recent");
            } else if (currentSelectedItem == 1) {
                data = new SyncData(progressDialog, 2);
            } else {
                data = new SyncData(progressDialog, 2, "category");
            }
            data.execute();
        }
        mSplashScreenOnScreen = false;
    }

    public void hideSplashScreen() {
        if (mSplashScreenDialog != null) {
            mSplashScreenDialog.dismiss();
            mSplashScreenDialog = null;
        }
        mSyncData = null;
    }

    @Override
    public void setGridAdapter(int position) {
        getActionBar().show();
        if (position < 3) {
            if (position == 0) {
                mGrid.setAdapter(new GridImageAdapter(this, mDataHolder
                        .getRecent(), mDataHolder.getFavourites()));
            } else if (position == 1) {
                Dialog progressDialog = ProgressDialog.show(MainActivity.this,
                        "", getString(R.string.please_wait));
                SyncData data = new SyncData(progressDialog, 11);
                data.execute();
            } else if (position == 2) {
                currentSelectedItem = 0;
                mGrid.setAdapter(new GridImageAdapter(this, mDataHolder
                        .getRecent(), mDataHolder.getFavourites()));
                Intent aboutUs = new Intent(this, AboutUsActivity.class);
                startActivity(aboutUs);
            }
        } else {
            mGrid.setAdapter(new GridImageAdapter(this, mDataHolder
                    .getCategories().get(position - 3), mDataHolder
                    .getFavourites()));
        }
    }

    @Override
    public void setTitle(int position) {
        if (position < 3) {
            if (position == 0) {
                setTitle(getString(R.string.recent));
            } else if (position == 1) {
                setTitle(getString(R.string.favourites));
            } else if (position == 2) {
                setTitle(getString(R.string.about_us));
            }
        } else {
            setTitle(mDataHolder.getCategories().get(position - 3).getName());
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        Intent intent = new Intent(this, FullScreenGalleryActivity.class);
        if (currentSelectedItem < 3) {
            if (currentSelectedItem == 0) {
                intent.putExtra(PARC_RECENT, mDataHolder.getRecent());
                intent.putExtra(PARC_DATA_HOLDER, mDataHolder);
            } else if (currentSelectedItem == 1) {
                intent.putStringArrayListExtra(PARC_FAVOURITES,
                        mDataHolder.getFavourites());
                intent.putExtra(PARC_DATA_HOLDER, mDataHolder);
            } else if (currentSelectedItem == 2) {
                intent.putStringArrayListExtra(PARC_FAVOURITES,
                        mDataHolder.getFavourites());
                intent.putExtra(PARC_DATA_HOLDER, mDataHolder);
            }
        } else {
            intent.putExtra(PARC_CATEGORIES,
                    mDataHolder.getCategories().get(currentSelectedItem - 3));
            intent.putExtra(PARC_DATA_HOLDER, mDataHolder);
        }
        intent.putExtra(PARC_POSITION, position);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        ArrayList<String> random;
        GridImageAdapter adapter;
        switch (item.getItemId()) {
            case R.id.action_search:
                if (currentSelectedItem != 2)
                    dialog.showDialog();
                return true;
            case R.id.action_refresh:
                Dialog progressDialog = ProgressDialog.show(MainActivity.this, "",
                        getString(R.string.please_wait));
                SyncData data;
                if (currentSelectedItem < 3) {
                    if (currentSelectedItem == 0) {
                        data = new SyncData(progressDialog, 10);
                    } else if (currentSelectedItem == 1) {
                        data = new SyncData(progressDialog, 11);
                    } else {
                        data = new SyncData();
                        progressDialog.dismiss();
                    }
                } else {
                    data = new SyncData(progressDialog, 12);
                }

                data.execute();
                return true;
            case R.id.random:
                if (currentSelectedItem < 3) {
                    if (currentSelectedItem == 0) {
                        random = new ArrayList<String>();
                        for (String st : mDataHolder.getRecent().getImages()) {
                            random.add(st);
                        }
                        Collections.shuffle(random);
                        mDataHolder.getRecent().setImages(random);
                        adapter = new GridImageAdapter(this,
                                mDataHolder.getRecent(),
                                mDataHolder.getFavourites());
                        mGrid.setAdapter(adapter);
                    } else if (currentSelectedItem == 1) {
                        Collections.shuffle(mDataHolder.getFavourites());
                        adapter = new GridImageAdapter(this,
                                mDataHolder.getFavourites());
                        mGrid.setAdapter(adapter);
                    } else {

                    }
                } else {
                    random = new ArrayList<String>();
                    for (String st : mDataHolder.getCategories()
                            .get(currentSelectedItem - 3).getImages()) {
                        random.add(st);
                    }
                    Collections.shuffle(random);
                    mDataHolder.getCategories().get(currentSelectedItem - 3)
                            .setImages(random);
                    adapter = new GridImageAdapter(this, mDataHolder
                            .getCategories().get(currentSelectedItem - 3),
                            mDataHolder.getFavourites());
                    mGrid.setAdapter(adapter);
                }
                return true;
            case R.id.sort_up:
                if (currentSelectedItem < 3) {
                    if (currentSelectedItem == 0) {
                        Collections.sort(mDataHolder.getRecent().getImages());
                        adapter = new GridImageAdapter(this,
                                mDataHolder.getRecent(),
                                mDataHolder.getFavourites());
                        mGrid.setAdapter(adapter);
                    } else if (currentSelectedItem == 1) {
                        Collections.sort(mDataHolder.getFavourites());
                        adapter = new GridImageAdapter(this,
                                mDataHolder.getFavourites());
                        mGrid.setAdapter(adapter);
                    } else {

                    }
                } else {
                    Collections.sort(mDataHolder.getCategories()
                            .get(currentSelectedItem - 3).getImages());
                    adapter = new GridImageAdapter(this, mDataHolder
                            .getCategories().get(currentSelectedItem - 3),
                            mDataHolder.getFavourites());
                    mGrid.setAdapter(adapter);
                }
                return true;
            case R.id.sort_down:
                Comparator comparator = Collections.reverseOrder();
                if (currentSelectedItem < 3) {
                    if (currentSelectedItem == 0) {
                        Collections.sort(mDataHolder.getRecent().getImages(),
                                comparator);
                        adapter = new GridImageAdapter(this,
                                mDataHolder.getRecent(),
                                mDataHolder.getFavourites());
                        mGrid.setAdapter(adapter);
                    } else if (currentSelectedItem == 1) {
                        Collections.sort(mDataHolder.getFavourites(), comparator);
                        adapter = new GridImageAdapter(this,
                                mDataHolder.getFavourites());
                        mGrid.setAdapter(adapter);
                    } else {

                    }
                } else {
                    Collections.sort(
                            mDataHolder.getCategories()
                                    .get(currentSelectedItem - 3).getImages(),
                            comparator);
                    adapter = new GridImageAdapter(this, mDataHolder
                            .getCategories().get(currentSelectedItem - 3),
                            mDataHolder.getFavourites());
                    mGrid.setAdapter(adapter);
                }
                return true;
            case R.id.rate_app:
                Uri uri = Uri.parse("market://details?id=" + this.getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id="
                                    + this.getPackageName())));
                }
                return true;
            case R.id.more_apps:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri
                        .parse("https://play.google.com/store/apps/developer?id=Stefan%20Ionescu%20Freelancing&hl=en"));
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void search(String result) {
        Dialog progressDialog = ProgressDialog.show(MainActivity.this, "",
                getString(R.string.please_wait));
        progressDialog.show();
        GridImageAdapter adapter;
        ArrayList<String> al = new ArrayList<String>();
        String text = result.toUpperCase();
        if (currentSelectedItem < 3) {
            if (currentSelectedItem == 0) {
                for (String st : mDataHolder.getRecent().getImages()) {
                    if (st.toUpperCase().contains(text)) {
                        al.add(st);
                    }
                }
                mDataHolder.getRecent().setImages(al);
                adapter = new GridImageAdapter(this, mDataHolder.getRecent(),
                        mDataHolder.getFavourites());
                mGrid.setAdapter(adapter);
            } else if (currentSelectedItem == 1) {
                for (String st : mDataHolder.getFavourites()) {
                    if (st.toUpperCase().contains(text)) {
                        al.add(st);
                    }
                }
                mDataHolder.setFavourites(new ArrayList<String>());
                for (String st : al) {
                    mDataHolder.getFavourites().add(st);
                }
                adapter = new GridImageAdapter(this,
                        mDataHolder.getFavourites());
                mGrid.setAdapter(adapter);
            } else {
                progressDialog.dismiss();
            }
        } else {
            for (String st : mDataHolder.getCategories()
                    .get(currentSelectedItem - 3).getImages()) {
                if (st.toUpperCase().contains(text)) {
                    al.add(st);
                }
            }
            mDataHolder.getCategories().get(currentSelectedItem - 3)
                    .setImages(al);
            adapter = new GridImageAdapter(this, mDataHolder.getCategories()
                    .get(currentSelectedItem - 3), mDataHolder.getFavourites());
            mGrid.setAdapter(adapter);
        }
        progressDialog.dismiss();
    }

    private class SyncData extends AsyncTask<Void, Void, Integer> {
        private Dialog progressDialog;
        private int selection;
        private Recent recent;
        private ArrayList<String> favourites;
        private ArrayList<Category> categories;
        private String pressCategory;

        public SyncData() {
            super();
        }

        public SyncData(Dialog progressDialog, int selection) {
            this.progressDialog = progressDialog;
            this.selection = selection;
            this.progressDialog.show();
        }

        public SyncData(Dialog progressDialog, int selection, String pressCategory) {
            this.progressDialog = progressDialog;
            this.selection = selection;
            this.progressDialog.show();
            this.pressCategory = pressCategory;
        }

        @Override
        protected Integer doInBackground(Void... params) {
            try {
                if (selection != 2) {
                    Categories allCategories = Controller.fetchCategories();
                    recent = allCategories.getRecent();
                    favourites = new ArrayList<String>();
                    String dir = Environment.getExternalStorageDirectory()
                            + "/" + getString(R.string.app_name)
                            + "/favourites";
                    File dirFav = new File(dir);
                    dirFav.mkdirs();
                    for (File favFolders : dirFav.listFiles()) {
                        if (!favFolders.isDirectory())
                            favourites.add(favFolders.toString());
                    }
                    categories = allCategories.getCategories();
                    mDataHolder.setFavourites(favourites);
                    mDataHolder.setRecent(recent);
                    mDataHolder.setCategories(categories);
                } else {
                    favourites = new ArrayList<String>();
                    String dir = Environment.getExternalStorageDirectory()
                            + "/" + getString(R.string.app_name)
                            + "/favourites";
                    File dirFav = new File(dir);
                    dirFav.mkdirs();
                    for (File favFolders : dirFav.listFiles()) {
                        if (!favFolders.isDirectory())
                            favourites.add(favFolders.toString());
                    }
                    mDataHolder.setFavourites(favourites);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return -1;
            }
            return 1;
        }

        @Override
        protected void onPostExecute(Integer result) {
            if (result == 1) {
                if (selection == 2) {
                    if (pressCategory != null && pressCategory.equals("recent")) {
                        mGrid.setAdapter(new GridImageAdapter(MainActivity.this,
                                mDataHolder.getRecent(), favourites));
                    } else if (pressCategory != null && pressCategory.equals("category")) {
                        mGrid.setAdapter(new GridImageAdapter(MainActivity.this,
                                mDataHolder.getCategories().get(currentSelectedItem - 3), favourites));
                    } else {
                        mGrid.setAdapter(new GridImageAdapter(MainActivity.this,
                                favourites));
                    }
                } else if (selection == 10) {
                    mGrid.setAdapter(new GridImageAdapter(MainActivity.this,
                            recent, favourites));
                } else if (selection == 11) {
                    mGrid.setAdapter(new GridImageAdapter(MainActivity.this,
                            favourites));
                } else if (selection == 12) {
                    mGrid.setAdapter(new GridImageAdapter(MainActivity.this,
                            categories.get(currentSelectedItem - 3), favourites));
                }
                if (progressDialog != null) {
                    progressDialog.dismiss();
                } else {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    hideSplashScreen();
                    selectItem(0);
                }
            } else {
                showToast(R.string.error);
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
            }
            selection = 0;
        }
    }
}
