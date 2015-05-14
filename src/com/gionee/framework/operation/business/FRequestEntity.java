package com.gionee.framework.operation.business;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import com.gionee.framework.event.IBusinessHandle;
import com.gionee.framework.model.bean.HttpConstants;
import com.gionee.framework.model.bean.MyBean;
import com.gionee.framework.model.bean.MyBeanFactory;
import com.gionee.framework.model.config.ControlKey;

/**
 * com.funshopping.framework.event.FRequestEntity
 * 
 * @author yuwei <br/>
 *         create at 2013-6-15 上午10:39:42
 */
public abstract class FRequestEntity {

    public String mBusinessType;
    public MyBean mRequestParam;
    public IBusinessHandle mHandle;
    public String mDataTargetKey;
    public String mTargetPageDataKey;
    public ListRequestParams mListRequestParams;
    public PageViewParams mPageViewParams;
    public int mImageCount;
    public boolean mIsHandlerResponse = true;

    /**
     * 
     * @param businessType
     * @param handle
     */
    protected FRequestEntity(String businessType, IBusinessHandle handle) {
        this.mBusinessType = businessType;
        this.mHandle = handle;
        mRequestParam = MyBeanFactory.createRequestBean(handle.getSelfContext());
    }

    /**
     * 普通请求
     * 
     * @param businessType
     * @param handle
     * @param dataTargetKey
     */
    protected FRequestEntity(String businessType, IBusinessHandle handle, String dataTargetKey) {
        this.mBusinessType = businessType;
        this.mDataTargetKey = dataTargetKey;
        this.mHandle = handle;
        mRequestParam = MyBeanFactory.createRequestBean(handle.getSelfContext());
        try {
            mTargetPageDataKey = handle.getSelfContext().getClass().getName();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }

    /**
     * 当列表需要加载更多时使用
     * 
     * @param businessType
     * @param handle
     * @param dataTargetKey
     * @param listRequestParams
     */
    protected FRequestEntity(String businessType, IBusinessHandle handle, String dataTargetKey,
            ListRequestParams listRequestParams) {
        this(businessType, handle, dataTargetKey);
        this.mListRequestParams = listRequestParams;
    }

    /**
     * 获得请求参数
     * 
     * @return
     */
    public final MyBean getRequestParam() {
        return mRequestParam;
    }

    /**
     * 添加图片上传
     * 
     * @param image
     */
    public void addImageByteParam(byte[] image) {
        mImageCount++;
        mRequestParam.put(HttpConstants.Request.IMG_PARAMS_PREFIX + mImageCount + ".png", image);
    }

    /**
     * 请求发出前对请求数据进行处理
     * 
     * @param mBusinessType
     */
    public abstract void onStartBusiness();

    public static class ListRequestParams {
        public String mListKey;
        public boolean mIsMore;

        /**
         * 
         * @param listKey
         * @param isMore
         */
        public ListRequestParams(String listKey, boolean isMore) {
            super();
            this.mListKey = listKey;
            this.mIsMore = isMore;
        }

    }

    /**
     * z 工具 把请求差数转化成json串
     * 
     * @param request
     * @param port
     */
    public static void buildRequest(MyBean requestParam, String paramKey) {
        if (requestParam != null) {
            JSONObject jsonParam = new JSONObject();
            Set<String> keys = requestParam.keySet();
            List<String> removKey = new ArrayList<String>();
            removeConfigKey(requestParam, jsonParam, keys, removKey);
            buildRequestParams(requestParam, paramKey, jsonParam, removKey);
        }
    }

    private static void buildRequestParams(MyBean requestParam, String paramKey, JSONObject jsonParam,
            List<String> removKey) {
        if (removKey.size() > 0) {
            for (String key : removKey) {
                requestParam.remove(key);
            }
            requestParam.put(paramKey, jsonParam.toString());
        }
    }

    private static void removeConfigKey(MyBean requestParam, JSONObject jsonParam, Set<String> keys,
            List<String> removKey) {
        for (String key : keys) {
            if (!key.startsWith(ControlKey.request.control.__)
                    && !key.startsWith(HttpConstants.Request.IMG_PARAMS_PREFIX)
                    && requestParam.get(key) != null) {
                try {
                    jsonParam.put(key, requestParam.get(key));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                removKey.add(key);
            }
        }
    }

    public static class PageViewParams {
        public int size;
        public int index;

        /**
         * 
         * @param size
         * @param index
         */
        public PageViewParams(int size, int index) {
            this.size = size;
            this.index = index;
        }
    }
}
// Gionee <yuwei><2013-6-15> add for CR00826374 end