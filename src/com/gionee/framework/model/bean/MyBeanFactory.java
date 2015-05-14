package com.gionee.framework.model.bean;

import android.content.Context;

import com.gionee.framework.model.config.ControlKey;
import com.gionee.framework.model.config.ControlKey.request.control.CacheType;

/**
 * com.gionee.app_framework.model.bean.MyBeanFactory
 * 
 * @author yuwei <br/>
 *         create at 2013-5-18 上午9:58:34 TODO Bean factory
 */
public class MyBeanFactory {

    public static MyBean createDataBean() {
        MyBean data = new MyBean(10);
        return data;
    }

    public static MyBean createEmptyBean() {
        return new MyBean(10);
    }

    public static MyBean createRequestBean(Context context) {
        MyBean request = new MyBean(10);
        request.put(HttpConstants.Request.VERSION_L, 1);
        request.put(ControlKey.request.control.__cacheType_enum, CacheType.ShowCacheAndNet);
        // request.put(K.Request.IS_SHOW_LOADING_B, true);
//        request.put(HttpConstants.Request.UID_S, PublicHeaderParamsManager.getUid(context));
        return request;

    }

    public static MyBean createResonseBean() {
        MyBean data = new MyBean(10);
        return data;
    }

}
