// Gionee <yuwei><2013-5-27> add for CR00821559 begin
package com.gionee.framework.operation.business;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.res.Resources.NotFoundException;
import android.text.TextUtils;

import com.gionee.framework.event.IResponseListener;
import com.gionee.framework.model.bean.HttpConstants;
import com.gionee.framework.model.bean.MyBean;
import com.gionee.framework.model.config.ControlKey;
import com.gionee.framework.model.config.ControlKey.request.control.CacheType;
import com.gionee.framework.operation.cache.ShareDataManager;
import com.gionee.framework.operation.net.NetRequestHandler;
import com.gionee.framework.operation.page.PageCacheManager;
import com.gionee.framework.operation.utills.JSONArrayHelper;
import com.gionee.framework.operation.utills.LogUtils;

/**
 * com.gionee.app_framework.operation.business.FPortBusiness
 * 
 * @author yuwei <br/>
 *         create at 2013-3-19 下午1:45:27 TODO handle network request
 */
public abstract class FPortBusiness {
    private static final String TAG = "FPortBusiness";

    protected FPortBusiness() {

    }

    /**
     * 开始请求数据
     * 
     * @param requestEntity
     * @param session
     */
    public final void startBusiness(final FRequestEntity requestEntity, final Object session) {
        try {
            requestEntity.onStartBusiness();
            // 缓存类型
            CacheType cacheType = requestEntity.mRequestParam
                    .getCacheType(ControlKey.request.control.__cacheType_enum);
            LogUtils.logd(TAG, "cacheType = " + cacheType);

            if (cacheType != CacheType.noneCache) {
                addCachedInterfaceVersion(requestEntity);
                String cache = getCacheData(requestEntity);
                boolean isAccessNet = true;
                isAccessNet = handleCacheData(requestEntity, session, cacheType, cache, isAccessNet);
                if (!isAccessNet) {
                    return;
                }
            }
            // 请求网络
            pushRequest(requestEntity, session);
        } catch (Exception e) {
            LogUtils.logd(TAG, LogUtils.getThreadName() + "exception: " + e);
            e.printStackTrace();
            requestEntity.mHandle.onErrorResult(requestEntity.mBusinessType, "", e.getMessage(), session);
        }

    }

    /**
     * 
     * @param requestEntity
     * @param id
     * @param keyOfId
     * @return
     * @description TODO 删除列表缓存中某个id的数据
     */
    public boolean deleteItemOnListCacheByRequest(final FRequestEntity requestEntity, String id,
            String keyOfId) {
        LogUtils.logd(TAG, LogUtils.getThreadName());
        try {
            requestEntity.onStartBusiness();
            addCachedInterfaceVersion(requestEntity);
            String response = getCacheData(requestEntity);
            JSONObject json = new JSONObject(response);
            boolean isSuccess = json.optBoolean(HttpConstants.Response.SUCCESS);
            JSONObject obj = json.optJSONObject(HttpConstants.Response.DATA);
            if (!isSuccess || obj == null) {
                return false;
            }
            JSONArray array = obj.optJSONArray(HttpConstants.Response.LIST_JA);
            for (int i = 0; i < array.length(); i++) {
                try {
                    JSONObject itemData = array.optJSONObject(i);
                    String itemId = itemData.optString(keyOfId);
                    if (itemId.equals(id)) {
                        new JSONArrayHelper(array).remove(i);
                        obj.put(HttpConstants.Response.LIST_JA, array);
                        json.put(HttpConstants.Response.DATA, obj);
                        // 更新文件缓存
                        saveToCache(requestEntity, json.toString());
                        // 更新内存缓存
                        updateCacheInRam(requestEntity, id, keyOfId);
                        LogUtils.logd(TAG, LogUtils.getThreadName() + " delete cache success!");
                        return true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtils.logd(TAG, LogUtils.getThreadName() + "Exception: i = " + i + ", " + e);
                }
            }
        } catch (Exception e) {
            LogUtils.logd(TAG, LogUtils.getThreadName() + " when get cache. " + e);
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @param requestEntity
     * @param id
     * @param keyOfId
     * @throws JSONException
     * @description TODO 更新内存缓存，仅用于缓存的列表数据
     */
    private void updateCacheInRam(final FRequestEntity requestEntity, String id, String keyOfId)
            throws JSONException {
        MyBean data = PageCacheManager.LookupPageData(requestEntity.mTargetPageDataKey);
        JSONObject jsonData = data.getJSONObject(requestEntity.mDataTargetKey);
        String listKey = requestEntity.mListRequestParams.mListKey;
        if (jsonData == null) {
            return;
        }
        JSONArray oldArray = jsonData.optJSONArray(listKey);
        for (int j = 0; j < oldArray.length(); j++) {
            JSONObject itemData1 = oldArray.optJSONObject(j);
            String itemId1 = itemData1.optString(keyOfId);
            if (itemId1.equals(id)) {
                new JSONArrayHelper(oldArray).remove(j);
                jsonData.put(HttpConstants.Response.LIST_JA, oldArray);
                data.putJSONObject(requestEntity.mDataTargetKey, jsonData);
                LogUtils.logd(TAG, LogUtils.getThreadName() + " memory cache updated: " + " j = " + j
                        + ", id = " + id);
                break;
            }
        }
    }

    /**
     * @param requestEntity
     * @description TODO 请求参数中添加接口版本号
     */
    private void addCachedInterfaceVersion(final FRequestEntity requestEntity) {
        String interfaceVersionKey = getInterfaceVersionkey(requestEntity);
        MyBean bean = requestEntity.getRequestParam();
        LogUtils.log("netCache", "interfaceVersionKey=" + interfaceVersionKey);
        long version;
        try {
            version = ShareDataManager.getInterfaceDataVersionNumber(requestEntity.mHandle.getSelfContext(),
                    interfaceVersionKey);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            ShareDataManager.setInterfaceDataVersionNumber(requestEntity.mHandle.getSelfContext(),
                    interfaceVersionKey, 1);
            version = 1;
        }
        bean.put(HttpConstants.Request.VERSION_L, version);
    }

    /**
     * @param requestEntity
     * @description TODO 请求参数中设置接口版本号
     */
    private void setInterfaceVersion(final FRequestEntity requestEntity, long version) {
        MyBean bean = requestEntity.getRequestParam();
        bean.put(HttpConstants.Request.VERSION_L, version);
    }

    /**
     * @param requestEntity
     * @return
     * @description TODO 获取sharePrefrence中存储接口版本号的key, 有分类，使用url+分类，没有分类使用单独url.
     */
    protected String getInterfaceVersionkey(final FRequestEntity requestEntity) {
        MyBean bean = requestEntity.getRequestParam();
        String interfaceVersionKey;
//        if (bean.containsKey(HttpConstants.Request.BargainPrice.CATEGORY)) {
//            int categoryId = bean.getInt(HttpConstants.Request.BargainPrice.CATEGORY);
//            interfaceVersionKey = requestEntity.mBusinessType + "?"
//                    + HttpConstants.Request.BargainPrice.CATEGORY + "=" + categoryId;
//        } else {
        interfaceVersionKey = requestEntity.mBusinessType;
//        }
        return interfaceVersionKey;
    }

    private boolean handleCacheData(final FRequestEntity requestEntity, final Object session,
            CacheType cacheType, String cache, boolean isAccessNet) {
        if (!TextUtils.isEmpty(cache)) {
            // 有缓存
            isAccessNet = handleCacheType(requestEntity, cacheType, isAccessNet);
            isAccessNet = handleCacheData(requestEntity, session, cache, isAccessNet);
        } else {
            setInterfaceVersion(requestEntity, 1); // 接口版本号设置为默认值 1
        }
        return isAccessNet;
    }

    /**
     * boolean 对不同的缓存类型进行初始化设置
     * 
     * @param requestEntity
     * @param cacheType
     * @param isAccessNet
     * @return TODO
     */
    protected abstract boolean handleCacheType(final FRequestEntity requestEntity, CacheType cacheType,
            boolean isAccessNet);

    /**
     * String
     * 
     * @param requestEntity
     * @return TODO
     */
    protected abstract String getCacheData(final FRequestEntity requestEntity);

    /**
     * 使用缓存数据，并返回给页面
     * 
     * @param requestEntity
     * @param session
     * @param cache
     * @param isAccessNet
     * @return
     */
    protected abstract boolean handleCacheData(final FRequestEntity requestEntity, final Object session,
            String cache, boolean isAccessNet);

    /**
     * void
     * 
     * @param requestEntity
     * @param responseEntity
     * @param data
     *            TODO
     */
    protected abstract void addListData(final FRequestEntity requestEntity, ResponseEntity responseEntity,
            MyBean data);

    /**
     * @param requestEntity
     * @param session
     * @throws Exception
     */
    protected void pushRequest(final FRequestEntity requestEntity, final Object session) throws Exception {
        if (!checkNet(requestEntity, session)) {
            return;
        }
        LogUtils.log(TAG, LogUtils.getThreadName() + requestEntity.getRequestParam().toString());
        NetRequestHandler.getInstance().pushRequest(requestEntity.mHandle.getSelfContext(),
                requestEntity.mRequestParam, new IResponseListener() {

                    @Override
                    public void onResponse(String response, String exceptionClassName) {
                        ResponseEntity responseEntity = new ResponseEntity(requestEntity, session);
                        boolean isNoException = checkException(response, exceptionClassName, responseEntity);
                        CacheType cacheType = requestEntity.mRequestParam
                                .getCacheType(ControlKey.request.control.__cacheType_enum);
                        if (isNoException && cacheType != CacheType.noneCache) {
                            setDataVersion(requestEntity, responseEntity);
                            saveToCache(requestEntity, response);
                        }
                        returnResponse(requestEntity, session, responseEntity);
                    }

                });
    }

    /**
     * @param requestEntity
     * @param session
     * @throws NotFoundException
     */
    protected abstract boolean checkNet(final FRequestEntity requestEntity, final Object session)
            throws Exception;

    /**
     * @param requestEntity
     * @param response
     */
    protected abstract void saveToCache(final FRequestEntity requestEntity, String response);

    protected abstract void clearCacheByRequest(final FRequestEntity requestEntity);

    /**
     * @param response
     * @param exceptionClassName
     * @param responseEntity
     */
    protected abstract boolean checkException(String response, String exceptionClassName,
            ResponseEntity responseEntity);

    /**
     * @param requestEntity
     * @param session
     * @param responseEntity
     */
    protected abstract void returnResponse(final FRequestEntity requestEntity, final Object session,
            ResponseEntity responseEntity);

    /**
     * 
     * @param responseEntity
     * @param response
     * @return
     * @throws JSONException
     */
    protected abstract void buildResponseEntity(ResponseEntity responseEntity, String response)
            throws Exception;

    /**
     * 
     * @param exceptionClassName
     * @return
     */
    protected String getErrorInfo(String exceptionClassName) {
        return exceptionClassName;
    }

    /**
     * com.gionee.app_framework.operation.business.ResponseEntity
     * 
     * @author yuwei <br/>
     *         create at 2013-3-22 上午11:52:11 TODO
     */
    protected static class ResponseEntity {

        public FRequestEntity mRequestEntity;
        public JSONObject mResponse;
        public boolean mIsSucceed;
        public String mErrorNum;
        public String mErrorInfo;
        public Object mSession;

        public ResponseEntity(FRequestEntity requestEntity, Object session) {
            super();
            this.mRequestEntity = requestEntity;
            this.mSession = session;
        }
    }

    /**
     * @param requestEntity
     * @param responseEntity
     * @author yuwei
     * @description TODO 设置请求数据的接口版本号
     */
    protected void setDataVersion(final FRequestEntity requestEntity, ResponseEntity responseEntity) {
        if (responseEntity.mResponse != null) {
            long version = responseEntity.mResponse.optLong(HttpConstants.Response.VERSION_I, 1);
            if (version > 0) {
                requestEntity.getRequestParam().put(HttpConstants.Request.VERSION_L, version);
            }
        }
    }

    // Gionee <yuwei><2013-6-8> add for CR00821559 end
}
