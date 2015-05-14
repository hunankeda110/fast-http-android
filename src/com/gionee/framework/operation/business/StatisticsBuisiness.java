package com.gionee.framework.operation.business;

public class StatisticsBuisiness extends FLocalBuisiness {
    private static final String TAG = "StatisticsBuisiness";

    private StatisticsBuisiness() {
        super(TAG);
    }

    /* 获取实例 */
    public static StatisticsBuisiness getInstance() {
        return SingletonFactory.mInstance;
    }

    /* 此处使用一个内部类来维护单例 */
    private static class SingletonFactory {
        private static StatisticsBuisiness mInstance = new StatisticsBuisiness();
    }
}
