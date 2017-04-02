package com.stefan.wallpaper.demo.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.stefan.wallpaper.demo.BaseActivity;
import com.stefan.wallpaper.demo.FullScreenGalleryActivity;
import com.stefan.wallpaper.demo.R;
import com.stefan.wallpaper.demo.SetAsWallpaperActivity;
import com.stefan.wallpaper.demo.util.Controller;
import com.stefan.wallpaper.demo.util.ImageViewUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class FullScreenGalleryFragment extends Fragment implements
        OnClickListener {

    public static final String FULL_SCREEN_GALLERY_CATEGORY = "com.stefan.wallpaper.demo.FullScreenGalleryActivity."
            + "FullScreenGalleryFragment.category";
    public static final String FULL_SCREEN_GALLERY_IMAGE = "com.stefan.wallpaper.demo.FullScreenGalleryActivity."
            + "FullScreenGalleryFragment.image";

    private ArrayList<String> mFavouritesList;

    private ImageView mImageView;
    private ProgressBar mBar;

    private TextView mImageTitle;
    private TextView mImageCategory;

    private String mCategory;
    private String mImage;

    private TextView mSetAsWallpaper;
    private TextView mSave;

    private ImageView mShare;
    private ImageView mBack;

    private File fileToSave;

    private SimpleImageLoadingListener listener = new SimpleImageLoadingListener() {

        @Override
        public void onLoadingFailed(String imageUri, View view,
                                    FailReason failReason) {
            mBar.setVisibility(View.GONE);
            if (getActivity() != null) {
                ((BaseActivity) getActivity())
                        .showToast(getString(R.string.error) + "\n"
                                + failReason.getCause().getMessage());
            }
        }

        @Override
        public void onLoadingComplete(String imageUri, View view,
                                      Bitmap loadedImage) {
            mBar.setVisibility(View.GONE);
            if (view == null) { // it is configured to set as favorite or share
                BufferedOutputStream out = null;
                try {
                    out = new BufferedOutputStream(new FileOutputStream(
                            fileToSave));
                    loadedImage.compress(CompressFormat.JPEG, 100, out);
                    if (!fileToSave.toString().contains("favourites")) {
                        Intent share = new Intent(Intent.ACTION_SEND);
                        share.setType("text/plain");
                        share.putExtra(Intent.EXTRA_SUBJECT, mImage);
                        share.putExtra(Intent.EXTRA_STREAM,
                                Uri.fromFile(fileToSave));
                        startActivity(Intent.createChooser(share,
                                getString(R.string.share)));
                    } else {
                        ((BaseActivity) getActivity())
                                .showToast(R.string.image_saved);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException e) {
                            // ignore
                        }
                    }
                }
            }
        }
    };

    public static final FullScreenGalleryFragment newInstance(String category,
                                                              String image) {
        FullScreenGalleryFragment f = new FullScreenGalleryFragment();
        Bundle args = new Bundle(2);
        args.putString(FULL_SCREEN_GALLERY_CATEGORY, category);
        args.putString(FULL_SCREEN_GALLERY_IMAGE, image);
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mCategory = getArguments().getString(FULL_SCREEN_GALLERY_CATEGORY);
        mImage = getArguments().getString(FULL_SCREEN_GALLERY_IMAGE);
        mFavouritesList = ((FullScreenGalleryActivity) getActivity())
                .getFavourites();

        View v = inflater.inflate(R.layout.full_screen_gallery_item, container,
                false);
        mImageView = (ImageView) v
                .findViewById(R.id.full_screen_gallery_item_image);
        mBar = (ProgressBar) v
                .findViewById(R.id.full_screen_gallery_item_imageProgressBar);

        mImageTitle = (TextView) v
                .findViewById(R.id.full_screen_gallery_item_image_label);
        mImageCategory = (TextView) v
                .findViewById(R.id.full_screen_gallery_item_category_name);

        mSetAsWallpaper = (TextView) v
                .findViewById(R.id.full_screen_gallery_item_set_as_wallpaper);
        mSave = (TextView) v
                .findViewById(R.id.full_screen_gallery_item_add_to_favorite);
        mShare = (ImageView) v
                .findViewById(R.id.full_screen_gallery_item_share);
        mBack = (ImageView) v.findViewById(R.id.full_screen_gallery_item_back);

        mImageView.setOnClickListener(this);
        ImageViewUtil.setImageWithImageLoader(mImageView, getActivity(),
                mCategory, mImage, listener);

        mImageTitle.setTypeface(BaseActivity.sRobotoLight);
        mImageCategory.setTypeface(BaseActivity.sRobotoLight);

        String[] imageSub = mImage.split("[.]");
        if (imageSub.length > 1) {
            mImageTitle.setText(imageSub[0]);
        } else {
            mImageTitle.setText(mImage);
        }
        mImageCategory.setText(mCategory.toUpperCase());

        for (String str : mFavouritesList) {
            String holderFav[] = str.split("[/]");
            // Log.v("holder", holderFav[holderFav.length-1] + mImage);
            if (holderFav[holderFav.length - 1].equals(mCategory + "--"
                    + mImage)) {
                mSave.setText(R.string.fragment_remove_from_favourite);
                break;
            } else {
                mSave.setText(R.string.fragment_add_to_favorites);
            }
        }

        mSetAsWallpaper.setOnClickListener(this);
        mSave.setOnClickListener(this);
        mShare.setOnClickListener(this);
        mBack.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        String dir = Environment.getExternalStorageDirectory() + "/"
                + getString(R.string.app_name);
        String dirFav = Environment.getExternalStorageDirectory() + "/"
                + getString(R.string.app_name) + "/favourites";
        switch (v.getId()) {
            case R.id.full_screen_gallery_item_set_as_wallpaper:
                Intent intent = new Intent(getActivity(),
                        SetAsWallpaperActivity.class);
                intent.putExtra(FULL_SCREEN_GALLERY_CATEGORY, mCategory);
                intent.putExtra(FULL_SCREEN_GALLERY_IMAGE, mImage);
                startActivity(intent);
                break;
            case R.id.full_screen_gallery_item_share:
                mBar.setVisibility(View.VISIBLE);
                fileToSave = new File(dir, mCategory + "--" + mImage);
                ImageLoader.getInstance().loadImage(
                        Controller.WALLPAPER_URL + mCategory + "/" + mImage,
                        listener);
                break;
            case R.id.full_screen_gallery_item_add_to_favorite:
                mBar.setVisibility(View.VISIBLE);
                File file = new File(dirFav, mCategory + "--" + mImage);
                if (file.exists()) {
                    file.delete();
                    mBar.setVisibility(View.GONE);
                    if (getActivity() != null) {
                        ((BaseActivity) getActivity())
                                .showToast("Image has been removed from favourites!");
                        mSave.setText(R.string.fragment_add_to_favorites);
                        removeFragmentFavourites(mCategory, mImage);
                    }
                } else {
                    fileToSave = new File(dirFav, mCategory + "--" + mImage);
                    ImageLoader.getInstance().loadImage(
                            Controller.WALLPAPER_URL + mCategory + "/" + mImage,
                            listener);
                    mSave.setText(R.string.fragment_remove_from_favourite);
                    fillFragmentFavourites(mCategory, mImage);
                }
                break;
            case R.id.full_screen_gallery_item_back:
                getActivity().onBackPressed();
                break;
        }
    }

    public void fillFragmentFavourites(String cat, String img) {
        String dir = Environment.getExternalStorageDirectory() + "/"
                + getActivity().getString(R.string.app_name) + "/favourites/";
        mFavouritesList.add(dir + cat + "--" + img);
        ((FullScreenGalleryActivity) getActivity()).fillFavourites(mCategory,
                mImage);
    }

    public void removeFragmentFavourites(String cat, String img) {
        String dir = Environment.getExternalStorageDirectory() + "/"
                + getActivity().getString(R.string.app_name) + "/favourites/";
        mFavouritesList.remove(dir + cat + "--" + img);
        ((FullScreenGalleryActivity) getActivity()).removeFavourites(mCategory,
                mImage);
    }

}