package com.gionee.framework.operation.business;

import org.json.JSONException;

import android.content.Context;
import android.text.TextUtils;

import com.gionee.framework.event.INetConnector;
import com.gionee.framework.model.bean.MyBean;
import com.gionee.framework.model.config.ControlKey;
import com.gionee.framework.operation.net.NetUtil;

public class NetConnector implements INetConnector {

    @Override
    public String openUrl(MyBean request, Context context) throws JSONException, Exception {
        String response = "";
        String requestURL = request.getString(ControlKey.request.control.__url_s);
        if (TextUtils.isEmpty(requestURL)) {
            return null;
        }
        String httpMethod = request.getString(ControlKey.request.control.__method_s, "GET");
        if (httpMethod.equalsIgnoreCase("GET")) {
            response = NetUtil.doGet(context, requestURL + NetUtil.encodeUrl(request));
        } else {
            response = NetUtil.doPost(context, requestURL, request);
        }
        return response;

    }

}
