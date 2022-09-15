package com.sparrow.orm.po;

import com.sparrow.protocol.ClientInformation;
import com.sparrow.protocol.MethodOrder;
import com.sparrow.protocol.POJO;
import com.sparrow.protocol.dao.SplitTable;
import com.sparrow.protocol.dao.Split;
import com.sparrow.protocol.dao.enums.TableSplitStrategy;
import com.sparrow.protocol.enums.Platform;

import javax.persistence.*;

@Table(name = "event", uniqueConstraints =
        {
                @UniqueConstraint(columnNames = {"index2 `ix_device_device_model`", "device", "device_model"}),
                @UniqueConstraint(columnNames = {"index2 `ix_website_business_id`", "website", "user_id", "user_type", "business_type", "business_id"})
        }, schema = "sparrow"
)
@Split(table_bucket_count = 2)
public class Event implements POJO {
    public Event() {
    }

    public Event(
            Long userId,
            String userType,
            Long businessId,
            String businessType,
            String event,
            String status,
            Long createTime,
            String content,
            ClientInformation client) {
        this.userId = userId;
        this.businessId = businessId;
        this.businessType = businessType;
        this.event = event;
        this.status = status;
        this.createTime = createTime;
        this.updateTime = createTime;
        this.ip = client.getIp();
        this.times = 1;
        this.content = content;
        this.website = client.getWebsite();
        this.channel = client.getChannel();
        if (this.channel == null) {
            this.channel = "";
        }
        this.userType = userType;
        //ip if pc
        this.deviceId = client.getDeviceId();
        this.platform = client.getPlatform();
        this.deviceModel = client.getDeviceModel();

        this.simulate = client.getSimulate();
        this.appId = client.getAppId();
        this.appVersion = client.getAppVersion();
        this.bssid = client.getBssid();
        this.imei = client.getImei();
        this.idfa = client.getIdfa();
        this.clientVersion = client.getClientVersion();
        this.userAgent = client.getUserAgent();
        this.os = client.getOs();
        this.network = client.getNetwork();
        this.device = client.getDevice();
        this.ssid = client.getSsid();
        if (Platform.PC.equals(this.platform)) {
            this.simulate = true;
            this.appId = 0;
            this.appVersion = 0.0F;
            this.bssid = "";
            this.imei = "";
            this.idfa = "";
            this.clientVersion = "";
            this.platform = Platform.PC;
            this.deviceModel = "";
            this.ssid = "";
            this.network = "";
        }
    }

    /**
     * 自增id
     */
    private Long eventId;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 用户类型
     */
    private String userType;
    /**
     * 业务id
     */
    private Long businessId;
    /**
     * 业务类型
     */
    private String businessType;
    /**
     * 事件(操作类型)
     */
    private String event;
    /**
     * 操作状态
     */
    private String status;
    /**
     * 操作时间
     */
    private Long createTime;
    /**
     * 第n次操作时间(幂等操作,日志只有一条)
     */
    private Long updateTime;
    /**
     * 操作次数
     */
    private Integer times;

    /**
     * 日志内容
     */
    private String content;
    /**
     * 子站域名
     */
    private String website;
    /**
     * 注册ip
     */
    private String ip;
    /**
     * 注册设备 iphone/三星/华为
     */
    private String device;
    /**
     * unique id
     */
    private String deviceId;

    /**
     * 设备型号 iphone 5s/小米5
     */
    private String deviceModel;

    /**
     * app id
     */
    private int appId;

    /**
     * app version e.g 7.02
     */
    private float appVersion;

    /**
     * os platform
     */
    private Platform platform;

    /**
     * 操作系统
     */
    private String os;

    private String userAgent;

    /**
     * os version not app version
     */
    private String clientVersion;

    /**
     * 渠道
     */
    private String channel;

    /**
     * 经度
     */
    private double longitude;

    /**
     * 纬度
     */
    private double latitude;

    /**
     * 中国移动
     * 中国联通
     * wifi
     */
    private String network;

    /**
     * 是否模拟机
     */
    private Boolean simulate;

    /**
     * International Mobile Equipment Identity
     */
    private String imei;

    private String bssid;

    private String ssid;

    /**
     * for apple unique id
     */
    private String idfa;

    /**
     * 启动时间
     */
    private long startTime;

    /**
     * 重启时间
     */
    private long resumeTime;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id", columnDefinition = "int(10) UNSIGNED AUTO_INCREMENT     COMMENT 'primary key'", nullable = false)
    @MethodOrder(order = 1)
    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    @SplitTable(index = 10, strategy = TableSplitStrategy.ORIGIN)
    @MethodOrder(order = 2)
    @Column(name = "user_type", columnDefinition = "varchar(16) DEFAULT '' COMMENT 'user type'", nullable = false)
    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    @SplitTable
    @Column(name = "user_id", columnDefinition = "int(10) UNSIGNED     COMMENT 'user id'", nullable = false)
    @MethodOrder(order = 3)
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }


    @MethodOrder(order = 4)
    @Column(name = "business_type", columnDefinition = "varchar(64) DEFAULT '' COMMENT 'business typ'", nullable = false)
    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }


    @MethodOrder(order = 5)
    @Column(name = "business_id", columnDefinition = "int(10) UNSIGNED     COMMENT 'business id'", nullable = false)
    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }


    @MethodOrder(order = 6)
    @Column(name = "times", columnDefinition = "int(10) DEFAULT 0 COMMENT 'times'", nullable = false)
    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }

    @MethodOrder(order = 6.1F)
    @Column(name = "channel", columnDefinition = "varchar(256) DEFAULT '' COMMENT 'channel'", nullable = false)
    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }


    @Column(name = "ip", columnDefinition = "char(16) DEFAULT '' COMMENT 'ip'", nullable = false)
    @MethodOrder(order = 7)
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @MethodOrder(order = 8)
    @Column(name = "device", columnDefinition = "varchar(32) DEFAULT '' COMMENT 'device'", nullable = false)
    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    @MethodOrder(order = 9)
    @Column(name = "device_id", columnDefinition = "varchar(64) DEFAULT '' COMMENT 'device unique id'", nullable = false)
    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @MethodOrder(order = 9.1F)
    @Column(name = "device_model", columnDefinition = "varchar(16) DEFAULT '' COMMENT 'device model 5s e.g ...'", nullable = false)
    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    @MethodOrder(order = 10)
    @Column(name = "event", columnDefinition = "varchar(64) DEFAULT '' COMMENT 'event type'", nullable = false)
    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    @Column(name = "content", columnDefinition = "varchar(512) DEFAULT '' COMMENT 'content'", nullable = false)
    @MethodOrder(order = 11)
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @MethodOrder(order = 12)
    @Column(name = "website", columnDefinition = "varchar(256) DEFAULT '' COMMENT 'website home url'", nullable = false)
    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    @MethodOrder(order = 13)
    @Column(name = "app_id", columnDefinition = "int(11) UNSIGNED DEFAULT 0 COMMENT 'app id'", nullable = false)
    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    @MethodOrder(order = 14)
    @Column(name = "app_version", columnDefinition = "float(11,2) DEFAULT 0.0 COMMENT 'app version'", nullable = false)
    public float getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(float appVersion) {
        this.appVersion = appVersion;
    }

    @MethodOrder(order = 15)
    @Column(name = "platform", columnDefinition = "tinyint(2) DEFAULT -1 COMMENT 'platform'", nullable = false)
    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    @MethodOrder(order = 16)
    @Column(name = "os", columnDefinition = "varchar(16) DEFAULT '' COMMENT 'operation system'", nullable = false)
    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    @MethodOrder(order = 17)
    @Column(name = "user_agent", columnDefinition = "varchar(512) DEFAULT '' COMMENT 'use agent'", nullable = false)
    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    @MethodOrder(order = 18)
    @Column(name = "client_version", columnDefinition = "varchar(64) DEFAULT '' COMMENT 'client os version'", nullable = false)
    public String getClientVersion() {
        return clientVersion;
    }

    public void setClientVersion(String clientVersion) {
        this.clientVersion = clientVersion;
    }


    @MethodOrder(order = 20)
    @Column(name = "longitude", columnDefinition = "double DEFAULT 0.0 COMMENT 'longitude'", nullable = false)
    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @MethodOrder(order = 21)
    @Column(name = "latitude", columnDefinition = "double DEFAULT 0.0 COMMENT 'latitude'", nullable = false)
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @MethodOrder(order = 22)
    @Column(name = "network", columnDefinition = "varchar(16) DEFAULT '' COMMENT 'network'", nullable = false)
    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    @MethodOrder(order = 23)
    @Column(name = "simulate", columnDefinition = "tinyint(1) DEFAULT 0 COMMENT 'is simulate'", nullable = false)
    public Boolean getSimulate() {
        return simulate;
    }

    public void setSimulate(Boolean simulate) {
        this.simulate = simulate;
    }

    @MethodOrder(order = 24)
    @Column(name = "imei", columnDefinition = "varchar(64) DEFAULT '' COMMENT 'imei'", nullable = false)
    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    @MethodOrder(order = 25)
    @Column(name = "bssid", columnDefinition = "varchar(64) DEFAULT '' COMMENT 'bssi'", nullable = false)
    public String getBssid() {
        return bssid;
    }

    public void setBssid(String bssid) {
        this.bssid = bssid;
    }

    @MethodOrder(order = 26)
    @Column(name = "ssid", columnDefinition = "char(64) DEFAULT '' COMMENT 'ssid'", nullable = false)
    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    @MethodOrder(order = 27)
    @Column(name = "idfa", columnDefinition = "char(64) DEFAULT '' COMMENT 'idfa'", nullable = false)
    public String getIdfa() {
        return idfa;
    }

    public void setIdfa(String idfa) {
        this.idfa = idfa;
    }

    @MethodOrder(order = 28)
    @Column(name = "start_time", columnDefinition = "bigint(20) DEFAULT 0 COMMENT 'client start time'", nullable = false)
    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    @MethodOrder(order = 29)
    @Column(name = "resume_time", columnDefinition = "bigint(20) DEFAULT 0 COMMENT 'client resume time'", nullable = false)
    public long getResumeTime() {
        return resumeTime;
    }

    public void setResumeTime(long resumeTime) {
        this.resumeTime = resumeTime;
    }

    @MethodOrder(order = 30)
    @Column(name = "create_time", columnDefinition = "bigint(20)  DEFAULT 0", nullable = false)
    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    @MethodOrder(order = 31)
    @Column(name = "update_time", columnDefinition = "bigint(20)  DEFAULT 0", nullable = false)
    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }
}
