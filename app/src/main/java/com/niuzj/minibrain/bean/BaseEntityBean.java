package com.niuzj.minibrain.bean;

import io.objectbox.annotation.BaseEntity;
import io.objectbox.annotation.Id;

@BaseEntity
public class BaseEntityBean {

    @Id
    public long id;

    public long createTime;

    public long updateTime;

}
