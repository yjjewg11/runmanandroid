package com.company.runman.utils;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Process;

/**
 * Created by Edison on 2014/6/6.
 */
public class SobeyHandlerCenter {

    private final static String TAG = "SobeyHandlerCenter";
    private final static Handler mainHandler = new Handler(Looper.getMainLooper());
    private final static HandlerThread businessHandlerThread = new HandlerThread("BusinessHandler");
    private static Handler businessHandler;
    private final static HandlerThread business0HandlerThread = new HandlerThread("Business0Handler");
    private static Handler business0Handler;
    private final static HandlerThread business1HandlerThread = new HandlerThread("Business1Handler");
    private static Handler business1Handler;
    private final static HandlerThread business2HandlerThread = new HandlerThread("Business2Handler");
    private static Handler business2Handler;
    private final static HandlerThread business3HandlerThread = new HandlerThread("Business3Handler");
    private static Handler business3Handler;
    private final static HandlerThread business4HandlerThread = new HandlerThread("Business4Handler");
    private static Handler business4Handler;
    private final static HandlerThread delayImage2GHandlerThread = new HandlerThread("delayImage2G");
    private static Handler delayImage2GHander;

    private static void bindUncaughtExceptionHandler(HandlerThread thead) {
        if (!LogHelper.isDebug()) {
            thead.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {

                @Override
                public void uncaughtException(Thread thd, Throwable ex) {
                    LogHelper.e(TAG, "uncaughtException:" + thd.toString(), ex);

                    // try restart thread
                    if (thd instanceof HandlerThread) {
                        ((HandlerThread) thd).quit();
                        ((HandlerThread) thd).start();
                    }
                }

            });
        }
    }

    /*
     * 该Handler运行在主线程中，因此一些必须放在主线程来处理的事务可以用该Hanlder来处理
     */
    public static Handler getMainHandler() {
        return mainHandler;
    }

    /*
     * 对于一些高优先级的操作
     */
    public static Handler getBusinessHandler() {
        if (businessHandler == null) {
            synchronized (SobeyHandlerCenter.class) {
                if (businessHandler == null) {
                    bindUncaughtExceptionHandler(businessHandlerThread);
                    businessHandlerThread.start();
                    businessHandler = new Handler(businessHandlerThread.getLooper());
                }
            }
        }
        return businessHandler;
    }

    /*
     * 对于一些耗时但30秒钟能执行完的操作，建议大家放到该Handler来处理
     */
    public static Handler getBusiness0Handler() {
        if (business0Handler == null) {
            synchronized (SobeyHandlerCenter.class) {
                if (business0Handler == null) {
                    bindUncaughtExceptionHandler(business0HandlerThread);
                    business0HandlerThread.setPriority(Process.THREAD_PRIORITY_BACKGROUND);
                    business0HandlerThread.start();
                    business0Handler = new Handler(business0HandlerThread.getLooper());
                }
            }
        }
        return business0Handler;
    }

    /*
     * 该Handler主要用于静默安装，它可能会非常繁忙，它也许10分钟或许更久都没有空。
     */
    public static Handler getBusiness1Handler() {
        if (business1Handler == null) {
            synchronized (SobeyHandlerCenter.class) {
                if (business1Handler == null) {
                    bindUncaughtExceptionHandler(business1HandlerThread);
                    business1HandlerThread.setPriority(Process.THREAD_PRIORITY_BACKGROUND);
                    business1HandlerThread.start();
                    business1Handler = new Handler(business1HandlerThread.getLooper());
                }
            }
        }
        return business1Handler;
    }

    /*
     * 该Handler主要用于一般性后台处理。
     */
    public static Handler getBusiness2Handler() {
        if (business2Handler == null) {
            synchronized (SobeyHandlerCenter.class) {
                if (business2Handler == null) {
                    bindUncaughtExceptionHandler(business2HandlerThread);
                    business2HandlerThread.setPriority(Process.THREAD_PRIORITY_BACKGROUND);
                    business2HandlerThread.start();
                    business2Handler = new Handler(business2HandlerThread.getLooper());
                }
            }
        }
        return business2Handler;
    }

    /*
     * 该Handler主要用于添加删除下载任务专用
     */
    public static Handler getBusiness3Handler() {
        if (business3Handler == null) {
            synchronized (SobeyHandlerCenter.class) {
                if (business3Handler == null) {
                    bindUncaughtExceptionHandler(business3HandlerThread);
                    business3HandlerThread.setPriority(Process.THREAD_PRIORITY_BACKGROUND);
                    business3HandlerThread.start();
                    business3Handler = new Handler(business3HandlerThread.getLooper());
                }
            }
        }
        return business3Handler;
    }

    /*
     * 该Handler主要用于CheckAction任务专用
     */
    public static Handler getBusiness4Handler() {
        if (business4Handler == null) {
            synchronized (SobeyHandlerCenter.class) {
                if (business4Handler == null) {
                    bindUncaughtExceptionHandler(business4HandlerThread);
                    business4HandlerThread.setPriority(Process.THREAD_PRIORITY_BACKGROUND);
                    business4HandlerThread.start();
                    business4Handler = new Handler(business4HandlerThread.getLooper());
                }
            }
        }
        return business4Handler;
    }

    /*
     * 该Handler延迟2g下的图片加载
     */
    public static Handler getDelayImage2GHandler() {
        if (delayImage2GHander == null) {
            synchronized (SobeyHandlerCenter.class) {
                if (delayImage2GHander == null) {
                    bindUncaughtExceptionHandler(delayImage2GHandlerThread);
                    delayImage2GHandlerThread.start();
                    delayImage2GHander = new Handler(delayImage2GHandlerThread.getLooper());
                }
            }
        }
        return delayImage2GHander;
    }
}
