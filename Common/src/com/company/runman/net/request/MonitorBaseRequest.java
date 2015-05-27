package com.company.runman.net.request;

import android.content.Context;
import android.text.TextUtils;
import com.company.runman.datacenter.model.*;
import com.company.runman.datacenter.provider.SharePreferenceProvider;
import com.company.runman.net.HttpReturn;
import com.company.runman.net.interfaces.IRequest;
import com.company.runman.net.interfaces.IResponse;
import com.company.runman.utils.Constant;
import com.company.runman.utils.LogHelper;
import com.company.runman.utils.Tool;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

/**
 * Created by EdisonZhao on 14-8-6.
 * Email:zhaoliangyu@sobey.com
 * 基础数据构造基类
 */
public class MonitorBaseRequest extends IRequest {

    private String baseUrl = "";
    protected Map<String, Object> params = new HashMap<String, Object>();
    private Context context;
    protected String openHost;

    public MonitorBaseRequest(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public void setData(Object obj) {
    }

    @Override
    public String getUrl() {
        return "";
    }

    @Override
    public String getPost() {
        return null;
    }

    @Override
    public int getHttpMode() {
        return Constant.RequestCode.REQUEST_GET;
    }

    public final String createBaseUrl() {
        return createBaseUrl(new HashMap<String, Object>());
    }

    public final String createBaseUrl(Map<String, Object> map) {
        StringBuffer source = new StringBuffer();
        if (!map.isEmpty()) {
            Set<String> keySet = map.keySet();
            for (String key : keySet) {
                if (null != map.get(key)) {
                    if (map.get(key) instanceof Long) {
                        if (Long.valueOf(map.get(key).toString()) <= 0) {
                            continue;
                        }
                    }
                    //组装参数
                    source.append("&").append(key).append("=").append(map.get(key));
                }
            }
        }

        if (TextUtils.isEmpty(baseUrl)) {
            String appKey = SharePreferenceProvider.getInstance(context).readData(Constant.Host.APP_KEY_KEY);
            String appId = SharePreferenceProvider.getInstance(context).readData(Constant.Host.APP_ID_KEY);
            if (!TextUtils.isEmpty(appKey) && !TextUtils.isEmpty(appId)) {
                StringBuffer sb = new StringBuffer();
                final long timestamp = System.currentTimeMillis();
                //先构造基本数据
                sb.append("type=json")
                        .append("&app=").append(appId)
                        .append("&timestamp=").append(timestamp)
                        .toString();
                if (!TextUtils.isEmpty(source.toString())) {
                    //添加额外参数
                    sb.append(source);
                }
                String md5Source = sb.toString();
                //md5数据源加上AppKey
                String sign = Tool.getMD5(appKey + md5Source);
                //请求地址加上签名
                baseUrl = (md5Source + "&sign=" + sign);
                LogHelper.d(TAG, "md5Source:   " + md5Source);
                LogHelper.d(TAG, "baseUrl:   " + baseUrl);
            }
        }
        //获取host地址
        openHost = SharePreferenceProvider.getInstance(context).readData(Constant.Host.OPEN_HOST_KEY);
        return baseUrl;
    }

    public static class MonitorBaseResponse extends IResponse {

        @Override
        public Object parseFrom(HttpReturn httpReturn) {
            return super.parseFrom(httpReturn);
        }

        /**
         * 解析设备信息
         *
         * @param jsonObject
         */
        public MonitorHostEntity parseMonitorHost(JSONObject jsonObject) {
            MonitorHostEntity monitorHostEntity = new MonitorHostEntity();
            MonitorHostSystemInfoEntity systemInfoEntity = new MonitorHostSystemInfoEntity();
            MonitorHostLoadInfoEntity loadInfoEntity = new MonitorHostLoadInfoEntity();
            MonitorHostAddressInfoEntity addressInfoEntity = new MonitorHostAddressInfoEntity();
            List<MonitorHostAttributesEntity> attributesEntityList = new ArrayList<MonitorHostAttributesEntity>();
            List<MonitorHostDiskInfoEntity> diskInfoEntityList = new ArrayList<MonitorHostDiskInfoEntity>();

            if (null != jsonObject) {
                //解析基础设备信息
                String hostKey = jsonObject.optString("HostKey");//设备唯一标识
                String hostName = jsonObject.optString("HostName");//设备名称
                String interIPAddress = jsonObject.optString("InterIPAddress");//内网IP
                String stationCode = jsonObject.optString("StationCode");//电视台编码
                String status = jsonObject.optString("Status");//设备状态
                String remoteIP = jsonObject.optString("RemoteIP");//外网IP地址

                //解析系统信息
                JSONObject systemInfoJson = jsonObject.optJSONObject("SystemInfo");
                if (systemInfoJson != null) {
                    JSONArray iPAddressArray = systemInfoJson.optJSONArray("IPAddress");//ip列表
                    List<String> ipAddressList = new ArrayList<String>();
                    if (iPAddressArray != null) {
                        for (int i = 0; i < iPAddressArray.length(); i++) {
                            ipAddressList.add(iPAddressArray.optString(i));
                        }
                    }

                    String operationSystem = systemInfoJson.optString("OperationSystem");//操作系统
                    String operationVersion = systemInfoJson.optString("OperationVersion");//操作系统版本
                    String platform = systemInfoJson.optString("Platform");//操作系统平台
                    String servicePackage = systemInfoJson.optString("ServicePackage");//操作系统补丁包
                    String netVersion = systemInfoJson.optString("NetVersion");//.NET Framework 框架版本
                    String isX64 = systemInfoJson.optString("IsX64");//是否为64位操作系统
                    String manufacturer = systemInfoJson.optString("Manufacturer");//生产厂商
                    String model = systemInfoJson.optString("Model");//设备型号
                    String cPUSpeed = systemInfoJson.optString("CPUSpeed");//CPU速度，单位MHZ
                    String cPUCount = systemInfoJson.optString("CPUCount");//CPU内核数
                    String totalMemorySize = systemInfoJson.optString("TotalMemorySize");//物理内存，单位byte
                    String totalStorageSize = systemInfoJson.optString("TotalStorageSize");//物理存储，单位byte
                    String totalFreeStorageSpace = systemInfoJson.optString("totalFreeStorageSpace");//剩余空间，单位byte
                    JSONArray diskArray = systemInfoJson.optJSONArray("DiskList");//磁盘列表
                    if (diskArray != null) {
                        for (int i = 0; i < diskArray.length(); i++) {
                            JSONObject diskInfoObject = diskArray.optJSONObject(i);
                            MonitorHostDiskInfoEntity diskInfoEntity = new MonitorHostDiskInfoEntity();
                            String diskName = diskInfoObject.optString("DiskName");//磁盘名称
                            String totalSize = diskInfoObject.optString("TotalSize");//容量，单位byte
                            String freeSpace = diskInfoObject.optString("FreeSpace");//剩余空间，单位byte
                            String usedSpace = diskInfoObject.optString("UsedSpace");//已用空间，单位byte
                            diskInfoEntity.setDiskName(diskName);
                            diskInfoEntity.setTotalSize(totalSize);
                            diskInfoEntity.setFreeSpace(freeSpace);
                            diskInfoEntity.setUsedSpace(usedSpace);
                            diskInfoEntityList.add(diskInfoEntity);
                        }
                    }

                    systemInfoEntity.setiPAddress(ipAddressList);
                    systemInfoEntity.setOperationSystem(operationSystem);
                    systemInfoEntity.setOperationVersion(operationVersion);
                    systemInfoEntity.setPlatform(platform);
                    systemInfoEntity.setServicePackage(servicePackage);
                    systemInfoEntity.setNetVersion(netVersion);
                    systemInfoEntity.setIsX64(isX64);
                    systemInfoEntity.setManufacturer(manufacturer);
                    systemInfoEntity.setModel(model);
                    systemInfoEntity.setcPUSpeed(cPUSpeed);
                    systemInfoEntity.setcPUCount(cPUCount);
                    systemInfoEntity.setTotalMemorySize(totalMemorySize);
                    systemInfoEntity.setTotalStorageSize(totalStorageSize);
                    systemInfoEntity.setTotalFreeStorageSpace(totalFreeStorageSpace);
                    systemInfoEntity.setDiskList(diskInfoEntityList);
                    //解析负载信息
                    JSONObject loadInfoJson = jsonObject.optJSONObject("LoadInfo");
                    String cPUUsedPercent = loadInfoJson.optString("CPUUsedPercent");//CPU使用百分比， 0-100
                    String usedMemoryPercent = loadInfoJson.optString("UsedMemoryPercent");//已使用内存百分比， 0-100
                    String usedMemoryBytes = loadInfoJson.optString("UsedMemoryBytes");//已使用内存量，byte
                    String totalUsedStorageSpace = loadInfoJson.optString("TotalUsedStorageSpace");//已使用存储空间，byte
                    String usedStoragePercent = loadInfoJson.optString("UsedStoragePercent");//已使用存储百分比
                    String totalFreeStorageSpaceLoadInfo = loadInfoJson.optString("TotalFreeStorageSpace");//剩余存储百分比
                    String processCount = loadInfoJson.optString("ProcessCount");//当前进程数
                    loadInfoEntity.setcPUUsedPercent(cPUUsedPercent);
                    loadInfoEntity.setUsedMemoryPercent(usedMemoryPercent);
                    loadInfoEntity.setUsedMemoryBytes(usedMemoryBytes);
                    loadInfoEntity.setTotalUsedStorageSpace(totalUsedStorageSpace);
                    loadInfoEntity.setUsedStoragePercent(usedStoragePercent);
                    loadInfoEntity.setTotalFreeStorageSpace(totalFreeStorageSpaceLoadInfo);
                    loadInfoEntity.setProcessCount(processCount);
                    //解析地理位置
                    JSONObject positionJson = jsonObject.optJSONObject("Position");
                    if (positionJson != null) {
                        String x = positionJson.optString("X");//百度地图X坐标
                        String y = positionJson.optString("Y");//百度地图Y坐标
                        String address = positionJson.optString("Address");//详细地址
                        String country = positionJson.optString("Contry");//国家
                        String province = positionJson.optString("Province");//省
                        String city = positionJson.optString("City");//市
                        String district = positionJson.optString("District");//区县
                        String street = positionJson.optString("Street");//街道
                        String streetNumber = positionJson.optString("StreetNumber");//门牌号
                        String lat = positionJson.optString("Lat");//纬度
                        String lng = positionJson.optString("Lng");//经度
                        addressInfoEntity.setX(x);
                        addressInfoEntity.setY(y);
                        addressInfoEntity.setAddress(address);
                        addressInfoEntity.setContry(country);
                        addressInfoEntity.setProvince(province);
                        addressInfoEntity.setCity(city);
                        addressInfoEntity.setDistrict(district);
                        addressInfoEntity.setStreet(street);
                        addressInfoEntity.setStreetNumber(streetNumber);
                        addressInfoEntity.setLat(lat);
                        addressInfoEntity.setLng(lng);
                    }

                    //解析附加属性
                    JSONArray attributesArray = jsonObject.optJSONArray("Attributes");
                    if (attributesArray != null) {
                        for (int i = 0; i < attributesArray.length(); i++) {
                            MonitorHostAttributesEntity attributesEntity = new MonitorHostAttributesEntity();
                            JSONObject attributesObject = attributesArray.optJSONObject(i);
                            String code = attributesObject.optString("Code");
                            String name = attributesObject.optString("Name");
                            String value = attributesObject.optString("Value");
                            attributesEntity.setCode(code);
                            attributesEntity.setName(name);
                            attributesEntity.setValue(value);
                            attributesEntityList.add(attributesEntity);
                        }
                    }
                    //将所有信息组合成Host的数据
                    monitorHostEntity.setHostKey(hostKey);
                    monitorHostEntity.setHostName(hostName);
                    monitorHostEntity.setInterIPAddress(interIPAddress);
                    monitorHostEntity.setStationCode(stationCode);
                    monitorHostEntity.setStatus(status);
                    monitorHostEntity.setSystemInfo(systemInfoEntity);
                    monitorHostEntity.setLoadInfo(loadInfoEntity);
                    monitorHostEntity.setPosition(addressInfoEntity);
                    monitorHostEntity.setAttributes(attributesEntityList);
                    monitorHostEntity.setRemoteIP(remoteIP);
                }
            }
            return monitorHostEntity;
        }
    }

}
