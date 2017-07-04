package com.chaoyue;

import com.chaoyue.bean.PageBean;
import com.chaoyue.bean.RoomBean;
import com.chaoyue.bean.SearchBean;
import com.chaoyue.service.CreateIndexService;
import com.chaoyue.service.SearchService;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.annotation.Resource;

/**
 * Created by chaoyue on 2017/5/23.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/*.xml"})
@EnableWebMvc
@WebAppConfiguration
public class CreateIndexTest extends TestCase{
    @Resource
    private CreateIndexService createIndexServiceImpl;
    @Resource
    private SearchService searchServiceImpl;

    @Test
    public void testCreateIndex(){
        createIndexServiceImpl.createFullIndex();
    }

    @Test
    public void testCreatePartIndex(){
        String ids = "100512";
        createIndexServiceImpl.createPartIndex(ids);
    }

    @Test
    public void testSearch(){
        PageBean<RoomBean> pageBean = new PageBean<>();
        pageBean.setPageNo(1L);
        pageBean.setPageSize(20L);
        pageBean.setTotalRows(100L);
        RoomBean roomBean = new RoomBean();
       // roomBean.setTitle("2室2厅2卫");

        SearchBean searchBean = new SearchBean();
        searchBean.setKeyword("园");

        PageBean<RoomBean> page = searchServiceImpl.findByKeyword(pageBean,searchBean);

        System.out.println(page.toString());
    }
}
