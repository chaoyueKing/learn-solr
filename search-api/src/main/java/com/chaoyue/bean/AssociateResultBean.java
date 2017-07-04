package com.chaoyue.bean;

import java.io.Serializable;

/**
 * 智能联想返回对象
 * Created by wyq on 2016/3/18.
 */
public class AssociateResultBean implements Serializable {

    private static final long serialVersionUID = -1046426638342749403L;

    private Integer type; //类型(1:分类 2:品牌)
    private String name; //名称
    private Long num;//数量

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getNum() {
        return num;
    }

    public void setNum(Long num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "AssociateResultBean{" +
                "type=" + type +
                ", name='" + name + '\'' +
                ", num=" + num +
                '}';
    }
}
