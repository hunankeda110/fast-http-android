/*
 * BaseIndex.java
 * classes : com.gionee.app_framework.model.index.BaseIndex
 * @author yuwei
 * V 1.0.0
 * Create at 2013-3-26 上午9:46:11
 */
package com.gionee.framework.model.index;

import com.gionee.framework.model.bean.BasePageData;
import com.gionee.framework.model.bean.BaseRequest;
import com.gionee.framework.model.bean.BaseResponse;

/**
 * com.gionee.app_framework.model.index.BaseIndex
 * 
 * @author yuwei <br/>
 *         create at 2013-3-26 上午9:46:11 TODO
 */
// Gionee <yuwei><2013-5-27> add for CR00821559 begin
public abstract class BaseIndex {
    public abstract class RequstIndex extends BaseRequest {
        /** 设备imei号 **/
        public static final String IMEI_ID = "imei";
        /**
         * 当前页码 默认为1
         */
        public static final String PAGE_S = "page";
        /**
         * 每页数据条数 默认为6
         */
        public static final String PERPAGE_S = "perpage";
        /**
         * 上一次缓存时间
         */
        public static final String CACHE_TIME_I = "cache_time";
        /**
         * 登录后服务端返回的会话证书
         */
        public static final String TOKEN_S = "token";
        /**
         * 客户端数据版本号 long
         */
        public static final String VERSION_L = "version";
        public static final String APP_VERSION_CODE_I = "app_version_code";
        public static final String ID = "id";

    }

    public abstract class ResponseIndex extends BaseResponse {
        /**
         * 服务端数据版本号 int
         */
        public static final String VERSION_I = "version";
        public static final String ID = "id";
    }

    public abstract class DataIndex extends BasePageData {
        public static final String DEFAULT_TARGERT_KEY = "defualt_data_key";

    }
}
// Gionee <yuwei><2013-5-27> add for CR00821559 end
