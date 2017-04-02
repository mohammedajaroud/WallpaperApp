package com.stefan.wallpaper.demo;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

public class AboutUsActivity extends BaseActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_us);

        TextView mTextOnImage = (TextView) findViewById(R.id.about_us_text_on_image);
        TextView mHeaderText = (TextView) findViewById(R.id.about_us_header_text);
        TextView mSubheaderText = (TextView) findViewById(R.id.about_us_second_italic_text);
        TextView mFirstText = (TextView) findViewById(R.id.about_us_first_text);
        TextView mSecondText = (TextView) findViewById(R.id.about_us_second_italic_text);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
            setTitle(getString(R.string.about_us));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            getActionBar().setHomeButtonEnabled(true);
        }

        mTextOnImage.setTypeface(BaseActivity.sRobotoThin);
        mHeaderText.setTypeface(BaseActivity.sRobotoLight);
        mSubheaderText.setTypeface(BaseActivity.sRobotoLight);
        mFirstText.setTypeface(BaseActivity.sRobotoLight);
        mSecondText.setTypeface(BaseActivity.sRobotoBlackItalic);

    }

    @SuppressLint("NewApi")
    @Override
    public void setTitle(CharSequence title) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getActionBar().setTitle(title);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
