// Gionee <yangxiong><2014-6-28> add for CR00850885 begin
/*
 * @author yangxiong
 * V 1.0.0
 * Create at 2014-6-28 上午11:25:45
 */
package com.gionee.framework.operation.business;

import android.os.Handler;
import android.os.Handler.Callback;
import android.os.HandlerThread;
import android.os.Message;

/**
 * com.gionee.framework.operation.business.FLocalBuisiness
 * 
 * @author yangxiong <br/>
 * @date create at 2014-6-28 上午11:25:45
 * @description TODO
 */
public class FLocalBuisiness {

    protected static final String TAG = "FLocalBuisiness";
    protected HandlerThread mHandlerThread;
    protected Handler mHandler;
    protected HandlerCallBack mCallBack;

    private void setmCallBack(HandlerCallBack mCallBack) {
        this.mCallBack = mCallBack;
    }

    public interface HandlerCallBack {
        void handerCallback();
    }

    public Handler getHandler() {
        return mHandler;
    }

    public void sendMessage(Message msg, HandlerCallBack callBack) {
        setmCallBack(callBack);
        mHandler.sendMessage(msg);

    }

    protected FLocalBuisiness(String threadname) {
        mHandlerThread = new HandlerThread(threadname);
        start();
        mHandler = new Handler(mHandlerThread.getLooper(), new Callback() {

            @Override
            public boolean handleMessage(Message msg) {
                mCallBack.handerCallback();
                return false;
            }
        });
    }

    public void postRunable(Runnable runable) {
        mHandler.post(runable);
    }

    /**
     * 
     * @author yuwei
     * @description TODO
     */
    private void start() {
        mHandlerThread.start();
    }
}