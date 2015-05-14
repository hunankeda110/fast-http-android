package com.gionee.framework.operation.business;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.gionee.framework.event.IBusinessHandle;
import com.gionee.framework.model.bean.HttpConstants;
import com.gionee.framework.model.config.ControlKey;
import com.gionee.framework.operation.utills.LogUtils;

public class RequestEntity extends FRequestEntity {
    // Gionee <yuwei><2013-5-24> add for CR00821559 begin
    public static final String mJsonK = "jsonK";
    private String mCurrentUrl;

    public RequestEntity(String businessType, IBusinessHandle handle, String dataTargetKey,
            ListRequestParams listRequestParams) {
        super(businessType, handle, dataTargetKey, listRequestParams);
        init(businessType);
    }

    /**
     * 
     * @param businessType
     * @param handle
     * @param dataTargetKey
     *            接口访问成功后response在data中的键值
     */
    public RequestEntity(String businessType, IBusinessHandle handle, String dataTargetKey) {
        super(businessType, handle, dataTargetKey);
        init(businessType);
    }

    public RequestEntity(String businessType, IBusinessHandle handle) {
        super(businessType, handle);
        init(businessType);
    }

    private void init(String businessType) {
        mCurrentUrl = businessType;
    }

    @Override
    public void onStartBusiness() {
        mRequestParam.put(ControlKey.request.control.__url_s, mCurrentUrl);
        LogUtils.log("network", mRequestParam.toString());
        // buildRequest(requestParam, jsonK);
    }

    protected void jsonRestore2Param() {
        String jsonSrt = mRequestParam.getString(mJsonK);
        mRequestParam.remove(mJsonK);
        try {
            JSONObject json = new JSONObject(jsonSrt);
            JSONArray names = json.names();
            for (int i = 0; i < names.length(); i++) {
                String name = names.optString(i);
                mRequestParam.put(name, json.opt(name));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addImageByteParam(byte[] image) {
        mRequestParam.put(HttpConstants.Request.IMG_PARAMS_PREFIX, image);
    }

    public void addFileParam(String fileSdFullPath) {
        mRequestParam.put(HttpConstants.Request.IMG_PARAMS_PREFIX, fileSdFullPath);
    }

    protected void setIsHandleResponse(boolean isHandleResponse) {
        this.mIsHandlerResponse = isHandleResponse;
    }
    // Gionee <yuwei><2013-6-8> add for CR00821559 end
}
