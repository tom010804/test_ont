package com.jk.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.jk.dao.FareDao;
import com.jk.dao.FareDaoEs;
import com.jk.pojo.FareBean;
import com.jk.service.FareServie;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: 李旺
 * Date: 2020/12/28
 * Time: 20:10
 */
@Service
public class FareServiceImpl implements FareServie {

    @Resource
    private FareDao fareDao;

    @Autowired
    private FareDaoEs fareDaoEs;

    @Autowired
    private ElasticsearchTemplate template;

    @Override
    public void saveFare(FareBean fareBean) {
        if (fareBean.getFareId()==null){
            fareDao.saveFare(fareBean);
        }else {
            fareDao.upFare(fareBean);
        }
        fareDaoEs.save(fareBean);
    }

    @Override
    public HashMap<String, Object> findAll(Integer rows, Integer page, FareBean fareBean) {
        HashMap<String, Object> map = new HashMap<String, Object>();

        List<FareBean> list = new ArrayList<>();

        //1、获取es客户端对象
        Client client = template.getClient();
        //2、创建查询对象：设置索引、类型
        SearchRequestBuilder search = client.prepareSearch("fare")//索引、数据库
                .setTypes("t_fare");//类型、表

        //混合查询、组合查询：同时查询名称、简介
        BoolQueryBuilder bool = new BoolQueryBuilder();
        if(!StringUtils.isEmpty(fareBean.getStart())){
            //查询名称
            bool.should(QueryBuilders.matchQuery("start",fareBean.getStart()));

            //查询简介
            bool.should(QueryBuilders.matchQuery("end",fareBean.getStart()));
        }
        //价格的区间查询
        RangeQueryBuilder price = QueryBuilders.rangeQuery("farePrice");
        if(fareBean.getStartPrice()!=null){
            price.gte(fareBean.getStartPrice());
        }

        if(fareBean.getEndPrice()!=null){
            price.lte(fareBean.getEndPrice());
        }
        if(fareBean.getStartPrice()!=null || fareBean.getEndPrice()!=null){
            bool.must(price);
        }
        //bool.must(QueryBuilders.rangeQuery("price").gte(book.getStartprice()).lte(book.getEndprice()));
        search.setQuery(bool);
        //设置高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("start");//名称高亮
        highlightBuilder.field("end");//简介高亮
        // <font color="red"></font>
        highlightBuilder.preTags("<font color=\"red\">");//前缀
        highlightBuilder.postTags("</font>");//后缀
        search.highlighter(highlightBuilder);

        //排序: 先价格升序、id降序
        search.addSort("fareDate", SortOrder.ASC);

        //分页
        search.setFrom((page-1)*rows);//开始位置
        search.setSize(rows);//没有条数

        //3、执行、获取查询结果
        SearchResponse searchResponse = search.get();

        SearchHits hits = searchResponse.getHits();

        Iterator<SearchHit> iterator = hits.iterator();
        while (iterator.hasNext()){
            SearchHit next = iterator.next();
            String str = next.getSourceAsString();
            //把字符串转换成javabean对象
            FareBean fareBean1 = JSONObject.parseObject(str, FareBean.class);

            //获取name的高亮内容
            Map<String, HighlightField> highlightFields = next.getHighlightFields();
            HighlightField start = highlightFields.get("start");
            if(start!=null){
                /*Text[] fragments = name.getFragments();
                Text fragment = fragments[0];
                String s = fragment.toString();*/

                String start2 = start.getFragments()[0].toString();
                fareBean1.setStart(start2);
            }

            //处理简介高亮
            HighlightField end = highlightFields.get("end");
            if(end!=null){
                String end2 = end.getFragments()[0].toString();
                fareBean1.setEnd(end2);
            }

            list.add(fareBean1);
        }

        //获取总条数：
        long total = hits.getTotalHits();
        map.put("total",total);
        map.put("rows",list);
        return map;
    }

    @Override
    public void delFare(Integer fareId) {
        fareDaoEs.deleteById(fareId);
        fareDao.delFare(fareId);
    }

    @Override
    public FareBean findFareById(Integer fareId) {
        Optional<FareBean> byId = fareDaoEs.findById(fareId);
        FareBean fareBean = byId.get();
        return fareBean;
    }

    @Override
    public HashMap<String, Object> findAll1(Integer rows, Integer page, FareBean fareBean) {
        HashMap<String, Object> map = new HashMap<String, Object>();

        List<FareBean> list = new ArrayList<>();

        //1、获取es客户端对象
        Client client = template.getClient();
        //2、创建查询对象：设置索引、类型
        SearchRequestBuilder search = client.prepareSearch("fare")//索引、数据库
                .setTypes("t_fare");//类型、表

        //混合查询、组合查询：同时查询名称、简介
        BoolQueryBuilder bool = new BoolQueryBuilder();
        if(!StringUtils.isEmpty(fareBean.getZhan())){
            //查询名称
            bool.should(QueryBuilders.matchQuery("detail",fareBean.getZhan()));
        }
        //价格的区间查询
        RangeQueryBuilder price = QueryBuilders.rangeQuery("farePrice");
        if(fareBean.getStartPrice()!=null){
            price.gte(fareBean.getStartPrice());
        }

        if(fareBean.getEndPrice()!=null){
            price.lte(fareBean.getEndPrice());
        }
        if(fareBean.getStartPrice()!=null || fareBean.getEndPrice()!=null){
            bool.must(price);
        }
        //bool.must(QueryBuilders.rangeQuery("price").gte(book.getStartprice()).lte(book.getEndprice()));
        search.setQuery(bool);
        //设置高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("start");//名称高亮
        highlightBuilder.field("end");//简介高亮
        // <font color="red"></font>
        highlightBuilder.preTags("<font color=\"red\">");//前缀
        highlightBuilder.postTags("</font>");//后缀
        search.highlighter(highlightBuilder);

        //排序: 先价格升序、id降序
        search.addSort("fareDate", SortOrder.ASC);

        //分页
        search.setFrom((page-1)*rows);//开始位置
        search.setSize(rows);//没有条数

        //3、执行、获取查询结果
        SearchResponse searchResponse = search.get();

        SearchHits hits = searchResponse.getHits();

        Iterator<SearchHit> iterator = hits.iterator();
        while (iterator.hasNext()){
            SearchHit next = iterator.next();
            String str = next.getSourceAsString();
            //把字符串转换成javabean对象
            FareBean fareBean1 = JSONObject.parseObject(str, FareBean.class);

            //获取name的高亮内容
            Map<String, HighlightField> highlightFields = next.getHighlightFields();
            HighlightField start = highlightFields.get("start");
            if(start!=null){
                /*Text[] fragments = name.getFragments();
                Text fragment = fragments[0];
                String s = fragment.toString();*/

                String start2 = start.getFragments()[0].toString();
                fareBean1.setStart(start2);
            }

            //处理简介高亮
            HighlightField end = highlightFields.get("end");
            if(end!=null){
                String end2 = end.getFragments()[0].toString();
                fareBean1.setEnd(end2);
            }

            list.add(fareBean1);
        }

        //获取总条数：
        long total = hits.getTotalHits();
        map.put("total",total);
        map.put("rows",list);
        return map;
    }
}
