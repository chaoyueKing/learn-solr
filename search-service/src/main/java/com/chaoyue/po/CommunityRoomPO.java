package com.chaoyue.po;

import org.apache.solr.client.solrj.beans.Field;

import java.io.Serializable;

/**
 * Created by chaoyue on 2017/5/22.
 */
public class CommunityRoomPO implements Serializable {
    private static final long serialVersionUID = -6246815290478950001L;

    @Field
    private String id;

    @Field
    private String communityName;

    @Field
    private String title;

    @Field
    private String downPayment;

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
        return "CommunityRoom{" +
                "id=" + id +
                ", communityName='" + communityName + '\'' +
                ", title='" + title + '\'' +
                ", downPayment=" + downPayment +
                '}';
    }
}
