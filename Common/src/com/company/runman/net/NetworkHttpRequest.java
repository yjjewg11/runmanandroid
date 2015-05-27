package com.company.runman.net;

import android.content.Context;
import android.text.TextUtils;
import com.company.runman.datacenter.provider.SharePreferenceProvider;
import com.company.runman.net.interfaces.IRequest;
import com.company.runman.net.utils.ConnectManager;
import com.company.runman.net.utils.ProxyHttpClient;
import com.company.runman.utils.*;
import org.apache.http.*;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Created by Edison on 2014/6/6.
 */
public class NetworkHttpRequest {
    private static final String TAG = "NetworkHttpRequest";
    private static final String APPLICATION_JSON = "application/json";
    private static final String CONTENT_TYPE_TEXT_JSON = "text/json";
    private static final int DEFAULT_IMG_TIMEOUT = 30000;
    private static final int DEFAULT_TIMEOUT = 15000;
    private static final int DEFAULT_POST_TIMEOUT = 20000;
    private static final int DEFAULT_2G_TIMEOUT = 30000;
    private static String cookies = "";
    private static Map<String, String> cookieMap = new HashMap<String, String>();

    private static volatile Long totalBytes = 0L;

    public static long getTotalBytes() {
        return NetworkHttpRequest.totalBytes;
    }

    public static void resetTotalBytes() {
        NetworkHttpRequest.totalBytes = 0L;
    }

    public static void addTotalBytes(long bytes) {
        NetworkHttpRequest.totalBytes += bytes;
    }

    private static AtomicInteger networkRequestCount = new AtomicInteger(0);

    private static void incrementNetwork() {
        SobeyHandlerCenter.getDelayImage2GHandler().post(new Runnable() {

            @Override
            public void run() {
                networkRequestCount.incrementAndGet();
            }

        });
    }

    private static void decrementNetwork() {
        SobeyHandlerCenter.getDelayImage2GHandler().post(new Runnable() {

            @Override
            public void run() {
                networkRequestCount.decrementAndGet();
            }

        });
    }

    private static boolean isIdleNetwork() {
        return networkRequestCount.get() > 0;
    }

    public static void checkNetworkType(Context context) {
        Tool.check2GNetWork(context);
        ConnectManager.checkNetworkType(context);
        enableHttpResponseCache(context);
    }

    private static int getTimeout() {
        return Tool.is2GNetWork() ? DEFAULT_2G_TIMEOUT : DEFAULT_TIMEOUT;
    }

    private static int getPostTimeout() {
        return Tool.is2GNetWork() ? DEFAULT_2G_TIMEOUT : DEFAULT_POST_TIMEOUT;
    }

    private static int getImgTimeout() {
        return Tool.is2GNetWork() ? DEFAULT_2G_TIMEOUT : DEFAULT_IMG_TIMEOUT;
    }

    // 创建一个本地Cookie存储的实例
    private static final CookieStore cookieStore = new BasicCookieStore() {

        @Override
        public synchronized void addCookie(Cookie cookie) {
            super.addCookie(cookie);
            LogHelper.d("Cookie", "addCookie:" + cookie.toString());
        }

    };
    // 创建一个本地上下文信息
    private static volatile HttpContext httpContext = new BasicHttpContext();

    static {
        // 在本地上下文中绑定一个本地存储
        httpContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
    }

    private static void enableHttpResponseCache(Context context) {
        try {
            long httpCacheSize = 10 * 1024 * 1024; // 10 MiB
            File httpCacheDir = new File(context.getCacheDir(), "http");
            Class.forName("android.net.http.HttpResponseCache")
                    .getMethod("install", File.class, long.class)
                    .invoke(null, httpCacheDir, httpCacheSize);
        } catch (Exception httpResponseCacheNotAvailable) {
        }
    }

    private static byte[] unzipData(InputStream gzis) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[4096];
            int len = -1;
            while ((len = gzis.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            gzis.close();
            return baos.toByteArray();
        } catch (IOException e) {
            LogHelper.e(TAG, "gzip error:", e);
        } finally {
            try {
                baos.close();
            } catch (IOException e) {
                LogHelper.e(TAG, "gzip close stream error:", e);
            }
        }
        return new byte[0];
    }

    public static InputStream getUngzippedContent(HttpEntity entity) throws IOException {
        InputStream responseStream = entity.getContent();
        if (responseStream == null) {
            return responseStream;
        }
        Header header = entity.getContentEncoding();
        if (header == null) {
            return responseStream;
        }
        String contentEncoding = header.getValue();
        if (contentEncoding == null) {
            return responseStream;
        }
        if (contentEncoding.contains("gzip")) {
            return new GZIPInputStream(responseStream);
        }
        return responseStream;
    }

    private static byte[] unzipData(HttpEntity entity) {
        try {
            return unzipData(getUngzippedContent(entity));
        } catch (IOException e) {
            return new byte[0];
        }
    }

    public static InputStream getUngzippedContent(HttpURLConnection urlConn) throws IOException {
        InputStream responseStream = urlConn.getInputStream();
        if (responseStream == null) {
            return responseStream;
        }
        String contentEncoding = urlConn.getContentEncoding();
        if (contentEncoding == null) {
            return responseStream;
        }
        if (contentEncoding.contains("gzip")) {
            return new GZIPInputStream(responseStream);
        }
        return responseStream;
    }

    private static byte[] unzipData(HttpURLConnection urlConn) {
        try {
            return unzipData(getUngzippedContent(urlConn));
        } catch (IOException e) {
            return new byte[0];
        }
    }

    private static AbstractHttpEntity getGZipEntity(String data, String charsetName) throws IOException {
        byte[] bytes;
        try {
            bytes = data.getBytes(charsetName);
        } catch (UnsupportedEncodingException e) {
            bytes = data.getBytes();
        }

        ByteArrayOutputStream arr = new ByteArrayOutputStream();
        OutputStream zipper = new GZIPOutputStream(arr);
        zipper.write(bytes);
        zipper.close();
        AbstractHttpEntity entity = new ByteArrayEntity(arr.toByteArray());
        entity.setContentEncoding("gzip");
        return entity;
    }

    private static byte[] getGZipData(String data, String charsetName) throws IOException {
        byte[] bytes;
        try {
            bytes = data.getBytes(charsetName);
        } catch (UnsupportedEncodingException e) {
            bytes = data.getBytes();
        }

        ByteArrayOutputStream arr = new ByteArrayOutputStream();
        OutputStream zipper = new GZIPOutputStream(arr);
        zipper.write(bytes);
        zipper.close();
        return arr.toByteArray();
    }

    private static AbstractHttpClient createHttpClient(int timeOut) {
        AbstractHttpClient httpClient = new ProxyHttpClient();
        HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), timeOut);
        HttpConnectionParams.setSoTimeout(httpClient.getParams(), timeOut);
        httpClient.getConnectionManager().closeExpiredConnections();
        httpClient.getConnectionManager().closeIdleConnections(20, TimeUnit.SECONDS);
        // set retry count as 5 for IOException
        httpClient.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler(5, false));
        return httpClient;
    }

    private static void closeHttpClient(AbstractHttpClient httpClient) {
        if (httpClient != null) {
            if (httpClient instanceof ProxyHttpClient) {
                ((ProxyHttpClient) httpClient).close();
            } else {
                httpClient.getConnectionManager().shutdown();
            }
        }
    }


    public static HttpReturn executeHttpGet(String urlString) {
        HttpReturn ret = new HttpReturn();

        int timeOut = getTimeout();
        long contentLength = 0L;

        URI url;
        try {
            url = new URI(urlString);
        } catch (Exception e) {
            ret.code = 400;
            ret.err = e.getMessage();
            return ret;
        }

        AbstractHttpClient httpClient = null;
        try {
            httpClient = createHttpClient(timeOut);

            HttpGet httpget = new HttpGet(url);
            //httpget.setHeader("Accept-Encoding", "gzip");

            if("".equals(cookies)&&SharePreferenceProvider.getInstance(null)!=null)cookies=SharePreferenceProvider.getInstance(null).getCookies();
            if(!"".equals(cookies)) {
                httpget.addHeader("Cookie", cookies);

            }
            HttpResponse response = httpClient.execute(httpget);


            Header[] headers = response.getHeaders("Set-Cookie");
            if(headers.length != 0 && !"".equals(headers[0].getValue())) {
                String cookieStr = headers[0].getValue();
                String[] cookieValues = cookieStr.split(";");
                for (String cookieValue : cookieValues) {
                    String[] singles = cookieValue.split("=");
                    if(singles.length != 0 && !"".equals(singles[0])) {
                        cookieMap.put(singles[0], singles[1]);
                    }
                }
                if(!cookieMap.isEmpty()) {
                    cookies = "";
                    for (String key : cookieMap.keySet()) {
                        cookies += key + "=" + cookieMap.get(key) + ";";
                    }

                    if(SharePreferenceProvider.getInstance(null)!=null)SharePreferenceProvider.getInstance(null).saveCookies(cookies);
                    TraceUtil.traceLog( "setCookie="+cookies);
                }
            }
            ret.code = response.getStatusLine().getStatusCode();
            if (ret.code == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                contentLength = entity.getContentLength();
                if (contentLength > 0) {
                    addTotalBytes(contentLength);
                }

                // unzip body if needed
                ret.bytes = unzipData(entity);
            } else {
                LogHelper.e(TAG, "executeHttpGet code:" + ret.code + ", urlString:" + urlString);
            }
        } catch (UnknownHostException e) {
            LogHelper.e(TAG, "NetworkHttpRequest executeHttpGet 1 UnknownHostException :" + urlString, e);
            ret.code = Constant.ResponseCode.UNKNOWN_HOST;
            ret.err = e.getMessage();
        } catch (IOException ioe) {
            LogHelper.e(TAG, "NetworkHttpRequest executeHttpGet 1 IOException urlString :" + urlString, ioe);
            ret.code = Constant.ResponseCode.TIMEOUT;
            ret.err = ioe.getMessage();
        } catch (Exception e) {
            LogHelper.e(TAG, "NetworkHttpRequest executeHttpGet 1 error urlString :" + urlString, e);
            ret.code = Constant.ResponseCode.UNKNOWN_ERROR;
            ret.err = e.getMessage();
        } finally {
            closeHttpClient(httpClient);
        }
        return ret;
    }

    public static HttpReturn executeHttpPost(Context context, String urlString, String post) {
        HttpReturn ret = new HttpReturn();

        int timeOut = getPostTimeout();
        long contentLength = 0L;

        URI url;
        try {
            url = new URI(urlString);
        } catch (Exception e) {
            ret.code = 400;
            ret.err = e.getMessage();
            return ret;
        }


        AbstractHttpClient httpClient = null;
        try {
            // LogHelper.i(TAG, "Request start time:" + startTime);
            httpClient = createHttpClient(timeOut);

            HttpPost httppost = new HttpPost(url);
            //httppost.setHeader("Accept-Encoding", "gzip");
          //  httppost.setHeader("Content-Type", "application/json;charset=UTF-8");
           // httppost.addHeader("Accept", "*/*");
          //  httppost.addHeader("Accept-Language", "zh-cn");
         //   httppost.addHeader("Referer", urlString);
           // httppost.addHeader("Cache-Control", "no-cache");
          //  httppost.addHeader("Accept-Encoding", "gzip, deflate");
//            httpPost.addHeader("User-Agent",
//                    "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)");
        //    httppost.addHeader("Host", Constant.Host.BASE_HOST);
            if("".equals(cookies)&&SharePreferenceProvider.getInstance(null)!=null)cookies=SharePreferenceProvider.getInstance(null).getCookies();
            if(!"".equals(cookies)) {
                httppost.addHeader("Cookie", cookies);

            }
           // httppost.addHeader("Connection", "Keep-Alive");
           StringEntity se = new StringEntity(post, HTTP.UTF_8);
            se.setContentType(APPLICATION_JSON + HTTP.CHARSET_PARAM +HTTP.UTF_8);
            se.setContentEncoding(HTTP.UTF_8);
          //  se.setContentType();
           //ByteArrayEntity se=new ByteArrayEntity(post.getBytes( HTTP.UTF_8));

          //  InputStreamEntity se=new InputStreamEntity()
            httppost.setEntity(se);
            // long startTime2 = System.currentTimeMillis();
            HttpResponse response = httpClient.execute(httppost, httpContext);

            Header[] headers = response.getHeaders("Set-Cookie");
            if(headers.length != 0 && !"".equals(headers[0].getValue())) {
                String cookieStr = headers[0].getValue();
                String[] cookieValues = cookieStr.split(";");
                for (String cookieValue : cookieValues) {
                    String[] singles = cookieValue.split("=");
                    if(singles.length != 0 && !"".equals(singles[0])) {
                        cookieMap.put(singles[0], singles[1]);
                    }
                }
                if(!cookieMap.isEmpty()) {
                    cookies = "";
                    for (String key : cookieMap.keySet()) {
                        cookies += key + "=" + cookieMap.get(key) + ";";
                    }
                    if(SharePreferenceProvider.getInstance(null)!=null)SharePreferenceProvider.getInstance(null).saveCookies(cookies);
                    TraceUtil.traceLog( "setCookie="+cookies);
                }
            }
            ret.code = response.getStatusLine().getStatusCode();
            if (ret.code == HttpStatus.SC_OK) {
                // retrieve expire field
                try {
//                    Header expiresHeader = response.getLastHeader("Expires");
//                    Header accessTokenHeader = response.getLastHeader("access_token");
//                    if (expiresHeader != null) {
//                        String expireValue = expiresHeader.getValue();
//                        if (!TextUtils.isEmpty(expireValue)) {
//                            //ret.time = new Date(expireValue).getTime();
//                        }
//                    }
//                    //保存令牌
//                    if (accessTokenHeader != null) {
//                        String accessToken = accessTokenHeader.getValue();
//                        if (!TextUtils.isEmpty(accessToken)) {
//                            LogHelper.d(TAG, "accessToken:" + accessToken);
//                            SharePreferenceProvider.getInstance(context).setAccessToken(accessToken);
//                        }
//                    }
                } catch (Exception e) {
                    ret.time = new Date().getTime();
                }

                HttpEntity entity = response.getEntity();
                contentLength = entity.getContentLength();

                if (contentLength > 0) {
                    addTotalBytes(contentLength);
                }

                // unzip body if needed
                ret.bytes = unzipData(entity);
            } else {
                LogHelper.e(TAG, "executeHttpPost code:" + ret.code + ", urlString:" + urlString);
            }
        } catch (UnknownHostException e) {
            LogHelper.e(TAG, "NetworkHttpRequest executeHttpPost 1 UnknownHostException :" + urlString, e);
            ret.code = Constant.ResponseCode.UNKNOWN_HOST;
            ret.err = e.getMessage();
        } catch (IOException ioe) {
            LogHelper.e(TAG, "NetworkHttpRequest executeHttpPost 1 IOException urlString :" + urlString, ioe);
            ret.code = Constant.ResponseCode.TIMEOUT;
            ret.err = ioe.getMessage();
        } catch (Exception e) {
            LogHelper.e(TAG, "NetworkHttpRequest executeHttpPost 1 error urlString :" + urlString, e);
            ret.code = Constant.ResponseCode.UNKNOWN_ERROR;
            ret.err = e.getMessage();
        } finally {
            closeHttpClient(httpClient);
        }

        return ret;
    }

    public static HttpReturn executeHttpGetImage(String urlString) {
        HttpReturn ret = new HttpReturn();

        int timeOut = getImgTimeout();
        long contentLength = 0L;

        URI url;
        try {
            if (Tool.is2GNetWork()) {
                if (urlString.contains("?")) {
                    url = new URI(urlString + "&nt=2");
                } else {
                    url = new URI(urlString + "?nt=2");
                }
            } else {
                url = new URI(urlString);
            }
        } catch (Exception e) {
            ret.code = Constant.ResponseCode.UNKNOWN_ERROR;
            ret.err = e.getMessage();
            return ret;
        }

        // wait for major data request completed on 2G
        for (int i = 0; isIdleNetwork() && Tool.is2GNetWork() && i < 20; i++) {
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
            }
        }

        AbstractHttpClient httpClient = null;
        try {
            httpClient = createHttpClient(timeOut);

            HttpGet httpget = new HttpGet(url);
            httpget.setHeader("Accept-Encoding", "gzip");
            LogHelper.d(TAG, "HttpImg url:" + url);

            HttpResponse response = httpClient.execute(httpget);

            ret.code = response.getStatusLine().getStatusCode();
            if (ret.code == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                contentLength = entity.getContentLength();

                if (contentLength > 0) {
                    addTotalBytes(contentLength);
                }

                // unzip body if needed
                ret.bytes = unzipData(entity);
            }

            LogHelper.d(TAG, "HttpGetImage code:" + ret.code + ", urlString:" + urlString);
        } catch (IOException ioe) {
            LogHelper.e(TAG, "NetworkHttpRequest executeHttpGetImage 1 IOException urlString :" + urlString, ioe);
            ret.code = Constant.ResponseCode.TIMEOUT;
            ret.err = ioe.getMessage();
        } catch (Exception e) {
            LogHelper.e(TAG, "NetworkHttpRequest executeHttpGetImage 1 error urlString :" + urlString, e);
            ret.code = Constant.ResponseCode.UNKNOWN_ERROR;
            ret.err = e.getMessage();
        } finally {
            closeHttpClient(httpClient);
        }
        return ret;
    }

    public static HttpReturn executeHttpPut(Context context, String urlString, String post) {
        HttpReturn ret = new HttpReturn();

        int timeOut = getPostTimeout();
        long contentLength = 0L;

        URI url;
        try {
            url = new URI(urlString);
        } catch (Exception e) {
            ret.code = 400;
            ret.err = e.getMessage();
            return ret;
        }

        // for zip data config
        boolean bZipData = false;
        if (!TextUtils.isEmpty(post) && post.startsWith(IRequest.ZIPPrefix)) {
            bZipData = true;
            post = post.substring(IRequest.ZIPPrefix.length());
        }

        AbstractHttpClient httpClient = null;
        try {
            // LogHelper.i(TAG, "Request start time:" + startTime);
            httpClient = createHttpClient(timeOut);

            HttpPut httpPut = new HttpPut(url);

            httpPut.setHeader("Accept-Encoding", "gzip");
            if (bZipData) {
                httpPut.setHeader("Content-Encoding", "gzip");
            }
            httpPut.setHeader("Content-Type",APPLICATION_JSON + HTTP.CHARSET_PARAM +HTTP.UTF_8);
            if("".equals(cookies)&&SharePreferenceProvider.getInstance(null)!=null)cookies=SharePreferenceProvider.getInstance(null).getCookies();
            if(!"".equals(cookies)) {
                httpPut.addHeader("Cookie", cookies);

            }
            // by for zip post data
            if (!TextUtils.isEmpty(post)) {
                if (bZipData) {
                    httpPut.setEntity(getGZipEntity(post, HTTP.UTF_8));
                } else {
                    httpPut.setEntity(new StringEntity(post, HTTP.UTF_8));
                }
            }
            StringEntity se = new StringEntity(post, HTTP.UTF_8);

            // long startTime2 = System.currentTimeMillis();
            HttpResponse response = httpClient.execute(httpPut, httpContext);


            Header[] headers = response.getHeaders("Set-Cookie");
            if(headers.length != 0 && !"".equals(headers[0].getValue())) {
                String cookieStr = headers[0].getValue();
                String[] cookieValues = cookieStr.split(";");
                for (String cookieValue : cookieValues) {
                    String[] singles = cookieValue.split("=");
                    if(singles.length != 0 && !"".equals(singles[0])) {
                        cookieMap.put(singles[0], singles[1]);
                    }
                }
                if(!cookieMap.isEmpty()) {
                    cookies = "";
                    for (String key : cookieMap.keySet()) {
                        cookies += key + "=" + cookieMap.get(key) + ";";
                    }
                    if(SharePreferenceProvider.getInstance(null)!=null)SharePreferenceProvider.getInstance(null).saveCookies(cookies);
                    TraceUtil.traceLog( "setCookie="+cookies);
                }
            }
            ret.code = response.getStatusLine().getStatusCode();
            if (ret.code == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                contentLength = entity.getContentLength();

                if (contentLength > 0) {
                    addTotalBytes(contentLength);
                }

                // unzip body if needed
                ret.bytes = unzipData(entity);
            } else {
                LogHelper.e(TAG, "executeHttpPost code:" + ret.code + ", urlString:" + urlString);
            }
        } catch (UnknownHostException e) {
            LogHelper.e(TAG, "NetworkHttpRequest executeHttpPost 1 UnknownHostException :" + urlString, e);
            ret.code = Constant.ResponseCode.UNKNOWN_HOST;
            ret.err = e.getMessage();
        } catch (IOException ioe) {
            LogHelper.e(TAG, "NetworkHttpRequest executeHttpPost 1 IOException urlString :" + urlString, ioe);
            ret.code = Constant.ResponseCode.TIMEOUT;
            ret.err = ioe.getMessage();
        } catch (Exception e) {
            LogHelper.e(TAG, "NetworkHttpRequest executeHttpPost 1 error urlString :" + urlString, e);
            ret.code = Constant.ResponseCode.UNKNOWN_ERROR;
            ret.err = e.getMessage();
        } finally {
            closeHttpClient(httpClient);
        }

        return ret;
    }
}
