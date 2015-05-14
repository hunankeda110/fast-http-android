// Gionee <yuwei><2014-1-14> add for CR00821559 begin
/*
 * NetRequestHandler.java
 * classes : com.gionee.framework.operation.net.NetRequestHandler
 * @author yuwei
 * V 1.0.0
 * Create at 2014-1-14 上午11:42:53
 */
package com.gionee.framework.operation.net;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import com.gionee.framework.event.IResponseListener;
import com.gionee.framework.model.bean.MyBean;
import com.gionee.framework.model.config.ControlKey;
import com.gionee.framework.operation.business.NetConnector;
import com.gionee.framework.operation.utills.LogUtils;

/**
 * NetRequestHandler
 * 
 * @author yuwei <br/>
 * @date create at 2014-1-14 上午11:42:53
 * @description TODO
 */
public class NetRequestHandler {
    private static final String TAG = "NetRequestHandler";
    private HandlerThread mHandlerThread;
    private Handler mHandler;
    private static NetRequestHandler sInstance;

    private NetRequestHandler() {
        mHandlerThread = new HandlerThread(TAG);
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper());
    }

    /**
     * 添加请求
     * 
     * @param page
     * @param request
     * @param listener
     */
    public void pushRequest(Context page, MyBean request, IResponseListener listener) {
        if (NetUtil.isNetworkAvailable(page)) {
            Session session = new Session(page, request, listener);
            mHandler.post(new MyRunable(session));
            showLoading(session.request, page);
        } else {
            if (listener != null) {
                listener.onResponse(null, "无法访问网络！");
            }
        }
    }

    static class MyRunable implements Runnable {
        Session mSession;

        public MyRunable(Session session) {
            // TODO Auto-generated constructor stub
            this.mSession = session;
        }

        @Override
        public void run() {
            LogUtils.log(TAG, LogUtils.getThreadName() + "运行数据线程");
            try {
                NetConnector connector = new NetConnector();
                String responseJson = connector.openUrl(mSession.request, mSession.mContext);
                mSession.response = responseJson;
            } catch (Exception e) {
                e.printStackTrace();
                mSession.exceptionClassName = e.getClass().getName();

            }
            Message msg = handler.obtainMessage();
            msg.obj = mSession;
            handler.sendMessage(msg);

        }
    }

    public static NetRequestHandler getInstance() {
        if (sInstance == null) {
            sInstance = new NetRequestHandler();
        }
        return sInstance;

    }

    private void showLoading(final MyBean request, Context context) {
        if (request.getBoolean(ControlKey.request.control.__isShowLoading_b, false)) {
            try {
//                ((BaseFragmentActivity) context).showLoadingProgress();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static class Session {
        private String pageDataKey = "";
        private MyBean request;
        private IResponseListener listener;
        private String response;
        private String exceptionClassName;
        private Context mContext;

        public Session(Context page, MyBean request, IResponseListener listener) {
            setPageDataKey(page.getClass().getName());
            this.request = request;
            this.listener = listener;
            this.mContext = page;
        }

        /**
         * @return the pageDataKey
         */
        @SuppressWarnings("unused")
        public String getPageDataKey() {
            return pageDataKey;
        }

        /**
         * @param pageDataKey
         *            the pageDataKey to set
         */
        public void setPageDataKey(String pageDataKey) {
            this.pageDataKey = pageDataKey;
        }
    }

    /**
     * 回调handler
     */
    private static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Session session = (Session) msg.obj;
            // 返回监听
            if (session.listener != null) {
                session.listener.onResponse(session.response, session.exceptionClassName);
            }
            hideLoading();

        }

        public void hideLoading() {
            try {
//                ((BaseFragmentActivity) session.mContext).hideLoadingProgress();
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    };

}
