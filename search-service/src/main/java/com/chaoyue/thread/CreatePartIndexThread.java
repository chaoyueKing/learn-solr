package com.chaoyue.thread;

import com.chaoyue.biz.CommunityRoomBiz;
import com.chaoyue.biz.service.SolrService;
import com.chaoyue.common.SpringApplicationContext;
import com.chaoyue.po.CommunityRoomPO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * 增加增量索引
 * Created by chaoyue on 2017/5/24.
 */
public class CreatePartIndexThread implements Callable<Boolean> {

    private static final Logger logger = LoggerFactory.getLogger(CreateFullIndexThread.class);
    private static final Long DEFAULT_PAGE_SIZE = 1000000L;//默认每页大小

    private List<String> ids;// ids
    private String channelCode;//渠道编号
    private String solrUrl;//搜索引擎URL
    private String coreName;//核名称

    public CreatePartIndexThread(List<String> ids, String channelCode, String solrUrl, String coreName) {
        this.ids = ids;
        this.channelCode = channelCode;
        this.solrUrl = solrUrl;
        this.coreName = coreName;
    }

    @Override
    public Boolean call() throws Exception {
        Boolean returnValue = false;

        SolrService solrService = SpringApplicationContext.getBean(SolrService.class);
        CommunityRoomBiz communityRoomBiz = SpringApplicationContext.getBean(CommunityRoomBiz.class);

        //删除
        returnValue = solrService.deleteByIds(solrUrl, coreName, ids);
        if (returnValue){
            List<CommunityRoomPO> list = communityRoomBiz.findByIds(ids);
            if (list != null && !list.isEmpty()){
                //创建索引
                returnValue =solrService.createIndex(solrUrl,coreName,list);
            }
        }
        logger.info("渠道{}创建增量索引{}", channelCode, returnValue);
        return returnValue;
    }
}
