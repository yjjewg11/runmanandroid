package com.company.runman.datacenter.provider;

import android.content.Context;
import android.text.TextUtils;
import com.company.news.form.UserLoginForm;
import com.company.runman.datacenter.database.dao.IMonitorDao;
import com.company.runman.datacenter.database.dao.impl.MonitorStatsAlertRequestDaoImpl;
import com.company.runman.datacenter.model.AccountEntity;
import com.company.runman.datacenter.model.ConfigEntity;
import com.company.runman.net.HttpReturn;
import com.company.runman.net.interfaces.IResponse;
import com.company.runman.net.request.*;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 分拣数据中心,包括网络数据,本地数据等
 * Created by Edison on 2014/6/6.
 */
public class CategoryDataProvider {

    private static CategoryDataProvider categoryDataProvider;

    private CategoryDataProvider() {
    }

    public static synchronized CategoryDataProvider getInstance() {
        if (null == categoryDataProvider) {
            categoryDataProvider = new CategoryDataProvider();
        }
        return categoryDataProvider;
    }

    /**
     * 注销
     *
     * @param context
     * @return
     */
    public Object loginOut(Context context) {
        HttpReturn httpReturn = HttpDataProvider.getInstance().loginOut(context);
        LoginRequest.LoginResponse response = new LoginRequest.LoginResponse();
        return response.parseFrom(httpReturn);
    }

    /**
     * 登陆
     *
     * @param accountEntity
     * @param context
     * @return
     */
    public Object login(UserLoginForm accountEntity, Context context) {
        HttpReturn httpReturn = HttpDataProvider.getInstance().login(accountEntity, context);
        IResponse response = new IResponse();
        return response.parseFrom(httpReturn);
    }


    /**
     * 获取消息列表
     *
     * @param context
     * @return
     */
    public Object getMessageList(Context context) {
        HttpReturn httpReturn = HttpDataProvider.getInstance().getMessageList(context);
        MessageRequest.MessageResponse response = new MessageRequest.MessageResponse();
        return response.parseFrom(httpReturn);
    }

    /**
     * 标记已读消息
     *
     * @param context
     * @param id
     * @return
     */
    public Object readMessage(Context context, Object id) {
        HttpReturn httpReturn = HttpDataProvider.getInstance().readMessage(context, id);
        MessageReadRequest.MessageReadResponse response = new MessageReadRequest.MessageReadResponse();
        return response.parseFrom(httpReturn);
    }

    /**
     * 获取未读消息数量
     *
     * @param context
     * @return
     */
    public Object getUnReadMessageCount(Context context) {
        HttpReturn httpReturn = HttpDataProvider.getInstance().getUnReadMessageCount(context);
        MessageUnReadRequest.MessageResponse response = new MessageUnReadRequest.MessageResponse();
        return response.parseFrom(httpReturn);
    }

    /**
     * 获得监控的所有电视台
     *
     * @param context
     * @return
     */
    public Object getMonitorStationList(Context context) {
        HttpReturn httpReturn = HttpDataProvider.getInstance().getMonitorStationList(context);
        MonitorStationRequest.MonitorStationResponse response = new MonitorStationRequest.MonitorStationResponse();
        return response.parseFrom(httpReturn);
    }

    /**
     * 获取某一个电视台的所有设备列表
     *
     * @param context
     * @param stationKey
     * @return
     */
    public Object getMonitorHostList(Context context, Object stationKey) {
        HttpReturn httpReturn = HttpDataProvider.getInstance().getMonitorHostList(context, stationKey);
        MonitorHostRequest.MonitorHostResponse response = new MonitorHostRequest.MonitorHostResponse();
        return response.parseFrom(httpReturn);
    }

    /**
     * 获得某一台设备的详细信息
     *
     * @param context
     * @param hostKey
     * @return
     */
    public Object getMonitorHostDetail(Context context, Object hostKey) {
        HttpReturn httpReturn = HttpDataProvider.getInstance().getMonitorHostDetail(context, hostKey);
        MonitorHostDetailRequest.MonitorHostDetailResponse response = new MonitorHostDetailRequest.MonitorHostDetailResponse();
        return response.parseFrom(httpReturn);
    }

    /**
     * 获得报警信息列表
     *
     * @param context
     * @param station
     * @return
     */
    public Object getMonitorEventList(Context context, Object station) {
        HttpReturn httpReturn = HttpDataProvider.getInstance().getMonitorEventList(context, station);
        MonitorNewEventRequest.MonitorNewEventResponse response = new MonitorNewEventRequest.MonitorNewEventResponse();
        return response.parseFrom(httpReturn);
    }

    /**
     * 获取某一个电视台所有设备的状态
     *
     * @param context
     * @param stationCode
     * @return
     */
    public Object getMonitorHostStatus(Context context, Object stationCode) {
        HttpReturn httpReturn = HttpDataProvider.getInstance().getMonitorHostStatus(context, stationCode);
        MonitorHostStatsRequest.MonitorStationStatsResponse response = new MonitorHostStatsRequest.MonitorStationStatsResponse();
        return response.parseFrom(httpReturn);
    }

    /**
     * 获得监控事件摘要
     *
     * @param context
     * @param monitorStatsEventRequestEntity
     * @return
     */
    public Object getMonitorSummary(Context context, Object monitorStatsEventRequestEntity) {
        HttpReturn httpReturn = HttpDataProvider.getInstance().getMonitorEventSummary(context, monitorStatsEventRequestEntity);
        MonitorEventSummaryRequest.MonitorEventSummaryResponse response = new MonitorEventSummaryRequest.MonitorEventSummaryResponse();
        return response.parseFrom(httpReturn);
    }

    /**
     * 监控事件查询
     *
     * @param context
     * @param monitorQueryEventRequestEntity
     * @return
     */
    public Object getMonitorQueryEvent(Context context, Object monitorQueryEventRequestEntity) {
        HttpReturn httpReturn = HttpDataProvider.getInstance().getMonitorQueryEvent(context, monitorQueryEventRequestEntity);
        MonitorQueryEventRequest.MonitorQueryEventResponse response = new MonitorQueryEventRequest.MonitorQueryEventResponse();
        return response.parseFrom(httpReturn);
    }

    /**
     * 监控事件统计
     *
     * @param context
     * @param monitorStatsEventRequestEntity
     * @return
     */
    public Object getMonitorStatsEvent(Context context, Object monitorStatsEventRequestEntity) {
        HttpReturn httpReturn = HttpDataProvider.getInstance().getMonitorStatsEvent(context, monitorStatsEventRequestEntity);
        MonitorStatsEventRequest.MonitorStatsEventResponse response = new MonitorStatsEventRequest.MonitorStatsEventResponse();
        return response.parseFrom(httpReturn);
    }

    /**
     * 监控事件统计
     *
     * @param context
     * @param monitorStatsEventRequestEntity
     * @return
     */
    public Object getMonitorStatsList(Context context, Object monitorStatsEventRequestEntity) {
        HttpReturn httpReturn = HttpDataProvider.getInstance().getMonitorStatsList(context, monitorStatsEventRequestEntity);
        MonitorStatsListRequest.MonitorStatsListResponse response = new MonitorStatsListRequest.MonitorStatsListResponse();
        return response.parseFrom(httpReturn);
    }

    /**
     * 获得设备分组
     *
     * @param context
     * @param stationKey
     * @return
     */
    public Object getMonitorGroupHost(Context context, String stationKey) {
        HttpReturn httpReturn = HttpDataProvider.getInstance().getMonitorGroup(context, stationKey);
        MonitorGroupRequest.MonitorGroupResponse response = new MonitorGroupRequest.MonitorGroupResponse();
        return response.parseFrom(httpReturn);
    }

    /**
     * 升级检查
     *
     * @param context
     * @param requestEntity
     * @return
     */
    public Object checkVersion(Context context, Object requestEntity) {
        HttpReturn httpReturn = HttpDataProvider.getInstance().checkVersion(context, requestEntity);
        CheckUpgradeRequest.CheckUpgradeResponse response = new CheckUpgradeRequest.CheckUpgradeResponse();
        return response.parseFrom(httpReturn);
    }

    /**
     * 注册
     *
     * @param context
     * @param requestEntity
     * @return
     */
    public Object register(Context context, Object requestEntity) {
        HttpReturn httpReturn = HttpDataProvider.getInstance().register(context, requestEntity);
        return new IResponse().parseFrom(httpReturn);
    }

    /**
     * 意见反馈
     *
     * @param context
     * @param contentValues
     * @return
     */
    public Object feedBack(Context context, Object contentValues) {
        HttpReturn httpReturn = HttpDataProvider.getInstance().feedBack(context, contentValues);
        FeedBackRequest.FeedBackResponse response = new FeedBackRequest.FeedBackResponse();
        return response.parseFrom(httpReturn);
    }

    /**
     * 获取用户配置信息
     *
     * @param context
     * @return
     */
    public Object requestUserSetting(Context context) {
        HttpReturn httpReturn = HttpDataProvider.getInstance().requestUserSetting(context);
        UserSettingRequest.UserSettingResponse response = new UserSettingRequest.UserSettingResponse();
        return response.parseFrom(httpReturn);
    }

    /**
     * 保存用户配置信息
     *
     * @param context
     * @param contentValues
     * @return
     */
    public Object saveUserSetting(Context context, Object contentValues) {
        HttpReturn httpReturn = HttpDataProvider.getInstance().saveUserSetting(context, contentValues);
        UserSettingSaveRequest.UserSettingSaveResponse response = new UserSettingSaveRequest.UserSettingSaveResponse();
        return response.parseFrom(httpReturn);
    }
}
