/*
 * BaseResponse.java
 * classes : com.gionee.app_framework.model.response.BaseResponse
 * @author yuwei
 * V 1.0.0
 * Create at 2013-3-25 下午3:16:46
 */
package com.gionee.framework.model.bean;

/**
 * com.gionee.app_framework.model.response.BaseResponse
 * 
 * @author yuwei <br/>
 *         create at 2013-3-25 下午3:16:46 TODO
 */
public class BaseResponse {
    /** 数据 json数组 **/
    public static final String DATA = "data";
    /** 请求成功标识 true表示成功，false表示失败 **/
    public static final String SUCCESS = "success";
    /** 信息 success为true时，为空;success为false，为错误描述. **/
    public static final String ERR_MSG = "msg";
    /** 是否有下一页 string **/
    public static final String HASNEXT = "hasnext";
    /**
     * code 错误码 0表示成功
     */
    public static final String CODE = "code";
    /**
     * 当前第几页 1
     */
    public static final String CURPAGE_I = "curpage";

    /**
     * list 列表
     */
    public static final String LIST_JA = "list";
    /**
     * 数据ID 自增长 int
     */
    public static final String ID_I = "id";
    /**
     * 链接url
     */
    public static final String URL_S = "url";
}
