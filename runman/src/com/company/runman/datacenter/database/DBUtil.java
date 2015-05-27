package com.company.runman.datacenter.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.company.runman.utils.LogHelper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by EdisonZhao on 14-7-28.
 * Email:zhaoliangyu@sobey.com
 */
public class DBUtil {
    private final static String TAG = "DBUtil";
    public final static int VERSION = 2;
    public final static byte[] writeLock = new byte[0];
    public final static byte[] dbCountLock = new byte[0];
    private final static AtomicInteger dbCount = new AtomicInteger(0);
    private static SQLiteDatabase dbInst = null;

    private DBUtil() {
    }

    /**
     * 获取SD卡数据库文件
     *
     * @return File
     */
    private static File getDBFile(Context context) {
        return new File(getDBFilePath(context));
    }

    /**
     * 获取数据库文件路径
     *
     * @return File;
     */
    private static String getDBFilePath(Context context) {
        final String packageName = context.getPackageName();
        return "/data/data/" + packageName + "/SobeyStore.db";
    }

    static boolean checkedUpgrade = false;

    /**
     * 创建并检查数据库表
     * <p/>
     * 此方法启动应用时运行。
     */
    public static void checkUpgrade(Context context) {
        if (checkedUpgrade) {
            return;
        }

        File dataFile = getDBFile(context);
        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                LogHelper.d(TAG, "DataBase create failed!");
                return;
            }
        }

        // 加锁保证数据
        synchronized (writeLock) {
            SQLiteDatabase database;
            try {
                database = openDatabase(dataFile);
            } catch (Exception e) {
                LogHelper.e(TAG, "db.checkUpgrade(), open database error", e);
                return;
            }

            if (database == null) {
                return;
            }

            try {
                int currentVersion = database.getVersion();

                LogHelper.d(TAG, "DBUtil.CheckUpgrade(); db.getVersion():" + currentVersion + ", newVersion:"
                        + VERSION);

                if (currentVersion == 0) {
                    onCreate(database);
                    currentVersion = VERSION;
                }

                if (currentVersion < VERSION) {
                    currentVersion = onUpgrade(database, VERSION);
                    if (currentVersion != VERSION) {
                        onCreate(database);
                        currentVersion = VERSION;
                    }
                }

                if (currentVersion > VERSION) {
                    onCreate(database);
                    currentVersion = VERSION;
                }

                database.setVersion(currentVersion);
                checkedUpgrade = true;
            } catch (Exception e) {
                LogHelper.d(TAG, "db.checkUpgrade()", e);
            } finally {
                closeDatabase();
            }
        }
    }

    /**
     * 创建数据库表
     *
     * @param db
     */
    private static boolean onCreate(SQLiteDatabase db) {
        LogHelper.d(TAG, "DBUtil.onCreate(); db.getVersion():" + db.getVersion() + ", newVersion:" + VERSION);

        try {
            db.beginTransaction();

            //有表的话，先删表
            String sql = "drop table if EXISTS web_pages";
            db.execSQL(sql);
            sql = "drop table if EXISTS monitor_stats_alert_request";
            db.execSQL(sql);
            sql = "drop table if EXISTS monitor_stats_alert_kitGroupDetail";
            db.execSQL(sql);
            sql = "drop table if EXISTS search_keyword_table";
            db.execSQL(sql);
            sql = "drop table if EXISTS code_scan_history_table";
            db.execSQL(sql);
            //创建收藏网页数据表
            sql = "CREATE TABLE 'web_pages' ('_id' INTEGER PRIMARY KEY, 'title' TEXT NOT NULL, 'url' TEXT NOT NULL, "
                    + " 'userCode' TEXT NOT NULL, " + " 'collectionTime' TEXT NOT NULL)";
            db.execSQL(sql);
            //创建报警信息请求数据表
            sql = "CREATE TABLE 'monitor_stats_alert_request' ('_id' INTEGER PRIMARY KEY,"
                    + " 'UserCode' TEXT NOT NULL, "
                    + " 'Time' TEXT NOT NULL, "
                    + " 'LastAlertTime' TEXT NOT NULL, "
                    + " 'KitCount' TEXT NOT NULL, "
                    + " 'StationCode' TEXT NOT NULL,"
                    + " 'IsReviewed' INTEGER NOT NULL)";
            db.execSQL(sql);
            //创建设备报警信息表
            sql = "CREATE TABLE 'monitor_stats_alert_kitGroupDetail' ('_id' INTEGER PRIMARY KEY,"
                    + " 'UserCode' TEXT NOT NULL, "
                    + " 'StationCode' TEXT NOT NULL, "
                    + " 'GroupCode' TEXT NOT NULL, "
                    + " 'GroupName' TEXT NOT NULL, "
                    + " 'WarningCount' TEXT NOT NULL)";
            db.execSQL(sql);
            //创建最近10次搜索关键词表
            sql = "CREATE TABLE 'search_keyword_table' ('_id' INTEGER PRIMARY KEY,"
                    + " 'UserCode' TEXT NOT NULL, "
                    + " 'Keyword' TEXT NOT NULL, "
                    + " 'Count' INTEGER NOT NULL, "
                    + " 'Timestamp' TEXT NOT NULL)";
            db.execSQL(sql);
            sql = "CREATE TABLE 'code_scan_history_table' ('_id' INTEGER PRIMARY KEY," + " 'Context' TEXT NOT NULL, "
                    + " 'UserCode' TEXT NOT NULL," + " 'Date' TEXT NOT NULL, " + " 'Time' TEXT NOT NULL)";
            db.execSQL(sql);

            db.setTransactionSuccessful();
        } catch (Exception e) {
            LogHelper.e(TAG, "onCreate", e);
        } finally {
            db.endTransaction();
        }
        return false;
    }

    private static int onUpgrade(SQLiteDatabase db, int newVersion) {
        LogHelper.d("zml", "DBUtil.onUpgrade(); db.getVersion():" + db.getVersion() + ", newVersion:" + newVersion);

        // 开始数据库升级
        int currentVersion = db.getVersion();
        if (currentVersion == 1 && newVersion == 2) {
            if (upgradeFrom1To2(db)) {
                currentVersion = newVersion;
            }
        }

        if (currentVersion == 2 && newVersion == 3) {
            if (upgradeFrom2To3(db)) {
                currentVersion = newVersion;
            }

        }

        return currentVersion;
    }

    private static boolean upgradeFrom1To2(SQLiteDatabase db) {
        //升级数据库版本,本次更新新增电视台报警时间保存
        LogHelper.d(TAG, "DBUtil.onCreate(); db.getVersion():" + db.getVersion() + ", newVersion:" + VERSION);

        try {
            db.beginTransaction();
            //创建报警信息请求数据表
            String sql = "CREATE TABLE 'monitor_stats_alert_request' ('_id' INTEGER PRIMARY KEY,"
                    + " 'UserCode' TEXT NOT NULL, "
                    + " 'Time' TEXT NOT NULL, "
                    + " 'LastAlertTime' TEXT NOT NULL, "
                    + " 'KitCount' TEXT NOT NULL, "
                    + " 'StationCode' TEXT NOT NULL)"
                    + " 'IsReviewed' INTEGER NOT NULL)";
            db.execSQL(sql);
            //创建设备报警信息表
            sql = "CREATE TABLE 'monitor_stats_alert_kitGroupDetail' ('_id' INTEGER PRIMARY KEY,"
                    + " 'UserCode' TEXT NOT NULL, "
                    + " 'StationCode' TEXT NOT NULL, "
                    + " 'GroupCode' TEXT NOT NULL, "
                    + " 'GroupName' TEXT NOT NULL, "
                    + " 'WarningCount' TEXT NOT NULL)";
            db.execSQL(sql);
            //创建最近10次搜索关键词表
            sql = "CREATE TABLE 'search_keyword_table' ('_id' INTEGER PRIMARY KEY,"
                    + " 'UserCode' TEXT NOT NULL, "
                    + " 'Keyword' TEXT NOT NULL, "
                    + " 'Count' INTEGER NOT NULL, "
                    + " 'Timestamp' TEXT NOT NULL)";
            db.execSQL(sql);

            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            LogHelper.e(TAG, "onCreate", e);
        } finally {
            db.endTransaction();
        }
        return false;
    }

    private static boolean upgradeFrom2To3(SQLiteDatabase db) {
        try {
            db.beginTransaction();
            String sql = "CREATE TABLE 'code_scan_history_table' ('_id' INTEGER PRIMARY KEY," + " 'Context' TEXT NOT NULL, "
                    + " 'UserCode' TEXT NOT NULL," + " 'Date' TEXT NOT NULL, " + " 'Time' TEXT NOT NULL)";
            db.execSQL(sql);
            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            LogHelper.e(TAG, "onCreate", e);
        } finally {
            db.endTransaction();
        }
        return false;
    }

    private static synchronized SQLiteDatabase openDatabase(File dataFile) {
        synchronized (dbCountLock) {
            dbCount.incrementAndGet();

            if (dbInst == null || !dbInst.isOpen()) {
                dbInst = SQLiteDatabase.openOrCreateDatabase(dataFile, null);
                LogHelper.d(TAG, "db.open()");
            }
            dbInst.acquireReference();
            return dbInst;
        }
    }

    /**
     * 打开数据库
     *
     * @return SQLiteDatabase
     */
    public static synchronized SQLiteDatabase openDatabase(Context context) {
        File dataFile = getDBFile(context);
        if (dataFile != null) {
            return openDatabase(dataFile);
        }
        return null;
    }

    public static void closeDatabase() {
        synchronized (dbCountLock) {
            if (dbCount.get() == 0) {
                if (dbInst != null && dbInst.isOpen()) {
                    LogHelper.d(TAG, "db.close()");
                    dbInst.close();
                }
                return;
            }
            if (dbCount.decrementAndGet() == 0) {
                // 直接关吧，别等了
                if (dbInst != null && dbInst.isOpen()) {
                    LogHelper.d(TAG, "db.releaseReference()");
                    dbInst.releaseReference();
                }
            }
        }
    }

    /**
     * 判断db是否正常，以后用于扩展。
     *
     * @return
     */
    public static boolean isDBOk(Context context) {
        return (new File(getDBFilePath(context))).exists();
    }

    /**
     * 清除数据库数据
     *
     * @param context
     */
    public static void clearData(Context context) {
        SQLiteDatabase database;
        synchronized (DBUtil.writeLock) {
            try {
                database = openDatabase(context);
                if (database == null) {
                    return;
                }

                DBStatus.codeStatus.clear();
                onCreate(database);
                database.setVersion(VERSION);
            } catch (Exception e) {
                LogHelper.d(TAG, "db.clearData()", e);
            } finally {
                closeDatabase();
            }
        }
    }

    public static final class DBStatus {
        public static Map<String, Long> codeStatus = new HashMap<String, Long>();
        public static final int KEYWORD_MAX_COUNT = 10;
    }
}
