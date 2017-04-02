package com.stefan.wallpaper.demo.util;

import android.content.Context;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.stefan.wallpaper.demo.R;

public class ImageViewUtil {

    public static void setImageWithImageLoader(ImageView imageView,
                                               Context context, String categoryName, String image,
                                               ImageLoadingListener listener) {
        ImageLoader loader = ImageLoader.getInstance();
        if (!loader.isInited()) {
            loader.init(getImageConfig(context));
        }
        try {
            String uri;
            if (categoryName != null) {
                uri = Controller.WALLPAPER_URL + categoryName.trim() + "/"
                        + image.trim();
            } else {
                uri = Controller.WALLPAPER_URL + image.trim();
            }
            if (listener != null) {
                loader.displayImage(uri, imageView, options, listener);
            } else {
                loader.displayImage(uri, imageView, options);
            }
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
    }

    public static void setThumbsImageWithImageLoader(ImageView imageView,
                                                     Context context, String categoryName, String image,
                                                     ImageLoadingListener listener) {
        ImageLoader loader = ImageLoader.getInstance();
        if (!loader.isInited()) {
            loader.init(getImageConfig(context));
        }
        try {
            String uri;
            uri = Controller.THUMBS_URL + categoryName.trim() + "/"
                    + image.trim();
            if (listener != null) {
                loader.displayImage(uri, imageView, optionsForThumbs, listener);
            } else {
                loader.displayImage(uri, imageView, optionsForThumbs);
            }
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
    }

    public static final DisplayImageOptions optionsForThumbs = new DisplayImageOptions.Builder()
            .imageScaleType(ImageScaleType.EXACTLY)
            .showImageOnFail(R.drawable.icon_default)
            .showImageForEmptyUri(R.drawable.icon_default)
            .showImageOnLoading(R.drawable.icon_default).cacheOnDisc(true)
            .cacheInMemory(true).build();

    public static final DisplayImageOptions options = new DisplayImageOptions.Builder()
            .imageScaleType(ImageScaleType.EXACTLY)
            .showImageOnFail(R.drawable.icon_default)
            .showImageForEmptyUri(R.drawable.icon_default)
            .showImageOnLoading(R.drawable.icon_default).cacheOnDisc(false)
            .cacheInMemory(true).build();

    private static ImageLoaderConfiguration imageConfig;

    private static ImageLoaderConfiguration getImageConfig(Context context) {
        if (imageConfig == null) {
            imageConfig = new ImageLoaderConfiguration.Builder(context).build();
        }
        return imageConfig;
    }
}
