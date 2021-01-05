package com.jk.dao;

import com.jk.pojo.FareBean;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Created by IntelliJ IDEA.
 * User: 李旺
 * Date: 2020/12/28
 * Time: 20:11
 */
public interface FareDaoEs extends ElasticsearchRepository<FareBean,Integer> {
}
