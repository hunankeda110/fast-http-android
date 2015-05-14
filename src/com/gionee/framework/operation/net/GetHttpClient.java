/*
 * DefaultHttpClient.java
 * classes : cn.com.donson.naf.other.download.DefaultHttpClient
 * @author 余炜
 * V 1.0.0
 * Create at 2012-5-29 上午08:52:15
 */
package com.gionee.framework.operation.net;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpProtocolParams;

import android.util.Log;

import com.gionee.framework.operation.utills.LogUtils;
import com.gionee.framework.operation.utills.StringUtils;

/**
 * com.gionee.app_framework.operation.net.GetHttpClient
 * 
 * @author yuwei <br/>
 *         create at 2013-3-19 下午1:48:03 TODO http request
 */
public class GetHttpClient {
//	private static final String TAG = "DefaultHttpClient";
    private static HttpClient mHttpClient;

    /**
     * 数据流连接
     * 
     * @param url
     * @throws ClientProtocolException
     * @throws IOException
     * @return InputStream
     * @throws
     */
    public static InputStream doGet4stream(String url) throws ClientProtocolException, IOException {
        try {
            BasicHttpParams httpParams = new BasicHttpParams();

            // 判断手机客户端连接的cmwap网络，设置代理
            if (NetUtil.isWap()) {
                HttpHost proxy = new HttpHost("10.0.0.172", 80);
                httpParams.setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
            }
            Log.d("debugUrl", "url=" + url);
            HttpConnectionParams.setConnectionTimeout(httpParams, 300 * 1000);
            HttpConnectionParams.setSoTimeout(httpParams, 300 * 1000);
            HttpConnectionParams.setSocketBufferSize(httpParams, 8192);
            HttpClientParams.setRedirecting(httpParams, true);

            String userAgent = "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2) Gecko/20100115 Firefox/3.6";
            HttpProtocolParams.setUserAgent(httpParams, userAgent);
            SchemeRegistry schemeRegistry = new SchemeRegistry();
            schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            ClientConnectionManager cm = new ThreadSafeClientConnManager(httpParams, schemeRegistry);
            mHttpClient = new DefaultHttpClient(cm, httpParams);
            if (StringUtils.areEmpty(url))
                return null;
            HttpGet httpRequest = new HttpGet(url);

            /* 发送请求并等待响应 */
            if (httpRequest == null || mHttpClient == null)
                return null;
            HttpResponse httpResponse = mHttpClient.execute(httpRequest);

            mHttpClient = null;
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                return httpResponse.getEntity().getContent();
            } else {
                Log.e("TAG", "下载异常: " + httpResponse.getStatusLine().getStatusCode());
            }

        } catch (Exception e) {

            LogUtils.log("TAG", "非法url = " + url);
            e.printStackTrace();
            // TODO: handle exception
        }
        return null;
    }

    public static void shutDown() {
        if (mHttpClient != null)
            mHttpClient.getConnectionManager().shutdown();
    }
}
