package com.gionee.framework.operation.business;

public class LocalBuisiness extends FLocalBuisiness {

    private static final String TAG = "LocalBuisiness";

    private LocalBuisiness() {
        super(TAG);
    }

    /* 此处使用一个嵌套类来维护单例 */
    private static class SingletonFactory {
        private static LocalBuisiness mInstance = new LocalBuisiness();
    }

    /* 获取实例 */
    public static LocalBuisiness getInstance() {
        return SingletonFactory.mInstance;
    }

    public void postRunable(Runnable runable) {
        getInstance().getHandler().post(runable);
    }
}
