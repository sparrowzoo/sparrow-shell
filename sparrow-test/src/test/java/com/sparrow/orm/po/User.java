package com.sparrow.orm.po;

import com.sparrow.constant.Config;
import com.sparrow.protocol.POJO;
import com.sparrow.utility.ConfigUtility;
import com.sparrow.utility.StringUtility;

import javax.persistence.*;
import java.sql.Date;

@Table(name = "user", schema = "user")
public class User implements POJO, Cloneable {
    /*-------基本信息-------------*/
    private Long userId;
    private String userName;
    private String nickName;
    private String password;
    private String avatar;
    private Byte sex;
    private Date birthday;
    private Long cent;
    private Long createTime;
    private Long updateTime;
    private Byte status;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Column(name = "nick_name")
    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    @Column(name = "user_name", updatable = false, unique = true)
    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Column(name = "password", updatable = false)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "sex")
    public Byte getSex() {
        return sex;
    }

    public void setSex(Byte sex) {
        this.sex = sex;
    }

    @Column(name = "avatar", updatable = false)
    public String getAvatar() {
        if (StringUtility.isNullOrEmpty(this.avatar)) {
            this.avatar = ConfigUtility.getValue(Config.DEFAULT_AVATAR);
        }
        return avatar;
    }

    public void setAvatar(String headImg) {
        this.avatar = headImg;
    }

    @Column(name = "birthday")
    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    @Column(name = "create_time", updatable = false)
    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    @Column(name = "cent", updatable = false)
    public Long getCent() {
        return cent;
    }

    public void setCent(Long cent) {
        this.cent = cent;
    }

    @Column(name = "status", updatable = false)
    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    @Column(name = "update_time")
    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }
}
