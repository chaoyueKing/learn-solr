package com.chaoyue.biz.service;

import java.util.Map;

/**
 * Solr配置服务
 * Created by chaoyue on 2017/5/22.
 */
public class SolrConfigService {
    //solr地址
    private String solrUrl;
    //core名称配置
    private Map<String, String> coreMap;

    public String getSolrUrl() {
        return solrUrl;
    }

    public void setSolrUrl(String solrUrl) {
        this.solrUrl = solrUrl;
    }

    public Map<String, String> getCoreMap() {
        return coreMap;
    }

    public void setCoreMap(Map<String, String> coreMap) {
        this.coreMap = coreMap;
    }

    public String getCoreName(String key) {
        return coreMap.get(key);
    }

}
