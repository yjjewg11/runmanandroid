package com.company.runman.datacenter.provider;

import android.content.Context;
import com.company.news.form.UserLoginForm;
import com.company.runman.datacenter.model.AccountEntity;
import com.company.runman.net.HttpControl;
import com.company.runman.net.HttpReturn;
import com.company.runman.net.request.*;

/**
 * 网络数据返回中心
 * Created by Edison on 2014/6/6.
 */
public class HttpDataProvider {

    private static HttpDataProvider httpDataProvider;

    private HttpDataProvider() {
    }

    public static synchronized HttpDataProvider getInstance() {
        if (null == httpDataProvider) {
            httpDataProvider = new HttpDataProvider();
        }
        return httpDataProvider;
    }

    /**
     * 登陆
     *
     * @param accountEntity
     * @param context
     * @return
     */
    public HttpReturn login(UserLoginForm accountEntity, Context context) {
        LoginRequest request = new LoginRequest(context);
        request.setData(accountEntity);
        return HttpControl.execute(request, context);
    }

    /**
     * 注销
     *
     * @param context
     * @return
     */
    public HttpReturn loginOut(Context context) {
        LoginOutRequest request = new LoginOutRequest(context);
        return HttpControl.execute(request, context);
    }

    /**
     * 验证token
     *
     * @param context
     * @return
     */
    public HttpReturn checkToken(Context context) {
        CheckTokenRequest request = new CheckTokenRequest(context);
        return HttpControl.execute(request, context);
    }

    /**
     * 获取openfire注册信息
     *
     * @param context
     * @return
     */
    public HttpReturn getOpenFireUser(Context context) {
        OpenFireUserRequest request = new OpenFireUserRequest(context);
        return HttpControl.execute(request, context);
    }

    /**
     * 获取公共配置
     *
     * @param context
     * @return
     */
    public HttpReturn getConfig(Context context, Object md5) {
        ConfigRequest request = new ConfigRequest(context);
        request.setData(md5);
        return HttpControl.execute(request, context);
    }

    /**
     * 获取所有消息
     *
     * @param context
     * @return
     */
    public HttpReturn getMessageList(Context context) {
        MessageRequest request = new MessageRequest(context);
        return HttpControl.execute(request, context);
    }

    /**
     * 阅读消息
     *
     * @param context
     * @param id
     * @return
     */
    public HttpReturn readMessage(Context context, Object id) {
        MessageReadRequest request = new MessageReadRequest(context);
        request.setData(id);
        return HttpControl.execute(request, context);
    }

    /**
     * 获取未读消息个数
     *
     * @param context
     * @return
     */
    public HttpReturn getUnReadMessageCount(Context context) {
        MessageUnReadRequest request = new MessageUnReadRequest(context);
        return HttpControl.execute(request, context);
    }

    /**
     * 获得监控的所有电视台
     *
     * @param context
     * @return
     */
    public HttpReturn getMonitorStationList(Context context) {
        MonitorStationRequest request = new MonitorStationRequest(context);
        return HttpControl.execute(request, context);
    }

    /**
     * 获取某一个电视台的所有设备列表
     *
     * @param context
     * @param station
     * @return
     */
    public HttpReturn getMonitorHostList(Context context, Object station) {
        MonitorHostRequest request = new MonitorHostRequest(context);
        request.setData(station);
        return HttpControl.execute(request, context);
    }

    /**
     * 获取某一个电视台所有设备的状态
     *
     * @param context
     * @param stationCode
     * @return
     */
    public HttpReturn getMonitorHostStatus(Context context, Object stationCode) {
        MonitorHostStatsRequest request = new MonitorHostStatsRequest(context);
        request.setData(stationCode);
        return HttpControl.execute(request, context);
    }

    /**
     * 获得某一台设备的详细信息
     *
     * @param context
     * @param hostKey
     * @return
     */
    public HttpReturn getMonitorHostDetail(Context context, Object hostKey) {
        MonitorHostDetailRequest request = new MonitorHostDetailRequest(context);
        request.setData(hostKey);
        return HttpControl.execute(request, context);
    }

    /**
     * 获得报警信息列表
     *
     * @param context
     * @param station
     * @return
     */
    public HttpReturn getMonitorEventList(Context context, Object station) {
        MonitorNewEventRequest request = new MonitorNewEventRequest(context);
        request.setData(station);
        return HttpControl.execute(request, context);
    }

    /**
     * 获得监控事件摘要
     *
     * @param context
     * @param monitorStatsEventRequestEntity
     * @return
     */
    public HttpReturn getMonitorEventSummary(Context context, Object monitorStatsEventRequestEntity) {
        MonitorEventSummaryRequest request = new MonitorEventSummaryRequest(context);
        request.setData(monitorStatsEventRequestEntity);
        return HttpControl.execute(request, context);
    }

    /**
     * 监控事件查询
     *
     * @param context
     * @param monitorQueryEventRequestEntity
     * @return
     */
    public HttpReturn getMonitorQueryEvent(Context context, Object monitorQueryEventRequestEntity) {
        MonitorQueryEventRequest request = new MonitorQueryEventRequest(context);
        request.setData(monitorQueryEventRequestEntity);
        return HttpControl.execute(request, context);
    }

    /**
     * 监控事件统计
     *
     * @param context
     * @param monitorStatsEventRequestEntity
     * @return
     */
    public HttpReturn getMonitorStatsEvent(Context context, Object monitorStatsEventRequestEntity) {
        MonitorStatsEventRequest request = new MonitorStatsEventRequest(context);
        request.setData(monitorStatsEventRequestEntity);
        return HttpControl.execute(request, context);
    }

    /**
     * 监控事件统计
     *
     * @param context
     * @param monitorStatsEventRequestEntity
     * @return
     */
    public HttpReturn getMonitorStatsList(Context context, Object monitorStatsEventRequestEntity) {
        MonitorStatsListRequest request = new MonitorStatsListRequest(context);
        request.setData(monitorStatsEventRequestEntity);
        return HttpControl.execute(request, context);
    }

    /**
     * 获取电视台分组设备
     *
     * @param context
     * @param stationKey
     * @return
     */
    public HttpReturn getMonitorGroup(Context context, String stationKey) {
        MonitorGroupRequest request = new MonitorGroupRequest(context);
        request.setData(stationKey);
        return HttpControl.execute(request, context);
    }

    /**
     * 获取版本更新信息
     *
     * @param context
     * @param requestEntity
     * @return
     */
    public HttpReturn checkVersion(Context context, Object requestEntity) {
        CheckUpgradeRequest request = new CheckUpgradeRequest(context);
        request.setData(requestEntity);
        return HttpControl.execute(request, context);
    }

    /**
     * 注册
     *
     * @param context
     * @param requestEntity
     * @return
     */
    public HttpReturn register(Context context, Object requestEntity) {
        RegisterRequest request = new RegisterRequest(context);
        request.setData(requestEntity);
        return HttpControl.execute(request, context);
    }

    /**
     * 意见反馈
     *
     * @param context
     * @param contentValues
     * @return
     */
    public HttpReturn feedBack(Context context, Object contentValues) {
        FeedBackRequest request = new FeedBackRequest(context);
        request.setData(contentValues);
        return HttpControl.execute(request, context);
    }

    /**
     * 获取用户配置信息
     *
     * @param context
     * @return
     */
    public HttpReturn requestUserSetting(Context context) {
        UserSettingRequest request = new UserSettingRequest(context);
        return HttpControl.execute(request, context);
    }

    /**
     * 保存用户配置信息
     *
     * @param context
     * @param contentValues
     * @return
     */
    public HttpReturn saveUserSetting(Context context, Object contentValues) {
        UserSettingSaveRequest request = new UserSettingSaveRequest(context);
        request.setData(contentValues);
        return HttpControl.execute(request, context);
    }
}
