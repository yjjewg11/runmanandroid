package com.company.runman.net.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Proxy;
import android.text.TextUtils;
import com.company.runman.utils.LogHelper;

/**
 * Created by Edison on 2014/6/6.
 */
public class ConnectManager {
    private static String mNetType = "";
    private static int mPort = -1;
    private static String mProxy = "";
    private static boolean mUseWap = false;

    private ConnectManager() {
    }

    private static void checkApn(Context context, NetworkInfo networkinfo) {
        String extraInfo = networkinfo.getExtraInfo();
        if (extraInfo != null) {
            extraInfo = extraInfo.toLowerCase();
            if (extraInfo.startsWith("cmwap") || extraInfo.startsWith("uniwap") || extraInfo.startsWith("3gwap")) {
                mUseWap = true;
                mProxy = "10.0.0.172";
                mPort = 80;
                return;
            }

            if (extraInfo.startsWith("ctwap")) {
                mUseWap = true;
                mProxy = "10.0.0.200";
                mPort = 80;
                return;
            }

            if (extraInfo.startsWith("cmnet") || extraInfo.startsWith("uninet") || extraInfo.startsWith("3gnet") || extraInfo.startsWith("ctnet")) {
                mUseWap = false;
                mProxy = "";
                mPort = -1;
                return;
            }

            checkDefaultProxy();
        } else {
            mUseWap = false;
            mProxy = "";
            mPort = -1;
        }
    }

    private static void checkDefaultProxy() {
        String host = Proxy.getDefaultHost();
        int port = Proxy.getDefaultPort();
        if (!TextUtils.isEmpty(host)) {
            mProxy = host.trim();
            if ("10.0.0.172".equals(mProxy)) {
                mUseWap = true;
                mPort = port;
            } else if ("10.0.0.200".equals(mProxy)) {
                mUseWap = true;
                mPort = port;
            } else {
                mUseWap = false;
                mPort = port;
            }
        } else {
            mUseWap = false;
            mProxy = "";
            mPort = -1;
        }
    }

    public static void checkNetworkType(Context context) {
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMgr != null) {
            NetworkInfo ni = conMgr.getActiveNetworkInfo();
            if (ni != null) {
                if (ni.getType() == ConnectivityManager.TYPE_WIFI) {
                    mUseWap = false;
                    mNetType = "wifi";
                } else {
                    checkApn(context, ni);
                    mNetType = "" + ni.getExtraInfo();
                }
            } else {
                mUseWap = false;
                mNetType = "na";
                mProxy = "";
                mPort = -1;
            }
        } else {
            mUseWap = false;
            mNetType = "na";
            mProxy = "";
            mPort = -1;
        }

        LogHelper.i("net", "checkNetworkType(useWap:" + mUseWap + ", proxy:" + mProxy + ":" + mPort);
        LogHelper.i("net", "defaultProxy:" + Proxy.getDefaultHost() + ":" + Proxy.getDefaultPort());
    }

    public static String getNetType() {
        return mNetType;
    }

    public static String getProxy() {
        return mProxy;
    }

    public static int getProxyPort() {
        return mPort;
    }

    public static boolean needProxy() {
        String defProxy = Proxy.getDefaultHost();
        return mUseWap && TextUtils.isEmpty(defProxy);
    }
}
