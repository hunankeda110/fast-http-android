package com.gionee.framework.event;

import android.content.Context;

/**
 * com.gionee.app_framework.event.IBusinessHandle
 * 
 * @author yuwei <br/>
 *         create at 2013-3-19 上午11:54:20 TODO
 */
public interface IBusinessHandle {

    /**
     * 获取数据成功
     * 
     * @param businessType
     * @param isCache
     * @param session
     */
    void onSucceed(String businessType, boolean isCache, Object session);

    /**
     * 返回错误
     * 
     * @param businessType
     * @param errorOn
     * @param errorInfo
     * @param session
     */
    void onErrorResult(String businessType, String errorOn, String errorInfo, Object session);

    /**
     * 获取当前上下文
     * 
     * @return
     */
    Context getSelfContext();

    /**
     * 取消请求
     * 
     * @param businessType
     * @param session
     */
    void onCancel(String businessType, Object session);
}
