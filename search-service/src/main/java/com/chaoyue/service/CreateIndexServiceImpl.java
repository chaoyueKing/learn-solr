package com.chaoyue.service;

import com.chaoyue.biz.service.SolrConfigService;
import com.chaoyue.thread.CreateFullIndexThread;
import com.chaoyue.thread.CreatePartIndexThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by chaoyue on 2017/5/23.
 */
@Service
public class CreateIndexServiceImpl implements CreateIndexService{

    private static final Logger logger = LoggerFactory.getLogger(CreateIndexServiceImpl.class);
    @Resource
    private SolrConfigService solrConfig;

    @Override
    public Boolean createFullIndex() {
        logger.info("CreateIndexServiceImpl createFullIndex start...");
        long start = System.currentTimeMillis();
        String channelCode = null;
        boolean returnValue = true;
        ExecutorService pool = null;

        try{
            Map<String,String> coreMap = solrConfig.getCoreMap();
            //根据渠道数创建线程池
            int sectionThread = coreMap.size();

            pool = Executors.newFixedThreadPool(sectionThread);
            //按照渠道创建全量索引
            List<Future<Boolean>> result = new ArrayList();
            for (Map.Entry<String,String> entry :coreMap.entrySet()){
                channelCode = entry.getKey();
                CreateFullIndexThread thread = new CreateFullIndexThread(channelCode,solrConfig.getSolrUrl(),entry.getValue());
                Future<Boolean> flag = pool.submit(thread);
                result.add(flag);
            }
            for (Future<Boolean> future : result){
                if (!future.get()){
                    returnValue = false;
                    logger.error("渠道[{}]创建全量索引失败!", channelCode);
                }
            }
        }catch (Exception e){
            logger.error(e.getMessage()+":[{}]", e);
            returnValue = false;
        }finally {
            if (pool != null){
                pool.shutdown();
            }
        }
        logger.info("CreateIndexRpcService createFullIndex end [总耗时:{}秒]...", (System.currentTimeMillis() - start) / 1000.0);
        return returnValue;
    }

    @Override
    public Boolean createPartIndex(String... ids) {
        long start = System.currentTimeMillis();
        boolean returnValue = true;
        String channelCode = null;
        ExecutorService pool = null;
        try {
            //入参校验
            this.validateParams(ids);

            Map<String, String> coreMap = solrConfig.getCoreMap();
            //根据渠道数创建线程池
            int sectionThread = coreMap.size();
            pool = Executors.newFixedThreadPool(sectionThread);

            //按照渠道创建增量索引
            List<String> idList = Arrays.asList(ids);
            List<Future<Boolean>> result = new ArrayList<Future<Boolean>>();
            for (Map.Entry<String, String> entry : coreMap.entrySet()) {
                channelCode = entry.getKey();
                CreatePartIndexThread thread = new CreatePartIndexThread(idList, channelCode, solrConfig.getSolrUrl(), entry.getValue());
                Future<Boolean> flag = pool.submit(thread);
                result.add(flag);
            }

            for (Future<Boolean> future : result) {
                if (!future.get()) {
                    returnValue = false;
                    logger.error("渠道{}创建增量索引失败!", channelCode);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            returnValue = false;
        } finally {
            if (pool != null) {
                pool.shutdown();
            }
        }
        logger.info("CreateIndexRpcService createPartIndex end [总耗时:{}秒]...", (System.currentTimeMillis() - start) / 1000.0);
        return returnValue;
    }

    /**
     * 创建增量索引服务，入参校验
     * @param ids
     * @return boolean
     */
    private boolean validateParams(String... ids) {
        if (ids == null || ids.length <= 0) {
            throw new RuntimeException("商品编号不能为空");
        }

        return true;
    }
}
