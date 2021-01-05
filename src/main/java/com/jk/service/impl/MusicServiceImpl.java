package com.jk.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.jk.dao.MusicDaoEs;
import com.jk.pojo.MusicBean;
import com.jk.service.MusicService;
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

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: 李旺
 * Date: 2020/12/31
 * Time: 10:54
 */
@Service
public class MusicServiceImpl implements MusicService {
    @Autowired
    private MusicDaoEs musicDao;

    @Autowired
    private ElasticsearchTemplate template;

    @Override
    public void save(MusicBean music) {
        musicDao.save(music);
    }

    @Override
    public HashMap<String, Object> findAll(int page, int rows, MusicBean musicBean) {
        HashMap<String, Object> map = new HashMap<String, Object>();

        List<MusicBean> list = new ArrayList<>();

        //1、获取es客户端对象
        Client client = template.getClient();
        //2、创建查询对象：设置索引、类型
        SearchRequestBuilder search = client.prepareSearch("music")//索引、数据库
                .setTypes("t_music");//类型、表

        //混合查询、组合查询：同时查询名称、简介
        BoolQueryBuilder bool = new BoolQueryBuilder();
        if(!StringUtils.isEmpty(musicBean.getName())){
            bool.should(QueryBuilders.matchQuery("name",musicBean.getName()));
            bool.should(QueryBuilders.matchQuery("author",musicBean.getName()));
        }
        //价格的区间查询
        RangeQueryBuilder date = QueryBuilders.rangeQuery("createdate");
        if(!StringUtils.isEmpty(musicBean.getStart())){
            date.gte(musicBean.getStart());
        }

        if(!StringUtils.isEmpty(musicBean.getEnd())){
            date.lte(musicBean.getEnd());
        }
        if(!StringUtils.isEmpty(musicBean.getEnd()) || !StringUtils.isEmpty(musicBean.getStart())){
            bool.must(date);
        }
        //bool.must(QueryBuilders.rangeQuery("price").gte(book.getStartprice()).lte(book.getEndprice()));
        search.setQuery(bool);
        //设置高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("name");//名称高亮
        highlightBuilder.field("author");//简介高亮
        // <font color="red"></font>
        highlightBuilder.preTags("<font color=\"red\">");//前缀
        highlightBuilder.postTags("</font>");//后缀
        search.highlighter(highlightBuilder);

        //排序: 先价格升序、id降序
        //search.addSort("createdate", SortOrder.DESC);
        search.addSort("price", SortOrder.ASC);

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
            MusicBean musicBean1 = JSONObject.parseObject(str, MusicBean.class);

            //获取name的高亮内容
            Map<String, HighlightField> highlightFields = next.getHighlightFields();
            HighlightField name = highlightFields.get("name");
            if(name!=null){
                /*Text[] fragments = name.getFragments();
                Text fragment = fragments[0];
                String s = fragment.toString();*/

                String name2 = name.getFragments()[0].toString();
                musicBean1.setName(name2);
            }

            //处理简介高亮
            HighlightField author = highlightFields.get("author");
            if(author!=null){
                String author2 = author.getFragments()[0].toString();
                musicBean1.setAuthor(author2);
            }

            list.add(musicBean1);
        }

        //获取总条数：
        long total = hits.getTotalHits();
        map.put("total",total);
        map.put("rows",list);
        return map;
    }
}
