package com.stefan.wallpaper.demo;

import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

public class BaseActivity extends FragmentActivity {

    public static Typeface sRobotoBlackItalic;
    public static Typeface sRobotoThin;
    public static Typeface sRobotoLight;
    public static Typeface sRobotoBlack;
    public static Typeface sRobotoRegular;

    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initTypefaces();
    }

    public void makeToast(int resId) {
        makeToast(getString(resId));
    }

    public void makeToast(String text) {
        if (mToast == null) {
            mToast = Toast.makeText(this, text, Toast.LENGTH_LONG);
        } else {
            mToast.setText(text);
        }
        mToast.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mToast != null) {
            mToast.cancel();
        }
    }

    public void showToast(int resId) {
        showToast(getString(resId));
    }

    public void showToast(String message) {
        if (mToast != null) {
            mToast.setText(message);
        } else {
            mToast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        }
        mToast.show();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void initTypefaces() {
        if (sRobotoBlackItalic == null) {
            sRobotoBlackItalic = Typeface.createFromAsset(getAssets(),
                    "fonts/Roboto-BlackItalic.ttf");
        }
        if (sRobotoThin == null) {
            sRobotoThin = Typeface.createFromAsset(getAssets(),
                    "fonts/Roboto-Thin.ttf");
        }
        if (sRobotoLight == null) {
            sRobotoLight = Typeface.createFromAsset(getAssets(),
                    "fonts/Roboto-Light.ttf");
        }
        if (sRobotoBlack == null) {
            sRobotoBlack = Typeface.createFromAsset(getAssets(),
                    "fonts/Roboto-Black.ttf");
        }
        if (sRobotoRegular == null) {
            sRobotoRegular = Typeface.createFromAsset(getAssets(),
                    "fonts/Roboto-Regular.ttf");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // EasyTracker.getInstance(this).activityStart(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        //  EasyTracker.getInstance(this).activityStop(this);
    }
}
