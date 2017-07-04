package com.chaoyue.bean;

import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * Created by chaoyue on 2017/5/24.
 */
public class SearchBean implements Serializable {
    private static final long serialVersionUID = 949153107612892592L;

    @NotBlank(message = "搜索关键字不能为空")
    private String keyword;//关键字

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }


}
