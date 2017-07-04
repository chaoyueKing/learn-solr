package com.chaoyue.biz.service;

import com.chaoyue.common.Page;
import com.chaoyue.model.FilterParams;
import com.chaoyue.model.QueryParams;
import com.chaoyue.model.SortParams;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.SortClause;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 搜索引擎服务
 * Created by chaoyue on 2017/5/22.
 */

@Service
public class SolrService {

    private static final Logger logger = LoggerFactory.getLogger(SolrService.class);

    /**
     * 创建索引
     * @param sorlUrl
     * @param coreName
     * @param docs
     * @param <T>
     * @return boolean
     */
    public <T> boolean createIndex(String sorlUrl, String coreName, List<T> docs) {
        logger.info("createIndex docs is {}", docs);

        HttpSolrClient solrClient = null;
        try {
            solrClient = new HttpSolrClient(sorlUrl);
            if (docs != null && docs.size() > 0) {
                solrClient.addBeans(coreName, docs);
                solrClient.commit(coreName);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        } finally {
            if (solrClient != null) {
                try {
                    solrClient.close();
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    solrClient = null;
                }
            }
        }
        return true;
    }

    /**
     * 基本查询
     * @param sorlUrl
     * @param coreName
     * @param queryParams
     * @param filterParams
     * @param sortParams
     * @param clazz
     * @param <T>
     * @return List<T>
     */
    public <T> List<T> queryByConditiontion(String sorlUrl, String coreName, List<QueryParams> queryParams, List<FilterParams> filterParams, List<SortParams> sortParams, Class<T> clazz) {
        logger.info("queryByConditiontion queryParams is {}", queryParams);
        logger.info("queryByConditiontion filterParams is {}", filterParams);
        logger.info("queryByConditiontion sortParams is {}", sortParams);

        HttpSolrClient solrClient = null;
        List<T> result = new ArrayList<T>();
        try {
            SolrQuery sq = new SolrQuery();
            sq.setQuery(getQueryStr(queryParams));//查询参数
            sq.setFilterQueries(getFilterStr(filterParams));//Filter参数
            sq.setSorts(getSortList(sortParams));//排序参数

            solrClient = new HttpSolrClient(sorlUrl);
            QueryResponse qr = solrClient.query(coreName, sq);
            result = qr.getBeans(clazz);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (solrClient != null) {
                try {
                    solrClient.close();
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    solrClient = null;
                }
            }
        }
        return result;
    }

    /**
     * 通过ID删除
     * @param sorlUrl
     * @param coreName
     * @param ids
     * @return boolean
     */
    public boolean deleteByIds(String sorlUrl, String coreName, List<String> ids) {
        logger.info("deleteByIds params is {}", ids);

        HttpSolrClient solrClient = null;
        try {
            solrClient = new HttpSolrClient(sorlUrl);
            solrClient.deleteById(coreName, ids);
            solrClient.commit(coreName);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        } finally {
            if (solrClient != null) {
                try {
                    solrClient.close();
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    solrClient = null;
                }
            }
        }
        return true;
    }

    /**
     * 删除全部索引
     * @param sorlUrl
     * @param coreName
     * @return boolean
     */
    public boolean deleteAll(String sorlUrl, String coreName) {
        HttpSolrClient solrClient = null;
        try {
            solrClient = new HttpSolrClient(sorlUrl);
            solrClient.deleteByQuery(coreName, "*:*");
            solrClient.commit(coreName);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        } finally {
            if (solrClient != null) {
                try {
                    solrClient.close();
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    solrClient = null;
                }
            }
        }
        return true;
    }

    /**
     * 分组功能
     * @param sorlUrl
     * @param coreName
     * @param queryParams
     * @param filterParams
     * @param groupNames
     * @return Map<String,Object>
     */
    public Map<String, Object> groupBy(String sorlUrl, String coreName, List<QueryParams> queryParams, List<FilterParams> filterParams, List<String> groupNames) {
        logger.info("groupBy queryParams is {}", queryParams);
        logger.info("groupBy filterParams is {}", filterParams);
        logger.info("groupBy groupNames is {}", groupNames);

        HttpSolrClient solrClient = null;
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            SolrQuery sq = new SolrQuery();
            sq.setQuery(getQueryStr(queryParams));//查询参数
            sq.setFilterQueries(getFilterStr(filterParams));//Filter参数
            sq.setRows(0);//不返回查询结果
            sq.setFacet(true);//分组
            sq.setIncludeScore(true);//是否按每组数量高低排序
            sq.setFacetLimit(1000);//限制分组结果
            for (String groupName : groupNames) {
                sq.addFacetField(groupName);
            }

            solrClient = new HttpSolrClient(sorlUrl);
            QueryResponse qr = solrClient.query(coreName, sq);

            Long totalRows = qr.getResults().getNumFound();
            result.put("totalRows", totalRows);

            List<FacetField> facetFields = qr.getFacetFields();
            if (facetFields != null && !facetFields.isEmpty()) {
                for (FacetField facetField : facetFields) {
                    List<Map<String, Object>> groupList = new ArrayList<Map<String, Object>>();
                    String groupName = facetField.getName();
                    List<FacetField.Count> values = facetField.getValues();
                    if (values != null && !values.isEmpty()) {
                        for (FacetField.Count value : values) {
                            Long count = value.getCount();
                            if (count > 0) {
                                Map<String, Object> map = new LinkedHashMap<String, Object>();
                                map.put("name", value.getName());
                                map.put("num", count);
                                groupList.add(map);
                            }
                        }
                    }
                    result.put(groupName, groupList);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (solrClient != null) {
                try {
                    solrClient.close();
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    solrClient = null;
                }
            }
        }
        return result;
    }



    /**
     * 根据查询参数生成SOLR查询条件
     * @param queryParams
     * @return String
     */
    private String getQueryStr(List<QueryParams> queryParams) {
        StringBuilder builder = new StringBuilder();
        if (queryParams != null && !queryParams.isEmpty()) {
            int length = queryParams.size();
            for (int i = 0; i < length; i++) {
                QueryParams param = queryParams.get(i);
                builder.append(param.getName()).append(":");
                String type = param.getType();

                if ("0".equals(type)) {
                    //精确查询
                    builder.append(param.getValue());
                } else if ("1".equals(type)) {
                    //模糊查询
                    builder.append("*").append(param.getValue()).append("*");
                } else {
                    throw new RuntimeException("invalid queryParams type!");
                }

                if (i < length - 1) {
                    String relation = param.getRelation();
                    if (relation != null && !"".equals(relation)) {
                        if ("0".equals(relation)) {
                            builder.append(" AND ");
                        } else if ("1".equals(relation)) {
                            builder.append(" OR ");
                        } else {
                            throw new RuntimeException("invalid queryParams relation!");
                        }
                    }
                }
            }
        }
        return builder.toString();
    }


    /**
     * 根据Filter参数生成SOLR查询条件
     * @param filterParams
     * @return String
     */
    private String getFilterStr(List<FilterParams> filterParams) {
        StringBuilder builder = new StringBuilder();
        if (filterParams != null && !filterParams.isEmpty()) {
            int size = filterParams.size();
            for (int i = 0; i < size; i++) {
                FilterParams param = filterParams.get(i);
                //add by harrison han
                //针对APP支持多维度值筛选
                String[] values = param.getValue().split(",");
                if (values != null && values.length >= 2) { //  如果存在多个值查询,则需要按照OR关键字拼接
                    builder.append("(");
                    for (int k = 0; k < values.length; k++) {
                        builder.append(param.getName()).append(":");
                        builder.append(values[k]);
                        if (k + 1 < values.length) {
                            builder.append(" OR ");
                        }
                    }
                    builder.append(")");
                } else {
                    builder.append(param.getName()).append(":");
                    String type = param.getType();
                    if ("0".equals(type)) {
                        //精确查询
                        builder.append(param.getValue());
                    } else if ("1".equals(type)) {
                        //模糊查询
                        builder.append("*").append(param.getValue()).append("*");
                    } else if ("2".equals(type)) {
                        //范围查询(范围值在业务逻辑特殊处理)
                        builder.append(param.getValue());
                    } else {
                        throw new RuntimeException("invalid filterParams type!");
                    }

                    if (i < size - 1) {
                        builder.append(" AND ");
                    }
                }
            }
        }
        return builder.toString();
    }

    /**
     * 根据排序参数生成SOLR排序条件
     * @param sortParams
     * @return List<SortClause>
     */
    private List<SortClause> getSortList(List<SortParams> sortParams) {
        List<SortClause> list = new ArrayList<SortClause>();
        if (sortParams != null && !sortParams.isEmpty()) {
            int size = sortParams.size();
            for (int i = 0; i < size; i++) {
                SortParams param = sortParams.get(i);
                SortClause sortClause = null;
                String type = param.getType();
                if ("0".equals(type)) {
                    sortClause = new SortClause(param.getName(), SolrQuery.ORDER.asc);
                } else if ("1".equals(type)) {
                    sortClause = new SortClause(param.getName(), SolrQuery.ORDER.desc);
                } else {
                    throw new RuntimeException("invalid sortParams type!");
                }
                list.add(sortClause);
            }
        }
        return list;
    }


    /**
     * 分页查询
     * @param sorlUrl
     * @param coreName
     * @param queryParams
     * @param filterParams
     * @param sortParams
     * @param page
     * @param clazz
     * @param <T>
     * @return Page<T>
     */
    public <T> Page<T> queryByPage(String sorlUrl, String coreName, List<QueryParams> queryParams, List<FilterParams> filterParams, List<SortParams> sortParams, Page<T> page, Class<T> clazz) {
        logger.info("queryByPage queryParams is {}", queryParams);
        logger.info("queryByPage filterParams is {}", filterParams);
        logger.info("queryByPage sortParams is {}", sortParams);
        logger.info("queryByPage page is {}", page);

        HttpSolrClient solrClient = null;
        try {
            SolrQuery sq = new SolrQuery();
            sq.setQuery(getQueryStr(queryParams));//查询参数
            sq.setFilterQueries(getFilterStr(filterParams));//Filter参数
            sq.setSorts(getSortList(sortParams));//排序参数
            //设置分页参数
            Long start = (page.getPageNo() - 1) * page.getPageSize();
            sq.setStart(start.intValue());
            sq.setRows(page.getPageSize().intValue());

            solrClient = new HttpSolrClient(sorlUrl);
            QueryResponse qr = solrClient.query(coreName, sq);
            SolrDocumentList list = qr.getResults();

            page.setTotalRows(list.getNumFound());
            page.setResult(qr.getBeans(clazz));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (solrClient != null) {
                try {
                    solrClient.close();
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    solrClient = null;
                }
            }
        }
        return page;
    }

}
