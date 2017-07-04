package com.chaoyue.model;

import java.io.Serializable;

/**
 * Created by wangyq on 2015/10/28.
 */
public class QueryParams implements Serializable {

    private String name;//参数名称
    private String value;//参数值
    private String type;//查询类型(0:精确 1:模糊)
    private String relation;//与其他条件的关系(0:AND 1:OR)

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("QueryParams{");
        sb.append("name='").append(name).append('\'');
        sb.append(", value='").append(value).append('\'');
        sb.append(", type='").append(type).append('\'');
        sb.append(", relation='").append(relation).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
