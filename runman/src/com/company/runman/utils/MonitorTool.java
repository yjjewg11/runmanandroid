package com.company.runman.utils;

import android.content.ContentValues;
import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;
import com.company.runman.R;
import com.company.runman.datacenter.model.MonitorStationEntity;
import com.company.runman.datacenter.provider.CategoryDataProvider;
import com.company.runman.datacenter.provider.SharePreferenceProvider;
import org.apache.http.client.utils.URLEncodedUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EdisonZhao on 14-8-29.
 * Email:zhaoliangyu@sobey.com
 */
public class MonitorTool {

    /**
     * 同步服务器,保存订阅的电视台数据到服务端
     */
    public synchronized static void saveEditStationByHttp(final Context context) {
        SobeyHandlerCenter.getBusinessHandler().post(new Runnable() {
            @Override
            public void run() {
                try {
                    ContentValues contentValues = new ContentValues();
                    List<MonitorStationEntity> list = getEditStation(context);
                    JSONArray jsonArray = new JSONArray();
                    for (MonitorStationEntity monitor : list) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("StationCode", monitor.getStationCode());
                        jsonArray.put(jsonObject);
                    }
                    JSONObject data = new JSONObject();
                    data.put("stationList", jsonArray.toString());
                    contentValues.put(Constant.ResponseData.DATA, data.toString());
                    CategoryDataProvider.getInstance().saveUserSetting(context, contentValues);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 保存订阅的电视台到本地
     *
     * @param list
     * @param context
     */
    public synchronized static void saveEditStationByLocal(List<MonitorStationEntity> list, Context context) {
        //编辑监控电视台顺序
        StringBuilder stringBuilder = new StringBuilder();
        for (MonitorStationEntity station : list) {
            if (station.isChecked()) {
                String stationCode = station.getStationCode();
                stringBuilder.append(stationCode).append("|");
            }
        }
        //将编辑后的电视台存起来
        SharePreferenceProvider.getInstance(context).setStationEditData(stringBuilder.toString());
    }

    /**
     * 获取并且过滤订阅的电视台
     *
     * @param context
     */
    public synchronized static List<MonitorStationEntity> getEditStation(Context context) {
        //将保存的定制列表取出来
        String editStation = SharePreferenceProvider.getInstance(context).getStationEditData();
        List<MonitorStationEntity> list = new ArrayList<MonitorStationEntity>();
        if (!TextUtils.isEmpty(editStation)) {
            String[] array = editStation.split("\\|");
            if (!TextUtils.isEmpty(editStation) && array.length > 0) {
                for (int i = 0; i < array.length; i++) {
                    MonitorStationEntity monitorStationEntity = new MonitorStationEntity();
                    monitorStationEntity.setStationCode(array[i]);
                    list.add(monitorStationEntity);
                }
            }
        }
        return list;
    }

    /**
     * 获取并且过滤订阅的电视台
     *
     * @param sourceList
     * @param context
     */
    public synchronized static List<MonitorStationEntity> filterStation(List<MonitorStationEntity> sourceList, Context context) {
        //将保存的定制列表取出来
        String editStation = SharePreferenceProvider.getInstance(context).getStationEditData();
        List<MonitorStationEntity> list = new ArrayList<MonitorStationEntity>();
        if (!TextUtils.isEmpty(editStation)) {
            String[] array = editStation.split("\\|");
            if (!TextUtils.isEmpty(editStation) && array.length > 0) {
                for (MonitorStationEntity station : sourceList) {
                    //先重置为非选中
                    station.setChecked(false);
                    for (String code:array) {
                        if (TextUtils.equals(code, station.getStationCode())) {
                            //如果属于定制列表里的电视台,就选中该电视台
                            station.setChecked(true);
                            break;
                        }
                    }
                    list.add(station);
                }
            }
        } else {
            //如果订阅为空,则重置
            for (MonitorStationEntity station : sourceList) {
                //重置为非选中
                station.setChecked(false);
            }
            list.addAll(sourceList);
        }
        return list;
    }

    public static String parseLevel(String key) {
        String level;
        if (TextUtils.equals(key, Constant.EventType.Warnning)) {
            level = "报警";
        } else if (TextUtils.equals(key, Constant.EventType.Failure)) {
            level = "失败";
        } else if (TextUtils.equals(key, Constant.EventType.Error)) {
            level = "错误";
        } else if (TextUtils.equals(key, Constant.EventType.Caution)) {
            level = "注意";
        } else if (TextUtils.equals(key, Constant.EventType.Info)) {
            level = "通知";
        } else if (TextUtils.equals(key, Constant.EventType.Ignore)) {
            level = "忽略";
        } else if (TextUtils.equals(key, Constant.EventType.Processed)) {
            level = "已处理";
        } else if (TextUtils.equals(key, Constant.EventType.NotProcessing)) {
            level = "未处理";
        } else if (TextUtils.equals(key, Constant.EventType.Good)) {
            level = "修复通知";
        } else if (TextUtils.equals(key, Constant.EventType.Unknown)) {
            level = "未知";
        } else {
            level = key;
        }
        return level;
    }

    public static String parseProcessResult(String key) {
        String result;
        if (TextUtils.equals(key, Constant.EventType.NotProcessing)) {
            result = "未处理";
        } else if (TextUtils.equals(key, Constant.EventType.Processed)) {
            result = "已处理";
        } else {
            result = "未知";
        }
        return result;
    }

    public static String parseHostStatus(String key) {
        String result;
        if (TextUtils.equals(key, Constant.HostStatus.Unknown)) {
            result = "未知";
        } else if (TextUtils.equals(key, Constant.HostStatus.Normal)) {
            result = "在线";
        } else if (TextUtils.equals(key, Constant.HostStatus.NotStartup)) {
            result = "离线";
        } else if (TextUtils.equals(key, Constant.HostStatus.Abnormal)) {
            result = "异常";
        } else if (TextUtils.equals(key, Constant.HostStatus.Paused)) {
            result = "暂停";
        } else {
            result = "未知";
        }
        return result;
    }

    public static String parseHostStatusByTextView(String key, TextView textView) {
        String result;
        if (TextUtils.equals(key, Constant.HostStatus.Unknown)) {
            result = "未知";
        } else if (TextUtils.equals(key, Constant.HostStatus.Normal)) {
            result = "在线";
            textView.setTextColor(textView.getResources().getColor(R.color.monitor_status_green));
        } else if (TextUtils.equals(key, Constant.HostStatus.NotStartup)) {
            result = "离线";
            textView.setTextColor(textView.getResources().getColor(R.color.monitor_status_gray));
        } else if (TextUtils.equals(key, Constant.HostStatus.Abnormal)) {
            result = "报警";
            textView.setTextColor(textView.getResources().getColor(R.color.monitor_status_red));
        } else if (TextUtils.equals(key, Constant.HostStatus.Paused)) {
            result = "暂停";
        } else {
            result = "未知";
        }
        return result;
    }
}
