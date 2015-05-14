package com.gionee.framework.event;

import android.content.Context;

import com.gionee.framework.model.bean.MyBean;

/**
 * com.gionee.app_framework.event.inject.INetCacheManage
 * 
 * @author yuwei <br/>
 *         create at 2013-3-19 上午11:53:24 TODO
 */
public interface INetDiscCacheManage {
    // Gionee <yuwei><2012-5-27> add for CR00821559 begin

    /**
     * 
     * @param content
     * @param request
     * @param response
     * @return
     */
    public boolean save(Context content, MyBean request, String response);

    /**
     * 
     * @param content
     * @param request
     * @param response
     * @return
     */
    public boolean saveList(Context content, MyBean request, String response);

    /**
     * 
     * @param content
     * @param request
     * @return
     */
    public String load(Context content, MyBean request);

    /**
     * 
     * @param content
     * @param request
     * @return
     */
    public String loadList(Context content, MyBean request);

    /**
     * 
     * @param context
     */
    public void clearAll(Context context);

    /***
     * 
     * @param context
     * @param request
     */
    public void clearCacheByRequest(Context context, MyBean request);

    /**
     * 
     * @param request
     * @return
     */
    public String createKeyByRequest(MyBean request);
    // Gionee <yuwei><2012-6-5> add for CR00821559 end
}
