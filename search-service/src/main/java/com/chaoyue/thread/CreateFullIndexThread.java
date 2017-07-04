package com.chaoyue.thread;

import com.chaoyue.biz.CommunityRoomBiz;
import com.chaoyue.biz.service.SolrService;
import com.chaoyue.common.SpringApplicationContext;
import com.chaoyue.po.CommunityRoomPO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * 增加全量索引
 * Created by chaoyue on 2017/5/23.
 */
public class CreateFullIndexThread implements Callable<Boolean> {

    private static final Logger logger = LoggerFactory.getLogger(CreateFullIndexThread.class);
    private static final Long DEFAULT_PAGE_SIZE = 10000L;//默认每页大小

    private String channelCode;//渠道编号
    private String solrUrl;//搜索引擎URL
    private String coreName;//核名称

    public CreateFullIndexThread(String channelCode, String solrUrl, String coreName) {
        this.channelCode = channelCode;
        this.solrUrl = solrUrl;
        this.coreName = coreName;
    }

    @Override
    public Boolean call() throws Exception {

        Integer success = 0;
        Integer failed = 0;
        Boolean returnValue = true;
        try{
            SolrService solrService = SpringApplicationContext.getBean(SolrService.class);
            CommunityRoomBiz communityRoomBiz = SpringApplicationContext.getBean(CommunityRoomBiz.class);

            Long num = communityRoomBiz.countNum();

            //计算总页数
            if (num != null && num >0){
                Long totalPages = num / DEFAULT_PAGE_SIZE;
                if (num % 100 >0){
                    totalPages++;
                }
                //删除当前核下的索引
                solrService.deleteAll(solrUrl,coreName);
               // for (int i=1; i<=totalPages;i++){
                    //查询一页数据
                    List<CommunityRoomPO> list = communityRoomBiz.findByPage(new Long(1),DEFAULT_PAGE_SIZE);
                    //对当前页list数据创建索引
                    boolean flag = solrService.createIndex(solrUrl,coreName,list);
                    if (flag){
                        success++;
                    }else {
                        failed++;
                        returnValue = false;
                    }
               // }
            }else {
                logger.info("渠道[{}]创建索引失败,无有效商品",channelCode);
            }
        }catch (Exception e){
            logger.error("创建索引失败:[{}]",e);
            returnValue = false;
        }
        logger.info("创建全量索引，成功{}页，失败{}页", success, failed);
        return returnValue;
    }
}
