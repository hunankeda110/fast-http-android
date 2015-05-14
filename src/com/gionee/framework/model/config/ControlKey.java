package com.gionee.framework.model.config;

public final class ControlKey {

    public static final class request {

        /**
         * com.gionee.app_framework.model.config.control
         * 
         * @author yuwei <br/>
         *         create at 2013-3-20 下午5:47:54 TODO request的公共控制参数key
         */
        public final static class control {
            /**
             * 控制参数前缀
             */
            public static final String __ = "_@";// 控制参数前缀
            /**
             * 请求url
             */
            public static final String __url_s = __ + "url";// 请求url
            /**
             * 是否显示loading，默认：true
             */
            public static final String __isShowLoading_b = __ + "isShowLoading";// 是否显示loading，默认：true
            /**
             * 显示loading信息，默认：加载中……
             */
            public static final String __showLoadingInfo_s = __ + "info";// 显示loading信息，默认：加载中……
            /**
             * loading是否可取消，默认：true
             */
            public static final String __isCanDismiss_b = __ + "canDismiss";// loading是否可取消，默认：true
            /**
             * 请求方法 POST 或 GET，默认：POST
             */
            public static final String __method_s = __ + "method";// 请求方法 POST 或 GET，默认：POST
            /**
             * 缓存类型，默认CacheType.noneCache
             */
            public static final String __cacheType_enum = __ + "cacheType";// 缓存类型，默认CacheType.noneCache
            /**
             * 是否当前页有效，默认：false
             */
            public static final String __isCurrPage_b = __ + "Currpage";// 是否当前页有效，默认：false
            /**
             * 是否显示loading，默认：true
             */
            public static final String __isCansel_b = __ + "isCansel";// 是否显示loading，默认：true
            /**
             * 自定义字段
             */
            public static final String __custom_o = __ + "custom";// 自定义字段

            /**
             * com.gionee.app_framework.model.config.CacheType
             * 
             * @author yuwei <br/>
             *         create at 2013-3-20 下午5:48:17
             */
            public static enum CacheType {
                /**
                 * 不缓存: 直接使用网络数据,不缓存数据
                 */
                noneCache,
                /**
                 * 只使用缓存： 如果没有缓存，访问网络，刷新视图，并缓存数据。 如果已有缓存，直接使用缓存，不访问网络。 （可通过缓存管理手动清除缓存）
                 */
                olnyUseCache,
                /**
                 * 只使用缓存，并每次更新缓存 如果没有缓存，访问网络，刷新视图，并缓存数据。 如果已有缓存，直接使用缓存，并访问网络后更新缓存数据，不刷新视图。
                 */
                olnyShowCacheAccessNet,
                /**
                 * 使用缓存和网络数据 如果没有缓存，访问网络，刷新视图，并缓存数据。 如果已有缓存，直接使用缓存，并访问网络后更新缓存数据，刷新视图。
                 */
                ShowCacheAndNet
            }
        }

    }

}
