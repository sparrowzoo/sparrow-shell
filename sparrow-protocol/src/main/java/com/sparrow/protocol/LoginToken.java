package com.sparrow.protocol;

/**
 * 登录 DTO
 *
 * @author harry
 */
public class LoginToken implements VO{
    private Long userId;
    private String nickName;
    private String userName;
    private String avatar;
    private Long cent;
    private String deviceId;
    private Boolean activate;
    private Integer days;
    private Long expireAt;
    private String permission;

    public static LoginToken create(Long userId,
                                  String userName,
                                  String nickName,
                                  String avatar,
                                  Long cent,
                                  String deviceId,
                                  Boolean activate,
                                  Integer expireDays) {
        LoginToken login = new LoginToken();
        login.userId = userId;
        login.userName = userName;
        login.nickName = nickName;
        login.avatar = avatar;
        login.cent = cent;
        login.deviceId = deviceId;
        login.activate = activate;
        login.days = expireDays;
        if(login.days==null){
            login.days=1;
        }
        if (expireDays > 0) {
            login.expireAt = System.currentTimeMillis() + 1000 * 60 * 60 * 24L * expireDays;
        } else {
            login.expireAt = 0L;
        }
        return login;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Long getCent() {
        return cent;
    }

    public void setCent(Long cent) {
        this.cent = cent;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Boolean getActivate() {
        return activate;
    }

    public void setActivate(Boolean activate) {
        this.activate = activate;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public Long getExpireAt() {
        return expireAt;
    }

    public void setExpireAt(Long expireAt) {
        this.expireAt = expireAt;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }
}
