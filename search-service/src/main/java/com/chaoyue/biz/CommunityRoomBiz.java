package com.chaoyue.biz;

import com.chaoyue.mapper.CommunityRoomMapper;
import com.chaoyue.po.CommunityRoomPO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chaoyue on 2017/5/22.
 */
@Service
public class CommunityRoomBiz {
    private static final Logger logger = LoggerFactory.getLogger(CommunityRoomBiz.class);


    @Resource
    private CommunityRoomMapper communityRoomMapper;

    public Long countNum() {
        return communityRoomMapper.countNum();
    }

    /**
     * 分页查询房源信息
     *
     * @param pageNo
     * @param pageSize
     * @return List<CommunityRoom>
     */
    public List<CommunityRoomPO> findByPage(Long pageNo, Long pageSize) {
        Long start = (pageNo - 1) * pageSize;
        Map<String, Object> params = new HashMap();
        params.put("start", start);
        params.put("rows", pageSize);
        List<CommunityRoomPO> result = communityRoomMapper.findByPage(params);
        return result;
    }

    /**
     * 根据IDS 获取信息
     * @param ids
     * @return
     */
    public List<CommunityRoomPO> findByIds(List<String> ids){
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ids", ids);
        List<CommunityRoomPO> list = communityRoomMapper.findByIds(params);
        return list;
    }
}
