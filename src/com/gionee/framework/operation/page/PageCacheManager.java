package com.gionee.framework.operation.page;

import android.text.TextUtils;

import com.gionee.framework.model.bean.MyBean;
import com.gionee.framework.model.bean.MyBeanFactory;
import com.gionee.framework.operation.utills.LogUtils;

/**
 * com.gionee.app_framework.operation.manage.DataManage
 * 
 * @author yuwei <br/>
 *         create at 2013-3-19 下午1:46:35 TODO page data manager
 */
public final class PageCacheManager {

    private static MyBean cache = MyBeanFactory.createEmptyBean();

    public static MyBean LookupPageData(String pageDataKey) {
        if (TextUtils.isEmpty(pageDataKey)) {
            return null;
        }
        if (cache.containsKey(pageDataKey)) {
            return cache.getMyBean(pageDataKey);
        } else {
            LogUtils.log("pagedata", "创建pagedata : " + pageDataKey);
            MyBean newData = MyBeanFactory.createDataBean();
            cache.putMyBean(pageDataKey, newData);
            return newData;
        }
    }

    public static void ClearPageData(String pageDataKey) {
        if (cache.remove(pageDataKey) != null) {
            LogUtils.log("pagedata", "清除pagedata : " + pageDataKey);
        }
    }

    public static void ClearAll() {
        cache.clear();
    }
}
