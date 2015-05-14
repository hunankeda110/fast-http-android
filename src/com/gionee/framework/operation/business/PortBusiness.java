/*
 * PortBusiness.java
 * classes : com.gionee.app_framework.operation.business.PortBusiness
 * @author yuwei
 * V 1.0.0
 * Create at 2013-4-3 下午2:43:19
 */
package com.gionee.framework.operation.business;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.example.fasthttp.R;
import com.gionee.framework.event.SuperInjectFactory;
import com.gionee.framework.model.bean.HttpConstants;
import com.gionee.framework.model.bean.MyBean;
import com.gionee.framework.model.config.ControlKey;
import com.gionee.framework.model.config.ControlKey.request.control.CacheType;
import com.gionee.framework.operation.cache.ShareDataManager;
import com.gionee.framework.operation.net.NetUtil;
import com.gionee.framework.operation.page.PageCacheManager;
import com.gionee.framework.operation.utills.JSONArrayHelper;
import com.gionee.framework.operation.utills.LogUtils;

/**
 * com.gionee.app_framework.operation.business.PortBusiness
 * 
 * @author yuwei <br/>
 *         create at 2013-4-3 下午2:43:19 TODO
 */
public class PortBusiness extends FPortBusiness {
    private static FPortBusiness instance;

    private static final String TAG = PortBusiness.class.getSimpleName();

    private PortBusiness() {
    }

    public static FPortBusiness getInstance() {
        if (instance == null) {
            instance = new PortBusiness();
        }
        return instance;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.gionee.app_framework.operation.business.FPortBusiness#buildResponseEntity
     * (
     * com.gionee.app_framework.operation.business.FPortBusiness.ResponseEntity,
     * java.lang.String)
     */
    @Override
    protected void buildResponseEntity(ResponseEntity responseEntity, String response) throws Exception {

        JSONObject json = new JSONObject(response);
        boolean isSuccess = json.optBoolean(HttpConstants.Response.SUCCESS);
        JSONObject obj = json.optJSONObject(HttpConstants.Response.DATA);
        if (isSuccess && obj != null) {
            responseEntity.mIsSucceed = true;
            responseEntity.mResponse = obj;
        } else {
            responseEntity.mIsSucceed = false;
            String errorMsg = json.optString(HttpConstants.Response.ERR_MSG);
            responseEntity.mErrorInfo = errorMsg;
            responseEntity.mErrorNum = json.optString(HttpConstants.Response.CODE);
            throw new MyErrorException(errorMsg);
        }

    }

    @Override
    protected boolean handleCacheType(final FRequestEntity requestEntity, CacheType cacheType,
            boolean isAccessNet) {
        switch (cacheType) {
            case olnyShowCacheAccessNet:
                requestEntity.mIsHandlerResponse = false;
                break;
            case olnyUseCache:
                isAccessNet = false;
                break;
            default:
                break;
        }
        return isAccessNet;
    }

    @Override
    protected String getCacheData(final FRequestEntity requestEntity) {
        String cache;
        if (requestEntity.mListRequestParams != null) {
            LogUtils.logd(TAG, LogUtils.getThreadName() + "list request param.");
            cache = SuperInjectFactory.lookupNetCacheManage().loadList(
                    requestEntity.mHandle.getSelfContext(), requestEntity.mRequestParam);
        } else {

            LogUtils.logd(TAG, LogUtils.getThreadName() + "normal request(not list request)");
            cache = SuperInjectFactory.lookupNetCacheManage().load(requestEntity.mHandle.getSelfContext(),
                    requestEntity.mRequestParam);
        }
        return cache;
    }

    /**
     * @param requestEntity
     * @author yangxiong
     * @description TODO 请求参数中添加接口版本号
     */
//    private void addInterfaceVersion(final FRequestEntity requestEntity) {
//        String interfaceVersionKey = getInterfaceVersionkey(requestEntity);
//        MyBean bean = requestEntity.getRequestParam();
//        bean.put(HttpConstants.Request.VERSION_L, ShareDataManager.getInterfaceDataVersionNumber(
//                requestEntity.mHandle.getSelfContext(), interfaceVersionKey));
//    }

    @Override
    protected boolean handleCacheData(final FRequestEntity requestEntity, final Object session, String cache,
            boolean isAccessNet) {
        LogUtils.log("network", "get data from cache" + cache);
        ResponseEntity responseEntity = new ResponseEntity(requestEntity, session);
        isAccessNet = buildResponse(requestEntity, cache, isAccessNet, responseEntity);
        isAccessNet = returnCacheResponse(requestEntity, session, isAccessNet, responseEntity);
        return isAccessNet;
    }

    private boolean buildResponse(final FRequestEntity requestEntity, String cache, boolean isAccessNet,
            ResponseEntity responseEntity) {
        try {
            buildResponseEntity(responseEntity, cache);
            setDataVersion(requestEntity, responseEntity);
        } catch (Exception e) {
            e.printStackTrace();
            e.printStackTrace();
            isAccessNet = true;
            requestEntity.mIsHandlerResponse = true;
        }
        return isAccessNet;
    }

    private boolean returnCacheResponse(final FRequestEntity requestEntity, final Object session,
            boolean isAccessNet, ResponseEntity responseEntity) {
        if (TextUtils.isEmpty(requestEntity.mTargetPageDataKey)) {
            returnToSession(requestEntity, responseEntity);
        } else {
            isAccessNet = returnToCache(requestEntity, session, isAccessNet, responseEntity);
        }
        return isAccessNet;
    }

    private void returnToSession(final FRequestEntity requestEntity, ResponseEntity responseEntity) {
        requestEntity.mHandle.onSucceed(requestEntity.mBusinessType, true, responseEntity.mResponse);
    }

    private boolean returnToCache(final FRequestEntity requestEntity, final Object session,
            boolean isAccessNet, ResponseEntity responseEntity) {
        MyBean data = PageCacheManager.LookupPageData(requestEntity.mTargetPageDataKey);

        // 处理列表加载更多功能
        if (requestEntity.mListRequestParams != null) {
            // 加载更多
            addListData(requestEntity, responseEntity, data);
            data.putJSONObject(requestEntity.mDataTargetKey, responseEntity.mResponse);
        } else {
            data.putJSONObject(requestEntity.mDataTargetKey, responseEntity.mResponse);
        }
        requestEntity.mHandle.onSucceed(requestEntity.mBusinessType, true, session);
        return isAccessNet;
    }

    @Override
    protected void addListData(final FRequestEntity requestEntity, ResponseEntity responseEntity, MyBean data) {
        JSONObject jsonData = data.getJSONObject(requestEntity.mDataTargetKey);
        String listKey = requestEntity.mListRequestParams.mListKey;
        if (jsonData != null && requestEntity.mListRequestParams.mIsMore) {
            JSONArray oldArray = jsonData.optJSONArray(listKey);
            add(responseEntity, listKey, oldArray);
        }
    }

    private void add(ResponseEntity responseEntity, String listKey, JSONArray oldArray) {
        if (oldArray == null) {
            return;
        }

        JSONArray newArray = responseEntity.mResponse.optJSONArray(listKey);
        new JSONArrayHelper(oldArray).addAll(newArray, false);
        try {
            responseEntity.mResponse.put(listKey, oldArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.log("network", "after add lenth=" + oldArray.length());
    }

    @Override
    protected boolean checkNet(final FRequestEntity requestEntity, final Object session) throws Exception {
        boolean isNetworkAvailable = NetUtil.isNetworkAvailable(requestEntity.mHandle.getSelfContext());
        if (!isNetworkAvailable) {
            requestEntity.mHandle.onErrorResult(requestEntity.mBusinessType, "", "networkEror", session);
        }
        return isNetworkAvailable;
    }

    @Override
    protected void saveToCache(final FRequestEntity requestEntity, String response) {
        if (requestEntity.mRequestParam.getCacheType(ControlKey.request.control.__cacheType_enum) != CacheType.noneCache) {
            saveData(requestEntity, response);
        }
    }

    private void saveData(final FRequestEntity requestEntity, String response) {
        // 处理列表加载更多功能
        long versionCode = getVersionCode(response);
        try {
            JSONObject object = new JSONObject(response);
            if (object.getJSONObject(HttpConstants.Response.DATA) != null) {
                String interfaceVersionKey = getInterfaceVersionkey(requestEntity);
                LogUtils.log("netCache", "interfaceVersionKey=" + interfaceVersionKey + ", save version="
                        + versionCode);
                ShareDataManager.setInterfaceDataVersionNumber(requestEntity.mHandle.getSelfContext(),
                        interfaceVersionKey, versionCode);
                if (requestEntity.mListRequestParams != null) {

                    SuperInjectFactory.lookupNetCacheManage().saveList(
                            requestEntity.mHandle.getSelfContext(), requestEntity.mRequestParam, response);
                } else {
                    SuperInjectFactory.lookupNetCacheManage().save(requestEntity.mHandle.getSelfContext(),
                            requestEntity.mRequestParam, response);
                }
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Override
    protected void clearCacheByRequest(final FRequestEntity requestEntity) {
        LogUtils.logd(TAG, LogUtils.getThreadName());
        // 处理列表加载更多功能
        SuperInjectFactory.lookupNetCacheManage().clearCacheByRequest(requestEntity.mHandle.getSelfContext(),
                requestEntity.mRequestParam);
    }

    private long getVersionCode(String response) {
        long versionCode = 1;
        try {
            JSONObject object = new JSONObject(response);
            JSONObject data = object.optJSONObject(HttpConstants.Response.DATA);
//            LogUtils.log("netCache", "data=" + data.toString());
            versionCode = data.optLong(HttpConstants.Response.VERSION_I, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    @Override
    protected boolean checkException(String response, String exceptionClassName, ResponseEntity responseEntity) {
        if (TextUtils.isEmpty(exceptionClassName)) {
            return checkSeverException(response, responseEntity);
        } else {
            responseEntity.mIsSucceed = false;
            responseEntity.mErrorInfo = exceptionClassName;
            return false;
        }
    }

    private boolean checkSeverException(String response, ResponseEntity responseEntity) {
        try {
            buildResponseEntity(responseEntity, response);
            return true;
        } catch (Exception e) {
            LogUtils.logd(TAG, LogUtils.getThreadName() + " response = " + response);
            e.printStackTrace();
            responseEntity.mIsSucceed = false;
            responseEntity.mErrorInfo = e.getMessage();
            return false;
        }
    }

    @Override
    protected void returnResponse(final FRequestEntity requestEntity, final Object session,
            ResponseEntity responseEntity) {
        if (requestEntity.mIsHandlerResponse
                && !requestEntity.getRequestParam()
                        .getBoolean(ControlKey.request.control.__isCansel_b, false)) {
            returnToObserver(requestEntity, session, responseEntity);
        } else {
            LogUtils.log("net", "请求被取消!");
        }
    }

    private void returnToObserver(final FRequestEntity requestEntity, final Object session,
            ResponseEntity responseEntity) {
        if (responseEntity.mIsSucceed) {
            returnSuccessData(requestEntity, session, responseEntity);
        } else {
            returnError(requestEntity, session, responseEntity);
        }
    }

    private void returnError(final FRequestEntity requestEntity, final Object session,
            ResponseEntity responseEntity) {
        // 错误
        requestEntity.mHandle.onErrorResult(requestEntity.mBusinessType, responseEntity.mErrorNum,
                getErrorInfo(TextUtils.isEmpty(responseEntity.mErrorInfo) ? responseEntity.mErrorNum
                        : responseEntity.mErrorInfo), session);
    }

    private void returnSuccessData(final FRequestEntity requestEntity, final Object session,
            ResponseEntity responseEntity) {
        // 如果targetPageDataKey为空，则直接把response作为session返回。
        if (TextUtils.isEmpty(requestEntity.mTargetPageDataKey)
                || TextUtils.isEmpty(requestEntity.mDataTargetKey)) {
            returnBySession(requestEntity, responseEntity);
        } else {
            returnToCache(requestEntity, session, responseEntity);
        }
    }

    private void returnBySession(final FRequestEntity requestEntity, ResponseEntity responseEntity) {
        requestEntity.mHandle.onSucceed(requestEntity.mBusinessType, false, responseEntity.mResponse);
    }

    private void returnToCache(final FRequestEntity requestEntity, final Object session,
            ResponseEntity responseEntity) {
        MyBean data = PageCacheManager.LookupPageData(requestEntity.mTargetPageDataKey);
        // 处理列表加载更多功能
        if (requestEntity.mListRequestParams != null) {
            handleListData(requestEntity, responseEntity, data);

        } else {
            // 处理普通请求
            data.putJSONObject(requestEntity.mDataTargetKey, responseEntity.mResponse);
        }
        // 回调监听
        requestEntity.mHandle.onSucceed(requestEntity.mBusinessType, false, session);
    }

    private void handleListData(final FRequestEntity requestEntity, ResponseEntity responseEntity, MyBean data) {
//        if (requestEntity.requestParam.getInt(BaseIndex.RequstIndex.CACHE_TIME_I) == 0) {
//
//            data.putJSONObject(requestEntity.dataTargetKey, responseEntity.response);
//        } else {
        addListData(requestEntity, responseEntity, data);
        data.putJSONObject(requestEntity.mDataTargetKey, responseEntity.mResponse);
//        }
    }

}
