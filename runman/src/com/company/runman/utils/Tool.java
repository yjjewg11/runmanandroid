package com.company.runman.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.zip.GZIPOutputStream;

/**
 * Created by Edison on 2014/6/6.
 */
public final class Tool {

    private static final String TAG = "Tool";

    private static boolean sIs2G = false;

    private Tool() {
    }

    static public String objectToString(Object o){
        if(o==null)return "";
        return o+"";
    }

    public static Integer convertInteger(String valueStr) {
        int rate = 0;
        try {
            rate = Integer.valueOf(valueStr);
        } catch (Exception e) {
            rate = 0;
        }
        return rate;
    }

    public static Long convertLong(String valueStr) {
        long rate = 0;
        try {
            rate = Long.valueOf(valueStr);
        } catch (Exception e) {
            rate = 0;
        }
        return rate;
    }

    /**
     * 传入url，如果是相对路径，则加载配置的服务器地址返回，完整url
     * @param url
     * @return
     */
    public static String getFullUrl(String url){
        if(url.indexOf("http://")==-1&&url.indexOf("https://")==-1){
            return   Constant.Host.BASE_HOST+url;
        }
      return url;
    }
    public static Double convertDouble(String valueStr) {
        Double rate = Double.longBitsToDouble(0);
        try {
            rate = Double.valueOf(valueStr);
        } catch (Exception e) {
            Double.longBitsToDouble(0);
        }
        return rate;
    }

    public synchronized static List<String> run(String[] cmd,
                                                String workdirectory) throws IOException {
        List<String> lines = new ArrayList<String>();

        InputStream in = null;
        BufferedReader reader = null;
        InputStreamReader isReader = null;

        try {
            ProcessBuilder builder = new ProcessBuilder(cmd);
            if (workdirectory != null) {
                builder.directory(new File(workdirectory));
            }
            builder.redirectErrorStream(true);
            Process process = builder.start();
            in = process.getInputStream();

            isReader = new InputStreamReader(in);
            reader = new BufferedReader(isReader);

            String line = null;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            LogHelper.w(TAG, "", ex);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception exp) {
                    LogHelper.w(TAG, "", exp);
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception exp) {
                    LogHelper.w(TAG, "", exp);
                }
            }
            if (isReader != null) {
                try {
                    isReader.close();
                } catch (Exception exp) {
                    LogHelper.w(TAG, "", exp);
                }
            }
        }

        return lines;
    }

    public static String getTotalMem() {
        String result = null;
        try {
            String[] args = {"/system/bin/cat", "/proc/meminfo"};
            List<String> lines = run(args, "/system/bin/");
            if (lines == null) {
                return "0";
            }
            String prefix = "MemTotal:";
            String line = null;
            for (int i = 0; i < lines.size(); i++) {
                line = lines.get(i);
                if (line != null && line.startsWith(prefix)) {
                    String res = line.substring(prefix.length()).trim();
                    if (res.endsWith("kB")) {
                        res = res.substring(0, res.length() - 2);
                    }
                    result = res.trim();
                    break;
                }
            }
        } catch (IOException ex) {
            LogHelper.i("fetch_process_info", "ex", ex);
        }
        return result;
    }

    public static boolean runShellWaitForResult(final String cmd) throws SecurityException {
        LogHelper.i(TAG, "begain runResultWaitFor");
        RunShellThread thread = new RunShellThread(cmd, false, false);
        thread.setDaemon(true);
        thread.start();
        int k = 0;
        final int N = 20;
        while (!thread.isReturn() && k++ < N) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                // e.printStackTrace();
            }
        }
        if (k >= N) {
            thread.interrupt();
        }
        LogHelper.i(TAG, "end runResultWaitFor isSuccess :" + thread.isSuccess() + " runResultWaitFor isReturn " + thread.isReturn);
        return thread.isSuccess();
    }

    public static void runShell(final String cmd) throws SecurityException {
        RunShellThread thread = new RunShellThread(cmd, false, false);
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * 执行Process的线程 cfzheng
     */
    private static class RunShellThread extends Thread {
        private boolean isReturn;
        private boolean isSuccess;
        private String cmd;

        public boolean isReturn() {
            return isReturn;
        }

        public boolean isSuccess() {
            return isSuccess;
        }

        /**
         * @param cmd       命令内容
         * @param isReturn  线程是否已经返回
         * @param isSuccess Process是否执行成功
         */
        public RunShellThread(String cmd, boolean isReturn, boolean isSuccess) {
            this.isReturn = isReturn;
            this.isSuccess = isSuccess;
            this.cmd = cmd;
        }

        @Override
        public void run() {
            try {
                Runtime runtime = Runtime.getRuntime();
                Process proc = runtime.exec(cmd);
                if (proc == null) {
                    LogHelper.i(TAG, "runResultWaitFor proc == null ");
                } else if (proc.waitFor() == 0) {
                    isSuccess = true;
                }
            } catch (IOException e) {
                LogHelper.w(TAG, "IOException", e);
            } catch (InterruptedException e) {
                LogHelper.w(TAG, "InterruptedException", e);
            }
            isReturn = true;
        }
    }

    private static final AtomicInteger businessCount = new AtomicInteger();

    public static int increaseBusinessCount(String tag) {
        if (TextUtils.isEmpty(tag)) {
            LogHelper.d(TAG, "increaseBusinessCount");
        } else {
            LogHelper.d(TAG, "increaseBusinessCount for " + tag);
        }
        return businessCount.incrementAndGet();
    }

    public static int decreaseBusinessCount(String tag) {
        if (TextUtils.isEmpty(tag)) {
            LogHelper.d(TAG, "decreaseBusinessCount");
        } else {
            LogHelper.d(TAG, "decreaseBusinessCount for " + tag);
        }
        return businessCount.decrementAndGet();
    }

    public static int getBusinessCount() {
        return businessCount.get();
    }

    public static void ensurePostBusinessLaunch(final String tag, final long delayMillis) {
        increaseBusinessCount(tag);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(delayMillis);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                decreaseBusinessCount(tag);
            }
        }).start();
    }

    /**
     * Returns whether the network is available
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns whether the network is roaming
     */
    public static boolean isNetworkRoaming(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.getType() == ConnectivityManager.TYPE_MOBILE) {
                if (telephonyManager.isNetworkRoaming()) {
                    return true;
                }
            }
        }
        return false;
    }


    public static boolean isWifiNetwork(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.getType() == ConnectivityManager.TYPE_WIFI) {
                return true;
            }
        }
        return false;
    }

    private static final int kSystemRootStateUnknow = -1;
    private static final int kSystemRootStateDisable = 0;
    private static final int kSystemRootStateEnable = 1;

    private static int systemRootState = kSystemRootStateUnknow;

    /**
     * 用于判断系统是否已经Root
     */
    public static boolean isRootSystem() {
        if (systemRootState == kSystemRootStateEnable) {
            return true;
        } else if (systemRootState == kSystemRootStateDisable) {

            return false;
        }
        File f = null;
        final String kSuSearchPaths[] = {"/system/bin/", "/system/xbin/",
                "/system/sbin/", "/sbin/", "/vendor/bin/"};
        try {
            for (int i = 0; i < kSuSearchPaths.length; i++) {
                f = new File(kSuSearchPaths[i] + "su");
                if (f != null && f.exists()) {
                    systemRootState = kSystemRootStateEnable;
                    return true;
                }
            }
        } catch (Exception e) {
        }
        systemRootState = kSystemRootStateDisable;
        return false;
    }

    public static String chgUrlByNewHost(String strUrl, String newHost) {
        if (TextUtils.isEmpty(newHost)) {
            return strUrl;
        }

        URL url = null;
        try {
            url = new URL(strUrl);
        } catch (MalformedURLException e) {
            LogHelper.e(TAG, "", e);
            return strUrl;
        }

        final String host = url.getHost();

        final String host2;
        if (newHost.startsWith("http://")) {
            try {
                host2 = new URL(newHost).getHost();
            } catch (MalformedURLException e) {
                LogHelper.e(TAG, "", e);
                return strUrl;
            }
        } else {
            host2 = newHost;
        }

        if (TextUtils.equals(host, host2)) {
            return strUrl;
        }

        return strUrl.replaceFirst(host, host2);
    }

    public static boolean chinpLength(String s, int minlen) {
        if (s.length() >= minlen) {
            return true;
        }
        int l = 0;
        char[] cs = s.toCharArray();
        for (int i = 0; i < cs.length; i++) {
            if (cs[i] < 255) {
                l++;
            } else {
                l += 2;
            }
        }
        return l >= minlen;
    }

    public static void setApnDialog(Context c) {
        Intent i = new Intent("android.settings.APN_SETTINGS");
        c.startActivity(i);
    }

    public static boolean checkApkFile(File file) {
        if (file == null) {
            return false;
        }
        if (!file.exists()) {
            return false;
        }
        if (file.isDirectory()) {
            return false;
        }
        String head = readFile(file, 2);
        if (!"PK".equals(head)) {
            return false;
        }
        return true;
    }

    public static String readFile(File file, int maxSize) {
        byte[] buffer = new byte[maxSize];
        int nRead = 0;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            nRead = fis.read(buffer, 0, maxSize);
            return new String(buffer, 0, nRead);
        } catch (Exception e) {
            // e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    // e.printStackTrace();
                }
            }
        }
        return "";
    }

    public static String getNetworkType(Context context) {
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMgr == null) {
            return "";
        }
        NetworkInfo ni = conMgr.getActiveNetworkInfo();
        return getNetworkType(ni);
    }

    public static String getNetworkType(NetworkInfo ni) {
        if (ni == null) {
            return "";
        }
        if (ni.getState() != State.CONNECTED) {
            return "offline";
        }
        if (ni.getType() == ConnectivityManager.TYPE_WIFI) {
            return "wifi";
        }
        String extraInfo = ni.getExtraInfo();
        if (extraInfo != null && extraInfo.toLowerCase().endsWith("wap")) {
            return "wap";
        }
        return "mobile";
    }

    public static boolean isWap(Context context) {
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMgr == null) {
            return false;
        }
        NetworkInfo ni = conMgr.getActiveNetworkInfo();
        return isWap(ni);
    }

    public static boolean isWap(NetworkInfo ni) {
        if (ni == null) {
            return false;
        }
        if (ni.getState() != State.CONNECTED) {
            return false;
        }
        if (ni.getType() != ConnectivityManager.TYPE_MOBILE) {
            return false;
        }
        String extraInfo = ni.getExtraInfo();
        if (extraInfo == null) {
            return false;
        }

        return extraInfo.toLowerCase().endsWith("wap");
    }

    public static boolean isWifi(Context context) {
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMgr == null) {
            return false;
        }
        NetworkInfo ni = conMgr.getActiveNetworkInfo();
        return isWifi(ni);
    }

    public static boolean isWifi(NetworkInfo ni) {
        if (ni == null) {
            return false;
        }
        if (ni.getState() != State.CONNECTED) {
            return false;
        }
        if (ni.getType() != ConnectivityManager.TYPE_WIFI) {
            return false;
        }
        return true;
    }

    public static boolean isMobile(Context context) {
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMgr == null) {
            return false;
        }
        NetworkInfo ni = conMgr.getActiveNetworkInfo();
        return isMobile(ni);
    }

    public static boolean isMobile(NetworkInfo ni) {
        if (ni == null) {
            return false;
        }
        if (ni.getState() != State.CONNECTED) {
            return false;
        }
        if (ni.getType() != ConnectivityManager.TYPE_MOBILE) {
            return false;
        }
        return true;
    }

    public static boolean is3GWap(Context context) {
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMgr == null) {
            return false;
        }
        NetworkInfo ni = conMgr.getActiveNetworkInfo();
        return is3GWap(ni);
    }

    public static boolean is3GWap(NetworkInfo ni) {
        if (ni == null) {
            return false;
        }
        if (ni.getState() != State.CONNECTED) {
            return false;
        }
        if (ni.getType() != ConnectivityManager.TYPE_MOBILE) {
            return false;
        }
        String extraInfo = ni.getExtraInfo();
        if (extraInfo == null) {
            return false;
        }

        return extraInfo.toLowerCase().endsWith("3gwap");
    }

    private static boolean isShown3GWarnDlg = false;

    public static boolean isConnection(Context context) {
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 返回是否为2G网络
     *
     * @return
     */
    public static boolean is2GNetWork() {
        return sIs2G;
    }

    public static void check2GNetWork(Context context) {
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMgr == null) {
            sIs2G = false;
            return;
        }
        NetworkInfo ni = conMgr.getActiveNetworkInfo();
        if (ni == null) {
            sIs2G = false;
            return;
        }
        if (ni.getState() != State.CONNECTED) {
            sIs2G = false;
        }
        if (!isConnectionFast(ni.getType(), ni.getSubtype())) {
            sIs2G = true;
        }
        sIs2G = false;
    }

    /**
     * 判断是否为高速网络
     *
     * @param type
     * @param subType
     * @return true 为3G或者wifi高速网络， false 2G网络
     */
    public static boolean isConnectionFast(int type, int subType) {
        if (type == ConnectivityManager.TYPE_WIFI) {
            return true;
        } else if (type == ConnectivityManager.TYPE_MOBILE) {
            switch (subType) {
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                    return false; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_CDMA:
                    return false; // ~ 14-64 kbps
                case TelephonyManager.NETWORK_TYPE_EDGE:
                    return false; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    return true; // ~ 400-1000 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    return true; // ~ 600-1400 kbps
                case TelephonyManager.NETWORK_TYPE_GPRS:
                    return false; // ~ 100 kbps
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                    return true; // ~ 2-14 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPA:
                    return true; // ~ 700-1700 kbps
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                    return true; // ~ 1-23 Mbps
                case TelephonyManager.NETWORK_TYPE_UMTS:
                    return true; // ~ 400-7000 kbps
                // Unknown
                case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                    return false;
                default:
                    return false;
            }
        } else {
            return false;
        }
    }

    public static void hideIme(Context c, View v) {
        InputMethodManager imm = (InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    public static boolean checkIsWap(final Context c) {
        return false;
    }

    public static String getLcidFromUrl(String url) {
        Pattern pattern = Pattern.compile("lcaid=\\d*");
        Matcher matcher = pattern.matcher(url);
        String lcidStr = "";
        if (matcher.find()) {
            lcidStr = matcher.group();
            lcidStr = lcidStr.substring(6);
        }
        return lcidStr;
    }

    public static String getDownloadShortLink(Context context) {
        String downloadShortLink = "http://www.lenovomm.com/appstore/shortlink/";
        try {
            PackageManager pm = context.getPackageManager();
            ApplicationInfo appInfo = pm.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            downloadShortLink = appInfo.metaData.getString("downloadShortLink");
        } catch (Exception e) {
            downloadShortLink = "http://www.lenovomm.com/appstore/shortlink/";
        }
        return downloadShortLink;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    private final static String STRING_FILTER_EMAIL = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";

    /**
     * 判断是否是正确得邮箱格式
     *
     * @param str
     * @return
     */
    public static boolean isRightEmail(CharSequence str) {
        Pattern pattern = Pattern.compile(STRING_FILTER_EMAIL);
        Matcher matcher = pattern.matcher(str);
        return matcher.find();
    }

    private final static String STRING_FILTER = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&;*（）——+|{}【】‘；：”“’。，、？]";

    /**
     * 清除掉所有特殊字符
     *
     * @param str 需要过滤的字符串
     * @return
     * @throws PatternSyntaxException
     */
    public static String filterString(String str) throws PatternSyntaxException {
        Pattern p = Pattern.compile(STRING_FILTER);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    /**
     * 是否有特殊字符
     *
     * @param str
     * @return
     * @throws PatternSyntaxException
     */
    public static boolean isContainsSpecChars(CharSequence str) throws PatternSyntaxException {
        Pattern p = Pattern.compile("[\\w]+");
        Matcher m = p.matcher(str);
        return !m.matches();
    }

    /**
     * 是否有特殊字符
     *
     * @param str
     * @return
     * @throws PatternSyntaxException
     */
    public static boolean isSpecSearchCode(CharSequence str) throws PatternSyntaxException {
        Pattern p = Pattern.compile("^[a-zA-Z]{3}[0-9]{8}");
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 处理需要高亮显示的字符
     *
     * @param str 需要处理的完整字符串
     * @return 需要高亮处理的特定字符串列表
     */
    public static List<String> filterHighLightString(String str) throws PatternSyntaxException {
        List<String> result = new ArrayList<String>();
        Pattern srcPattern = Pattern.compile("<em>.*");
        if (!str.contains("</em>")) {
            return result;
        }
        String[] t = str.split("</em>");

        if (t.length > 0) {
            for (int i = 0; i < t.length; i++) {
                Matcher matcher = srcPattern.matcher(t[i]);
                if (matcher.find()) {
                    String temp = matcher.group();
                    if (temp != null && temp.length() > 0) {
                        temp = temp.replace("<em>", "");
                        result.add(temp);
                    }
                }
            }
        }
        return result;
    }

    /**
     * 替换高亮显示的标签
     *
     * @param str 需要处理的字符串
     * @return 处理完成后的字符串
     * @throws PatternSyntaxException
     */
    public static String replceHighLight(String str) throws PatternSyntaxException {
        if (str == null) {
            return str;
        } else if (str.length() > 0) {
            String result = str.replaceAll("<em>", "<font color=\"#fe8800\">");
            result = result.replaceAll("</em>", "</font>");
            return result;
        } else {
            return str;
        }
    }

    /**
     * 替换高亮显示的标签
     *
     * @param str 需要处理的字符串
     * @return 处理完成后的字符串
     * @throws PatternSyntaxException
     */
    public static String replceHighLightColor(String str, String color) throws PatternSyntaxException {
        if (str == null) {
            return str;
        } else if (str.length() > 0) {
            String result = str.replaceAll("<em>", "<font color=\"" + color + "\">");
            result = result.replaceAll("</em>", "</font>");
            return result;
        } else {
            return str;
        }
    }

    public static String replceHighLight(String str, String color) throws PatternSyntaxException {
        if (str == null) {
            return str;
        } else if (str.length() > 0) {
            String result = str.replaceAll("fuckColor1", "<font color=\"" + color + "\">");
            result = result.replaceAll("fuckColor2", "</font>");
            return result;
        } else {
            return str;
        }
    }

    public static String replceUnderLine(String str) throws PatternSyntaxException {
        if (str == null) {
            return str;
        } else if (str.length() > 0) {
            String result = str.replaceAll("underLine1", "<u>");
            result = result.replaceAll("underLine2", "</u>");
            return result;
        } else {
            return str;
        }
    }

    public static String replceHighLight2(String str, String color1, String color2, String color3) throws PatternSyntaxException {
        if (str == null) {
            return str;
        } else if (str.length() > 0) {
            String result = str.replaceAll("fuckColor1", "&nbsp;<font color=\"" + color1 + "\">");
            result = result.replaceAll("fuckColor2", "</font>&nbsp;&nbsp;");
            result = result.replaceAll("fuckColor3", "&nbsp;<font color=\"" + color2 + "\">");
            result = result.replaceAll("fuckColor4", "</font>");
            result = result.replaceAll("fuckColor5", "<font color=\"" + color3 + "\">");
            result = result.replaceAll("fuckColor6", "</font>");
            return result;
        } else {
            return str;
        }
    }

    public static String replceHighLight2PAD(String str, String color1, String color2) throws PatternSyntaxException {
        if (str == null) {
            return str;
        } else if (str.length() > 0) {
            String result = str.replaceAll("fuckColor1", "&nbsp;<font color=\"" + color1 + "\">");
            result = result.replaceAll("fuckColor2", "</font>&nbsp;&nbsp;");
            result = result.replaceAll("fuckColor3", "&nbsp;<font color=\"" + color1 + "\">");
            result = result.replaceAll("fuckColor4", "</font>");
            result = result.replaceAll("fuckColor5", "<font color=\"" + color2 + "\">");
            result = result.replaceAll("fuckColor6", "</font>&nbsp;&nbsp;");
            result = result.replaceAll("fuckColor7", "<font color=\"" + color2 + "\">&nbsp;&nbsp;&nbsp;&nbsp;");
            result = result.replaceAll("fuckColor8", "</font>&nbsp;&nbsp;");
            return result;
        } else {
            return str;
        }
    }

    /**
     * 替换搜索结果中的高亮标签
     *
     * @param str
     * @return
     */
    public static final String replceTagToNull(String str) {
        String result;
        if (str != null) {
            result = str.replaceAll("<em>", "");
            result = result.replaceAll("</em>", "");
        } else {
            result = "";
        }
        return result;
    }

    /**
     * 返回处理后的XML字符串 xml解析无法识别特殊字符串 & < > ' "
     *
     * @param body
     * @return
     */
    public static String replaceXML(String body) {
        String result = body;
        if (body == null) {
            return result;
        } else if (body.length() > 0) {
            result = result.replaceAll("&", "&amp;");
            result = result.replaceAll("'", "&apos;");
            result = result.replaceAll("\"", "&quot;");
            return result;
        } else {
            return result;
        }
    }

    public static boolean isStringFilter(String str) throws PatternSyntaxException {
        Pattern p = Pattern.compile(STRING_FILTER);
        Matcher m = p.matcher(str);
        return m.find();
    }

    public static String getAppName(Context context, String packageName) {
        PackageInfo pkgInfo = getAppPkgInfo(context, packageName);
        try {
            if (pkgInfo != null) {
                return pkgInfo.applicationInfo.loadLabel(context.getPackageManager()).toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return packageName;
    }

    public static PackageInfo getAppPkgInfo(Context context, String packageName) {
        try {
            PackageManager pm = context.getPackageManager();
            return pm.getPackageInfo(packageName, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean checkUpdate(Context context, String packageName, int versionCode) {
        PackageInfo pi = getAppPkgInfo(context, packageName);
        if (pi == null) {
            return false;
        }
        return (versionCode <= 0) || (pi.versionCode < versionCode);
    }

    public static PackageInfo getAppStorePkgInfo(Context context) {
        try {
            String packageName = context.getPackageName();
            PackageManager pm = context.getPackageManager();
            return pm.getPackageInfo(packageName, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String appStoreVersion = null;
    private static int appStoreVersionCode = -1;

    public static String getAppStoreVersion(Context context) {
        if (TextUtils.isEmpty(appStoreVersion)) {
            retrieveAppStorePkgInfo(context);
        }
        return appStoreVersion;
    }

    public static int getAppStoreVersionCode(Context context) {
        if (TextUtils.isEmpty(appStoreVersion)) {
            retrieveAppStorePkgInfo(context);
        }
        return appStoreVersionCode;
    }

    private static void retrieveAppStorePkgInfo(Context context) {
        try {
            String packageName = context.getPackageName();
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            if (pi != null) {
                appStoreVersion = "" + pi.versionName;
                appStoreVersionCode = pi.versionCode;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static volatile Map<String, String> ipMap = new HashMap<String, String>();

    public static String getIpForHostName(String hostName) {
        return ipMap.get(hostName);
    }

    public static String getIP4ByHostName(String hostName) {
        String ipAddr = "0.0.0.0";
        try {
            InetAddress address = InetAddress.getByName(hostName);
            byte[] bt = address.getAddress();
            ipAddr = (0x0FF & bt[0]) + "." + (0x0FF & bt[1]) + "." + (0x0FF & bt[2]) + "." + (0x0FF & bt[3]);
        } catch (Exception e) {
            // e.printStackTrace();
            ipAddr = "0.0.0.0";
        } catch (Error e) {
            // e.printStackTrace();
            ipAddr = "0.0.0.0";
        }
        return ipAddr;
    }

    @Deprecated
    private static String pingIP4ByDomainName(String domainName) {
        String ipAddr = "";
        try {
            Pattern pattern = Pattern.compile("\\d+\\.\\d+\\.\\d+\\.\\d+");

            Process process = Runtime.getRuntime().exec("ping -a " + domainName);
            InputStream is = process.getInputStream();
            DataInputStream dis = new DataInputStream(is);
            for (int i = 0; i < 10; i++) {
                String line = dis.readLine();
                if (line == null) {
                    break;
                }
                if (TextUtils.isEmpty(line)) {
                    continue;
                }
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    ipAddr = matcher.group();
                    break;
                }
            }
            dis.close();
        } catch (Exception e) {
            // e.printStackTrace();
            ipAddr = e.getMessage();
        }
        return ipAddr;
    }


    private static String getVersionType(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            ApplicationInfo appInfo = pm.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            return appInfo.metaData.getString("lenovo:versionType");
        } catch (Exception e) {
            return "";
        }
    }

    public static String getBuildVersion(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            ApplicationInfo appInfo = pm.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            return String.valueOf(appInfo.metaData.get("lenovo:svn"));
        } catch (Exception e) {
            return "";
        }
    }

    public static boolean isBuildInVersion(Context context) {
        return "buildin".equalsIgnoreCase(getVersionType(context));
    }

    public static boolean isCTAVersion(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            ApplicationInfo appInfo = pm.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            return appInfo.metaData.getBoolean("lenovo:ctaMode");
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 过滤应用名称转为拼音后的非法字符
     *
     * @return
     */
    public static String filterAppName(String py) {
        try {
            Pattern p = Pattern.compile("[^a-zA-Z0-9]");
            Matcher m = p.matcher(py);
            return m.replaceAll("").trim();
        } catch (Exception e) {
            // e.printStackTrace();
            return py;
        }
    }

    /**
     * 转换字符串过滤掉非小数点和数字
     *
     * @param string
     * @return
     */
    public static Float filterStringToFloat(String string) {
        try {
            Pattern pattern = Pattern.compile("[^0-9.]");
            Matcher matcher = pattern.matcher(string);
            return Float.parseFloat(matcher.replaceAll("").trim());
        } catch (Exception e) {
            // e.printStackTrace();
            return 0f;
        }
    }

    /**
     * 转换字符串过滤掉非小数点和数字
     *
     * @param string
     * @return
     */
    public static String filterStringToInteger(String string) {
        try {
            Pattern pattern = Pattern.compile("[^0-9]");
            Matcher matcher = pattern.matcher(string);
            return matcher.replaceAll("").trim();
        } catch (Exception e) {
            // e.printStackTrace();
            return "0";
        }
    }

    /*
     * 在第一个出现字母或者数字的前面加上换行 add by edison
     */
    public static String findCharFromString(String str) {
        String string = str;
        if (string.length() > 12) {
            string = string.substring(0, 10).concat("…");
        }
        String result = string;
        int num = 0;
        try {
            Pattern pattern = Pattern.compile("[a-zA-Z0-9]");
            Matcher matcher = pattern.matcher(string);
            if (matcher.find()) {
                num = matcher.start();
                if (num != 0) {
                    String begin = result.substring(0, num);
                    String end = result.substring(num, result.length());
                    StringBuffer sb = new StringBuffer();
                    // 可以换成\n,具体选择随个人喜好了
                    sb.append(begin).append(" ").append(end);
                    return sb.toString();
                }
            }
            return result;
        } catch (Exception e) {
            LogHelper.d(TAG, "" + e.getMessage());
            return result;
        }
    }

    /**
     * @param keyword 需要转换的关键词
     * @return 返回一个字符串数组 [0]:转换后的字符串 [1]:变色起始下标 ,-1表示没有需要高亮的文字
     */
    public static String[] replaceHighLightSearchTips(String keyword) {
        String[] resultArray = new String[4];
        try {
            if (null != keyword) {
                resultArray[0] = keyword.replace("<a>", "\"").replace("</a>", "\"") + " ";
                int start = resultArray[0].indexOf("\"");
                int end = resultArray[0].indexOf("\"", start + 1);
                resultArray[1] = "" + start; // 关键词开始位置
                resultArray[2] = resultArray[0].substring(start, end);// 需要去除最后一个空格和引号的位置
                resultArray[3] = "" + (end + 1); // 关键词结束位置,加1是为了包含最后一个引号
            } else {
                resultArray = new String[]{" ", "-1", "", "-1"};
            }
            return resultArray;
        } catch (Exception e) {
            return new String[]{" ", "-1", "", "-1"};
        }
    }

    // 根据Unicode编码完美的判断中文汉字和符号
    private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            return true;
        }
        return false;
    }

    // 完整的判断中文汉字和符号
    public static boolean isChinese(String strName) {
        char[] ch = strName.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (isChinese(c)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 将字符串转成MD5值
     *
     * @param value
     * @return
     */
    public static String getMD5(String value) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] strTemp = value.toLowerCase().getBytes();
            //使用MD5创建MessageDigest对象
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte b = md[i];
                //System.out.println((int)b);
                //将没个数(int)b进行双字节加密
                str[k++] = hexDigits[b >> 4 & 0xf];
                str[k++] = hexDigits[b & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 将bytes转换为G
     *
     * @param value
     * @return
     */
    public static String getSizeFromBytes(String value) {
        float size = 0;
        if (!TextUtils.isEmpty(value)) {
            BigDecimal bigDecimal = new BigDecimal(value);
            size = bigDecimal.longValue() / 1024.0f / 1024.0f / 1024.0f;
        }
        DecimalFormat decimalFormat = new DecimalFormat("##0.00");
        return decimalFormat.format(size);
    }
}