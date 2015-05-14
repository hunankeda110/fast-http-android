package com.gionee.framework.operation.cache;

import android.content.Context;

import com.gionee.framework.event.INetDiscCacheManage;
import com.gionee.framework.model.bean.HttpConstants;
import com.gionee.framework.model.bean.MyBean;
import com.gionee.framework.model.config.ControlKey;
import com.gionee.framework.operation.net.NetUtil;
import com.gionee.framework.operation.utills.FileUtil;
import com.gionee.framework.operation.utills.LogUtils;
import com.gionee.framework.operation.utills.Md5Utill;

/**
 * com.gionee.app_framework.operation.cache.FNetCacheManage
 * 
 * @author yuwei <br/>
 *         create at 2013-4-1 下午4:30:16 TODO local object cache
 */
public class FNetDiscCacheManage implements INetDiscCacheManage {
    private static final String TAG = "netCache";

    // Gionee <yuwei><2012-5-27> add for CR00821559 begin
    @Override
    public boolean save(Context context, MyBean request, String response) {
        LogUtils.log(TAG, "写key = " + request.toString());
        String key = createKeyByRequest(request);
        LogUtils.log(TAG, "写key = " + key + response.toString());
        return FileUtil.writeObjectToLocation(context, key, response);
    }

    @Override
    public String loadList(Context context, MyBean request) {
        LogUtils.log(TAG, "读key = " + request.toString());
        String key = createKeyByRequest(request);
        LogUtils.log(TAG, "读key = " + key);
        String str = FileUtil.readObjectFromLocation(context, key);
        return str;
    }

    @Override
    public boolean saveList(Context context, MyBean request, String response) {
        LogUtils.log(TAG, "写key = " + request.toString());
        String key = createKeyByRequest(request);
        LogUtils.log(TAG, "写key = " + key);
        return FileUtil.writeObjectToLocation(context, key, response);
    }

    @Override
    public String load(Context context, MyBean request) {
        LogUtils.log(TAG, "读key = " + request.toString());
        String key = createKeyByRequest(request);
        LogUtils.log(TAG, "读key = " + key);
        String str = FileUtil.readObjectFromLocation(context, key);
        LogUtils.log(TAG, key + str);
        return str;
    }

    @Override
    public void clearAll(Context context) {
        FileUtil.deleteFile(context.getApplicationContext().getFilesDir());
    }

    @Override
    public void clearCacheByRequest(Context context, MyBean request) {
        String key = createKeyByRequest(request);
        FileUtil.deleteFileFromLocation(context, key);
    }

    @Override
    public String createKeyByRequest(MyBean request) {
        MyBean bean = (MyBean) request.clone();
        String url = bean.getString(ControlKey.request.control.__url_s);
        String port = url.substring(url.lastIndexOf("/") + 1);
        bean.remove(HttpConstants.Request.UID_S);
        return Md5Utill.makeMd5Sum(url + NetUtil.encodeUrl(bean)) + port;
    }
    // Gionee <yuwei><2012-6-5> add for CR00821559 end
}
