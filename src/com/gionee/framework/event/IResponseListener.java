package com.gionee.framework.event;

/**
 * com.gionee.app_framework.event.IResponseListener
 * 
 * @author yuwei <br/>
 *         create at 2013-3-19 上午11:54:33 TODO
 */
public interface IResponseListener {
    /**
     * 
     * @param response
     * @param exceptionClassName
     */
    void onResponse(String response, String exceptionClassName);

}
