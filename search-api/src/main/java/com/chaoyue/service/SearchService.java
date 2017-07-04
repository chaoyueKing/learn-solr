package com.chaoyue.service;

import com.chaoyue.bean.AssociateResultBean;
import com.chaoyue.bean.PageBean;
import com.chaoyue.bean.RoomBean;
import com.chaoyue.bean.SearchBean;

import java.util.List;

/**
 * 搜索服务
 * Created by chaoyue on 2017/5/23.
 */
public interface SearchService{

    /**
     * 根据关键字查询
     * @param pageBean
     * @param searchBean
     * @return
     */
    PageBean<RoomBean> findByKeyword(PageBean<RoomBean> pageBean, SearchBean searchBean);


    /**
     * 通过关键字智能联想分类或品牌
     * @param searchBean
     * @return List<AssociateResultBean>
     */
    public List<AssociateResultBean> AssociateByKeyword(SearchBean searchBean);

}
