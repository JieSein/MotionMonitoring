package com.json.motionmonitoring.model;

import org.litepal.crud.DataSupport;

import java.util.Date;

public class Motion_Device extends DataSupport {
    private Integer id;
    private String deviceCode;
    private Date create_time;
    private Integer user_id;
    private Motion_User motion_user;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Motion_User getMotion_user() {
        return motion_user;
    }

    public void setMotion_user(Motion_User motion_user) {
        this.motion_user = motion_user;
    }
}
