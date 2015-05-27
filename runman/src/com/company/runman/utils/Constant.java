package com.company.runman.utils;

/**
 * Created by Edison on 2014/6/5.
 */
public class Constant {
    public static final String BAIDU_API_KEY = "e05rICXOFayEpRYsWq4Xmki4";
    public static final String WEIXIN_API_KEY = "";

    public static final int MESSAGE_NOTIFY = 10001;
    public static final int MONITOR_NOTIFY = 10002;
    public static final String DEFAULT_ENCODEING = "UTF-8";
    public static final String PRIVATE_DATA_PATH = "/data/runman/";
    public static final String HTTP_POST_TYPE_JSON = "HTTP_POST_TYPE_JSON";
    public static final String HTTP_POST_TYPE_JSON_DATA = "HTTP_POST_TYPE_JSON_DATA";

    public static final class Action {
        public static final String EXIT_ACTION = "EXIT_ACTION";
    }

    //环境配置
    public static final class Host {
        public static String BASE_HOST = "http://120.25.127.141/runman-rest/";
        //        public static String BASE_HOST = "http://app.sobey.com/CloudMaintain/";
        public static final String LOGIN_HOST_KEY = "LOGIN_HOST_KEY";
        public static final String OPEN_HOST_KEY = "OPEN_HOST_KEY";
        public static final String APP_KEY_KEY = "APP_KEY_KEY";
        public static final String APP_ID_KEY = "APP_ID_KEY";
        public static int PULL_TIME = 30 * 1000;
    }

    public static final class WebViewUrl {
        //public static final String WEB_VIEW_URL_ASK = "http://172.16.134.189/faq/?/publish/";
        //public static final String WEB_VIEW_URL_LIB = "http://172.16.134.189/wiki/";
        public static final String WEB_VIEW_URL_ASK_KEY = "WEB_VIEW_URL_ASK_KEY";
        public static final String WEB_VIEW_URL_ASK_REQUEST = "?/publish/";
        public static final String WEB_VIEW_URL_LIB_KEY = "WEB_VIEW_URL_LIB_KEY";
        public static final String WEB_VIEW_URL_LIB_SEARCH = "?search-fulltext-content-{keyword}--all-0-within-time-desc-1";
        public static final String WEB_VIEW_URL_LIB_SEARCH_EX = "http://kb.sobey.com/index.php?doc-innerlink-";
        public static final String WEB_COOKIE_NAME_KEY = "WEB_COOKIE_NAME_KEY";
        public static final String WEB_COOKIE_DOMAIN_KEY = "WEB_COOKIE_DOMAIN_KEY";
        public static final String WEB_COOKIE_PATH_KEY = "WEB_COOKIE_PATH_KEY";
        public static final String WEB_KDEV_URL_KEY = "WEB_KDEV_URL";
        public static final String WEB_KDOC_URL_KEY = "WEB_KDOC_URL";
    }

    public static final class MessageType {
        public static final String MESSAGE_ACTION = "MESSAGE_ACTION";
        public static final int MESSAGE = 1;
        public static final int NOTICE = 2;
        public static final int MONITOR = 4;
    }

    public static final class ExtraKey {
        public static final String EXTRA_URI_KEY = "uri";
        public static final String EXTRA_KEYWORD = "keyword";
        public static final String EXTRA_MESSAGE = "message";
        public static final String EXTRA_COMMON_KEY = "common_key";
        public static final String EXTRA_COMMON_ARRAY_KEY = "common_array_key";
    }

    public static final class SharePreferenceKey {
        public static final String KEY_PASSWORD_SELECTOR = "KEY_PASSWORD_SELECTOR";//是否保存密码
        public static final String KEY_ACCOUNT = "key_account_name";
        public static final String KEY_JSESSIONID = "JSESSIONID";
        public static final String CONFIG_MD5 = "config_md5";
        public static final String CONFIG_JSON = "config_json";
        public static final String PRE_MONITOR_STATES_TIME = "PRE_MONITOR_STATES_TIME";
        public static final String UN_READ_MESSAGE_COUNT = "UN_READ_MESSAGE_COUNT";
        public static final String UN_READ_MONITOR_COUNT = "UN_READ_MONITOR_COUNT";
        public static final String EDIT_STATION_DATA = "EDIT_STATION_DATA";
        public static final String IS_SHOW_NOTIFY = "IS_SHOW_NOTIFY";
        public static final String Userinfo = "userinfo";
        public static final String Cookies = "Cookies";
    }

    public static final class ActivityRequestCode {
        public static final int STATION_EDIT_REQUEST_CODE = 110001;
    }

    public static final class RequestCode {
        public static final int REQUEST_GET = 0;
        public static final int REQUEST_POST = 1;
        public static final int REQUEST_PUT = 2;
    }

    public static final class ResponseCode {
        public static final int TIMEOUT = -1;
        public static final int UNKNOWN_ERROR = -2;
        public static final int UNKNOWN_HOST = -3;
        public static final int NOT_CONNECTION = -4;
        public static final int SUCCESS = 200;
        public static final int ACCOUNT_ERROR = 403;
        public static final int URL_ERROR = 404;
    }

    /**
     * 设备状态
     */
    public static final class HostStatus {
        public static final String Unknown = "Unknown";//未知
        public static final String Normal = "Normal";//正常
        public static final String Paused = "Paused";//暂停
        public static final String NotStartup = "NotStartup";//离线
        public static final String Abnormal = "Abnormal";//异常
    }

    /**
     * 事件类型
     */
    public static final class EventType {
        public static final String Unknown = "Unknown";//未知
        public static final String Good = "Good";//修复通知
        public static final String Info = "Info";//通知
        public static final String Caution = "Caution";//注意
        public static final String Warnning = "Warnning";//警告
        public static final String Error = "Error";//错误
        public static final String Failure = "Failure";//失败
        public static final String NotProcessing = "NotProcessing";//未处理
        public static final String Ignore = "Ignore";//忽略
        public static final String Processed = "Processed";//已处理
    }

    /**
     * 消息统计取值
     */
    public static final class EventStatsType {
        public static final String Station = "station";//按电视台汇总
        public static final String Kit = "kit";//按设备汇总
    }

    public static final class ResponseData {
        public static final String STATUS = "status";
        public static final String STATUS_SUCCESS = "success";
        public static final String STATUS_FAIL = "fail";
        public static final String MESSAGE = "message";
        public static final String DATA = "data";
        public static final String ResMsg = "ResMsg";
    }
    public static final class Query {
        static public  Integer Operate_Refresh=0;//刷新操作
        static public  Integer Operate_LoadMore=1;//加载更多操作
    }

}
