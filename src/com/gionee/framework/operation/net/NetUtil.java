package com.gionee.framework.operation.net;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.gionee.framework.model.bean.HttpConstants;
import com.gionee.framework.model.bean.MyBean;
import com.gionee.framework.model.bean.MyBeanFactory;
import com.gionee.framework.model.config.ControlKey;
import com.gionee.framework.operation.utills.LogUtils;

/**
 * com.gionee.app_framework.operation.net.NetUtil
 * 
 * @author yuwei <br/>
 *         create at 2013-3-19 下午1:49:08 TODO net utills
 */
@SuppressWarnings("deprecation")
public class NetUtil {

    // public static String charSet = "gb2312";
    public static final String charSet = HTTP.UTF_8;
    private static final String TAG = "NetUtill";

    /**
     * 对于InputStream类型的上传数据，上传缓冲区大小
     */
    public static final Integer CONNECT_TIMEOUT = 10 * 1000;

    /**
     * 将用&号链接的URL参数转换成key-value形式的参数集
     * 
     * @param s
     *            将用&号链接的URL参数
     * @return key-value形式的参数集
     */
    public static Bundle decodeUrl(String s) {
        Bundle params = new Bundle();
        if (s != null) {
            String array[] = s.split("&");
            for (String parameter : array) {
                String v[] = parameter.split("=");
                if (v.length > 1) {
                    params.putString(v[0], URLDecoder.decode(v[1]));
                }
            }
        }
        return params;
    }

    /**
     * 将URL中的查询串转换成key-value形式的参数集
     * 
     * @param url
     *            待解析的url
     * @return key-value形式的参数集
     */
    public static Bundle parseUrl(String url) {
        url = url.replace("#", "?");
        try {
            URL u = new URL(url);
            Bundle b = decodeUrl(u.getQuery());
            Bundle ref = decodeUrl(u.getRef());
            if (ref != null)
                b.putAll(ref);
            return b;
        } catch (MalformedURLException e) {
            return new Bundle();
        }
    }

    /**
     * 将key-value形式的参数集转换成用&号链接的URL查询参数形式。
     * 
     * @param parameters
     *            key-value形式的参数集
     * @return 用&号链接的URL查询参数
     */
    public static String encodeUrl(MyBean parameters) {
        if (parameters == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        boolean first = true;

        for (Entry<String, Object> entry : parameters.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (key.startsWith(ControlKey.request.control.__)
                    || key.startsWith(HttpConstants.Request.IMG_PARAMS_PREFIX) || value == null) {
                continue;
            }
            if (first) {
                first = false;
                sb.append("?");
            } else {
                sb.append("&");
            }
            try {
                sb.append(URLEncoder.encode(key, charSet) + "="
                        + URLEncoder.encode(parameters.get(key) + "", charSet));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        };
        return sb.toString();
    }

    /**
     * Get请求
     * 
     * @throws IOException
     * @throws
     */
    @SuppressWarnings("deprecation")
    public static String doGet(Context context, String url) throws IOException {
        LogUtils.log(TAG, "get url = " + url);
        HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, CONNECT_TIMEOUT);
        HttpConnectionParams.setSoTimeout(httpParams, CONNECT_TIMEOUT);
        ConnManagerParams.setTimeout(httpParams, CONNECT_TIMEOUT);
        HttpClient httpClient = new DefaultHttpClient(httpParams);
        HttpGet httpGet = new HttpGet(url);
//        HttpProtocolParams.setUserAgent(httpParams, UAUtils.getUserAgent((mContext)));
//        httpGet.setHeader("Accept-Encoding", "gzip, deflate");
//        HttpProtocolParams.setUserAgent(httpParams, UAUtils.getUserAgent((context)));
        HttpResponse response = httpClient.execute(httpGet);
        String strResult = read(response.getEntity().getContent());
        httpClient.getConnectionManager().shutdown();
        LogUtils.log(TAG, "get data from network" + "=" + strResult);
        return strResult;

    }

    @SuppressWarnings("deprecation")
    public static String doPost(Context context, String url, MyBean params) throws IOException {
        LogUtils.log(TAG, "doPost url = " + url);
        HttpPost post = new HttpPost(url);
        MultipartEntity multipartEntity = new MultipartEntity();
        if (params != null) {
            Set<java.util.Map.Entry<String, Object>> entrySet = params.entrySet();
            for (java.util.Map.Entry<String, Object> para : entrySet) {
                if (!para.getKey().startsWith(ControlKey.request.control.__) && para.getValue() != null) {
                    if (para.getKey().startsWith(HttpConstants.Request.IMG_PARAMS_PREFIX)) {
                        LogUtils.log(TAG,
                                "post param : " + para.getKey() + " image file path: " + para.getValue());
                        FileBody fb = new FileBody(new File((String) para.getValue()));
                        multipartEntity.addPart(para.getKey(), fb);
                    } else {
                        LogUtils.log(TAG, "post param : " + para.getKey() + "=" + para.getValue());
                        multipartEntity.addPart(para.getKey(), new StringBody(para.getValue().toString(),
                                Charset.forName(charSet)));
                    }
                }
            }
        }
        post.setEntity(multipartEntity);

        HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, CONNECT_TIMEOUT);
        HttpConnectionParams.setSoTimeout(httpParams, CONNECT_TIMEOUT);
        ConnManagerParams.setTimeout(httpParams, CONNECT_TIMEOUT);
        HttpClient client = new DefaultHttpClient(httpParams);
//        HttpProtocolParams.setUserAgent(httpParams, UAUtils.getUserAgent((context)));
        HttpResponse response = client.execute(post);
        String strResult = EntityUtils.toString(response.getEntity(), charSet);
        LogUtils.log(TAG, "doPost response=" + strResult);
        client.getConnectionManager().shutdown();
        return strResult;
    }

    /**
     * 封装multi-part方式上传的数据段
     * 
     * @param parameters
     *            key-value形式的参数集
     * @param boundary
     *            数据分割线
     * @return 用于multi-part方式上传的数据
     */
    public static String encodePostBody(Bundle parameters, String boundary) {
        if (parameters == null)
            return "";
        StringBuilder sb = new StringBuilder();

        for (String key : parameters.keySet()) {
            if (parameters.getString(key) != null) {
                continue;
            }
            sb.append("Content-Disposition: form-data; name=\"" + key + "\"\r\n\r\n"
                    + parameters.getString(key));
            sb.append("\r\n" + "--" + boundary + "\r\n");
        }

        return sb.toString();
    }

    /**
     * 读取http请求的网络数据流
     * 
     * @param in
     *            网络数据流
     * @return 读取的网络数据流
     * @throws IOException
     */
    public static String read(InputStream in) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader r = new BufferedReader(new InputStreamReader(in, charSet), 1000);
        for (String line = r.readLine(); line != null; line = r.readLine()) {
            sb.append(line);
        }
        in.close();
        return sb.toString();
    }

    /**
     * 清除cookie
     * 
     * @param context
     */
    public static void clearCookies(Context context) {
        @SuppressWarnings("unused")
        CookieSyncManager cookieSyncMngr = CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
    }

    @SuppressLint("NewApi")
    public static String md5(String input) {
        String result = input;
        if (input != null) {
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(input.getBytes(Charset.forName("UTF-8")));
                BigInteger hash = new BigInteger(1, md.digest());
                result = hash.toString(16);
                if ((result.length() % 2) != 0) {
                    result = "0" + result;
                }
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 建立Http连接，如果apn的设置中配置了代理服务器，那么建立连接时也需要做相关的处理
     * 
     * @throws IOException
     */
    public static HttpURLConnection getConnection(Context context, URL url) throws IOException {

        // 说明：联网时优先选择WIFI联网，如果WIFI没开或不可用，则使用移动网络
        HttpURLConnection httpsURLConn = null;

        // 获取当前可用网络信息
        ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connMgr.getActiveNetworkInfo();

        // 如果当前是WIFI连接
        if (null != netInfo && ConnectivityManager.TYPE_WIFI == netInfo.getType()) {
            httpsURLConn = (HttpURLConnection) url.openConnection();
        } else {// 非WIFI联网
            String proxyHost = android.net.Proxy.getDefaultHost();

            if (null == proxyHost) { // 直连模式
                httpsURLConn = (HttpURLConnection) url.openConnection();
            } else { // 代理模式
                java.net.Proxy p = new java.net.Proxy(java.net.Proxy.Type.HTTP, new InetSocketAddress(
                        android.net.Proxy.getDefaultHost(), android.net.Proxy.getDefaultPort()));
                httpsURLConn = (HttpURLConnection) url.openConnection(p);
            }
        }
        httpsURLConn.setReadTimeout(CONNECT_TIMEOUT);
        httpsURLConn.setConnectTimeout(CONNECT_TIMEOUT);
        return httpsURLConn;
    }

    /**
     * 判断字符串中是否有中文字符
     * 
     * @param str
     *            字符串
     * @return boolean
     */
    public static boolean isContainChinese(String str) {
        if (str == null || str.trim().length() <= 0) {
            return false;
        }

        int len = str.length();
        for (int i = 0; i < len; i++) {
            char word = str.charAt(i);
            if ((word >= 0x4e00) && (word <= 0x9fbb)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断网络是否可用
     * 
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 判断网络类型
     * 
     * @return
     */
    public static boolean isWap() {
        String proxyHost = android.net.Proxy.getDefaultHost();
        if (proxyHost != null) {
            return true;
        } else {
            return false;
        }
    }

    @SuppressWarnings("unused")
    private MyBean httpPost(String url) throws ClientProtocolException, IOException {
        MyBean bean = MyBeanFactory.createResonseBean();
        DefaultHttpClient client = new DefaultHttpClient();
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        NameValuePair pair1 = new BasicNameValuePair("name", "");
        NameValuePair pair2 = new BasicNameValuePair("age", "");
        list.add(pair1);
        list.add(pair2);
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, "UTF-8");
        HttpPost post = new HttpPost(url);// 此处的URL为http://..../path
        post.setEntity(entity);
        HttpResponse response = client.execute(post);
        int code = response.getStatusLine().getStatusCode();
        LogUtils.log(TAG, LogUtils.getThreadName() + " code = " + code);
        return bean;
    }

    // private MyBean httpGet(String url) {
    // MyBean bean = MyBeanFactory.createResonseBean();
    // DefaultHttpClient client = new DefaultHttpClient();
    // HttpGet get = new HttpGet(url);//
    // 此处的URL为http://..../path?arg1=value&....argn=value
    // HttpResponse response = client.execute(get); // 模拟请求
    // int code = response.getStatusLine().getStatusCode();// 返回响应码
    // InputStream in = response.getEntity().getContent();// 服务器返回的数据
    // return bean;
    // }
    /**
     * @param url
     * @param fileParam
     * @param jsonParam
     * @return
     * @throws IOException
     *             upload file
     */
    @SuppressWarnings("unused")
    private String upLoad(String url, Object fileParam, String jsonParam) throws IOException {

        // 上传到文件服务器
        HttpPost httpPost = new HttpPost(url);
        // 设置上传文件
        if (fileParam instanceof String) {
            FileBody fb = new FileBody(new File((String) fileParam));
            MultipartEntity reqEntity = new MultipartEntity();
            reqEntity.addPart("file", fb);
            httpPost.setEntity(reqEntity);
        } else if (fileParam instanceof byte[]) {
            ByteArrayBody fb = new ByteArrayBody((byte[]) fileParam, "file.png");
            MultipartEntity reqEntity = new MultipartEntity();
            reqEntity.addPart("file", fb);
            httpPost.setEntity(reqEntity);
        } else {
            return null;
        }

        httpPost.setHeader("parameters", jsonParam);
        // 执行上传
        HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, NetUtil.CONNECT_TIMEOUT);
        HttpConnectionParams.setSoTimeout(httpParams, NetUtil.CONNECT_TIMEOUT);
        HttpResponse httpResponse = new DefaultHttpClient(httpParams).execute(httpPost);
        String strResult = EntityUtils.toString(httpResponse.getEntity(), NetUtil.charSet);

        try {
            LogUtils.logd(TAG, "post response=" + new JSONObject(strResult).toString(1));
        } catch (JSONException e) {
            LogUtils.logd(TAG, "post response=" + strResult);
        }

        return strResult;

    }

//    private void getHttpClient() {
//        HttpClient httpClient;
//        HttpParams params = new BasicHttpParams();
////设置允许链接的做多链接数目
//        ConnManagerParams.setMaxTotalConnections(params, 200);
////设置超时时间.
//        ConnManagerParams.setTimeout(params, 10000);
////设置每个路由的最多链接数量是20
//        ConnPerRouteBean connPerRoute = new ConnPerRouteBean(20);
////设置到指定主机的路由的最多数量是50
//        HttpHost localhost = new HttpHost("127.0.0.1", 80);
//        connPerRoute.setMaxForRoute(new HttpRoute(localhost), 50);
//        ConnManagerParams.setMaxConnectionsPerRoute(params, connPerRoute);
////设置链接使用的版本
//        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
////设置链接使用的内容的编码
//        HttpProtocolParams.setContentCharset(params, HTTP.DEFAULT_CONTENT_CHARSET);
////是否希望可以继续使用.
//        HttpProtocolParams.setUseExpectContinue(params, true);
//
//        SchemeRegistry schemeRegistry = new SchemeRegistry();
//        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
//        schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
//        ClientConnectionManager cm = new ThreadSafeClientConnManager(params, schemeRegistry);
//        httpClient = new DefaultHttpClient(cm, params);
//    }
}
