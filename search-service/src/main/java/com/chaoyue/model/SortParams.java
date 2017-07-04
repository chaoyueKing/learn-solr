package com.chaoyue.model;

import java.io.Serializable;

/**
 * Created by wangyq on 2015/10/28.
 */
public class SortParams implements Serializable {
    private String name; //排序字段名称
    private String type; //排序类型(0:ASC 1:DESC)

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SortParams{");
        sb.append("name='").append(name).append('\'');
        sb.append(", type='").append(type).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
