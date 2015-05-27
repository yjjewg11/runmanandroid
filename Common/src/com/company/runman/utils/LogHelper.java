package com.company.runman.utils;


import java.io.*;
import java.text.DateFormat;
import java.util.Date;

import android.os.Environment;
import android.text.TextUtils;

/**
 * Created by star on 2014/6/5.
 */
public class LogHelper {

    public static final int ALL = 1;

    public static final int VERBOSE = android.util.Log.VERBOSE;

    public static final int DEBUG = android.util.Log.DEBUG;

    public static final int INFO = android.util.Log.INFO;

    public static final int WARN = android.util.Log.WARN;

    public static final int ERROR = android.util.Log.ERROR;

    public static final int ASSERT = android.util.Log.ASSERT;

    private static final boolean isLog = true;

    private static int filter = VERBOSE;

    public static void setFilter(int level) {
        filter = level;
    }

    public static boolean isDebug() {
        if (isLog && filter <= DEBUG) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isLoggable(String tag, int level) {
        if (isLog && filter <= level) {
            return true;
        } else {
            return false;
        }
    }

    public static int e(String tag, String msg) {
        return (isLog && filter <= ERROR) ? android.util.Log.e(tag, msg) : 0;
    }

    public static int e(String tag, String msg, Throwable tr) {
        return (isLog && filter <= ERROR) ? android.util.Log.e(tag, msg, tr) : 0;
    }

    public static int w(String tag, String msg) {
        return (isLog && filter <= WARN) ? android.util.Log.w(tag, msg) : 0;
    }

    public static int w(String tag, String msg, Throwable tr) {
        return (isLog && filter <= WARN) ? android.util.Log.w(tag, msg, tr) : 0;
    }

    public static int i(String tag, String msg) {
        return (isLog && filter <= INFO) ? android.util.Log.i(tag, msg) : 0;
    }

    public static int i(String tag, String msg, Throwable tr) {
        return (isLog && filter <= INFO) ? android.util.Log.i(tag, msg, tr) : 0;
    }

    public static int d(String tag, String msg) {
        return (isLog && filter <= DEBUG) ? android.util.Log.d(tag, msg) : 0;
    }

    public static int d(String tag, String msg, Throwable tr) {
        return (isLog && filter <= DEBUG) ? android.util.Log.d(tag, msg, tr) : 0;
    }

    public static int v(String tag, String msg) {
        return (isLog && filter <= VERBOSE) ? android.util.Log.v(tag, msg) : 0;
    }

    public static int v(String tag, String msg, Throwable tr) {
        return (isLog && filter <= VERBOSE) ? android.util.Log.v(tag, msg, tr) : 0;
    }

    public static void getTraces(String tag) {
        LogHelper.w(tag, tag, new Exception(tag));
    }

    public static int dLstr(String tag, String msg) {
        return (isLog && filter <= DEBUG) ? logLd(tag, msg) : 0;
    }

    private static int logLd(String tag, String msg) {
        int result = 0;
        int LEN_MAX = 3000;
        int start = 0;
        int len = msg.length();
        int end = len > LEN_MAX ? LEN_MAX : len;
        int temp = 0;
        while (start < end) {
            result = android.util.Log.d(tag, msg.substring(start, end));
            start = end;
            temp = len - end;
            end += temp > LEN_MAX ? LEN_MAX : temp;
        }
        return result;
    }

    /**
     * 打印程序调用的Task信息
     *
     * @param str
     */
    public static void printStackTrace(String str) {
        StackTraceElement st[] = Thread.currentThread().getStackTrace();
        for (int i = 0; i < st.length; i++) {
            LogHelper.d(str, i + ":" + st[i]);
        }
    }

    /**
     * 打印程序调用的Task信息
     *
     * @param str
     * @param index StackTrace起始位置
     */
    public static void printStackTrace(String str, int index) {
        StackTraceElement st[] = Thread.currentThread().getStackTrace();
        if (index < st.length) {
            for (int i = index; i < st.length; i++) {
                LogHelper.d(str, i + ":" + st[i]);
            }
        } else {
            LogHelper.d(str, "index invalid");
        }
    }

    /**
     * 打印程序调用的Task信息
     *
     * @param str
     * @param begin StackTrace起始位置
     * @param end   StackTrace结束位置
     */
    public static void printStackTrace(String str, int begin, int end) {
        StackTraceElement st[] = Thread.currentThread().getStackTrace();
        if (begin < st.length) {
            end = end < st.length ? ++end : st.length;
            for (int i = begin; i < end; i++) {
                LogHelper.d(str, i + ":" + st[i]);
            }
        } else {
            LogHelper.d(str, "index invalid");
        }
    }

    final static byte SLIPER[] = " ".getBytes();
    final static Date date = new Date();
    final static DateFormat longDateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    static long logFileStamp = 0;

    synchronized public static void f(String tag, String msg) {
        OutputStream os = null;

        String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        File logDir = new File(rootPath + File.separator + Constant.PRIVATE_DATA_PATH);
        if (!TextUtils.isEmpty(rootPath)
                && (logDir.exists() || logDir.mkdirs())) {
            File dir = Environment.getExternalStorageDirectory();
            String logFileName = rootPath + File.separator + Constant.PRIVATE_DATA_PATH + "runmanApp.log";
            final File f = new File(logFileName);
            if (logFileStamp <= 0) {
                if (f == null || !f.exists()) {
                    logFileStamp = System.currentTimeMillis();
                } else {
                    logFileStamp = f.lastModified();
                }
            }
            if (System.currentTimeMillis() - logFileStamp > 24 * 60 * 60 * 1000 && f.exists()) {
                f.delete();
                logFileStamp = System.currentTimeMillis();
            }
            if (os == null) {
                try {
                    os = new FileOutputStream(logFileName, true);
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    LogHelper.e(tag, "", e);
                }
            }
        }
        if (os == null) {
            return;
        }
        try {
            if (!TextUtils.isEmpty(tag)) {
                date.setTime(System.currentTimeMillis());
                os.write(longDateFormat.format(date).getBytes());
            }
            os.write(SLIPER);
            if (!TextUtils.isEmpty(tag)) {
                os.write(tag.getBytes());
            }
            os.write(SLIPER);
            if (!TextUtils.isEmpty(msg)) {
                os.write(msg.getBytes());
            }
            os.write("\r\n".getBytes());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            LogHelper.e(tag, "", e);
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    LogHelper.e(tag, "", e);
                }
            }
        }
    }
}
