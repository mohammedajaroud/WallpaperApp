package com.stefan.wallpaper.demo.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.stefan.wallpaper.demo.BaseActivity;
import com.stefan.wallpaper.demo.MainActivity;
import com.stefan.wallpaper.demo.R;
import com.stefan.wallpaper.demo.models.Category;
import com.stefan.wallpaper.demo.models.Recent;
import com.stefan.wallpaper.demo.util.Controller;
import com.stefan.wallpaper.demo.util.ImageViewUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GridImageAdapter extends BaseAdapter implements OnClickListener {

    private Context mContext;
    private Recent mRecent;
    private List<String> mFavourites;
    private Category mCategory;
    private LayoutInflater mInflater;
    private ArrayList<String> mFavouritesList;

    private File fileToSave;
    private String imageName = "";
    private String categoryName = "";

    public static String currentCategoryName;

    public Category getCurrentCategory() {
        return mCategory;
    }

    public GridImageAdapter(Context context, Recent recent,
                            ArrayList<String> favouritesList) {
        mContext = context;
        mRecent = recent;
        mFavouritesList = favouritesList;
        currentCategoryName = "Recent";
        mInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public GridImageAdapter(Context context, List<String> favourites) {
        mContext = context;
        mFavourites = favourites;
        mFavouritesList = (ArrayList<String>) favourites;
        currentCategoryName = "Favourites";
        mInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public GridImageAdapter(Context context, Category category,
                            ArrayList<String> favouriteList) {
        mContext = context;
        mCategory = category;
        mFavouritesList = favouriteList;
        currentCategoryName = category.getName();
        mInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if (mCategory != null) {
            return mCategory.getImages().size();
        } else if (mRecent != null) {
            return mRecent.getImages().size();
        } else if (mFavourites != null) {
            return mFavourites.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        public ImageView mImage;
        public ImageView mIcon;
        public TextView mText;
        public LinearLayout mLayout;
    }

    private SimpleImageLoadingListener listener = new SimpleImageLoadingListener() {

        @Override
        public void onLoadingFailed(String imageUri, View view,
                                    FailReason failReason) {
            String s[] = imageUri.split("/");
            if (mCategory != null
                    && mCategory.getImages().contains(
                    imageUri.split("/")[s.length - 1])) {
                mCategory.getImages().remove(imageUri.split("/")[s.length - 1]);
                notifyDataSetChanged();
            } else if (mRecent != null
                    && mRecent.getImages().contains(
                    imageUri.split("/")[s.length - 2] + "/"
                            + imageUri.split("/")[s.length - 1])) {
                mRecent.getImages().remove(
                        imageUri.split("/")[s.length - 2] + "/"
                                + imageUri.split("/")[s.length - 1]);
                notifyDataSetChanged();
            }
        }

        @Override
        public void onLoadingComplete(String imageUri, View view,
                                      Bitmap loadedImage) {
            if (view == null) {
                BufferedOutputStream out = null;
                try {
                    out = new BufferedOutputStream(new FileOutputStream(
                            fileToSave));
                    loadedImage.compress(CompressFormat.JPEG, 100, out);
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.grid_image, parent, false);
            holder = new ViewHolder();
            holder.mImage = (ImageView) convertView
                    .findViewById(R.id.grid_image);
            holder.mIcon = (ImageView) convertView
                    .findViewById(R.id.grid_image_icon);
            holder.mText = (TextView) convertView
                    .findViewById(R.id.grid_image_text);
            holder.mLayout = (LinearLayout) convertView
                    .findViewById(R.id.grid_image_layout);
            holder.mIcon.setOnClickListener(this);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mText.setTypeface(BaseActivity.sRobotoLight);

        if (mCategory != null) {
            String image = mCategory.getImages().get(position);
            String imageNameInFavourite = image.split("[.]")[0];
            String categoryNameInFavourite = mCategory.getName();
            for (String stri : mFavouritesList) {
                if (stri.contains(imageNameInFavourite)
                        && stri.contains(categoryNameInFavourite)) {
                    holder.mIcon.setImageResource(R.drawable.favorite_on_white);
                    break;
                } else {
                    holder.mIcon
                            .setImageResource(R.drawable.favorite_off_white);
                }
            }
            ImageViewUtil.setThumbsImageWithImageLoader(holder.mImage,
                    mContext, mCategory.getName(), image, listener);
            String[] imageSub = image.split("[.]");
            if (imageSub.length > 1) {
                holder.mText.setText(imageSub[0]);
            } else {
                holder.mText.setText(image);
            }
        } else if (mFavourites != null) {
            ImageLoader.getInstance().displayImage(
                    Uri.fromFile(new File(mFavourites.get(position)))
                            .toString().replace("%20", " "), holder.mImage);
            holder.mLayout.setVisibility(View.INVISIBLE);
        } else if (mRecent != null) {
            String image = mRecent.getImages().get(position);
            String imageNameInFavourite = mRecent.getImages().get(position)
                    .split("[/]")[1];
            String categoryNameInFavourite = mRecent.getImages().get(position)
                    .split("[/]")[0];
            for (String stri : mFavouritesList) {
                if (stri.contains(imageNameInFavourite)
                        && stri.contains(categoryNameInFavourite)) {
                    holder.mIcon.setImageResource(R.drawable.favorite_on_white);
                    break;
                } else {
                    holder.mIcon
                            .setImageResource(R.drawable.favorite_off_white);
                }
            }
            String recentCatName = image.split("[/]")[0];
            String recentImageName = image.split("[/]")[1];

            ImageViewUtil.setThumbsImageWithImageLoader(holder.mImage,
                    mContext, recentCatName, recentImageName, listener);
            String[] imageSub = image.split("[.]");
            if (imageSub.length > 1) {
                holder.mText.setText(imageSub[0].split("[/]")[1]);
            } else {
                holder.mText.setText(image.split("[/]")[1]);
            }
        }
        holder.mIcon.setTag(position);
        return convertView;
    }

    private void fillFavourites(String cat, String img) {
        String dir = Environment.getExternalStorageDirectory() + "/"
                + mContext.getString(R.string.app_name) + "/favourites/";
        mFavouritesList.add(dir + cat + "--" + img);
        ((MainActivity) mContext).setFavourites(mFavouritesList);
    }

    private void removeFavourites(String cat, String img) {
        String dir = Environment.getExternalStorageDirectory() + "/"
                + mContext.getString(R.string.app_name) + "/favourites/";
        mFavouritesList.remove(dir + cat + "--" + img);
        ((MainActivity) mContext).setFavourites(mFavouritesList);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        int position = (Integer) v.getTag();
        String dirFav = Environment.getExternalStorageDirectory() + "/"
                + mContext.getString(R.string.app_name) + "/favourites";
        if (currentCategoryName.equals("Recent")) {
            imageName = mRecent.getImages().get(position).split("[/]")[1];
            categoryName = mRecent.getImages().get(position).split("[/]")[0];
            File file = new File(dirFav, categoryName + "--" + imageName);
            if (file.exists()) {
                file.delete();
                if (mContext != null) {
                    removeFavourites(categoryName, imageName);
                    Toast.makeText(mContext,
                            "Image has been removed from favourites!",
                            Toast.LENGTH_SHORT).show();
                    ImageView icon = (ImageView) v
                            .findViewById(R.id.grid_image_icon);
                    icon.setImageResource(R.drawable.favorite_off_white);
                }
            } else {
                fileToSave = new File(dirFav, categoryName + "--" + imageName);
                ImageLoader.getInstance().loadImage(
                        Controller.WALLPAPER_URL + categoryName + "/"
                                + imageName, listener);
                if (mContext != null) {
                    fillFavourites(categoryName, imageName);
                    Toast.makeText(mContext,
                            "Image has been added in favourites!",
                            Toast.LENGTH_SHORT).show();
                    ImageView icon = (ImageView) v
                            .findViewById(R.id.grid_image_icon);
                    icon.setImageResource(R.drawable.favorite_on_white);
                }
            }
        } else {
            imageName = mCategory.getImages().get(position);
            categoryName = mCategory.getName();
            File file = new File(dirFav, categoryName + "--" + imageName);
            if (file.exists()) {
                file.delete();
                if (mContext != null) {
                    removeFavourites(categoryName, imageName);
                    Toast.makeText(mContext,
                            "Image has been removed from favourites!",
                            Toast.LENGTH_SHORT).show();
                    ImageView icon = (ImageView) v
                            .findViewById(R.id.grid_image_icon);
                    icon.setImageResource(R.drawable.favorite_off_white);
                }
            } else {
                fileToSave = new File(dirFav, categoryName + "--" + imageName);
                ImageLoader.getInstance().loadImage(
                        Controller.WALLPAPER_URL + categoryName + "/"
                                + imageName, listener);
                if (mContext != null) {
                    fillFavourites(categoryName, imageName);
                    Toast.makeText(mContext,
                            "Image has been added in favourites!",
                            Toast.LENGTH_SHORT).show();
                    ImageView icon = (ImageView) v
                            .findViewById(R.id.grid_image_icon);
                    icon.setImageResource(R.drawable.favorite_on_white);
                }
            }
        }
    }
}
