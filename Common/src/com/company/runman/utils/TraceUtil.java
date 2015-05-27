package com.company.runman.utils;

import android.os.Environment;
import android.text.TextUtils;

import java.io.*;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 崩溃以及日志记录
 */
public class TraceUtil {

    public static final String TAG = "TraceUtil";
    private static final String INFO = "Info:";
    private static final String WARNING = "Warning:";
    private static final String ERROR = "Error:";
    private static final int TRACE_LOG_FILE_SIZE = 1024 * 1024 * 2;// 2M

    private TraceUtil() {
    }

    public static void setUncaughtExceptionHandler() {
        /*final UncaughtExceptionHandler defaultHandler = Thread
                .getDefaultUncaughtExceptionHandler();*/
        Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable e) {
                traceThrowableLog(ERROR, e);
                //defaultHandler.uncaughtException(thread, e);
            }

        });
    }

    public static void traceThrowableLog(Throwable e) {
        traceThrowableLog(WARNING, e);
    }

    public static void traceThrowableLog(String label, Throwable e) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        PrintWriter writer = null;
        try {
            // 1.打印堆栈到Console
            e.printStackTrace();

            // 2.记录堆栈到trace.log文件
            writer = getDebugLogWriter();
            if (null == writer) {
                return;
            }

            writer.write(label + "Exception time: " + format.format(new Date())
                    + "\r\n");
            e.printStackTrace(writer);
            writer.write("\r\n");
            writer.flush();
        } catch (Exception ex) {
            //
        } finally {
            close(writer);
        }
    }

    public static void traceLog(String logContent) {
        LogHelper.d(TAG, logContent);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        PrintWriter writer = null;
        try {
            writer = getDebugLogWriter();
            if (null == writer) {
                return;
            }

            writer.write(INFO + format.format(new Date()) + "\r\n");
            writer.write(logContent);
            writer.write("\r\n");
            writer.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            close(writer);
        }
    }

    private static PrintWriter getDebugLogWriter()
            throws FileNotFoundException, UnsupportedEncodingException {
        String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        File logDir = new File(rootPath + File.separator + Constant.PRIVATE_DATA_PATH);
        if (!TextUtils.isEmpty(rootPath)
                && (logDir.exists() || logDir.mkdirs())) {
            FileOutputStream fos;
            File logFile = new File(rootPath + File.separator + Constant.PRIVATE_DATA_PATH + "trace.txt");
            // 日志文件大于2M后，覆盖重写
            if (logFile.exists() && logFile.length() > TRACE_LOG_FILE_SIZE) {
                fos = new FileOutputStream(logFile, false);
            } else {
                fos = new FileOutputStream(logFile, true);
            }
            return new PrintWriter(new OutputStreamWriter(fos, Constant.DEFAULT_ENCODEING), false);
        }
        return null;
    }

    public static void close(Closeable... closeables) {
        for (Closeable closeable : closeables) {
            if (null != closeable) {
                try {
                    closeable.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}