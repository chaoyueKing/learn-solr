package com.chaoyue.service;

import com.chaoyue.bean.AssociateResultBean;
import com.chaoyue.bean.PageBean;
import com.chaoyue.bean.RoomBean;
import com.chaoyue.bean.SearchBean;
import com.chaoyue.biz.service.SolrConfigService;
import com.chaoyue.biz.service.SolrService;
import com.chaoyue.common.Page;
import com.chaoyue.common.SearchCommon;
import com.chaoyue.common.ValidateUtil;
import com.chaoyue.model.QueryParams;
import com.chaoyue.po.CommunityRoomPO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by chaoyue on 2017/5/24.
 */
@Service
public class SearchServiceImpl implements SearchService {

    private static final Logger logger = LoggerFactory.getLogger(SearchServiceImpl.class);
    private static final Integer ASSOCIATE_MAX_SIZE = 10;//智能联想最大值

    @Resource
    private SolrService solrService;
    @Resource
    private SolrConfigService solrConfig;
    @Override
    public PageBean<RoomBean> findByKeyword(PageBean<RoomBean> pageBean, SearchBean searchBean) {
        logger.info("findByKeyword param: pageBean:[{}],searchBean:[{}]",pageBean,searchBean);

        if (ValidateUtil.validateParam(pageBean) && ValidateUtil.validateParam(searchBean)){
            List<QueryParams> queryParams = SearchCommon.getQueryParams(searchBean.getKeyword());
            Page<CommunityRoomPO> pageInfo = new Page(pageBean.getPageNo(), pageBean.getPageSize());
            pageInfo = solrService.queryByPage(solrConfig.getSolrUrl(), solrConfig.getCoreName("room"), queryParams, null, null, pageInfo, CommunityRoomPO.class);
            pageBean = SearchCommon.pageConvert(pageBean, pageInfo);
        }

        return pageBean;
    }

    @Override
    public List<AssociateResultBean> AssociateByKeyword(SearchBean searchBean) {
        return null;
    }
}
