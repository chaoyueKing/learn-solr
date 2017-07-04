package com.chaoyue.bean;

import java.io.Serializable;

/**
 * Created by chaoyue on 2017/5/24.
 */
public class RoomBean implements Serializable{

    private static final long serialVersionUID = -5697920665068781103L;
    private Integer id;
    private String communityName;
    private String title;
    private String downPayment;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDownPayment() {
        return downPayment;
    }

    public void setDownPayment(String downPayment) {
        this.downPayment = downPayment;
    }

    @Override
    public String toString() {
        return "RoomBean{" +
                "id=" + id +
                ", communityName='" + communityName + '\'' +
                ", title='" + title + '\'' +
                ", downPayment='" + downPayment + '\'' +
                '}';
    }
}
