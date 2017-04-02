package com.stefan.wallpaper.demo;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.nhaarman.listviewanimations.appearance.AnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.SwingLeftInAnimationAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.stefan.wallpaper.demo.adapters.GridImageAdapter;
import com.stefan.wallpaper.demo.adapters.LeftMenuAdapter;
import com.stefan.wallpaper.demo.util.DataHolder;

public class NavigationDrawerActivity extends BaseActivity {

    private DrawerLayout mDrawerLayout;
    protected DynamicListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private boolean mShouldFinish = false;
    protected DataHolder mDataHolder;

    protected GridView mGrid;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    protected int currentSelectedItem = 0;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        mDrawerTitle = getString(R.string.select_category);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (DynamicListView) findViewById(R.id.left_drawer);

        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
                GravityCompat.START);
        // TODO Set adapter
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            getActionBar().setHomeButtonEnabled(true);
        }

        mDrawerLayout.setFocusableInTouchMode(false);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, R.string.drawer_open,
                R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                if (currentSelectedItem < 3) {
                    if (currentSelectedItem == 0) {
                        setTitle(GridImageAdapter.currentCategoryName);
                    } else if (currentSelectedItem == 1) {
                        setTitle("Favourites");
                    } else if (currentSelectedItem == 2) {
                        setTitle(getString(R.string.about_us));
                    }
                } else {
                    setTitle(GridImageAdapter.currentCategoryName);
                }
                supportInvalidateOptionsMenu();
                mShouldFinish = false;
                mDrawerList.setAdapter(null);
            }

            public void onDrawerOpened(View drawerView) {
                setTitle(mDrawerTitle.toString());
                LeftMenuAdapter adapter = new LeftMenuAdapter(NavigationDrawerActivity.this, mDataHolder.getCategories());
                /*mDrawerList.setAdapter(new LeftMenuAdapter(
                        NavigationDrawerActivity.this, mDataHolder.getCategories()));*/
                AnimationAdapter animAdapter;
                animAdapter = new SwingLeftInAnimationAdapter(adapter);
                animAdapter.setAbsListView(mDrawerList);
                mDrawerList.setAdapter(animAdapter);
                supportInvalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mGrid = (GridView) findViewById(R.id.grid);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content
        // view
        // boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        // menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            selectItem(position);
        }
    }

    public void selectItem(int position) {
        /*
		 * // update the main content by replacing fragments Fragment fragment =
		 * new Fragment(); Bundle args = new Bundle(); args.putInt("",
		 * position); fragment.setArguments(args);
		 * 
		 * FragmentManager fragmentManager = getSupportFragmentManager();
		 * fragmentManager.beginTransaction() .replace(R.id.content_frame,
		 * fragment).commit();
		 */
        currentSelectedItem = position;
        setGridAdapter(position);
        mDrawerList.setItemChecked(position, true);
        setTitle(position);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public void onBackPressed() {
        if (!mShouldFinish && !mDrawerLayout.isDrawerOpen(mDrawerList)) {
            makeToast(R.string.confirm_exit);
            mShouldFinish = true;
            mDrawerLayout.openDrawer(mDrawerList);
        } else if (!mShouldFinish && mDrawerLayout.isDrawerOpen(mDrawerList)) {
            mDrawerLayout.closeDrawer(mDrawerList);
        } else {
            super.onBackPressed();
        }
    }

    protected void setGridAdapter(int position) {
        throw new RuntimeException("Override this method in your class.");
    }

    @SuppressLint("NewApi")
    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getActionBar().setTitle(mTitle);
        } else {
            super.setTitle(mTitle);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
}