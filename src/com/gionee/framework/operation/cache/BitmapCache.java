package com.gionee.framework.operation.cache;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import android.graphics.Bitmap;

import com.gionee.framework.operation.utills.LogUtils;

/**
 * com.gionee.app_framework.operation.net.BitmapCache
 * 
 * @author yuwei <br/>
 *         create at 2013-3-19 下午1:47:46 TODO cache for bitmap
 */
public class BitmapCache {
    // Gionee <yuwei><2013-5-23> add for CR00821559 begin
    private static final int maxSize = 20;
    private Map<String, WeakReference<Bitmap>> cacheMap;
    private LinkedList<String> cacheKey;
    private static BitmapCache instance;

    private BitmapCache() {
        cacheMap = new HashMap<String, WeakReference<Bitmap>>(maxSize + 1);
        cacheKey = new LinkedList<String>();
    }

    public synchronized static BitmapCache getInstance() {
        if (instance == null) {
            instance = new BitmapCache();
        }
        return instance;
    }

    public synchronized void put(String key, Bitmap bitmap) {
        if (!cacheMap.containsKey(key)) {
            if (cacheMap.size() >= maxSize) {
                Bitmap remove = cacheMap.remove(cacheKey.poll()).get();
                LogUtils.log("netImg", "remov  bitmpa is " + remove);
            }
            cacheKey.offer(key);
            cacheMap.put(key, new WeakReference<Bitmap>(bitmap));
        }
    }

    public synchronized Bitmap get(String key) {
        Bitmap target = null;
        if (cacheMap.containsKey(key)) {
            target = cacheMap.get(key).get();
            if (target == null || target.isRecycled()) {
                remove(key);
                target = null;
            }
            LogUtils.log("netImg", "get  bitmpa " + target);
        }
        return target;
    }

    public synchronized void remove(String key) {
        if (cacheMap.containsKey(key)) {
            Bitmap remove = cacheMap.remove(key).get();
            cacheKey.remove(key);
            LogUtils.log("netImg", "remov  bitmpa is " + remove);
        }
    }

    public synchronized void recycledAll() {
        for (String key : cacheKey) {
            WeakReference<Bitmap> bitmap = cacheMap.get(key);
            if (bitmap == null)
                return;
            Bitmap imageBitmap = bitmap.get();
            if (imageBitmap != null) {
                imageBitmap.recycle();
            }
        }
        removeAll();
    }

    public synchronized void removeAll() {
        cacheKey.clear();
        cacheMap.clear();
    }
    // Gionee <yuwei><2013-6-2> add for CR00821559 end
}
