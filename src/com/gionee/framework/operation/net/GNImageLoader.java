// Gionee <yuwei><2013-11-12> add for CR00821559 begin
/*
 * GNImageLoader.java
 * classes : com.gionee.framework.operation.net.GNImageLoader
 * @author yuwei
 * V 1.0.0
 * Create at 2013-11-12 下午4:35:38
 */
package com.gionee.framework.operation.net;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory.Options;
import android.text.TextUtils;
import android.widget.ImageView;

import com.gionee.framework.operation.utills.PictureUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * GNImageLoader
 * 
 * @author yuwei <br/>
 * @date create at 2013-11-12 下午4:35:38
 * @description TODO 图片加载管理器
 */
public class GNImageLoader {
    private DisplayImageOptions options;
    private ImageLoader imageLoader;
    private static GNImageLoader mInstance;

    private GNImageLoader() {
        super();
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.ARGB_8888).build();
    }

    public DisplayImageOptions getShowStubOptions(int stubImgId) {
        return new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.ARGB_8888).showImageOnLoading(stubImgId).build();
    }

    public DisplayImageOptions getShowDefaultOptions(int defaultId) {
        return new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.ARGB_8888).showImageOnFail(defaultId)
                .showImageOnLoading(defaultId).build();
    }

    /**
     * @return
     * @author yuwei
     * @description TODO 获取GNImageLoader实例
     */
    public static GNImageLoader getInstance() {
        if (mInstance == null) {
            mInstance = new GNImageLoader();

        }
        return mInstance;

    }

    public void init(Context context) {
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));

    }

    /**
     * @param url
     * @param imageView
     * @author yuwei
     * @description TODO 下载url下的图片并设置到imageView
     */
    public void loadBitmap(String url, ImageView imageView) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        try {
            imageLoader.displayImage(url, imageView, getDefaultOptions());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * @param url
     * @param imageView
     * @author yuwei
     * @description TODO 下载url下的图片并设置到imageView
     */
    public void loadBitmap(String url, ImageView imageView, int stubImgId) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        try {
            imageLoader.displayImage(url, imageView, getShowStubOptions(stubImgId));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * @param url
     * @param imageView
     * @author yuwei
     * @description TODO 下载url下的图片并设置到imageView
     */
    public void loadBitmapWithDefualt(String url, ImageView imageView, int defaultId) {

        try {
            if (TextUtils.isEmpty(url)) {
                return;
            }
            imageLoader.displayImage(url, imageView, getShowDefaultOptions(defaultId));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void loadBitmap(String url, ImageView imageView, ImageLoadingListener listener) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        try {
            imageLoader.displayImage(url, imageView, getDefaultOptions(), listener);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void loadLocalBitmap(String path, final ImageView imageView) {
//        NativeImageLoader.getInstance().loadNativeImage(path, new NativeImageCallBack() {
//
//            @Override
//            public void onImageLoader(Bitmap bitmap, String path) {
//                if (bitmap == null) {
//                    return;
//                }
//                if (((String) imageView.getTag()).equals(path)) {
//                    imageView.setImageBitmap(bitmap);
//                }
//            }
//        });
        if (TextUtils.isEmpty(path)) {
            return;
        }
        Options mOPtions = PictureUtil.getDecodeOptions(path, imageView.getWidth(), imageView.getHeight());
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisk(false).displayer(new FadeInBitmapDisplayer(300)).decodingOptions(mOPtions)
                .build();
        try {
            imageLoader.displayImage("file://" + path, imageView, options);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void loadLocalBitmap(String path, ImageLoadingListener listener) {
        if (TextUtils.isEmpty(path)) {
            return;
        }
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(false)
                .cacheOnDisk(false).displayer(new FadeInBitmapDisplayer(300)).build();
        try {
            imageLoader.loadImage("file://" + path, options, listener);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * @return
     * @author yuwei
     * @description TODO 获取ImageLoader
     */
    public ImageLoader getImageLoader() {
        return imageLoader;
    }

    /**
     * @return
     * @author yuwei
     * @description TODO 获取加载配置选项
     */
    public DisplayImageOptions getDefaultOptions() {
        return options;
    }
}
