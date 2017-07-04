package com.chaoyue.service;

/**
 * 创建索引服务
 * Created by chaoyue on 2017/5/23.
 */
public interface CreateIndexService {

    /**
     * 创建全量索引
     *
     * @return Boolean
     */
    Boolean createFullIndex();

    /**
     * 根据ids 创建增量索引
     * @param ids
     * @return
     */
    Boolean createPartIndex(String... ids);
}
