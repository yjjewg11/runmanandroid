package com.company.runman.net.utils;

import android.text.TextUtils;
import com.company.runman.utils.LogHelper;
import org.apache.http.HttpHost;
import org.apache.http.HttpVersion;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

/**
 * Created by Edison on 2014/6/6.
 */
public final class ProxyHttpClient extends DefaultHttpClient {
    private static final String TAG = ProxyHttpClient.class.getSimpleName();

    private RuntimeException mLeakedException = null;

    public ProxyHttpClient() {
        this(null);
    }

    public ProxyHttpClient(String userAgent) {
        mLeakedException = new IllegalStateException("ProxyHttpClient created and never closed");

        HttpParams params = super.getParams();

        if (ConnectManager.needProxy()) {
            String proxy = ConnectManager.getProxy();
            int port = ConnectManager.getProxyPort();
            if (!TextUtils.isEmpty(proxy)) {
                LogHelper.i("net", "use proxy " + proxy + ":" + port);
                HttpHost httphost = new HttpHost(proxy, port);
                params.setParameter(ConnRoutePNames.DEFAULT_PROXY, httphost);
            }
        }

        if (!TextUtils.isEmpty(userAgent)) {
            HttpProtocolParams.setUserAgent(params, userAgent);
        }
    }

    public void close() {
        if (mLeakedException != null) {
            getConnectionManager().shutdown();
            mLeakedException = null;
        }
    }

    @Override
    protected HttpParams createHttpParams() {
        HttpParams params = super.createHttpParams();
        params.setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
        HttpConnectionParams.setSocketBufferSize(params, 8192);
        // 设置超时时间
        ConnManagerParams.setTimeout(params, 5000);
        // 设置重定向策略
        HttpClientParams.setRedirecting(params, true);
        HttpConnectionParams.setTcpNoDelay(params, true);
        // 设置授权模式
        HttpClientParams.setAuthenticating(params, false);
        // HTTP 协议的版本,1.1/1.0/0.9
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        // 字符集
        HttpProtocolParams.setContentCharset(params, "UTF-8");
        // 设置user agent
        HttpProtocolParams.setUserAgent(params, "HttpComponents/1.1");
        //杜绝二次握手
        HttpProtocolParams.setUseExpectContinue(params, false);
        return params;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if (mLeakedException != null) {
            LogHelper.e(TAG, "Leak found", mLeakedException);
        }
    }
}
