package com.company.runman.datacenter.provider;

import android.content.Context;
import android.content.SharedPreferences;
import com.company.runman.datacenter.model.AccountEntity;
import com.company.runman.utils.Constant;

/**
 * Created by star on 2014/6/10.
 */
public class SharePreferenceProvider {
    public static Context g_context;
    private static SharePreferenceProvider sharePreferenceProvider;
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    private SharePreferenceProvider(Context context) {
        sharedPreferences = context.getSharedPreferences("company.runman", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static SharePreferenceProvider getInstance(Context context) {
        if (sharePreferenceProvider == null) {
            if(context==null&&g_context!=null)context=g_context;
            sharePreferenceProvider = new SharePreferenceProvider(context);
        }
        return sharePreferenceProvider;
    }

    public boolean getPasswordselector() {
        return sharedPreferences.getBoolean(Constant.SharePreferenceKey.KEY_PASSWORD_SELECTOR, false);
    }

    public void savePasswordSelector(boolean isSaveData) {
        editor.putBoolean(Constant.SharePreferenceKey.KEY_PASSWORD_SELECTOR, isSaveData);
        editor.commit();
    }
    public String getCookies() {
        return sharedPreferences.getString(Constant.SharePreferenceKey.Cookies, "");
    }

    public void saveCookies(String isSaveData) {
        editor.putString(Constant.SharePreferenceKey.Cookies, isSaveData);
        editor.commit();
    }


    public String getAccountName() {
        return sharedPreferences.getString(Constant.SharePreferenceKey.KEY_ACCOUNT, "");
    }

    public void saveAccountName(String account) {
        editor.putString(Constant.SharePreferenceKey.KEY_ACCOUNT, account);
        editor.commit();
    }

    public String getPassword(String key) {
        return sharedPreferences.getString(key, "");
    }

    public void savePassword(String key, String password) {
        editor.putString(key, password);
        editor.commit();
    }

    public String getJSESSIONID() {
        return sharedPreferences.getString(Constant.SharePreferenceKey.KEY_JSESSIONID, "");
    }

    public void setJSESSIONID(String JSESSIONID) {
        editor.putString(Constant.SharePreferenceKey.KEY_JSESSIONID, JSESSIONID);
        editor.commit();
    }

    public String getUserinfo() {
        return sharedPreferences.getString(Constant.SharePreferenceKey.Userinfo, "");
    }

    public void setUserinfo(String Userinfo) {
        editor.putString(Constant.SharePreferenceKey.Userinfo, Userinfo);
        editor.commit();
    }

    public String getMD5() {
        return sharedPreferences.getString(Constant.SharePreferenceKey.CONFIG_MD5, "");
    }

    public void setMD5(String md5) {
        editor.putString(Constant.SharePreferenceKey.CONFIG_MD5, md5);
        editor.commit();
    }

    public String getConfigJson() {
        return sharedPreferences.getString(Constant.SharePreferenceKey.CONFIG_JSON, "");
    }

    public void setConfigJson(String configJson) {
        editor.putString(Constant.SharePreferenceKey.CONFIG_JSON, configJson);
        editor.commit();
    }

    public long getPreMonitorStatesTime() {
        return sharedPreferences.getLong(Constant.SharePreferenceKey.PRE_MONITOR_STATES_TIME, 0);
    }

    public void setPreMonitorStatesTime(long preMonitorStatesTime) {
        editor.putLong(Constant.SharePreferenceKey.PRE_MONITOR_STATES_TIME, preMonitorStatesTime);
        editor.commit();
    }

    //推送设置
    public boolean isShowNotify() {
        return sharedPreferences.getBoolean(Constant.SharePreferenceKey.IS_SHOW_NOTIFY, true);
    }

    public void setShowNotify(boolean isShowNotify) {
        editor.putBoolean(Constant.SharePreferenceKey.IS_SHOW_NOTIFY, isShowNotify);
        editor.commit();
    }

    public int getUnReadMessageNumber() {
        return sharedPreferences.getInt(Constant.SharePreferenceKey.UN_READ_MESSAGE_COUNT, 0);
    }

    public void setUnReadMessageNumber(int count) {
        editor.putInt(Constant.SharePreferenceKey.UN_READ_MESSAGE_COUNT, count);
        editor.commit();
    }

    public int getUnReadMonitorNumber(String stationCode) {
        return sharedPreferences.getInt(Constant.SharePreferenceKey.UN_READ_MONITOR_COUNT + "_" + stationCode, 0);
    }

    public void setUnReadMonitorNumber(String stationCode, int count) {
        editor.putInt(Constant.SharePreferenceKey.UN_READ_MONITOR_COUNT + "_" + stationCode, count);
        editor.commit();
    }

    public int getUnReadMonitorAllNumber() {
        return sharedPreferences.getInt(Constant.SharePreferenceKey.UN_READ_MONITOR_COUNT, 0);
    }

    public void setUnReadMonitorAllNumber(int count) {
        editor.putInt(Constant.SharePreferenceKey.UN_READ_MONITOR_COUNT, count);
        editor.commit();
    }

    public String getStationEditData() {
        return sharedPreferences.getString(Constant.SharePreferenceKey.EDIT_STATION_DATA, null);
    }

    /**
     * 存储定制监控的电视台以|相隔
     *
     * @param stationCodeStr
     */
    public void setStationEditData(String stationCodeStr) {
        editor.putString(Constant.SharePreferenceKey.EDIT_STATION_DATA, stationCodeStr);
        editor.commit();
    }

    public void writeData(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public String readData(String key) {
        return sharedPreferences.getString(key, null);
    }

//    public String setConfigJson(String configJson) {
//        if (TextUtils.isEmpty(configJson)) {
//            return "";
//        }
//        String filePath = mContext.getFilesDir() + File.separator + Constant.SharePreferenceKey.CONFIG_JSON_FILE_NAME;
//        File tmpFile = new File(filePath);
//        if (tmpFile.exists()) {
//            tmpFile.delete();
//        }
//
//        FileOutputStream fos = null;
//        BufferedOutputStream buffered = null;
//
//        try {
//            fos = new FileOutputStream(filePath);
//            buffered = new BufferedOutputStream(fos);
//            buffered.write(configJson.getBytes());
//            buffered.flush();
//            return filePath;
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (fos != null) {
//                try {
//                    fos.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            if (buffered != null) {
//                try {
//                    buffered.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return null;
//    }
}
