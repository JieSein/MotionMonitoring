package com.json.motionmonitoring.model;

import org.litepal.crud.DataSupport;

public class Device extends DataSupport {
    private Integer id;

    private Integer user_id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

}
