package com.gionee.framework.event;

import android.content.Context;

import com.gionee.framework.model.bean.MyBean;

/**
 * com.gionee.app_framework.event.inject.INetConnector
 * 
 * @author yuwei <br/>
 *         create at 2013-3-19 上午11:53:37 TODO
 */
public interface INetConnector {

    /**
     * 
     * @param request
     * @return
     */
    String openUrl(MyBean request, Context context) throws Exception;
}
