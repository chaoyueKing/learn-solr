package com.chaoyue.common;

import com.chaoyue.bean.PageBean;
import com.chaoyue.bean.RoomBean;
import com.chaoyue.model.FilterParams;
import com.chaoyue.model.QueryParams;
import com.chaoyue.model.SortParams;
import com.chaoyue.po.CommunityRoomPO;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by wangyq on 2015/12/16.
 */
public class SearchCommon {
    /**
     * 查询关键字处理
     *
     * @param keyword
     * @return List<QueryParams>
     */
    public static List<QueryParams> getQueryParams(String keyword) {
        QueryParams qp = null;
        List<QueryParams> qList = new ArrayList<QueryParams>();
        //*号的兼容性处理
        if ("*".equals(keyword)) {
            qp = new QueryParams();
            qp.setName("keyword");
            qp.setValue(keyword);
            qp.setType("0");
            qList.add(qp);
            return qList;
        }

        //根据特殊字符分词
        String[] words = keyword.split("[,';. #&-/]");
        int length = words.length;
        for (int i = 0; i < length; i++) {
            String word = words[i];
            String[] numberArray = StringUtil.filterNumber(word);//数字部分
            String[] letterArray = StringUtil.filterLetter(word);//字母部分
            String[] chineseArray = StringUtil.filterChinese(word);//汉字部分

            //数字格式按模糊匹配
            if (numberArray != null && numberArray.length > 0) {
                for (String s : numberArray) {
                    qp = new QueryParams();
                    qp.setName("keyword");
                    qp.setValue(s);
                    qp.setType("1");//模糊
                    qp.setRelation("0");//AND
                    qList.add(qp);
                }
            }

            //字母格式按模糊匹配
            if (letterArray != null && letterArray.length > 0) {
                for (String s : letterArray) {
                    qp = new QueryParams();
                    qp.setName("keyword");
                    qp.setValue(s);
                    qp.setType("1");//模糊
                    qp.setRelation("0");//AND
                    qList.add(qp);
                }
            }

            //汉字格式走分词
            if (chineseArray != null && chineseArray.length > 0) {
                for (String s : chineseArray) {
                    qp = new QueryParams();
                    qp.setName("keyword");
                    qp.setValue(s);
                    qp.setType("1");
                    qp.setRelation("0");//AND
                    qList.add(qp);
                }
            }
        }
        return qList;
    }

    /**
     * 过滤条件处理
     *
     * @param fcName      一级分类名称
     * @param scName      二级分类名称
     * @param tcName      三级分类名称
     * @param brandName   品牌名称
     * @param minPrice    最小价格
     * @param maxPrice    最大价格
     * @param available   是否有货
     * @param selfSupport 是否自营
     * @param favourable  是否优惠
     * @param areaType 是否为海外购
     * @return List<FilterParams>
     */
    public static List<FilterParams> getFilterParams(String fcName, String scName, String tcName, String brandName, BigDecimal minPrice, BigDecimal maxPrice, Boolean available, String selfSupport, String favourable, String areaType) {
        String now = DateUtil.getNow(DateUtil.PATTERN_SIMPLE_DATETIME);
        List<FilterParams> fqList = new ArrayList<FilterParams>();
        FilterParams fq = new FilterParams();

        //一级分类过滤条件
        if (StringUtil.isNotEmpty(fcName)) {
            fq = new FilterParams();
            fq.setName("fcName");
            fq.setValue(fcName);
            fq.setType("1");
            fqList.add(fq);
        }

        //二级分类过滤条件
        if (StringUtil.isNotEmpty(scName)) {
            fq = new FilterParams();
            fq.setName("scName");
            fq.setValue(scName);
            fq.setType("1");
            fqList.add(fq);
        }

        //三级分类过滤条件
        if (StringUtil.isNotEmpty(tcName)) {
            fq = new FilterParams();
            fq.setName("tcName");
            fq.setValue(tcName);
            fq.setType("1");
            fqList.add(fq);
        }

        //品牌名称过滤条件
        if (StringUtil.isNotEmpty(brandName)) {
            fq = new FilterParams();
            fq.setName("brandName");
            fq.setValue(brandName);
            fq.setType("0");
            fqList.add(fq);
        }

        //价格区间过滤条件设置
        // 存储的数据中sellPrice = 原价 * 100
        String minAmount = "*";//价格范围，起始价格,* 表示不限起始价格
        if (minPrice != null) {
            minPrice = minPrice.multiply(new BigDecimal("100"));
            minAmount = minPrice.toString();
        }
        String maxAmount = "*";//价格范围，结束价格,* 表示不限结束价格
        if (maxPrice != null) {
            BigDecimal basePrice = new BigDecimal(1000 * 100);
            if (maxPrice.compareTo(basePrice) >= 0) {
                //大于等于1000时，业务认为不限制结束价格.
                maxAmount = "*";
            } else {
                maxPrice = maxPrice.multiply(new BigDecimal("100"));
                maxAmount = maxPrice.toString();
            }
        }
        fq = new FilterParams();
        fq.setName("sellPrice");
        fq.setValue("[" + minAmount + " TO " + maxAmount + "]");//价格区间
        fq.setType("2");
        fqList.add(fq);

        //是否有货过滤条件设置
        if (available != null && available == true) {
            fq = new FilterParams();
            fq.setName("sellNums");
            fq.setValue("[1 TO *]");//有货，数量＞0
            fq.setType("2");
            fqList.add(fq);
        }

        //是否自营过滤条件设置
        if (StringUtil.isNotEmpty(selfSupport)) {
            fq = new FilterParams();
            fq.setName("selfSupport");
            fq.setValue(selfSupport);
            fq.setType("0");
            fqList.add(fq);
        }

        //是否优惠过滤条件设置
        if (StringUtil.isNotEmpty(favourable)) {
            fq = new FilterParams();
            fq.setName("favourable");
            fq.setValue(favourable);
            fq.setType("0");
            fqList.add(fq);
        }

        //是否为海外购过滤条件设置
        if (StringUtil.isNotEmpty(areaType)) {
            fq = new FilterParams();
            fq.setName("areaType");
            fq.setValue(areaType);
            fq.setType("0");
            fqList.add(fq);
        }

        return fqList;
    }

    /**
     * 排序条件处理
     *
     * @param sortType
     * @return List<SortParams>
     */
    public static List<SortParams> getSortParams(Integer sortType) {
        List<SortParams> sqList = new ArrayList<SortParams>();
        SortParams sq = new SortParams();
        if (sortType == null || sortType == 0) {
            //默认根据排序值降序
            sq.setName("orderSort");
            sq.setType("1");
            sqList.add(sq);
        } else {
            if (sortType == 1) {
                //价格升排序
                sq.setName("sellPrice");
                sq.setType("0");
            } else if (sortType == -1) {
                //价格降排序
                sq.setName("sellPrice");
                sq.setType("1");
            } else if (sortType == 2) {
                //折扣升排序
                sq.setName("discountRate");
                sq.setType("0");
            } else if (sortType == -2) {
                //折扣降排序
                sq.setName("discountRate");
                sq.setType("1");
            }
            sqList.add(sq);
        }

        return sqList;
    }

    /**
     * Page对象转换
     *
     * @param pageBean
     * @param pageInfo
     * @return PageBean<ProductBean>
     */
    public static PageBean<RoomBean> pageConvert(PageBean<RoomBean> pageBean, Page<CommunityRoomPO> pageInfo) {
        if (pageInfo != null) {
            List<RoomBean> roomBeans = BeanUtil.copy(pageInfo.getResult(),RoomBean.class);
            pageBean.setTotalRows(pageInfo.getTotalRows());
            pageBean.setResult(roomBeans);
        }
        return pageBean;
    }

    /**
     * 智能联想查询参数处理
     *
     * @param keyword
     * @return List<QueryParams>
     */
    public static List<QueryParams> getAssociateParams(String keyword) {
        keyword = keyword.replaceAll(" ", "*");//替换空格
        keyword = keyword.toLowerCase();//转小写
        QueryParams qp = new QueryParams();
        qp.setName("groupKeyword");
        qp.setValue(keyword);
        qp.setType("1");

        List<QueryParams> qList = new ArrayList<QueryParams>();
        qList.add(qp);
        return qList;
    }

}
